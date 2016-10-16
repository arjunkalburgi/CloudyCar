package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by George on 2016-10-13.
 */

public class AcceptedRequest extends Request {
    private User driver;
    private boolean confirmed;
    private boolean completed;

    public AcceptedRequest(PendingRequest request, User driver) {
        super(request.getRider(),request.getRoute());
        this.driver = driver;
        this.id = request.getId();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public AcceptedRequest setConfirmed() {
        this.confirmed = true;
        return this;
    }

    public boolean isCompleted() {
        return completed;
    }

    public AcceptedRequest setCompleted() {
        if(!this.isConfirmed()) throw new IllegalStateException("A request cannot be completed until it is confirmed");
        this.completed = true;
        return this;
    }
    public User getDriver() {
        return driver;
    }
}
