package com.cloudycrew.cloudycar.driversummary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudycrew.cloudycar.BaseFragment;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.requestdetails.DriverRequestDetailsActivity;
import com.cloudycrew.cloudycar.search.SearchActivity;
import com.cloudycrew.cloudycar.search.SearchParamsActivity;
import com.cloudycrew.cloudycar.viewcells.AcceptedRequestViewCell;
import com.cloudycrew.cloudycar.viewcells.BaseRequestViewCell;
import com.cloudycrew.cloudycar.viewcells.ConfirmedRequestViewCell;
import com.cloudycrew.cloudycar.viewcells.DriverAcceptedRequestViewCell;
import com.cloudycrew.cloudycar.viewcells.HeaderViewCell;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.antonious.viewcelladapter.SectionWithHeaderViewCell;
import ca.antonious.viewcelladapter.ViewCellAdapter;
import rx.Observable;

/**
 * Created by George on 2016-11-05.
 */

public class DriverSummaryFragment extends BaseFragment implements IDriverSummaryView {
    @BindView(R.id.accepted_offers_list)
    protected RecyclerView requestView;
    @BindView(R.id.driver_summary_swipe_container)
    protected SwipeRefreshLayout swipeRefreshLayout;

    private ViewCellAdapter viewCellAdapter;
    private SectionWithHeaderViewCell confirmedRequestsSection;
    private SectionWithHeaderViewCell acceptedRequestsSection;

    private DriverSummaryController driverSummaryController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_summary, container, false);
        ButterKnife.bind(this, view);

        resolveDependencies();
        setUpRecyclerView();
        setUpSwipeRefreshLayout();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setActivityTitle("Be a Driver");
    }

    @Override
    public void onResume() {
        driverSummaryController.attachView(this);
        super.onResume();
    }

    @Override
    public void onFirstResume() {
        super.onFirstResume();
        driverSummaryController.refreshRequests();
    }

    @Override
    public void onPause() {
        super.onPause();
        driverSummaryController.detachView();
    }

    private void resolveDependencies() {
        driverSummaryController = getCloudyCarApplication().getDriverSummaryController();
    }

    private void setUpSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(() -> driverSummaryController.refreshRequests());
    }

    private void setUpRecyclerView() {
        viewCellAdapter = new ViewCellAdapter();
        viewCellAdapter.setHasStableIds(true);

        confirmedRequestsSection = new SectionWithHeaderViewCell();
        confirmedRequestsSection.setShowHeaderIfEmpty(false);
        confirmedRequestsSection.setSectionHeader(new HeaderViewCell("Confirmed Requests"));

        acceptedRequestsSection = new SectionWithHeaderViewCell();
        acceptedRequestsSection.setShowHeaderIfEmpty(false);
        acceptedRequestsSection.setSectionHeader(new HeaderViewCell("Accepted Requests"));

        viewCellAdapter.add(confirmedRequestsSection);
        viewCellAdapter.add(acceptedRequestsSection);

        viewCellAdapter.addListener(onRequestClickedListener);

        requestView.setAdapter(viewCellAdapter);
        requestView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @OnClick(R.id.fab)
    protected void onSearchRequestsClicked() {
        startActivity(new Intent(getActivity(), SearchParamsActivity.class));
    }

    private void launchRequestDetailsActivity(String requestId) {
        Intent intent = new Intent(getActivity(), DriverRequestDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_REQUEST_ID, requestId);
        startActivity(intent);
    }

    @Override
    public void displayLoading() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    public void stopLoading() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void displayAcceptedRequests(List<PendingRequest> acceptedRequests) {
        stopLoading();
        acceptedRequestsSection.setAll(getAcceptedRequestViewCells(acceptedRequests));
        viewCellAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayConfirmedRequests(List<ConfirmedRequest> confirmedRequests) {
        stopLoading();
        confirmedRequestsSection.setAll(getConfirmedRequestViewCells(confirmedRequests));
        viewCellAdapter.notifyDataSetChanged();
    }

    private BaseRequestViewCell.OnRequestClickedListener onRequestClickedListener = request -> {
        launchRequestDetailsActivity(request.getId());
    };


    private List<DriverAcceptedRequestViewCell> getAcceptedRequestViewCells(List<? extends PendingRequest> pendingRequests) {
        return Observable.from(pendingRequests)
                         .map(DriverAcceptedRequestViewCell::new)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }

    private List<ConfirmedRequestViewCell> getConfirmedRequestViewCells(List<? extends ConfirmedRequest> confirmedRequests) {
        return Observable.from(confirmedRequests)
                         .map(ConfirmedRequestViewCell::new)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }
}
