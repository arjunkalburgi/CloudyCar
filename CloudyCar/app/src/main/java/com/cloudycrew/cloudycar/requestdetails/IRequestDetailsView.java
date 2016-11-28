package com.cloudycrew.cloudycar.requestdetails;

import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;

/**
 * Created by George on 2016-11-05.
 */
public interface IRequestDetailsView {
    /**
     * Callback to display pending request.
     *
     * @param pendingRequest the pending request
     */
    void displayPendingRequest(PendingRequest pendingRequest);

    /**
     * Callback to display confirmed request.
     *
     * @param confirmedRequest the confirmed request
     */
    void displayConfirmedRequest(ConfirmedRequest confirmedRequest);

    /**
     * Callback to display completed request.
     *
     * @param completedRequest the completed request
     */
    void displayCompletedRequest(CompletedRequest completedRequest);
}
