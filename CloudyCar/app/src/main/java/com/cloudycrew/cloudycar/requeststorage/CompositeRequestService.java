package com.cloudycrew.cloudycar.requeststorage;


import com.cloudycrew.cloudycar.GeoDecoder;
import com.cloudycrew.cloudycar.connectivity.IConnectivityService;
import com.cloudycrew.cloudycar.elasticsearch.ElasticSearchConnectivityException;
import com.cloudycrew.cloudycar.models.Location;
import com.cloudycrew.cloudycar.models.requests.CancelledRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.search.SearchContext;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;


/**
 * Created by George on 2016-10-24.
 */

public class CompositeRequestService implements IRequestService {
    private IRequestService cloudRequestService;
    private IRequestService localRequestService;
    private IConnectivityService connectivityService;
    private PersistentRequestQueue requestQueue;
    private ISchedulerProvider schedulerProvider;
    private GeoDecoder geoDecoder;

    public CompositeRequestService(IRequestService cloudRequestService,
                                   IRequestService localRequestService,
                                   IConnectivityService connectivityService,
                                   PersistentRequestQueue requestQueue,
                                   ISchedulerProvider scheduler,
                                   GeoDecoder geoDecoder) {
        this.cloudRequestService = cloudRequestService;
        this.localRequestService = localRequestService;
        this.connectivityService = connectivityService;
        this.requestQueue = requestQueue;
        this.schedulerProvider = scheduler;
        this.geoDecoder = geoDecoder;

        this.connectivityService.setOnConnectivityChangedListener(new IConnectivityService.OnConnectivityChangedListener() {
            @Override
            public void onConnectivityChanged(boolean isConnected) {
                if (isConnected) {
                    ObservableUtils.create(new Action0() {
                                @Override
                                public void call() {
                                    syncLocalState();
                                }
                            })
                            .subscribeOn(schedulerProvider.ioScheduler())
                            .subscribe();
                }
            }
        });
    }

    @Override
    public List<Request> getRequests() {
        try {
            List<Request> cloudRequests = cloudRequestService.getRequests();
            localRequestService.batchUpdateRequests(cloudRequests);

            return cloudRequests;
        } catch (ElasticSearchConnectivityException e) {
            return localRequestService.getRequests();
        }

    }

    @Override
    public void createRequest(Request request) {
        try {
            cloudRequestService.createRequest(request);
        }
        catch (ElasticSearchConnectivityException e){
            requestQueue.enqueueNewRequest(request);
        }
        localRequestService.createRequest(request);
    }

    @Override
    public void updateRequest(Request request) {
        try {
            cloudRequestService.updateRequest(request);
        } catch (ElasticSearchConnectivityException e) {
            if(request instanceof PendingRequest) {
                requestQueue.enqueueAccept((PendingRequest)request);
            }
            else if (request instanceof CancelledRequest) {
                requestQueue.enqueueCancellation((CancelledRequest)request);
            }
            else if (request instanceof ConfirmedRequest) {
                requestQueue.enqueueConfirmation((ConfirmedRequest)request);
            }
        }
        localRequestService.updateRequest(request);
    }

    @Override
    public void batchUpdateRequests(Collection<? extends Request> requests) {
        for (Request request: requests) {
            updateRequest(request);
        }
    }

    @Override
    public void deleteRequest(String requestId) {
        cloudRequestService.deleteRequest(requestId);
        localRequestService.deleteRequest(requestId);
    }

    @Override
    public List<Request> search(SearchContext searchContext) {
        try {
            return cloudRequestService.search(searchContext);
        } catch (Exception e) {
            return localRequestService.search(searchContext);
        }
    }

    /**
     * To sync the local state we want to
     * A) Flush the cancellation, creation, and confirmation queues
     *    These requests belong to the user so only they are able to do perform these transitions
     * B) For any remaining pending requests merge the list of driversWhoAccepted/set as accepted if
     *    accepted offline
     * C) Perform any state transitions from the server
     *    i) From pending to confirmed
     *    ii) From confirmed to completed
     *    iii) From pending to completed
     */
    private void syncLocalState() {

        for (Request request: requestQueue.getCreateQueue()) {
            setDescriptions(request);
            cloudRequestService.createRequest(request);
            requestQueue.dequeueCreation(request);
        }

        //We can safely cancel since we 'own' this request
        for (CancelledRequest request: requestQueue.getCancellationQueue()) {
            cloudRequestService.updateRequest(request);
            requestQueue.dequeueCancellation(request);
        }

        //We can safely confirm since we 'own' this request
        for (ConfirmedRequest request: requestQueue.getConfirmationQueue()) {
            cloudRequestService.updateRequest(request);
            requestQueue.dequeueConfirmation(request);
        }

        List<Request> cloudRequests;

        try {
            cloudRequests = cloudRequestService.getRequests();
        }
        catch(ElasticSearchConnectivityException e) {
            e.printStackTrace();
            return;
        }

        //Add our acceptances... this is pretty dirty
        for (PendingRequest request: requestQueue.getAcceptedQueue()) {
            for(Request cloudRequest: cloudRequests) {
                if (cloudRequest.getId().equals(request.getId())) {
                    PendingRequest updatedRequest = new PendingRequest(cloudRequest.getRider(), cloudRequest.getRoute(), cloudRequest.getPrice(), cloudRequest.getDescription());
                    if (cloudRequest instanceof PendingRequest) {
                        for (String driver: request.getDriversWhoAccepted()) {
                            try {
                                updatedRequest = ((PendingRequest) cloudRequest).accept(driver);
                            }
                            catch(PendingRequest.DriverAlreadyAcceptedException e) {
                                //do nothing
                            }
                        }
                        cloudRequestService.updateRequest(updatedRequest);
                        requestQueue.dequeueAccept(request);
                    }
                }
            }
        }

        //Now that the cloud is synced to our changes we will just set the local service
        //to be equivalent to the cloud service
        cloudRequests.clear();

        try {
            cloudRequests = cloudRequestService.getRequests();
        }
        catch(ElasticSearchConnectivityException e) {
            e.printStackTrace();
            return;
        }


        for(Request request: cloudRequests) {
            localRequestService.updateRequest(request);
        }

    }

    private void setDescriptions(Request request) {
        Location startingPoint = request.getRoute().getStartingPoint();
        Location endingPoint = request.getRoute().getEndingPoint();

        startingPoint.setDescription(geoDecoder.decodeLatLng(startingPoint.getLongitude(), startingPoint.getLatitude()));
        endingPoint.setDescription(geoDecoder.decodeLatLng(endingPoint.getLongitude(), endingPoint.getLatitude()));
    }
}
