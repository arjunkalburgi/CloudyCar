package com.cloudycrew.cloudycar.viewcells;

import android.view.View;
import android.widget.TextView;

import ca.antonious.viewcelladapter.BaseViewHolder;

/**
 * Created by George on 2016-11-17.
 */

public class BaseRequestViewHolder extends BaseViewHolder {
    protected TextView requestDest;
    protected TextView requestSrc;

    public BaseRequestViewHolder(View itemView) {
        super(itemView);
    }

    public void setOnClickListener(View.OnClickListener onClickListner) {
        itemView.setOnClickListener(onClickListner);
    }

    public void setRequestDestination(String destination) {
        requestDest.setText(destination);
    }

    public void setRequestSource(String source) {
        requestSrc.setText(source);
    }
}
