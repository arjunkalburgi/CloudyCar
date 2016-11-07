package com.cloudycrew.cloudycar.controllers;

import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requeststorage.IRequestService;
import com.cloudycrew.cloudycar.requeststorage.RequestStore;

import java.util.List;

/**
 * Created by George on 2016-10-23.
 */

public class RequestController {
    private IRequestService requestService;
    private RequestStore requestStore;
    private String currentUser;

    public RequestController(String currentUser) {
        this.currentUser = currentUser;
    }

    public void refreshRequests() {
        List<Request> requests = requestService.getRequests();
        requestStore.setAll(requests);
    }

    public void createRequest(Route route) {
        PendingRequest pendingRequest = new PendingRequest(currentUser, route);
        requestService.createRequest(pendingRequest);
        requestStore.addItem(pendingRequest);
    }

    public void cancelRequest(String requestId) {
        requestService.deleteRequest(requestId);
        requestStore.deleteItem(requestId);
    }

    public void acceptRequest(String requestId) {
        PendingRequest pendingRequest = requestStore.getRequest(requestId, PendingRequest.class);

        if (pendingRequest != null) {
            AcceptedRequest acceptedRequest = pendingRequest.acceptRequest(currentUser);
            requestService.createRequest(acceptedRequest);
            requestStore.addItem(acceptedRequest);
        }

    }

    public void confirmRequest(String requestId) {
        AcceptedRequest acceptedRequest = requestStore.getRequest(requestId, AcceptedRequest.class);

        if (acceptedRequest != null) {
            ConfirmedRequest confirmedRequest = acceptedRequest.confirmRequest();
            requestService.updateRequest(confirmedRequest);
            requestStore.updateItem(confirmedRequest);
        }
    }

    public void completeRequest(String requestId) {
        ConfirmedRequest confirmedRequest = requestStore.getRequest(requestId, ConfirmedRequest.class);

        if (confirmedRequest != null) {
            CompletedRequest completedRequest = confirmedRequest.completeRequest();
            requestService.updateRequest(completedRequest);
            requestStore.updateItem(completedRequest);
        }
    }
}
