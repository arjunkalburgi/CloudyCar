package com.cloudycrew.cloudycar.driversummary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudycrew.cloudycar.BaseFragment;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.RequestAdapter;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.requestdetails.DriverRequestDetailsActivity;
import com.cloudycrew.cloudycar.requestdetails.RiderRequestDetailsActivity;
import com.cloudycrew.cloudycar.search.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by George on 2016-11-05.
 */

public class DriverSummaryFragment extends BaseFragment implements IDriverSummaryView {
    @BindView(R.id.accepted_offers_list)
    protected RecyclerView requestView;

    private DriverSummaryController driverSummaryController;
    private RequestAdapter requestAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_summary, container, false);
        ButterKnife.bind(this, view);
        resolveDependencies();
        setUpRecyclerView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Be a Driver");
        requestAdapter.setClickListener((v, r) -> launchRequestDetailsActivity(r.getId()));
    }

    @Override
    public void onResume() {
        super.onResume();
        driverSummaryController.attachView(this);
        driverSummaryController.refreshRequests();
    }

    @Override
    public void onPause() {
        super.onPause();
        driverSummaryController.detachView();
    }

    @OnClick(R.id.fab)
    protected void onSearchRequestsClicked() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    private void resolveDependencies() {
        driverSummaryController = getCloudyCarApplication().getDriverSummaryController();
    }

    private void setUpRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity());

        requestAdapter = new RequestAdapter();
        requestView.setAdapter(requestAdapter);
        requestView.setLayoutManager(layoutManager);

    }

    private void launchRequestDetailsActivity(String requestId) {
        Intent intent = new Intent(getActivity(), DriverRequestDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_REQUEST_ID, requestId);
        startActivity(intent);
    }

    @Override
    public void displayLoading() {

    }

    @Override
    public void displayAcceptedRequests(List<PendingRequest> acceptedRequests) {
        requestAdapter.setAcceptedRequests(acceptedRequests);
    }

    @Override
    public void displayConfirmedRequests(List<ConfirmedRequest> confirmedRequests) {
        requestAdapter.setConfirmedRequests(confirmedRequests);
    }
}
