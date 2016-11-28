package com.cloudycrew.cloudycar.ridersummary;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.observables.IObserver;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.users.IUserPreferences;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by George on 2016-11-05.
 */
public class RiderSummaryController extends ViewController<IRiderSummaryView> {
    private RequestController requestController;
    private IRequestStore requestStore;
    private IUserPreferences userPreferences;

    /**
     * Instantiates a new Rider summary controller.
     *
     * @param requestController the request controller
     * @param userPreferences   the user preferences
     * @param requestStore      the request store
     */
    public RiderSummaryController(RequestController requestController, IUserPreferences userPreferences, IRequestStore requestStore) {
        this.requestController = requestController;
        this.userPreferences = userPreferences;
        this.requestStore = requestStore;
    }

    /**
     * Refresh requests a rider is involved in asynchronously.
     */
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

    private IObserver<IRequestStore> requestStoreObserver = new IObserver<IRequestStore>() {
        @Override
        public void notifyUpdate(IRequestStore observable) {
            dispatchDisplayPendingRequests(getPendingRequestsThatHaveNotBeenAccepted());
            dispatchDisplayAcceptedRequests(getPendingRequestsThatHaveBeenAccepted());
            dispatchDisplayConfirmedRequests(getConfirmedRequestsForRider());
        }
    };

    private List<ConfirmedRequest> getConfirmedRequestsForRider() {
        return Observable.from(requestStore.getRequests(ConfirmedRequest.class))
                         .filter(new Func1<ConfirmedRequest, Boolean>() {
                             @Override
                             public Boolean call(ConfirmedRequest r) {
                                 return r.getRider().equals(userPreferences.getUserName());
                             }
                         })
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<ConfirmedRequest>());
    }

    private List<PendingRequest> getPendingRequestsThatHaveNotBeenAccepted() {
        return Observable.from(requestStore.getRequests(PendingRequest.class))
                         .filter(new Func1<PendingRequest, Boolean>() {
                             @Override
                             public Boolean call(PendingRequest r) {
                                 return r.getRider().equals(userPreferences.getUserName()) &&
                                         !r.hasBeenAccepted();
                             }
                         })
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<PendingRequest>());
    }

    private List<PendingRequest> getPendingRequestsThatHaveBeenAccepted() {
        return Observable.from(requestStore.getRequests(PendingRequest.class))
                         .filter(new Func1<PendingRequest, Boolean>() {
                             @Override
                             public Boolean call(PendingRequest r) {
                                 return r.getRider().equals(userPreferences.getUserName()) &&
                                         r.hasBeenAccepted();
                             }
                         })
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<PendingRequest>());
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

    private void dispatchDisplayConfirmedRequests(List<ConfirmedRequest> confirmedRequests) {
        if (getView() != null) {
            getView().displayConfirmedRequests(confirmedRequests);
        }
    }
}
