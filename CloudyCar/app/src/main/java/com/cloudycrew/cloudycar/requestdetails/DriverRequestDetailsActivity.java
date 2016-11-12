package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;
import android.view.View;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import butterknife.ButterKnife;

/**
 * Created by George on 2016-11-11.
 */

public class DriverRequestDetailsActivity extends BaseRequestDetailsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_details);
        ButterKnife.bind(this);
    }

    @Override
    public void displayPendingRequest(PendingRequest pendingRequest) {
        displayBaseRequestInformation(pendingRequest);
        statusTextView.setText("You have not accepted this ride");

        if (pendingRequest.hasBeenAcceptedBy(userPreferences.getUserName())) {
            updateButton.setText(R.string.accept_request_button_text);
            updateButton.setOnClickListener(v -> requestDetailsController.acceptRequest());
            updateButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayConfirmedRequest(ConfirmedRequest confirmedRequest) {
        displayBaseRequestInformation(confirmedRequest);
        statusTextView.setText("You have accepted this ride");
        updateButton.setVisibility(View.GONE);
    }

    @Override
    public void displayCompletedRequest(CompletedRequest completedRequest) {
        displayBaseRequestInformation(completedRequest);
    }
}
