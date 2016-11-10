package com.cloudycrew.cloudycar.createrequest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.TextView;

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

        TextView submit = (TextView)this.findViewById(R.id.submit_ride_request);
        CardView routeSelector = (CardView)this.findViewById(R.id.routeSummary);
        routeSelector.setOnClickListener(v -> {
            launchMapActivity();
        });
        submit.setOnClickListener(v -> {
            //Submit request to server
        });
    }

    private void launchMapActivity() {
        Intent intent = new Intent(this,RouteSelector.class);
        startActivity(intent);
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
