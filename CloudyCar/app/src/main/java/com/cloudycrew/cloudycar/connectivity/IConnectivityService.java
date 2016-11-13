package com.cloudycrew.cloudycar.connectivity;

/**
 * Created by George on 2016-10-24.
 */
public interface IConnectivityService {
    /**
     * Is internet available boolean.
     *
     * @return the boolean
     */
    boolean isInternetAvailable();

    /**
     * Sets on connectivity changed listener.
     *
     * @param onConnectivityChangedListener the on connectivity changed listener
     */
    void setOnConnectivityChangedListener(OnConnectivityChangedListener onConnectivityChangedListener);

    /**
     * The interface On connectivity changed listener.
     */
    interface OnConnectivityChangedListener {
        /**
         * On connectivity changed.
         *
         * @param isConnected the is connected
         */
        void onConnectivityChanged(boolean isConnected);
    }
}