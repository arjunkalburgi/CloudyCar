package com.cloudycrew.cloudycar.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.requestdetails.DriverRequestDetailsActivity;
import com.cloudycrew.cloudycar.requestdetails.RiderRequestDetailsActivity;
import com.cloudycrew.cloudycar.viewcells.BaseRequestViewCell;
import com.cloudycrew.cloudycar.viewcells.PendingRequestViewCell;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.antonious.viewcelladapter.SectionViewCell;
import ca.antonious.viewcelladapter.ViewCellAdapter;
import rx.Observable;

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

    private ViewCellAdapter viewCellAdapter;
    private SectionViewCell searchResultsSection;

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
        super.onPause();
        searchController.detachView();
    }

    private void resolveDependencies() {
        searchController = getCloudyCarApplication().getSearchController();
    }

    private void setUpRecyclerView() {
        viewCellAdapter = new ViewCellAdapter();
        viewCellAdapter.setHasStableIds(true);

        searchResultsSection = new SectionViewCell();

        viewCellAdapter.add(searchResultsSection);
        viewCellAdapter.addListener(onRequestClickedListener);

        searchRecyclerView.setAdapter(viewCellAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showLoading() {
        searchProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSearchResults(List<PendingRequest> searchResults) {
        searchProgressBar.setVisibility(View.GONE);

        searchResultsSection.setAll(getPendingRequestViewCells(searchResults));
        viewCellAdapter.notifyDataSetChanged();
    }

    private void launchRequestDetailsActivity(String requestId) {
        Intent intent = new Intent(this, DriverRequestDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_REQUEST_ID, requestId);
        startActivity(intent);
    }

    private BaseRequestViewCell.OnRequestClickedListener onRequestClickedListener = request -> {
        launchRequestDetailsActivity(request.getId());
    };

    private List<PendingRequestViewCell> getPendingRequestViewCells(List<? extends PendingRequest> pendingRequests) {
        return Observable.from(pendingRequests)
                         .map(PendingRequestViewCell::new)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }
}
