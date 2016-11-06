package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.models.requests.Request;

/**
 * Created by George on 2016-11-05.
 */

public class RequestDetailsActivity extends BaseActivity implements IRequestDetailsView {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView
    }
    
    @Override
    public void displayRequest(Request request) {

    }
}
