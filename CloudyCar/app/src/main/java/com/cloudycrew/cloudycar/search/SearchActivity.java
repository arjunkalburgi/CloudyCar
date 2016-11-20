package com.cloudycrew.cloudycar.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.RequestAdapter;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.requestdetails.DriverRequestDetailsActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

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
    @BindView(R.id.search_bar)
    protected MaterialSearchView searchView;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        searchView.showSearch(false);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.closeSearch();
                searchController.searchByKeyword(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                toolbar.setTitleTextColor(0xFFFFFFFF);
            }
        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchController.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchController.detachView();
    }

    private void resolveDependencies() {
        searchController = getCloudyCarApplication().getSearchController();
    }

    private void setUpRecyclerView() {
        requestAdapter = new RequestAdapter(true);
        searchRecyclerView.setAdapter(requestAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        requestAdapter.setClickListener((v, r) -> {
            Intent intent = new Intent(SearchActivity.this, DriverRequestDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_REQUEST_ID, r.getId());
            startActivity(intent);
        });
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
