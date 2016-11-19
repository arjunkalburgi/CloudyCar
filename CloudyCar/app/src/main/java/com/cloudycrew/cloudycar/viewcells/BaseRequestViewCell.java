package com.cloudycrew.cloudycar.viewcells;

import com.cloudycrew.cloudycar.models.requests.Request;

import ca.antonious.viewcelladapter.GenericSingleViewCell;

/**
 * Created by George on 2016-11-19.
 */

public abstract class BaseRequestViewCell<VH extends BaseRequestViewHolder, T extends Request> extends GenericSingleViewCell<VH, T> {
    private OnRequestClickedListener onRequestClickedListener;

    public BaseRequestViewCell(T model) {
        super(model);
    }

    @Override
    public void bindViewCell(VH viewHolder) {
        final Request request = getModel();
        viewHolder.setOnClickListener(v -> dispatchOnRequestClicked(request));
    }

    public OnRequestClickedListener getOnRequestClickedListener() {
        return onRequestClickedListener;
    }

    public void setOnRequestClickedListener(OnRequestClickedListener onRequestClickedListener) {
        this.onRequestClickedListener = onRequestClickedListener;
    }

    public void dispatchOnRequestClicked(Request request) {
        if (getOnRequestClickedListener() != null) {
            getOnRequestClickedListener().onRequestClicked(request);
        }
    }

    public interface OnRequestClickedListener {
        void onRequestClicked(Request request);
    }
}
