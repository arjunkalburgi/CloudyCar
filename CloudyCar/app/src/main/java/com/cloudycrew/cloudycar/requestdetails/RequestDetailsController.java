package com.cloudycrew.cloudycar.requestdetails;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
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
    private UserController userController;

    /**
     * Instantiates a new Request details controller.
     *
     * @param requestId         the request id
     * @param requestController the request controller
     * @param schedulerProvider the scheduler provider
     * @param requestStore      the request store
     */
    public RequestDetailsController(String requestId,
                                    RequestController requestController,
                                    UserController userController,
                                    ISchedulerProvider schedulerProvider,
                                    IRequestStore requestStore) {
        this.requestId = requestId;
        this.requestController = requestController;
        this.userController = userController;
        this.schedulerProvider = schedulerProvider;
        this.requestStore = requestStore;
    }

    /**
     * Accept request.
     */
    public void acceptRequest() {
        ObservableUtils.fromAction(requestController::acceptRequest, requestId)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe();
    }

    /**
     * Confirm request.
     *
     * @param driverUsername the driver username
     */
    public void confirmRequest(String driverUsername) {
        ObservableUtils.fromAction(requestController::confirmRequest, requestId, driverUsername)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe();
    }

    /**
     * Complete request.
     */
    public void completeRequest() {
        ObservableUtils.fromAction(requestController::completeRequest, requestId)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe();
    }

    public void markRequestAsRead() {
        userController.markRequestAsRead(requestId);
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
        Request request = store.getRequest(requestId);

        if (request instanceof PendingRequest) {
            dispatchDisplayPendingRequest((PendingRequest) request);
        } else if (request instanceof ConfirmedRequest) {
            dispatchDisplayConfirmedRequest((ConfirmedRequest) request);
        } else if (request instanceof CompletedRequest) {
            dispatchDisplayCompletedRequest((CompletedRequest) request);
        }
    };

    private void dispatchDisplayPendingRequest(PendingRequest pendingRequest) {
        if (isViewAttached()) {
            getView().displayPendingRequest(pendingRequest);
        }
    }

    private void dispatchDisplayConfirmedRequest(ConfirmedRequest confirmedRequest) {
        if (isViewAttached()) {
            getView().displayConfirmedRequest(confirmedRequest);
        }
    }

    private void dispatchDisplayCompletedRequest(CompletedRequest completedRequest) {
        if (isViewAttached()) {
            getView().displayCompletedRequest(completedRequest);
        }
    }
}
