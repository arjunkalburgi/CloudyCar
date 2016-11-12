package com.cloudycrew.cloudycar;

import android.support.v4.app.Fragment;

/**
 * Created by George on 2016-11-05.
 */

public class BaseFragment extends Fragment {
    private boolean firstTimeStarted = true;

    public CloudyCarApplication getCloudyCarApplication() {
        return (CloudyCarApplication) getActivity().getApplication();
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
