package com.cloudycrew.cloudycar.search;

import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */
public interface ISearchView {
    /**
     * Callback for when the search results are loading.
     */
    void showLoading();

    /**
     * Callback for when search results are successfully fetched
     *
     * @param searchResults the search results
     */
    void showSearchResults(List<PendingRequest> searchResults);
}
