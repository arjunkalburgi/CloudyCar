package com.cloudycrew.cloudycar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import java.util.List;

/**
 * Created by insanekillah on 2016-11-08.
 */

public class AcceptedRequestAdapter extends
        RecyclerView.Adapter<AcceptedRequestAdapter.ViewHolder>  {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView ListTitleText;
        public TextView ListSubText;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ListTitleText = (TextView) itemView.findViewById(R.id.request_title);
            ListSubText = (TextView) itemView.findViewById(R.id.request_subtext);

        }
    }

    // Store a member variable for the contacts
    private List<AcceptedRequest> requestList;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public AcceptedRequestAdapter(Context context, List<AcceptedRequest> c) {
        requestList = c;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public AcceptedRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.offer_listitem, parent, false);

        // Return a new holder instance
        AcceptedRequestAdapter.ViewHolder viewHolder = new AcceptedRequestAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(AcceptedRequestAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
//        Request request = requestList.get(position);

        // Set item views based on your views and data model
        TextView titleTextView = viewHolder.ListTitleText;
//        titleTextView.setText(request.getTitle());
        TextView subtitleTextView = viewHolder.ListTitleText;
//        subtitleTextView.setText(request.getDetails());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
