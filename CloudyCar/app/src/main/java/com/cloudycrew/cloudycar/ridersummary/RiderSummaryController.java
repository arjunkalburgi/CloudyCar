package com.cloudycrew.cloudycar.ridersummary;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.observables.IObserver;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.users.IUserPreferences;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by George on 2016-11-05.
 */
public class RiderSummaryController extends ViewController<IRiderSummaryView> {
    private RequestController requestController;
    private IRequestStore requestStore;
    private IUserPreferences userPreferences;
    private ISchedulerProvider schedulerProvider;

    /**
     * Instantiates a new Rider summary controller.
     *
     * @param requestController the request controller
     * @param userPreferences   the user preferences
     * @param requestStore      the request store
     */

    public RiderSummaryController(RequestController requestController,
                                  IUserPreferences userPreferences,
                                  IRequestStore requestStore,
                                  ISchedulerProvider schedulerProvider) {
        this.requestController = requestController;
        this.userPreferences = userPreferences;
        this.requestStore = requestStore;
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * Refresh requests a rider is involved in asynchronously.
     */
    public void refreshRequests() {
        dispatchShowLoading();
        requestController.refreshRequests();
    }

    public void deleteRequest(final String reqid) {
        ObservableUtils.create(new Action0() {
                            @Override
                            public void call() {
                                requestController.cancelRequest(reqid);
                            }
                        })
                        .subscribeOn(schedulerProvider.ioScheduler())
                        .subscribe();
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
            displaySortedConfirmedRequests();
            displaySortedAcceptedRequests();
            displaySortedPendingRequests();
        }
    };

    private void displaySortedConfirmedRequests() {
        Observable.from(requestStore.getRequests(ConfirmedRequest.class))
                  .observeOn(schedulerProvider.computationScheduler())
                  .filter(new Func1<ConfirmedRequest, Boolean>() {
                     @Override
                     public Boolean call(ConfirmedRequest r) {
                          return r.getRider().equals(userPreferences.getUserName());
                      }
                 })
                  .toSortedList(new Func2<ConfirmedRequest, ConfirmedRequest, Integer>() {
                     @Override
                     public Integer call(ConfirmedRequest confirmedRequest, ConfirmedRequest confirmedRequest2) {
                          return confirmedRequest2.getLastUpdated().compareTo(confirmedRequest.getLastUpdated());
                      }
                 })
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .subscribe(new Action1<List<ConfirmedRequest>>() {
                      @Override
                      public void call(List<ConfirmedRequest> confirmedRequests) {
                          dispatchDisplayConfirmedRequests(confirmedRequests);
                      }
                  });
    }

    private void displaySortedPendingRequests() {
        Observable.from(requestStore.getRequests(PendingRequest.class))
                  .filter(new Func1<PendingRequest, Boolean>() {
                     @Override
                     public Boolean call(PendingRequest r) {
                          return r.getRider().equals(userPreferences.getUserName()) &&
                                  !r.hasBeenAccepted();
                      }
                 })
                  .toSortedList(new Func2<PendingRequest, PendingRequest, Integer>() {
                     @Override
                     public Integer call(PendingRequest pendingRequest, PendingRequest pendingRequest2) {
                          return pendingRequest2.getLastUpdated().compareTo(pendingRequest.getLastUpdated());
                      }
                 })
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .subscribe(new Action1<List<PendingRequest>>() {
                      @Override
                      public void call(List<PendingRequest> pendingRequests) {
                          dispatchDisplayPendingRequests(pendingRequests);
                      }
                  });
    }

    private void displaySortedAcceptedRequests() {
        Observable.from(requestStore.getRequests(PendingRequest.class))
                  .observeOn(schedulerProvider.computationScheduler())
                  .filter(new Func1<PendingRequest, Boolean>() {
                     @Override
                     public Boolean call(PendingRequest r) {
                          return r.getRider().equals(userPreferences.getUserName()) &&
                                  r.hasBeenAccepted();
                      }
                 })
                  .toSortedList(new Func2<PendingRequest, PendingRequest, Integer>() {
                     @Override
                     public Integer call(PendingRequest pendingRequest, PendingRequest pendingRequest2) {
                          return pendingRequest2.getLastUpdated().compareTo(pendingRequest.getLastUpdated());
                      }
                 })
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .subscribe(new Action1<List<PendingRequest>>() {
                      @Override
                      public void call(List<PendingRequest> acceptedRequests) {
                          dispatchDisplayAcceptedRequests(acceptedRequests);
                      }
                  });
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
