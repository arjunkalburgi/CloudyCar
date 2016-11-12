package com.cloudycrew.cloudycar.requestdetails;

import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

/**
 * Created by George on 2016-11-05.
 */

public interface IRequestDetailsView {
    void displayPendingRequest(PendingRequest pendingRequest);
    void displayConfirmedRequest(ConfirmedRequest confirmedRequest);
    void displayCompletedRequest(CompletedRequest completedRequest);
}
