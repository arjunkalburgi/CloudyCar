package com.cloudycrew.cloudycar.driversummary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudycrew.cloudycar.BaseFragment;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public class DriverSummaryFragment extends BaseFragment implements IDriverSummaryView {
    private DriverSummaryController driverSummaryController;

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
