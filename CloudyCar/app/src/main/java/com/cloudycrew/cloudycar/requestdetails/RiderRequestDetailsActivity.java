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

public class RiderRequestDetailsActivity extends BaseActivity implements IRequestDetailsView {
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

    private void displayBaseRequestInformation(Request request) {
        fromTextView.setText(request.getRoute().getStartingPoint().getDescription());
        toTextView.setText(request.getRoute().getEndingPoint().getDescription());
        priceTextView.setText(String.format(Locale.getDefault(),"$%.2f",request.getPrice()));
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
    }
}
