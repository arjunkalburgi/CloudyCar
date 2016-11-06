package com.cloudycrew.cloudycar.driversummary;

import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;

import java.util.List;

/**
 * Created by George on 2016-11-05.
 */

public interface IDriverSummaryView {
    void displayLoading();
    void displayAcceptedRequests(List<AcceptedRequest> acceptedRequests);
    void displayConfirmedRequests(List<ConfirmedRequest> confirmedRequests);
}
