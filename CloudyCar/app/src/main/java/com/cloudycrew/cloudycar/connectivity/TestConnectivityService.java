package com.cloudycrew.cloudycar.connectivity;

/**
 * Created by Ryan on 2016-11-27.
 */

public class TestConnectivityService implements IConnectivityService {
    private OnConnectivityChangedListener onConnectivityChangedListener;
    @Override
    public boolean isInternetAvailable()
    {
        return false;
    }

    @Override
    public void setOnConnectivityChangedListener(OnConnectivityChangedListener onConnectivityChangedListener) {
        this.onConnectivityChangedListener = onConnectivityChangedListener;
    }

    public void setInternetConnectivity(Boolean connectivity) {
        this.dispatchConnectivityChange(connectivity);
    }

    private void dispatchConnectivityChange(Boolean connectivity) {
        if (onConnectivityChangedListener != null) {
            onConnectivityChangedListener.onConnectivityChanged(connectivity);
        }
    }
}
