package com.cloudycrew.cloudycar.createrequest;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.Route;

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
        TextView startText = (TextView)this.findViewById(R.id.display_route_start);
        TextView endText = (TextView)this.findViewById(R.id.display_route_end);
        Route userRoute = (Route) getIntent().getExtras().getSerializable("route");
        Resources resouces = getResources();
        String startToDisplay = String.format(resouces.getString(R.string.write_start_location),userRoute.getStartingPoint());
        String endToDisplay = String.format(resouces.getString(R.string.write_end_location),userRoute.getEndingPoint());
        startText.setText(startToDisplay);
        endText.setText(endToDisplay);

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
