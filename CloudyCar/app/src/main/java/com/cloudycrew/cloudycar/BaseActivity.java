package com.cloudycrew.cloudycar;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by George on 2016-11-05.
 */

public class BaseActivity extends AppCompatActivity {
    public CloudyCarApplication getCloudyCarApplication() {
        return (CloudyCarApplication) getApplication();
    }
}
