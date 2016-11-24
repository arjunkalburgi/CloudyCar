package com.cloudycrew.cloudycar.summarycontainer;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.observables.IObserver;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;

import java.util.Date;

import rx.Observable;

/**
 * Created by George on 2016-11-23.
 */

public class SummaryMenuController extends ViewController<ISummaryMenuView> {
    private IRequestStore requestStore;
    private UserController userController;

    public SummaryMenuController(UserController userController, IRequestStore requestStore) {
        this.userController = userController;
        this.requestStore = requestStore;
    }

    @Override
    public void attachView(ISummaryMenuView view) {
        super.attachView(view);
        requestStore.addObserver(requestStoreObserver);
    }

    @Override
    public void detachView() {
        super.detachView();
        requestStore.removeObserver(requestStoreObserver);
    }

    private IObserver<IRequestStore> requestStoreObserver = store -> {
        dispatchDisplayUnreadRiderRequests(getTotalUnreadRiderRequests());
        dispatchDisplayUnreadDriverRequests(getTotalUnreadDriverRequests());
    };

    private int getTotalUnreadRiderRequests() {
        return Observable.from(requestStore.getRequests())
                         .filter(r -> r.getRider().equals(getCurrentUsername()))
                         .filter(r -> !isRequestRead(r))
                         .count()
                         .toBlocking()
                         .first();
    }

    private int getTotalUnreadDriverRequests() {
        return Observable.from(requestStore.getRequests())
                         .filter(r -> isCurrentUserADriverForRequest(r))
                         .filter(r -> !isRequestRead(r))
                         .count()
                         .toBlocking()
                         .first();
    }

    private boolean isCurrentUserADriverForRequest(Request request) {
        if (request instanceof PendingRequest) {
            PendingRequest pendingRequest = (PendingRequest) request;
            return pendingRequest.hasBeenAcceptedBy(getCurrentUsername());
        } else if (request instanceof CompletedRequest) {
            CompletedRequest completedRequest = (CompletedRequest) request;
            return completedRequest.getDriverUsername().equals(getCurrentUsername());
        }
        return false;
    }

    private boolean isRequestRead(Request request) {
        Date lastUserReadTime = userController.getLastReadTime(request.getId());

        return lastUserReadTime == null ||
                request.getLastUpdated().compareTo(lastUserReadTime) > 0;
    }

    private String getCurrentUsername() {
        return userController.getCurrentUser().getUsername();
    }

    private void dispatchDisplayUnreadRiderRequests(int numUnread) {
        if (isViewAttached()) {
            getView().displayTotalUnreadRiderRequests(numUnread);
        }
    }

    private void dispatchDisplayUnreadDriverRequests(int numUnread) {
        if (isViewAttached()) {
            getView().displayTotalUnreadDriverRequests(numUnread);
        }
    }
}
