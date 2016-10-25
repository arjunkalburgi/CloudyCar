package com.cloudycrew.cloudycar.connectivity;

/**
 * Created by George on 2016-10-24.
 */

public interface IConnectivityService {
    boolean isInternetAvailable();
    void setOnConnectivityChangedListener(OnConnectivityChangedListener onConnectivityChangedListener);

    interface OnConnectivityChangedListener {
        void onConectivityChanged(boolean isConnected);
    }
}
