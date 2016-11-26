package com.cloudycrew.cloudycar.search;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */
public class SearchController extends ViewController<ISearchView> {
    private ISearchService searchService;
    private ISchedulerProvider schedulerProvider;

    /**
     * Instantiates a new Search controller.
     *
     * @param searchService     the search service
     * @param schedulerProvider the scheduler provider
     */
    public SearchController(ISearchService searchService, ISchedulerProvider schedulerProvider) {
        this.searchService = searchService;
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * Asynchronously searches for requests that match the search context
     *
     * @param searchContext the context of the Search
     */
    public void search(SearchContext searchContext) {
        dispatchShowLoading();

        ObservableUtils.fromFunction(searchService::search, searchContext)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe(this::dispatchShowSearchResults);
    }

    private void dispatchShowLoading() {
        if (isViewAttached()) {
            getView().showLoading();
        }
    }

    private void dispatchShowSearchResults(List<PendingRequest> searchResults) {
        if (isViewAttached()) {
            getView().showSearchResults(searchResults);
        }
    }
}
