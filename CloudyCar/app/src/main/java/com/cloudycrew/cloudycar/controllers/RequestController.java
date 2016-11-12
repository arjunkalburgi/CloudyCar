package com.cloudycrew.cloudycar.controllers;

import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.requeststorage.IRequestService;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.users.IUserPreferences;

import rx.Observable;

/**
 * Created by George on 2016-10-23.
 */

public class RequestController {
    private IRequestService requestService;
    private IRequestStore requestStore;
    private ISchedulerProvider schedulerProvider;
    private IUserPreferences userPreferences;

    public RequestController(IUserPreferences userPreferences, IRequestStore requestStore, IRequestService requestService, ISchedulerProvider schedulerProvider) {
        this.userPreferences = userPreferences;
        this.requestStore = requestStore;
        this.requestService = requestService;
        this.schedulerProvider = schedulerProvider;
    }

    public void refreshRequests() {
        Observable.just(null)
                  .observeOn(schedulerProvider.ioScheduler())
                  .map(nothing -> requestService.getRequests())
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .subscribe(requestStore::setAll);
    }

    public void createRequest(Route route, double price) {
        PendingRequest pendingRequest = new PendingRequest(userPreferences.getUserName(), route, price);

        requestService.createRequest(pendingRequest);
        requestStore.addRequest(pendingRequest);
    }

    public void cancelRequest(String requestId) {
        requestService.deleteRequest(requestId);
        requestStore.deleteRequest(requestId);
    }

    public void acceptRequest(String requestId) {
        PendingRequest pendingRequest = requestStore.getRequest(requestId, PendingRequest.class);

        if (pendingRequest != null) {
            PendingRequest acceptedPendingRequest = pendingRequest.accept(userPreferences.getUserName());

            requestService.updateRequest(acceptedPendingRequest);
            requestStore.updateRequest(acceptedPendingRequest);
        }
    }

    public void confirmRequest(String requestId, String driverUsername) {
        PendingRequest pendingRequest = requestStore.getRequest(requestId, PendingRequest.class);

        if (pendingRequest != null) {
            ConfirmedRequest confirmedRequest = pendingRequest.confirmRequest(driverUsername);

            requestService.updateRequest(confirmedRequest);
            requestStore.updateRequest(confirmedRequest);
        }
    }

    public void completeRequest(String requestId) {
        ConfirmedRequest confirmedRequest = requestStore.getRequest(requestId, ConfirmedRequest.class);

        if (confirmedRequest != null) {
            CompletedRequest completedRequest = confirmedRequest.completeRequest();

            requestService.updateRequest(completedRequest);
            requestStore.updateRequest(completedRequest);
        }
    }
}
