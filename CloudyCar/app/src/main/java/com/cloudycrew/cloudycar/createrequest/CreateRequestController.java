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

    public CreateRequestController(RequestController requestController, ISchedulerProvider schedulerProvider) {
        this.requestController = requestController;
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * Call the requestController to create a new request
     * @param userRoute The route selected from the previous RouteSelector activity
     * @param price The price, either selected by the user or suggested by the app
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
