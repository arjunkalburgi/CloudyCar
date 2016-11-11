package com.cloudycrew.cloudycar.ridersummary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudycrew.cloudycar.BaseFragment;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.RequestAdapter;
import com.cloudycrew.cloudycar.createrequest.CreateRequestActivity;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public class RiderSummaryFragment extends BaseFragment implements IRiderSummaryView {
    private RiderSummaryController riderSummaryController;
    private RecyclerView requestView;
    private RequestAdapter requestAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rider_summary, container, false);
        resolveDependencies();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener((v) -> startRequestActivity(v));

        layoutManager = new LinearLayoutManager(getActivity());

        requestView = (RecyclerView) view.findViewById(R.id.rider_requests);
        requestAdapter = new RequestAdapter(); // Create adapter passing in the sample user data
        requestView.setAdapter(requestAdapter); // Attach the adapter to the recyclerview to populate items
        requestView.setLayoutManager(layoutManager); // Set layout manager to position the items

        requestAdapter.setClickListener((view1, request) -> {
            // do nothing for now
        });

        return view;
    }

    private void startRequestActivity(View view) {
        Intent intent = new Intent(getActivity(), CreateRequestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.rider_summary_header);
    }

    @Override
    public void onResume() {
        super.onResume();
        riderSummaryController.attachView(this);
        riderSummaryController.refreshRequests();
    }

    @Override
    public void onPause() {
        super.onPause();
        riderSummaryController.detachView();
    }

    private void resolveDependencies() {
        riderSummaryController = getCloudyCarApplication().getRiderSummaryController();
    }

    @Override
    public void displayLoading() {
        
    }

    @Override
    public void displayPendingRequests(List<PendingRequest> pendingRequests) {
        requestAdapter.setPendingRequests(pendingRequests);
    }

    @Override
    public void displayAcceptedRequests(List<PendingRequest> acceptedRequests) {
        requestAdapter.setAcceptedRequests(acceptedRequests);
    }

    public void displayConfirmedRequests(List<ConfirmedRequest> confirmedRequests) {
        requestAdapter.setConfirmedRequests(confirmedRequests);
    }
}
