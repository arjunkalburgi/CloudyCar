package com.cloudycrew.cloudycar;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by insanekillah on 2016-11-08.
 */

public class RequestAdapter extends
        RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<Request> requestList;
    private List<PendingRequest> pendingRequests;
    private List<AcceptedRequest> acceptedRequests;

    private final int PENDING_REQUEST = 0;
    private final int ACCEPTED_REQUEST = 1;
    private final int CONFIRMED_REQUEST = 2;

    // Set up an interface to listen to clicks in the ViewHolder from an activity
    public interface CustomClickListener {
        void onItemClick(View view, Request request);
    }

    private CustomClickListener listener;

    // callable from MainActivity to call request actions from there
    public void setClickListener(CustomClickListener listener) {
        this.listener = listener;
    }

    public RequestAdapter() {
        requestList = new ArrayList<>();
        pendingRequests = new ArrayList<>();
        acceptedRequests = new ArrayList<>();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        public TextView requestDest;
        public TextView requestSrc;

        public ViewHolder(final View view) {
            super(view);

            final ViewHolder vh = this;

            // clicking on a list item other than the complete button
            view.setOnClickListener((v) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(view, requestList.get(position));
                }
            });
        }
    }

    // these classes hold each of the views that comprise a given item in the request list
    public class PendingViewHolder extends ViewHolder {

        public PendingViewHolder(final View view) {
            super(view);

            requestDest = (TextView) view.findViewById(R.id.pending_request_dest);
            requestSrc = (TextView) view.findViewById(R.id.pending_request_src);
        }
    }

    public class AcceptedViewHolder extends ViewHolder {

        public TextView requestAcceptedBy;

        public AcceptedViewHolder(final View view) {
            super(view);

            requestDest = (TextView) view.findViewById(R.id.accepted_request_dest);
            requestSrc = (TextView) view.findViewById(R.id.accepted_request_src);
            requestAcceptedBy = (TextView) view.findViewById(R.id.accepted_request_acceptedby);
        }
    }

    public class ConfirmedViewHolder extends ViewHolder {

        public TextView requestAcceptedBy;

        public ConfirmedViewHolder(final View view) {
            super(view);

            requestDest = (TextView) view.findViewById(R.id.confirmed_request_dest);
            requestSrc = (TextView) view.findViewById(R.id.confirmed_request_src);
            requestAcceptedBy = (TextView) view.findViewById(R.id.confirmed_request_acceptedby);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (requestList.get(position) instanceof PendingRequest) {
            return PENDING_REQUEST;
        } else if (requestList.get(position) instanceof AcceptedRequest) {
            return ACCEPTED_REQUEST;
        } else if (requestList.get(position) instanceof ConfirmedRequest) {
            return CONFIRMED_REQUEST;
        }
        return -1;
    }

    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View requestView;

        switch (viewType) {
            case PENDING_REQUEST:
                requestView = inflater.inflate(R.layout.item_pending_request, parent, false);
                return new PendingViewHolder(requestView);
            case ACCEPTED_REQUEST:
                requestView = inflater.inflate(R.layout.item_accepted_request, parent, false);
                return new AcceptedViewHolder(requestView);
            case CONFIRMED_REQUEST:
                requestView = inflater.inflate(R.layout.item_confirmed_request, parent, false);
                return new ConfirmedViewHolder(requestView);
            default:
                requestView = inflater.inflate(R.layout.item_pending_request, parent, false);
                return new PendingViewHolder(requestView);
        }
    }

    @Override
    public void onBindViewHolder(RequestAdapter.ViewHolder viewHolder, int position) {
        Request request = requestList.get(position);

        // populate the request item's views
        TextView habitDestView = viewHolder.requestDest;
        TextView habitSrcView = viewHolder.requestSrc;
        // change these to get actual place names later
        habitDestView.setText(String.valueOf(request.getRoute().getStartingPoint().getLatitude()));
        habitSrcView.setText(String.valueOf(request.getRoute().getStartingPoint().getLatitude()));

        if (request instanceof AcceptedRequest) {
            TextView habitAcceptedView = ((AcceptedViewHolder) viewHolder).requestAcceptedBy;
            habitAcceptedView.setText("Accepted by " + ((AcceptedRequest) request).getDriverUsername());
        } else if (request instanceof ConfirmedRequest) {
            TextView habitConfirmedView = ((ConfirmedViewHolder) viewHolder).requestAcceptedBy;
            habitConfirmedView.setText("Accepted by " + ((ConfirmedRequest) request).getDriverUsername());
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void setPendingRequests(List<PendingRequest> requests) {
        pendingRequests = requests;
        requestList = mergeLists();
        this.notifyDataSetChanged();
    }

    public void setAcceptedRequests(List<AcceptedRequest> requests) {
        acceptedRequests = requests;
        requestList = mergeLists();
        this.notifyDataSetChanged();
    }

    // our full list will be both pending and accepted put together
    public List<Request> mergeLists() {
        List<Request> requests = new ArrayList<>();
        requests.addAll(pendingRequests);
        requests.addAll(acceptedRequests);
        return requests;
    }
}
