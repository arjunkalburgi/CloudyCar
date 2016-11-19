package com.cloudycrew.cloudycar.viewcells;

import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;

import ca.antonious.viewcelladapter.GenericSingleViewCell;

/**
 * Created by George on 2016-11-17.
 */

public class ConfirmedRequestViewCell extends GenericSingleViewCell<ConfirmedRequestViewCell.ViewHolder, ConfirmedRequest> {

    public ConfirmedRequestViewCell(ConfirmedRequest model) {
        super(model);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_confirmed_request;
    }

    @Override
    public void bindViewCell(ViewHolder viewHolder) {
        ConfirmedRequest request = getModel();

        viewHolder.requestDest.setText(request.getRoute().getEndingPoint().getDescription());
        viewHolder.requestSrc.setText("from " + request.getRoute().getStartingPoint().getDescription());
        viewHolder.requestAcceptedBy.setText("Accepted by: " + request.getDriverUsername());
    }

    public static class ViewHolder extends BaseRequestViewHolder {
        public TextView requestAcceptedBy;

        public ViewHolder(View view) {
            super(view);

            requestDest = (TextView) view.findViewById(R.id.accepted_request_dest);
            requestSrc = (TextView) view.findViewById(R.id.accepted_request_src);
            requestAcceptedBy = (TextView) view.findViewById(R.id.accepted_request_acceptedby);
        }
    }
}
