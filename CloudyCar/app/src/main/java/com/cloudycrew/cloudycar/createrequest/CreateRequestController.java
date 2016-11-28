package com.cloudycrew.cloudycar.createrequest;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by George on 2016-11-05.
 */
public class CreateRequestController extends ViewController<ICreateRequestView> {
    private RequestController requestController;
    private ISchedulerProvider schedulerProvider;

    /**
     * Instantiates a new Create request controller.
     *
     * @param requestController the request controller
     * @param schedulerProvider the scheduler provider
     */
    public CreateRequestController(RequestController requestController,
                                   ISchedulerProvider schedulerProvider) {
        this.requestController = requestController;
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * Attempts to create a request asynchronously.
     * On success onRequestCreated() is called on the view.
     *
     * @param userRoute the user route
     * @param price     the price
     */
    public void saveRequest(final Route userRoute, final double price, final String description) {
        ObservableUtils.create(new Func0<PendingRequest>() {
                           @Override
                           public PendingRequest call() {
                               return requestController.createRequest(userRoute, price, description);
                           }
                       })
                       .doOnNext(new Action1<PendingRequest>() {
                           @Override
                           public void call(PendingRequest pendingRequest) {
                               requestController.markRequestAsRead(pendingRequest.getId());
                           }
                       })
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe(new Action1<PendingRequest>() {
                           @Override
                           public void call(PendingRequest pendingRequest) {
                               dispatchOnRequestCreated();
                           }
                       });
    }

    private void dispatchOnRequestCreated() {
        if (isViewAttached()) {
            getView().onRequestCreated();
        }
    }
}
