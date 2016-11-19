package com.cloudycrew.cloudycar.viewcells;

import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import ca.antonious.viewcelladapter.GenericSingleViewCell;

/**
 * Created by George on 2016-11-17.
 */

public class PendingRequestViewCell extends GenericSingleViewCell<PendingRequestViewCell.ViewHolder, PendingRequest> {

    public PendingRequestViewCell(PendingRequest model) {
        super(model);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_pending_request;
    }

    @Override
    public void bindViewCell(PendingRequestViewCell.ViewHolder viewHolder) {
        PendingRequest request = getModel();

        viewHolder.requestDest.setText(request.getRoute().getEndingPoint().getDescription());
        viewHolder.requestSrc.setText("from " + request.getRoute().getStartingPoint().getDescription());
    }

    public static class ViewHolder extends BaseRequestViewHolder {
        public ViewHolder(View view) {
            super(view);

            requestDest = (TextView) view.findViewById(R.id.pending_request_dest);
            requestSrc = (TextView) view.findViewById(R.id.pending_request_src);
        }
    }
}
