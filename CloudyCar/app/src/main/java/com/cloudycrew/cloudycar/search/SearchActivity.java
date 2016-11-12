package com.cloudycrew.cloudycar.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.RequestAdapter;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by George on 2016-11-05.
 */

public class SearchActivity extends BaseActivity implements ISearchView {
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.search_loading_progress_bar)
    protected ProgressBar searchProgressBar;
    @BindView(R.id.search_results_recycler_view)
    protected RecyclerView searchRecyclerView;

    private RequestAdapter requestAdapter;
    private SearchController searchController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        resolveDependencies();
        setUpRecyclerView();
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

    private void setUpRecyclerView() {
        requestAdapter = new RequestAdapter();
        searchRecyclerView.setAdapter(requestAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void showLoading() {
        searchProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSearchResults(List<PendingRequest> searchResults) {
        searchProgressBar.setVisibility(View.GONE);
        requestAdapter.setPendingRequests(searchResults);
    }
}
