package com.cloudycrew.cloudycar.ridersummary;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.observables.IObserver;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by George on 2016-11-05.
 */

public class RiderSummaryController extends ViewController<IRiderSummaryView> {
    private RequestController requestController;
    private IRequestStore requestStore;

    public RiderSummaryController(RequestController requestController, IRequestStore requestStore) {
        this.requestController = requestController;
        this.requestStore = requestStore;
    }
    public void refreshRequests() {
        dispatchShowLoading();
        requestController.refreshRequests();
    }

    @Override
    public void attachView(IRiderSummaryView view) {
        super.attachView(view);
        requestStore.addObserver(requestStoreObserver);
    }

    @Override
    public void detachView() {
        super.detachView();
        requestStore.removeObserver(requestStoreObserver);
    }

    private IObserver<IRequestStore> requestStoreObserver = store -> {
        dispatchDisplayPendingRequests(store.getRequests(PendingRequest.class));
        dispatchDisplayAcceptedRequests(getPendingRequestsThatHaveBeenAccepted());
    };

    private List<PendingRequest> getPendingRequestsThatHaveBeenAccepted() {
        return Observable.from(requestStore.getRequests(PendingRequest.class))
                         .filter(PendingRequest::hasBeenAccepted)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }

    private void dispatchShowLoading() {
        if (getView() != null) {
            getView().displayLoading();

        }
    }
    private void dispatchDisplayPendingRequests(List<PendingRequest> pendingRequests) {
        if (getView() != null) {
            getView().displayPendingRequests(pendingRequests);
        }
    }

    private void dispatchDisplayAcceptedRequests(List<PendingRequest> acceptedRequests) {
        if (getView() != null) {
            getView().displayAcceptedRequests(acceptedRequests);
        }
    }
}
