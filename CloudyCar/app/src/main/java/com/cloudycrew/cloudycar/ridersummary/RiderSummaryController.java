package com.cloudycrew.cloudycar.ridersummary;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.observables.IObserver;
import com.cloudycrew.cloudycar.requeststorage.RequestStore;
import com.cloudycrew.cloudycar.requeststorage.Store;

/**
 * Created by George on 2016-11-05.
 */

public class RiderSummaryController extends ViewController<IRiderSummaryView> {
    private RequestController requestController;
    private RequestStore requestStore;

    public void refreshRequests() {
        requestController.refreshRequests();
    }

    IObserver<Store<Request>> requestStoreObserver = store -> {

    };

    @Override
    public void attachView(IRiderSummaryView view) {
        super.attachView(view);
        requestStore.addObserver(requestStoreObserver);
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
