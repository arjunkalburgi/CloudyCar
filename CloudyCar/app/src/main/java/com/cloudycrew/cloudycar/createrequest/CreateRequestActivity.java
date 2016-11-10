package com.cloudycrew.cloudycar.createrequest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
        TextView textView = (TextView) findViewById(R.id.pricetextview);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attach_money_black_24dp, 0, 0, 0);

        this.getSupportActionBar().setTitle(R.string.request_create_header);
        resolveDependencies();

        TextView submit = (TextView)this.findViewById(R.id.submit_ride_request);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Submit request to server
            }
        });
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
