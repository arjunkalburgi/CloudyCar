package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.connectivity.IConnectivityService;
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

    public CompositeRequestService(IRequestService cloudRequestService, IRequestService localRequestService, IConnectivityService connectivityService) {
        this.cloudRequestService = cloudRequestService;
        this.localRequestService = localRequestService;
        this.connectivityService = connectivityService;
    }

    @Override
    public List<Request> getRequests() {
        try {
            return cloudRequestService.getRequests();
        } catch (Exception e) {
            return localRequestService.getRequests();
        }
    }

    @Override
    public void createRequest(Request request) {
        cloudRequestService.createRequest(request);
        localRequestService.createRequest(request);
    }

    @Override
    public void updateRequest(Request request) {
        cloudRequestService.updateRequest(request);
        localRequestService.updateRequest(request);
    }

    @Override
    public void deleteRequest(String requestId) {
        cloudRequestService.deleteRequest(requestId);
        localRequestService.deleteRequest(requestId);
    }

    private void syncLocalState() {

    }
}
