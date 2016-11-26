package com.cloudycrew.cloudycar.requeststorage;

import android.net.ConnectivityManager;

import com.cloudycrew.cloudycar.connectivity.IConnectivityService;
import com.cloudycrew.cloudycar.elasticsearch.ElasticSearchConnectivityException;
import com.cloudycrew.cloudycar.models.requests.CancelledRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.List;

/**
 * Created by George on 2016-10-24.
 */

public class CompositeRequestService implements IRequestService {
    private IRequestService cloudRequestService;
    private IRequestService localRequestService;
    /**
     * Kiuwan suggests removing this unused private member. We should either remove it or determine
     * why it was stubbed out and use it if the case is still viable.
     */
    private IConnectivityService connectivityService;
    private PersistentRequestQueue requestQueue;

    public CompositeRequestService(IRequestService cloudRequestService, IRequestService localRequestService, IConnectivityService connectivityService, PersistentRequestQueue requestQueue) {
        this.cloudRequestService = cloudRequestService;
        this.localRequestService = localRequestService;
        this.connectivityService = connectivityService;
        this.requestQueue = requestQueue;

        this.connectivityService.setOnConnectivityChangedListener(isConnected -> {
            if (isConnected) {
                syncLocalState();
            }
        });
    }

    @Override
    public List<Request> getRequests() {
        try {
            return cloudRequestService.getRequests();
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
    public void deleteRequest(String requestId) {
        cloudRequestService.deleteRequest(requestId);
        localRequestService.deleteRequest(requestId);
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
            this.createRequest(request); //If we have a hash collision we deserve it
        }

        //We can safely cancel since we 'own' this request
        for (Request request: requestQueue.getCancellationQueue()) {
            this.updateRequest(request);
        }

        //We can safely confirm since we 'own' this request
        for (Request request: requestQueue.getConfirmationQueue()) {
            this.updateRequest(request);
        }


        List<Request> cloudRequests = this.cloudRequestService.getRequests();

        //Add our acceptances... this is pretty dirty
        for (PendingRequest request: requestQueue.getAcceptedQueue()) {
            for(Request cloudRequest: cloudRequests) {
                if (cloudRequest.getId().equals(request.getId())) {
                    PendingRequest updatedRequest = new PendingRequest(cloudRequest.getRider(), cloudRequest.getRoute(), cloudRequest.getPrice());
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
                    }
                }
            }
        }

        //Now that the cloud is synced to our changes we will just set the local service
        //to be equivalent to the cloud service
        for(Request request: cloudRequestService.getRequests()) {
            localRequestService.updateRequest(request);
        }

    }
}
