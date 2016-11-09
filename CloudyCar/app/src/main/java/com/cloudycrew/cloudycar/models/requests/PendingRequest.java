package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.Route;

import java.util.UUID;

/**
 * Created by George on 2016-10-13.
 */

public class PendingRequest extends Request {
    public static final String TYPE_NAME = "pending";

    public PendingRequest(String riderUsername, Route route) {
        super(TYPE_NAME, riderUsername, route);
        this.id = UUID.randomUUID().toString();
    }

    public AcceptedRequest acceptRequest(String driverUsername){
        return new AcceptedRequest(this, driverUsername);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !getClass().equals(obj.getClass())) return false;

        PendingRequest otherPendingRequest = (PendingRequest) obj;

        return getId().equals(otherPendingRequest.getId()) &&
                getRider().equals(otherPendingRequest.getRider()) &&
                getRoute().equals(otherPendingRequest.getRoute());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
