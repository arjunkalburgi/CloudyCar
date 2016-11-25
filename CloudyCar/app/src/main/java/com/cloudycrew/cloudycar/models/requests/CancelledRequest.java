package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.Route;

import java.util.UUID;

/**
 * Created by Ryan on 2016-11-24.
 */

public class CancelledRequest extends Request
{

    public static final String TYPE_NAME = "cancelled";

    public CancelledRequest(PendingRequest pendingRequest) {
        super(TYPE_NAME, pendingRequest.getRider(), pendingRequest.getRoute(), pendingRequest.getPrice());
        this.id = pendingRequest.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !getClass().equals(obj.getClass())) return false;

        CancelledRequest otherCancelledRequest = (CancelledRequest) obj;

        return getId().equals(otherCancelledRequest.getId()) &&
                getRider().equals(otherCancelledRequest.getRider()) &&
                getRoute().equals(otherCancelledRequest.getRoute()) &&
                getPrice() == otherCancelledRequest.getPrice();
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
