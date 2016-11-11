package com.cloudycrew.cloudycar.controllers;

import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.requeststorage.IRequestService;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.users.IUserPreferences;
import com.cloudycrew.cloudycar.utils.Utils;

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

    public void createRequest(Route route) {
        Observable.just(route)
                  .map(r -> new PendingRequest(userPreferences.getUserName(), r))
                  .observeOn(schedulerProvider.ioScheduler())
                  .doOnNext(requestService::createRequest)
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .subscribe(requestStore::addRequest);
    }

    public void cancelRequest(String requestId) {
        Observable.just(requestId)
                  .observeOn(schedulerProvider.ioScheduler())
                  .doOnNext(requestService::deleteRequest)
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .subscribe(requestStore::deleteRequest);
    }

    public void acceptRequest(String requestId) {
        Observable.just(requestId)
                  .map(id -> requestStore.getRequest(requestId, PendingRequest.class))
                  .filter(Utils::isNotNull)
                  .doOnNext(r -> r.accept(userPreferences.getUserName()))
                  .observeOn(schedulerProvider.ioScheduler())
                  .doOnNext(requestService::updateRequest)
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .subscribe(requestStore::updateRequest);
    }

    public void confirmRequest(String requestId) {
        Observable.just(requestId)
                  .map(id -> requestStore.getRequest(requestId, PendingRequest.class))
                  .filter(Utils::isNotNull)
                  .map(r -> r.confirmRequest(userPreferences.getUserName()))
                  .observeOn(schedulerProvider.ioScheduler())
                  .doOnNext(requestService::updateRequest)
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .subscribe(requestStore::updateRequest);
    }

    public void completeRequest(String requestId) {
        Observable.just(requestId)
                  .map(id -> requestStore.getRequest(requestId, ConfirmedRequest.class))
                  .filter(Utils::isNotNull)
                  .map(ConfirmedRequest::completeRequest)
                  .observeOn(schedulerProvider.ioScheduler())
                  .doOnNext(requestService::updateRequest)
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .subscribe(requestStore::updateRequest);
    }
}
