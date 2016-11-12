package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by George on 2016-11-11.
 */

public class DriverRequestDetailsActivity extends BaseActivity implements IRequestDetailsView {
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

    private RequestDetailsController requestDetailsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_details);

        ButterKnife.bind(this);
        resolveDependencies();
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
    }

    private void displayBaseRequestInformation(Request request) {
        fromTextView.setText(request.getRoute().getStartingPoint().getDescription());
        toTextView.setText(request.getRoute().getEndingPoint().getDescription());
        priceTextView.setText(String.format(Locale.getDefault(),"$%.2f",request.getPrice()));
    }

    @Override
    public void displayPendingRequest(PendingRequest pendingRequest) {
        displayBaseRequestInformation(pendingRequest);
        statusTextView.setText("You have not accepted this ride");

        updateButton.setText(R.string.accept_request_button_text);
        updateButton.setOnClickListener(v -> requestDetailsController.acceptRequest());
        updateButton.setVisibility(View.VISIBLE);
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
