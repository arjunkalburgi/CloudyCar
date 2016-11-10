package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.Request;

/**
 * Created by George on 2016-11-05.
 */

public class RequestDetailsActivity extends BaseActivity implements IRequestDetailsView {
    private RequestDetailsController requestDetailsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        resolveDependencies();
    }

    @Override
    protected void onPause() {
        super.onPause();
        requestDetailsController.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestDetailsController.detachView();
    }

    private void resolveDependencies() {
        String requestId = getIntent().getStringExtra(Constants.EXTRA_REQUEST_ID);
        this.requestDetailsController = getCloudyCarApplication().getRequestDetailsController(requestId);
    }

    @Override
    public void displayRequest(Request request) {

    }
}
