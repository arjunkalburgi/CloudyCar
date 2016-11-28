package com.cloudycrew.cloudycar.viewcells;

import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import java.util.List;

import ca.antonious.viewcelladapter.GenericSingleViewCell;

/**
 * Created by George on 2016-11-17.
 */

public class AcceptedRequestViewCell extends BaseRequestViewCell<AcceptedRequestViewCell.ViewHolder, PendingRequest> {

    public AcceptedRequestViewCell(PendingRequest model) {
        super(model);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_accepted_request;
    }

    @Override
    public void bindViewCell(ViewHolder viewHolder) {
        super.bindViewCell(viewHolder);
        viewHolder.setAcceptedByDescription(getAcceptedByDescription());
    }

    private String getAcceptedByDescription() {
        List<String> drivers = getModel().getDriversWhoAccepted();

        String driversText = "Accepted by " + drivers.get(0);
        for (String driver : drivers.subList(1, drivers.size())) {
            driversText += ", " + driver;
        }

        return driversText;
    }

    public static class ViewHolder extends BaseRequestViewHolder {
        private TextView requestAcceptedBy;

        public ViewHolder(View view) {
            super(view);

            requestDest = (TextView) view.findViewById(R.id.accepted_request_dest);
            requestSrc = (TextView) view.findViewById(R.id.accepted_request_src);
            requestAcceptedBy = (TextView) view.findViewById(R.id.accepted_request_acceptedby);
        }

        public void setAcceptedByDescription(String acceptedByDescription) {
            requestAcceptedBy.setText(acceptedByDescription);
            requestAcceptedBy.setVisibility(View.VISIBLE);
        }

        public void hideAcceptedDriversDescription() {
            requestAcceptedBy.setVisibility(View.GONE);
        }
    }
}
