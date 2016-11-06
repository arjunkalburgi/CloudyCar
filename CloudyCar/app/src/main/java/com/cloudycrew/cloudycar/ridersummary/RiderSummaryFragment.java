package com.cloudycrew.cloudycar.ridersummary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudycrew.cloudycar.BaseFragment;
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
        // TODO: STUBBED IN: Replace with actual layout and return inflated view
        // View view = inflater.inflate(R.layout.FRAGMENT_LAYOUT, container, false);
        resolveDependencies();

        return null;
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
