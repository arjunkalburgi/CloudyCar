package com.cloudycrew.cloudycar.viewcells;

import android.view.View;

import com.cloudycrew.cloudycar.models.requests.Request;

import ca.antonious.viewcelladapter.GenericSingleViewCell;
import ca.antonious.viewcelladapter.ListenerCollection;

/**
 * Created by George on 2016-11-19.
 */

public abstract class BaseRequestViewCell<VH extends BaseRequestViewHolder, T extends Request> extends GenericSingleViewCell<VH, T> {

    public BaseRequestViewCell(T model) {
        super(model);
    }

    @Override
    public void bindViewCell(VH viewHolder) {
        Request request = getModel();

        viewHolder.setRequestDestination(request.getRoute().getEndingPoint().getDescription());
        viewHolder.setRequestSource("from " + request.getRoute().getStartingPoint().getDescription());

        if (request.hasBeenReadByUser()) {
            viewHolder.markAsRead();
        } else {
            viewHolder.markAsUnread();
        }
    }

    @Override
    public void bindListeners(VH viewHolder, ListenerCollection listeners) {
        super.bindListeners(viewHolder, listeners);
        bindOnClickListener(viewHolder, listeners.getListener(OnRequestClickedListener.class));
    }

    private void bindOnClickListener(VH viewHolder, final OnRequestClickedListener onRequestClickedListener) {
        if (onRequestClickedListener != null) {
            viewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRequestClickedListener.onRequestClicked(getModel());
                }
            });
        }
    }

    public interface OnRequestClickedListener {
        void onRequestClicked(Request request);
    }
}
