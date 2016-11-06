package com.cloudycrew.cloudycar.ridersummary;

import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public interface IRiderSummaryView {
    void displayLoading();
    void displayPendingRequests(List<PendingRequest> pendingRequests);
    void displayAcceptedRequests(List<AcceptedRequest> acceptedRequests);
}
