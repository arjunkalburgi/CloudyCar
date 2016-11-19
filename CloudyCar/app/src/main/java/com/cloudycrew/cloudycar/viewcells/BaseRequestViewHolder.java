package com.cloudycrew.cloudycar.viewcells;

import android.view.View;
import android.widget.TextView;

import ca.antonious.viewcelladapter.BaseViewHolder;

/**
 * Created by George on 2016-11-17.
 */

public class BaseRequestViewHolder extends BaseViewHolder {
    public TextView requestDest;
    public TextView requestSrc;

    public BaseRequestViewHolder(View itemView) {
        super(itemView);
    }
}
