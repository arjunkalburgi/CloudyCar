package com.cloudycrew.cloudycar.ridersummary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudycrew.cloudycar.BaseFragment;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public class RiderSummaryFragment extends BaseFragment implements IRiderSummaryView {
    private RiderSummaryController riderSummaryController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rider_summary, container, false);
        resolveDependencies();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Be a Rider");
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
