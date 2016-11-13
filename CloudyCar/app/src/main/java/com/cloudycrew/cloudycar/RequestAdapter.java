package com.cloudycrew.cloudycar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends
        RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<Request> requestList;
    private List<PendingRequest> pendingRequests;
    private List<PendingRequest> acceptedRequests;
    private List<ConfirmedRequest> confirmedRequests;
    private boolean useGenericLayout = false;

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
        confirmedRequests = new ArrayList<>();
    }

    public RequestAdapter(boolean useGenericLayout) {
        requestList = new ArrayList<>();
        pendingRequests = new ArrayList<>();
        acceptedRequests = new ArrayList<>();
        confirmedRequests = new ArrayList<>();
        this.useGenericLayout = useGenericLayout;
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
        Request r = requestList.get(position);
        if (this.useGenericLayout) {
            return PENDING_REQUEST;
        } else if (r instanceof PendingRequest && !((PendingRequest) r).hasBeenAccepted()) {
            return PENDING_REQUEST;
        } else if (r instanceof PendingRequest && ((PendingRequest) r).hasBeenAccepted()) {
            return ACCEPTED_REQUEST;
        } else if (r instanceof ConfirmedRequest) {
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
        TextView requestDestView = viewHolder.requestDest;
        TextView requestSrcView = viewHolder.requestSrc;
        // change these to get actual place names later

        requestDestView.setText(request.getRoute().getEndingPoint().getDescription());
        requestSrcView.setText("from " + request.getRoute().getStartingPoint().getDescription());

        if (!this.useGenericLayout) {
            if (request instanceof PendingRequest && ((PendingRequest) request).hasBeenAccepted()) {
                TextView requestAcceptedView = ((AcceptedViewHolder) viewHolder).requestAcceptedBy;
                List<String> drivers = ((PendingRequest) request).getDriversWhoAccepted();
                String driversText = "Accepted by " + drivers.get(0);
                for (String driver : drivers.subList(1, drivers.size())) {
                    driversText += ", " + driver;
                }
                requestAcceptedView.setText(driversText);
            } else if (request instanceof ConfirmedRequest) {
                TextView requestConfirmedView = ((ConfirmedViewHolder) viewHolder).requestAcceptedBy;
                requestConfirmedView.setText("Accepted by " + ((ConfirmedRequest) request).getDriverUsername());
            }
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

    public void setAcceptedRequests(List<PendingRequest> requests) {
        acceptedRequests = requests;
        requestList = mergeLists();
        this.notifyDataSetChanged();
    }

    public void setConfirmedRequests(List<ConfirmedRequest> requests) {
        confirmedRequests = requests;
        requestList = mergeLists();
        this.notifyDataSetChanged();
    }

    // our full list will be both pending and accepted put together
    public List<Request> mergeLists() {
        List<Request> requests = new ArrayList<>();
        requests.addAll(confirmedRequests);
        requests.addAll(acceptedRequests);
        requests.addAll(pendingRequests);
        return requests;
    }

    public void remove (int pos) {
        Request r = mergeLists().get(pos);
        pendingRequests.remove(r);
        confirmedRequests.remove(r);
        requestList = mergeLists();
        this.notifyDataSetChanged();
    }

    public Request get (int pos) {
        return mergeLists().get(pos);
    }
}
