package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.models.requests.Request;

/**
 * Created by George on 2016-11-05.
 */

public class RequestDetailsActivity extends BaseActivity implements IRequestDetailsView {
    private RequestDetailsController requestDetailsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: setContentView

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
        this.requestDetailsController = getCloudyCarApplication().getRequestDetailsController();
    }

    @Override
    public void displayRequest(Request request) {

    }
}
