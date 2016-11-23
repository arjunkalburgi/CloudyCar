package com.cloudycrew.cloudycar;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by George on 2016-11-05.
 */

public class BaseFragment extends Fragment {
    private boolean firstTimeStarted = true;

    public CloudyCarApplication getCloudyCarApplication() {
        return (CloudyCarApplication) getActivity().getApplication();
    }

    public void setActivityTitle(String title) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
    }

    public void setActivityTitle(int titleResourceId) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(titleResourceId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstTimeStarted) {
            onFirstResume();
            firstTimeStarted = false;
        }
    }
    
    public void onFirstResume() {

    }
}
