package com.cloudycrew.cloudycar.requestdetails;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.observables.IObserver;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

/**
 * Created by George on 2016-11-05.
 */

public class RequestDetailsController extends ViewController<IRequestDetailsView> {
    private String requestId;
    private IRequestStore requestStore;
    private ISchedulerProvider schedulerProvider;
    private RequestController requestController;

    public RequestDetailsController(String requestId, RequestController requestController, ISchedulerProvider schedulerProvider, IRequestStore requestStore) {
        this.requestId = requestId;
        this.requestController = requestController;
        this.schedulerProvider = schedulerProvider;
        this.requestStore = requestStore;
    }

    public void acceptRequest() {
        ObservableUtils.fromAction(requestController::acceptRequest, requestId)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe();
    }

    public void confirmRequest(String driverUsername) {
        ObservableUtils.fromAction(requestController::confirmRequest, requestId, driverUsername)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe();
    }

    public void completeRequest() {
        ObservableUtils.fromAction(requestController::completeRequest, requestId)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe();
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
