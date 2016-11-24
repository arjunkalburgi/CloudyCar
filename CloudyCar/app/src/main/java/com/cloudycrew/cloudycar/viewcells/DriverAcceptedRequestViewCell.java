package com.cloudycrew.cloudycar.viewcells;

import com.cloudycrew.cloudycar.models.requests.PendingRequest;

/**
 * Created by George on 2016-11-19.
 */

public class DriverAcceptedRequestViewCell extends AcceptedRequestViewCell {

    public DriverAcceptedRequestViewCell(PendingRequest model) {
        super(model);
    }

    @Override
    public void bindViewCell(ViewHolder viewHolder) {
        super.bindViewCell(viewHolder);
        viewHolder.hideAcceptedDriversDescription();
    }

    @Override
    public Class<? extends ViewHolder> getViewHolderClass(int position) {
        return AcceptedRequestViewCell.ViewHolder.class;
    }
}
