package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

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

    private RequestDetailsController requestDetailsController;
    private Request lastRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

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

    @OnClick(R.id.request_details_update_button)
    public void onUpdateRequestClicked() {
        if (lastRequest.getClass().equals(PendingRequest.class)) {
            requestDetailsController.acceptRequest();
        } else if (lastRequest.getClass().equals(AcceptedRequest.class)) {
            requestDetailsController.confirmRequest();
        } else if (lastRequest.getClass().equals(ConfirmedRequest.class)) {
            requestDetailsController.completeRequest();
        }
    }

    @Override
    public void displayRequest(Request request) {
        lastRequest = request;
        fromTextView.setText(String.valueOf(request.getRoute().getStartingPoint().getLatitude()));
        toTextView.setText(String.valueOf(request.getRoute().getEndingPoint().getLatitude()));
        priceTextView.setText("$10");
        updateButtonText(request);
    }

    private void updateButtonText(Request request) {
        updateButton.setVisibility(View.VISIBLE);

        if (request.getClass().equals(PendingRequest.class)) {
            updateButton.setText("Accept Request");
        } else if (request.getClass().equals(AcceptedRequest.class)) {
            updateButton.setText("Confirm Request");
        } else if (request.getClass().equals(ConfirmedRequest.class)) {
            updateButton.setText("Complete Request");
        } else if (request.getClass().equals(CompletedRequest.class)) {
            updateButton.setVisibility(View.GONE);
        }
    }
}
