package com.cloudycrew.cloudycar.createrequest;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

/**
 * Created by George on 2016-11-05.
 */
public class CreateRequestController extends ViewController<ICreateRequestView> {
    private RequestController requestController;
    private UserController userController;
    private ISchedulerProvider schedulerProvider;

    /**
     * Instantiates a new Create request controller.
     *
     * @param requestController the request controller
     * @param schedulerProvider the scheduler provider
     */
    public CreateRequestController(RequestController requestController,
                                   UserController userController,
                                   ISchedulerProvider schedulerProvider) {
        this.requestController = requestController;
        this.userController = userController;
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
        ObservableUtils.fromFunction(requestController::createRequest, userRoute, price)
                       .doOnNext(r -> userController.markRequestAsRead(r.getId()))
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
