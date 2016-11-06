package com.cloudycrew.cloudycar.createrequest;

import android.os.Bundle;

import com.cloudycrew.cloudycar.BaseActivity;

/**
 * Created by George on 2016-11-05.
 */

public class CreateRequestActivity extends BaseActivity implements ICreateRequestView {
    private CreateRequestController createRequestController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: setContentView

        resolveDependencies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createRequestController.attachView(this);
    }


    @Override
    protected void onPause() {
        createRequestController.detachView();
    }

    private void resolveDependencies() {
        createRequestController = getCloudyCarApplication().getCreateRequestController();
    }

    @Override
    public void onRequestCreated() {

    }
}
