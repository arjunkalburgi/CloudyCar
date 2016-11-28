package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.users.IUserPreferences;

import java.util.Locale;

import butterknife.BindView;

/**
 * Created by George on 2016-11-11.
 */

public abstract class BaseRequestDetailsActivity extends BaseActivity implements IRequestDetailsView {
    @BindView(R.id.request_details_from)
    protected TextView fromTextView;
    @BindView(R.id.request_details_to)
    protected TextView toTextView;
    @BindView(R.id.request_details_price)
    protected TextView priceTextView;
    @BindView(R.id.request_details_description)
    protected TextView descriptionTextView;
    @BindView(R.id.request_details_status)
    protected TextView statusTextView;
    @BindView(R.id.request_details_driver_label)
    protected TextView driverLabelTextView;
    @BindView(R.id.request_details_driver)
    protected TextView driverTextView;
    @BindView(R.id.request_details_rider)
    protected TextView riderTextView;
    @BindView(R.id.request_details_update_button)
    protected TextView updateButton;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    protected IUserPreferences userPreferences;
    protected RequestDetailsController requestDetailsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        this.userPreferences = getCloudyCarApplication().getUserPreferences();
    }

    protected void displayBaseRequestInformation(Request request) {
        updateButton.setVisibility(View.GONE);
        driverTextView.setVisibility(View.GONE);
        driverLabelTextView.setVisibility(View.GONE);

        fromTextView.setText(request.getRoute().getStartingPoint().getDescription());
        toTextView.setText(request.getRoute().getEndingPoint().getDescription());
        descriptionTextView.setText(request.getDescription());
        priceTextView.setText(String.format(Locale.getDefault(), "$%.2f", request.getPrice()));
        riderTextView.setText(request.getRider());
    }

    protected void setDriver(String driver) {
        driverTextView.setText(driver);
        driverTextView.setVisibility(View.VISIBLE);
        driverLabelTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        requestDetailsController.detachView();
        requestDetailsController.markRequestAsRead();
        super.onBackPressed();
    }
}
