package com.cloudycrew.cloudycar;

import android.support.v4.app.Fragment;

/**
 * Created by George on 2016-11-05.
 */

public class BaseFragment extends Fragment {
    public CloudyCarApplication getCloudyCarApplication() {
        return (CloudyCarApplication) getActivity().getApplication();
    }
}
