package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.GeoDecoder;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.users.IUserPreferences;
import com.cloudycrew.cloudycar.users.UserPreferences;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by George on 2016-11-05.
 */

public class RiderRequestDetailsActivity extends BaseRequestDetailsActivity {
    @BindView(R.id.accepted_drivers_recycler_view)
    protected RecyclerView acceptedDriversRecyclerView;
    @BindView(R.id.accepted_drivers_header)
    protected TextView acceptedDriversHeader;

    private AcceptedDriversAdapter acceptedDriversAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        ButterKnife.bind(this);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        acceptedDriversAdapter = new AcceptedDriversAdapter();
        acceptedDriversRecyclerView.setAdapter(acceptedDriversAdapter);
        acceptedDriversRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void displayPendingRequest(PendingRequest pendingRequest) {
        displayBaseRequestInformation(pendingRequest);

        statusTextView.setText("Pending");
        acceptedDriversAdapter.setAll(pendingRequest.getDriversWhoAccepted());
        acceptedDriversAdapter.notifyDataSetChanged();
        acceptedDriversAdapter.setOnConfirmClickedListener((i, username) -> requestDetailsController.confirmRequest(username));

        acceptedDriversHeader.setVisibility(View.VISIBLE);
        acceptedDriversRecyclerView.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.GONE);
    }

    @Override
    public void displayConfirmedRequest(ConfirmedRequest confirmedRequest) {
        displayBaseRequestInformation(confirmedRequest);

        statusTextView.setText("Confirmed");
        acceptedDriversHeader.setVisibility(View.GONE);
        acceptedDriversRecyclerView.setVisibility(View.GONE);

        updateButton.setText(R.string.confirm_request_button_text);
        updateButton.setOnClickListener(v -> requestDetailsController.completeRequest());
        updateButton.setVisibility(View.VISIBLE);

        setRider(confirmedRequest.getRider());
        setDriver(confirmedRequest.getDriverUsername());
    }

    @Override
    public void displayCompletedRequest(CompletedRequest completedRequest) {
        displayBaseRequestInformation(completedRequest);

        statusTextView.setText("Completed");
        acceptedDriversHeader.setVisibility(View.GONE);
        acceptedDriversRecyclerView.setVisibility(View.GONE);

        updateButton.setText(R.string.confirm_request_button_text);
        updateButton.setOnClickListener(v -> requestDetailsController.completeRequest());
        updateButton.setVisibility(View.VISIBLE);

        setRider(completedRequest.getRider());
        setDriver(completedRequest.getDriverUsername());
    }
}
