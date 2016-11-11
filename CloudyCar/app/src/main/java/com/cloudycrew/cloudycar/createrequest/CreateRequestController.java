package com.cloudycrew.cloudycar.createrequest;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

/**
 * Created by George on 2016-11-05.
 */

public class CreateRequestController extends ViewController<ICreateRequestView> {
    private RequestController requestController;
    public CreateRequestController(RequestController requestController) {
        this.requestController = requestController;
    }
    public void saveRequest(Route userRoute, double price) {
        PendingRequest newRequest = new PendingRequest(null,userRoute,price);
        requestController.createRequest(newRequest);
    }
}
