package com.cloudycrew.cloudycar.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by George on 2016-10-24.
 */

public class AndroidConnectivityService implements IConnectivityService {
    private Context context;
    private OnConnectivityChangedListener onConnectivityChangedListener;

    private AndroidConnectivityService(Context context) {
        this.context = context;
        registerConnectivityListener();
    }

    @Override
    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void setOnConnectivityChangedListener(OnConnectivityChangedListener onConnectivityChangedListener) {
        this.onConnectivityChangedListener = onConnectivityChangedListener;
    }

    private void registerConnectivityListener() {
        context.registerReceiver(new ConnectivityListener(),
                                 new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void dispatchConnectivityChange() {
        if (onConnectivityChangedListener != null) {
            onConnectivityChangedListener.onConectivityChanged(isInternetAvailable());
        }
    }

    private class ConnectivityListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            dispatchConnectivityChange();
        }
    }
}
