package com.cloudycrew.cloudycar.createrequest;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.SummaryActivity;
import com.cloudycrew.cloudycar.models.Route;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by George on 2016-11-05.
 */

/**
 * Activity where a rider finalizes and saves their request. They can review the route they chose,
 * see a suggested fare, and optionally set their own fare. When satisfied with their overall request,
 * they can submit the request, which returns them to the rider summary
 */
public class CreateRequestActivity extends BaseActivity implements ICreateRequestView {
    @BindView(R.id.set_price)
    protected EditText userPrice;
    @BindView(R.id.suggested_price)
    protected TextView suggestedPrice;
    @BindView(R.id.display_route_start)
    protected TextView startText;
    @BindView(R.id.display_route_end)
    protected TextView endText;

    private CreateRequestController createRequestController;
    private Route userRoute;
    private double price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        ButterKnife.bind(this);
        resolveDependencies();

        userRoute = (Route) getIntent().getExtras().getSerializable("route");

        this.getSupportActionBar().setTitle(R.string.request_create_header);
        suggestedPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attach_money_black_24dp, 0, 0, 0);
        formatRouteDisplay(startText, endText);

    }

    /**
     * When the submit button is pressed, send the route and price chosen to the createRequestController.
     */
    @OnClick(R.id.submit_ride_request)
    public void submitOnClick(){
        if(userPrice.getText().length()>0){
            price = Double.parseDouble(userPrice.getText().toString());
        }else{
            price = Double.parseDouble(suggestedPrice.getText().toString());
        }
        createRequestController.saveRequest(userRoute,price);
    }
    /**
     * Apply descriptions from the start and end point to the startText and endText view
     * @param startText The view to place the starting point description
     * @param endText The view to place the ending point description
     */
    private void formatRouteDisplay(TextView startText, TextView endText) {
        Resources resources = getResources();
        String startToDisplay = String.format(resources.getString(R.string.write_start_location),userRoute.getStartingPoint().getDescription());
        String endToDisplay = String.format(resources.getString(R.string.write_end_location),userRoute.getEndingPoint().getDescription());
        startText.setText(startToDisplay);
        endText.setText(endToDisplay);
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

    /**
     * Callback method for when the user created request has been successfully saved. The user is
     * returned to the rider summary view, and the CreateRequestActivity and RouteSelector activities
     * are ended
     */
    @Override
    public void onRequestCreated() {
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra("mode", "rider");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
