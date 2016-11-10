package com.cloudycrew.cloudycar.requestdetails;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.observables.IObserver;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;

/**
 * Created by George on 2016-11-05.
 */

public class RequestDetailsController extends ViewController<IRequestDetailsView> {
    private String requestId;
    private IRequestStore requestStore;

    public RequestDetailsController(String requestId, IRequestStore requestStore) {
        this.requestId = requestId;
        this.requestStore = requestStore;
    }

    @Override
    public void attachView(IRequestDetailsView view) {
        super.attachView(view);
        requestStore.addObserver(requestStoreObserver);
    }

    @Override
    public void detachView() {
        super.detachView();
        requestStore.removeObserver(requestStoreObserver);
    }

    private IObserver<IRequestStore> requestStoreObserver = store -> {
        dispatchDisplayRequest(store.getRequest(requestId));
    };

    private void dispatchDisplayRequest(Request request) {
        if (getView() != null) {
            getView().displayRequest(request);
        }
    }
}
