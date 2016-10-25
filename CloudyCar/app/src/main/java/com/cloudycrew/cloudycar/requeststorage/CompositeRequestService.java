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
    private IConnectivityService connectivityService;

    public CompositeRequestService(IRequestService cloudRequestService, IRequestService localRequestService, IConnectivityService connectivityService) {
        this.cloudRequestService = cloudRequestService;
        this.localRequestService = localRequestService;
        this.connectivityService = connectivityService;

        connectivityService.setOnConnectivityChangedListener(new IConnectivityService.OnConnectivityChangedListener() {
            @Override
            public void onConnectivityChanged(boolean isConnected) {
                if (isConnected) {
                    syncLocalState();
                }
            }
        });
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
