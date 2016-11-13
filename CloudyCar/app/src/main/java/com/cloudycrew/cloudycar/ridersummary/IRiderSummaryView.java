package com.cloudycrew.cloudycar.ridersummary;

import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */
public interface IRiderSummaryView {
    /**
     * Callback called when loading occurs.
     */
    void displayLoading();

    /**
     * Callback called when pending requests are successfully fetched.
     *
     * @param pendingRequests the pending requests
     */
    void displayPendingRequests(List<PendingRequest> pendingRequests);

    /**
     * Callback called when accepted requests are successfully fetched.
     *
     * @param acceptedRequests the accepted requests
     */
    void displayAcceptedRequests(List<PendingRequest> acceptedRequests);
}
