package com.cloudycrew.cloudycar.driversummary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudycrew.cloudycar.ConfirmedRequestAdapter;
import com.cloudycrew.cloudycar.BaseFragment;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public class DriverSummaryFragment extends BaseFragment implements IDriverSummaryView {
    private DriverSummaryController driverSummaryController;
    ArrayList<ConfirmedRequest> requestList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_summary, container, false);
        resolveDependencies();

        // Lookup the recyclerview in activity layout
        RecyclerView confirmedRequests = (RecyclerView) view.findViewById(R.id.acceptedOffersList);

        // Initialize contacts
        requestList = new ArrayList<ConfirmedRequest>(); //AcceptedRequest.createContactsList(20);
        ConfirmedRequestAdapter adapter = new ConfirmedRequestAdapter(getActivity(), requestList); // Create adapter passing in the sample user data
        confirmedRequests.setAdapter(adapter); // Attach the adapter to the recyclerview to populate items
        confirmedRequests.setLayoutManager(new LinearLayoutManager(getActivity())); // Set layout manager to position the items

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Be a Driver");
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

    @Override
    public void displayLoading() {

    }

    @Override
    public void displayAcceptedRequests(List<AcceptedRequest> acceptedRequests) {

    }

    @Override
    public void displayConfirmedRequests(List<ConfirmedRequest> confirmedRequests) {

    }
}
