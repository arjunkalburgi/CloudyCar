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
import com.cloudycrew.cloudycar.requestdetails.RequestDetailsActivity;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public class DriverSummaryFragment extends BaseFragment implements IDriverSummaryView {
    private DriverSummaryController driverSummaryController;
    private RecyclerView requestView;
    private RequestAdapter requestAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_summary, container, false);
        resolveDependencies();

        // Lookup the recyclerview in activity layout
        layoutManager = new LinearLayoutManager(getActivity());

        requestView = (RecyclerView) view.findViewById(R.id.accepted_offers_list);
        requestAdapter = new RequestAdapter(); // Create adapter
        requestView.setAdapter(requestAdapter); // Attach the adapter to the recyclerview to populate items
        requestView.setLayoutManager(layoutManager); // Set layout manager to position the items

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
    }

    @Override
    public void onPause() {
        super.onPause();
        driverSummaryController.detachView();
    }

    private void resolveDependencies() {
        driverSummaryController = getCloudyCarApplication().getDriverSummaryController();
    }

    private void launchRequestDetailsActivity(String requestId) {
        Intent intent = new Intent(getActivity(), RequestDetailsActivity.class);
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
