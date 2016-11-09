package com.cloudycrew.cloudycar.ridersummary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudycrew.cloudycar.AcceptedRequestAdapter;
import com.cloudycrew.cloudycar.BaseFragment;
import com.cloudycrew.cloudycar.ConfirmedRequestAdapter;
import com.cloudycrew.cloudycar.PendingRequestsAdapter;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.createrequest.CreateRequestActivity;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public class RiderSummaryFragment extends BaseFragment implements IRiderSummaryView {
    private RiderSummaryController riderSummaryController;
    private RecyclerView requestListView;
    private RecyclerView.Adapter requestToListViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<AcceptedRequest> accRequestList;
    ArrayList<PendingRequest> penRequestList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rider_summary, container, false);
        resolveDependencies();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRequestActivity(view);
            }
        });

        RecyclerView acceptedRequests = (RecyclerView) view.findViewById(R.id.accepted_requests);
        accRequestList = new ArrayList<AcceptedRequest>(); //AcceptedRequest.createContactsList(20);
        AcceptedRequestAdapter adapter = new AcceptedRequestAdapter(getActivity(), accRequestList); // Create adapter passing in the sample user data
        acceptedRequests.setAdapter(adapter); // Attach the adapter to the recyclerview to populate items
        acceptedRequests.setLayoutManager(new LinearLayoutManager(getActivity())); // Set layout manager to position the items

        RecyclerView pendingRequests = (RecyclerView) view.findViewById(R.id.pending_requests);
        penRequestList = new ArrayList<PendingRequest>(); //PendingRequest.createContactsList(20);
        PendingRequestsAdapter padapter = new PendingRequestsAdapter(getActivity(), penRequestList); // Create adapter passing in the sample user data
        pendingRequests.setAdapter(padapter); // Attach the adapter to the recyclerview to populate items
        pendingRequests.setLayoutManager(new LinearLayoutManager(getActivity())); // Set layout manager to position the items


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

    }

    @Override
    public void displayAcceptedRequests(List<AcceptedRequest> acceptedRequests) {

    }
}
