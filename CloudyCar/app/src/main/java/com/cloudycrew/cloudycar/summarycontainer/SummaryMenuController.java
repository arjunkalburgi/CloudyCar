package com.cloudycrew.cloudycar.summarycontainer;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.observables.IObserver;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;

import java.util.Date;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by George on 2016-11-23.
 */

public class SummaryMenuController extends ViewController<ISummaryMenuView> {
    private IRequestStore requestStore;
    private UserController userController;

    /**
     * Instantiates a SummaryMenuController
     *
     * @param userController a usercontroller
     * @param requestStore a requeststore
     */
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

    private IObserver<IRequestStore> requestStoreObserver = new IObserver<IRequestStore>() {
        @Override
        public void notifyUpdate(IRequestStore observable) {
            dispatchDisplayUnreadRiderRequests(getTotalUnreadRiderRequests());
            dispatchDisplayUnreadDriverRequests(getTotalUnreadDriverRequests());
        }
    };

    private int getTotalUnreadRiderRequests() {
        return Observable.from(requestStore.getRequests())
                         .filter(new Func1<Request, Boolean>() {
                             @Override
                             public Boolean call(Request r) {
                                 return r.getRider().equals(getCurrentUsername()) &&
                                         !isRequestRead(r);
                             }
                         })
                         .count()
                         .toBlocking()
                         .first();
    }

    private int getTotalUnreadDriverRequests() {
        return Observable.from(requestStore.getRequests())
                         .filter(new Func1<Request, Boolean>() {
                             @Override
                             public Boolean call(Request r) {
                                 return shouldDriverRequestBeNotified(r) &&
                                         !isRequestRead(r);
                             }
                         })
                         .count()
                         .toBlocking()
                         .first();
    }

    private boolean shouldDriverRequestBeNotified(Request request) {
        if (request instanceof ConfirmedRequest) {
            ConfirmedRequest confirmedRequest = (ConfirmedRequest) request;
            return confirmedRequest.getDriverUsername().equals(getCurrentUsername());
        }
        return false;
    }

    private boolean isRequestRead(Request request) {
        Date lastUserReadTime = userController.getLastReadTime(request.getId());

        return lastUserReadTime != null &&
                lastUserReadTime.compareTo(request.getLastUpdated()) >= 0;
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
