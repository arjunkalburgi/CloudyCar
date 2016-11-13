package com.cloudycrew.cloudycar.driversummary;

import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */
public interface IDriverSummaryView {
    /**
     * Callback for when loading starts.
     */
    void displayLoading();

    /**
     * Callback for when accepted requests are successfully fetched.
     *
     * @param acceptedRequests the accepted requests
     */
    void displayAcceptedRequests(List<PendingRequest> acceptedRequests);

    /**
     * Callback for when confirmed requests are successfully fetched.
     *
     * @param confirmedRequests the confirmed requests
     */
    void displayConfirmedRequests(List<ConfirmedRequest> confirmedRequests);
}
