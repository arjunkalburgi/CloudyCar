package com.cloudycrew.cloudycar.search;

import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public interface ISearchView {
    void showLoading();
    void showSearchResults(List<Request> searchResults);
}
