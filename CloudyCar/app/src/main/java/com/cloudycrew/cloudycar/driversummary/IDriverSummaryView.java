package com.cloudycrew.cloudycar.driversummary;

import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public interface IDriverSummaryView {
    void displayLoading();
    void displayAcceptedRequests(List<PendingRequest> acceptedRequests);
    void displayConfirmedRequests(List<ConfirmedRequest> confirmedRequests);
}
