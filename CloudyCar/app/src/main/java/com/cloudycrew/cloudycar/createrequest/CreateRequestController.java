package com.cloudycrew.cloudycar.createrequest;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

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
    public CreateRequestController(RequestController requestController, ISchedulerProvider schedulerProvider) {
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
    public void saveRequest(Route userRoute, double price) {
        ObservableUtils.fromAction(requestController::createRequest, userRoute, price)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe(nothing -> dispatchOnRequestCreated());
    }

    private void dispatchOnRequestCreated() {
        if (isViewAttached()) {
            getView().onRequestCreated();
        }
    }
}
