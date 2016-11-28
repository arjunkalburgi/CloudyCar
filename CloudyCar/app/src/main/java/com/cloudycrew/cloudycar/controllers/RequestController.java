package com.cloudycrew.cloudycar.controllers;

import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.requests.CancelledRequest;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requeststorage.IRequestService;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.users.IUserPreferences;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by George on 2016-10-23.
 */
public class RequestController {
    private IRequestService requestService;
    private IRequestStore requestStore;
    private ISchedulerProvider schedulerProvider;
    private UserController userController;

    /**
     * Instantiates a new Request controller.
     *
     * @param userController   the user controller
     * @param requestStore      the request store
     * @param requestService    the request service
     * @param schedulerProvider the scheduler provider
     */
    public RequestController(UserController userController, IRequestStore requestStore, IRequestService requestService, ISchedulerProvider schedulerProvider) {
        this.userController = userController;
        this.requestStore = requestStore;
        this.requestService = requestService;
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * Refresh requests. On success, the new requests are added to the request store.
     */
    public void refreshRequests() {
        Observable.<Void>just(null)
                  .observeOn(schedulerProvider.ioScheduler())
                  .map(new Func1<Void, List<Request>>() {
                      @Override
                      public List<Request> call(Void aVoid) {
                          return requestService.getRequests();
                      }
                  })
                  .doOnNext(new Action1<List<Request>>() {
                      @Override
                      public void call(List<Request> requests) {
                          attachReadDetailsToRequests(requests);
                      }
                  })
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .subscribe(new Action1<List<Request>>() {
                      @Override
                      public void call(List<Request> requests) {
                          requestStore.setAll(requests);
                      }
                  });
    }

    /**
     * Create a request.
     *
     * @param route the route
     * @param price the price
     *
     * @return pendingRequest the created request
     */
    public PendingRequest createRequest(Route route, double price, String description) {
        PendingRequest pendingRequest = new PendingRequest(getCurrentUsername(), route, price, description);

        requestService.createRequest(pendingRequest);
        requestStore.addRequest(pendingRequest);

        return pendingRequest;
    }

    /**
     * Cancels a request. No change will occur if no request can be found
     * matching the specified id
     *
     * @param requestId the request id
     */
    public void cancelRequest(String requestId) {
        PendingRequest pendingRequest = requestStore.getRequest(requestId, PendingRequest.class);

        if(pendingRequest != null) {
            CancelledRequest cancelledRequest = pendingRequest.cancel();

            requestService.updateRequest(cancelledRequest);
            requestStore.updateRequest(cancelledRequest);
        }
    }

    /**
     * Accepts a request as the current user. No changes will occur if no request can be found
     * matching the specified id.
     *
     * @param requestId the request id
     */
    public void acceptRequest(String requestId) {
        PendingRequest pendingRequest = requestStore.getRequest(requestId, PendingRequest.class);

        if (pendingRequest != null) {
            PendingRequest acceptedPendingRequest = pendingRequest.accept(getCurrentUsername());

            requestService.updateRequest(acceptedPendingRequest);
            requestStore.updateRequest(acceptedPendingRequest);
        }
    }

    /**
     * Confirms a request as the current user. No changes will occur if no request can be found
     * matching the specified id.
     *
     * @param requestId      the request id
     * @param driverUsername the driver username
     */
    public void confirmRequest(String requestId, String driverUsername) {
        PendingRequest pendingRequest = requestStore.getRequest(requestId, PendingRequest.class);

        if (pendingRequest != null) {
            ConfirmedRequest confirmedRequest = pendingRequest.confirmRequest(driverUsername);

            requestService.updateRequest(confirmedRequest);
            requestStore.updateRequest(confirmedRequest);
        }
    }

    /**
     * Completes a request as the current user. No changes will occur if no request can be found
     * matching the specified id.
     *
     * @param requestId the request id
     */
    public void completeRequest(String requestId) {
        ConfirmedRequest confirmedRequest = requestStore.getRequest(requestId, ConfirmedRequest.class);

        if (confirmedRequest != null) {
            CompletedRequest completedRequest = confirmedRequest.completeRequest();

            requestService.updateRequest(completedRequest);
            requestStore.updateRequest(completedRequest);
        }
    }

    public void markRequestAsRead(String requestId) {
        Request request = requestStore.getRequest(requestId);

        request.setLastReadTime(new Date());
        requestStore.updateRequest(request);
        userController.markRequestAsRead(request.getId());
    }

    private void attachReadDetailsToRequests(List<Request> requests) {
        for (Request request: requests) {
            request.setLastReadTime(userController.getLastReadTime(request.getId()));
        }
    }

    private String getCurrentUsername() {
        return userController.getCurrentUser().getUsername();
    }
}
