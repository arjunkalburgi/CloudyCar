package com.cloudycrew.cloudycar.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requestdetails.DriverRequestDetailsActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.cloudycrew.cloudycar.viewcells.BaseRequestViewCell;
import com.cloudycrew.cloudycar.viewcells.PendingRequestViewCell;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.antonious.viewcelladapter.SectionViewCell;
import ca.antonious.viewcelladapter.ViewCellAdapter;
import rx.Observable;
import rx.functions.Func1;

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
//    @BindView(R.id.search_bar)
//    protected MaterialSearchView searchView;

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

        toolbar.setTitleTextColor(0xFFFFFFFF);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchController.attachView(this);
        SearchContext sc = (SearchContext) getIntent().getSerializableExtra("searchcontext");
        searchController.search(sc);
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

    private BaseRequestViewCell.OnRequestClickedListener onRequestClickedListener = new BaseRequestViewCell.OnRequestClickedListener() {
        @Override
        public void onRequestClicked(Request request) {
            launchRequestDetailsActivity(request.getId());

        }
    };

    private List<PendingRequestViewCell> getPendingRequestViewCells(List<? extends PendingRequest> pendingRequests) {
        return Observable.from(pendingRequests)
                         .map(new Func1<PendingRequest, PendingRequestViewCell>() {
                             @Override
                             public PendingRequestViewCell call(PendingRequest pendingRequest) {
                                 return new PendingRequestViewCell(pendingRequest);
                             }
                         })
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<PendingRequestViewCell>());
    }
}
