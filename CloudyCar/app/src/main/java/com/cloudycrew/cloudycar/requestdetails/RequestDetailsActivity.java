package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.users.IUserPreferences;
import com.cloudycrew.cloudycar.users.UserPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by George on 2016-11-05.
 */

public class RequestDetailsActivity extends BaseActivity implements IRequestDetailsView {
    @BindView(R.id.request_details_from)
    protected TextView fromTextView;
    @BindView(R.id.request_details_to)
    protected TextView toTextView;
    @BindView(R.id.request_details_price)
    protected TextView priceTextView;
    @BindView(R.id.request_details_status)
    protected TextView statusTextView;
    @BindView(R.id.request_details_update_button)
    protected TextView updateButton;
    @BindView(R.id.accepted_drivers_recycler_view)
    protected RecyclerView acceptedDriversRecyclerView;
    @BindView(R.id.accepted_drivers_header)
    protected TextView acceptedDriversHeader;

    private AcceptedDriversAdapter acceptedDriversAdapter;
    private RequestDetailsController requestDetailsController;
    private IUserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        ButterKnife.bind(this);
        resolveDependencies();
        setUpRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onPause();
        requestDetailsController.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onResume();
        requestDetailsController.detachView();
    }

    private void resolveDependencies() {
        String requestId = getIntent().getStringExtra(Constants.EXTRA_REQUEST_ID);
        this.requestDetailsController = getCloudyCarApplication().getRequestDetailsController(requestId);
        this.userPreferences = getCloudyCarApplication().getUserPreferences();
    }

    private void setUpRecyclerView() {
        acceptedDriversAdapter = new AcceptedDriversAdapter();
        acceptedDriversRecyclerView.setAdapter(acceptedDriversAdapter);
        acceptedDriversRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void displayRequest(Request request) {
        fromTextView.setText(String.valueOf(request.getRoute().getStartingPoint().getLatitude()));
        toTextView.setText(String.valueOf(request.getRoute().getEndingPoint().getLatitude()));
        priceTextView.setText(String.format("$%.2f",request.getPrice()));

        if (request instanceof PendingRequest) {
            PendingRequest pendingRequest = (PendingRequest) request;

            if (pendingRequest.getRider().equals(userPreferences.getUserName())) {
                displayAsPendingRider(pendingRequest);
            } else {
                displayAsPendingDriver(pendingRequest);
            }
        } else if (request instanceof  ConfirmedRequest) {
            ConfirmedRequest confirmedRequest = (ConfirmedRequest) request;

            if (confirmedRequest.getRider().equals(userPreferences.getUserName())) {
                displayAsConfirmedRider(confirmedRequest);
            } else {
                displayAsConfirmedRider(confirmedRequest);
            }
        }
    }

    private void displayAsPendingRider(PendingRequest pendingRequest) {
        // Show drivers
        // Give option to accept a driver
        statusTextView.setText("Pending");

        acceptedDriversAdapter.setAll(pendingRequest.getDriversWhoAccepted());
        acceptedDriversAdapter.notifyDataSetChanged();
        acceptedDriversAdapter.setOnConfirmClickedListener((i, username) -> requestDetailsController.confirmRequest(username));

        acceptedDriversHeader.setVisibility(View.VISIBLE);
        acceptedDriversRecyclerView.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.GONE);
    }

    private void displayAsConfirmedRider(ConfirmedRequest confirmedRequest) {
        // Show complete button
        statusTextView.setText("Confirmed");
        acceptedDriversHeader.setVisibility(View.GONE);
        acceptedDriversRecyclerView.setVisibility(View.GONE);

        updateButton.setText("Confirm Request");
        updateButton.setOnClickListener(v -> requestDetailsController.completeRequest());
        updateButton.setVisibility(View.VISIBLE);
    }

    private void displayAsPendingDriver(PendingRequest pendingRequest) {
        // Give option to
        statusTextView.setText("You have not accepted this ride");
        acceptedDriversHeader.setVisibility(View.GONE);
        acceptedDriversRecyclerView.setVisibility(View.GONE);

        updateButton.setText("Accept Request");
        updateButton.setOnClickListener(v -> requestDetailsController.acceptRequest());
        updateButton.setVisibility(View.VISIBLE);
    }

    private void displayAsConfirmedDriver(ConfirmedRequest confirmedRequest) {
        // Just give deteails
        statusTextView.setText("You have accepted this ride");
        acceptedDriversHeader.setVisibility(View.GONE);
        acceptedDriversRecyclerView.setVisibility(View.GONE);
        updateButton.setVisibility(View.GONE);
    }
}
