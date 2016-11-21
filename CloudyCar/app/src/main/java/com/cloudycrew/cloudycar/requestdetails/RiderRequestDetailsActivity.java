package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.viewcells.AcceptedDriverViewCell;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.antonious.viewcelladapter.ViewCellAdapter;
import rx.Observable;

/**
 * Created by George on 2016-11-05.
 */

public class RiderRequestDetailsActivity extends BaseRequestDetailsActivity {
    @BindView(R.id.accepted_drivers_recycler_view)
    protected RecyclerView acceptedDriversRecyclerView;
    @BindView(R.id.accepted_drivers_header)
    protected TextView acceptedDriversHeader;
    @BindView(R.id.no_accepted_drivers)
    protected TextView noAcceptedDriversIndicator;

    private ViewCellAdapter viewCellAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        viewCellAdapter = new ViewCellAdapter();
        viewCellAdapter.setHasStableIds(true);

        viewCellAdapter.addListener(onConfirmClickedListener);

        acceptedDriversRecyclerView.setAdapter(viewCellAdapter);
        acceptedDriversRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void displayBaseRequestInformation(Request request) {
        super.displayBaseRequestInformation(request);

        updateButton.setVisibility(View.GONE);
        noAcceptedDriversIndicator.setVisibility(View.GONE);
        acceptedDriversRecyclerView.setVisibility(View.GONE);
        acceptedDriversHeader.setVisibility(View.GONE);
    }

    @Override
    public void displayPendingRequest(PendingRequest pendingRequest) {
        displayBaseRequestInformation(pendingRequest);

        statusTextView.setText("Pending");
        acceptedDriversHeader.setVisibility(View.VISIBLE);

        if (pendingRequest.hasBeenAccepted()) {
            acceptedDriversRecyclerView.setVisibility(View.VISIBLE);
            viewCellAdapter.setAll(getAcceptedDriverViewCells(pendingRequest.getDriversWhoAccepted()));
            viewCellAdapter.notifyDataSetChanged();
        } else {
            noAcceptedDriversIndicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayConfirmedRequest(ConfirmedRequest confirmedRequest) {
        displayBaseRequestInformation(confirmedRequest);

        statusTextView.setText("Confirmed");

        updateButton.setText(R.string.confirm_request_button_text);
        updateButton.setOnClickListener(v -> requestDetailsController.completeRequest());
        updateButton.setVisibility(View.VISIBLE);

        setDriver(confirmedRequest.getDriverUsername());
    }

    @Override
    public void displayCompletedRequest(CompletedRequest completedRequest) {
        displayBaseRequestInformation(completedRequest);

        statusTextView.setText("Completed");
        updateButton.setText(R.string.confirm_request_button_text);
        setDriver(completedRequest.getDriverUsername());
    }

    private AcceptedDriverViewCell.OnConfirmClickedListener onConfirmClickedListener = username -> {
        requestDetailsController.confirmRequest(username);
    };

    private List<AcceptedDriverViewCell> getAcceptedDriverViewCells(List<? extends String> usernames) {
        return Observable.from(usernames)
                         .map(AcceptedDriverViewCell::new)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }
}
