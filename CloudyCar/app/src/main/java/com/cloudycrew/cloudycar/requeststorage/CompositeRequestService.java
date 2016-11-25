package com.cloudycrew.cloudycar.requeststorage;

import android.net.ConnectivityManager;

import com.cloudycrew.cloudycar.connectivity.IConnectivityService;
import com.cloudycrew.cloudycar.elasticsearch.ElasticSearchConnectivityException;
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

    private List<Request> createQueue;
    private List<Request> updateQueue;

    public CompositeRequestService(IRequestService cloudRequestService, IRequestService localRequestService, IConnectivityService connectivityService) {
        this.cloudRequestService = cloudRequestService;
        this.localRequestService = localRequestService;
        this.connectivityService = connectivityService;

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
            this.createQueue.add(request);
        }
        localRequestService.createRequest(request);
    }

    @Override
    public void updateRequest(Request request) {
        try {
            cloudRequestService.updateRequest(request);
        } catch (ElasticSearchConnectivityException e) {
            this.updateQueue.add(request);
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
     * A) Cancel any local requests that are cancelled on the server
     * B) Perform any state transitions from the server
     *    i) From pending to confirmed
     *    ii) From confirmed to completed
     *    iii) From pending to completed
     * D) For any remaining pending requests merge the list of driversWhoAccepted/set as accepted if
     *    accepted offline
     * E) Perform any state transitions that happened offline
     *    i) From pending to confirmed
     *    ii) From confirmed to completed
     */
    private void syncLocalState() {

        for (Request request: createQueue) {
            this.createRequest(request); //If we have a hash collision we deserve it
        }

        List<Request> cloudRequests = this.cloudRequestService.getRequests();

        for (Request request: updateQueue) {
            //do stuff
        }

    }


}
