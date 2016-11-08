package com.cloudycrew.cloudycar.createrequest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;

/**
 * Created by George on 2016-11-05.
 */

public class CreateRequestActivity extends BaseActivity implements ICreateRequestView {
    private CreateRequestController createRequestController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        this.getSupportActionBar().setTitle(R.string.request_create_header);
        resolveDependencies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createRequestController.attachView(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        createRequestController.detachView();
    }

    private void resolveDependencies() {
        createRequestController = getCloudyCarApplication().getCreateRequestController();
    }

    @Override
    public void onRequestCreated() {

    }
}
