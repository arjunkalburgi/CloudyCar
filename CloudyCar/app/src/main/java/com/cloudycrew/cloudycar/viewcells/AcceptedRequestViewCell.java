package com.cloudycrew.cloudycar.viewcells;

import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import java.util.List;

import ca.antonious.viewcelladapter.BaseViewHolder;
import ca.antonious.viewcelladapter.GenericSingleViewCell;

/**
 * Created by George on 2016-11-17.
 */

public class AcceptedRequestViewCell extends GenericSingleViewCell<AcceptedRequestViewCell.ViewHolder, PendingRequest> {

    public AcceptedRequestViewCell(PendingRequest model) {
        super(model);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_accepted_request;
    }

    @Override
    public void bindViewCell(ViewHolder viewHolder) {
        PendingRequest request = getModel();

        viewHolder.requestDest.setText(request.getRoute().getEndingPoint().getDescription());
        viewHolder.requestSrc.setText("from " + request.getRoute().getStartingPoint().getDescription());
        viewHolder.requestAcceptedBy.setText(getAcceptedDescription());
    }

    private String getAcceptedDescription() {
        List<String> drivers = getModel().getDriversWhoAccepted();

        String driversText = "Accepted by " + drivers.get(0);
        for (String driver : drivers.subList(1, drivers.size())) {
            driversText += ", " + driver;
        }
        return driversText;
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
