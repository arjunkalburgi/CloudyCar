package com.cloudycrew.cloudycar.search;

import android.os.Bundle;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public class SearchActivity extends BaseActivity implements ISearchView {
    private SearchController searchController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: setContentView

        resolveDependencies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchController.attachView(this);
    }

    @Override
    protected void onPause() {
        searchController.detachView();
    }

    private void resolveDependencies() {
        searchController = getCloudyCarApplication().getSearchController();
    }

    @Override
    public void showLoading() {
        
    }

    @Override
    public void showSearchResults(List<Request> searchResults) {

    }
}
