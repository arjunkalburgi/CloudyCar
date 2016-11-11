package com.cloudycrew.cloudycar.models.requests;

/**
 * Created by George on 2016-10-23.
 */

public class ConfirmedRequest extends Request {
    public static final String TYPE_NAME = "confirmed";

    private String driverUsername;

    public ConfirmedRequest(PendingRequest pendingRequest, String driverUsername) {
        super(TYPE_NAME, pendingRequest.getRider(), pendingRequest.getRoute(), pendingRequest.getPrice());
        this.id = pendingRequest.getId();
        this.driverUsername = driverUsername;
    }

    public CompletedRequest completeRequest() {
        return new CompletedRequest(this);
    }

    public String getDriverUsername() {
        return driverUsername;
    }

    public void setDriverUsername(String driverUsername) {
        this.driverUsername = driverUsername;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !getClass().equals(obj.getClass())) return false;

        ConfirmedRequest otherConfirmedRequest = (ConfirmedRequest) obj;

        return getId().equals(otherConfirmedRequest.getId()) &&
                getDriverUsername().equals(otherConfirmedRequest.getDriverUsername()) &
                getRider().equals(otherConfirmedRequest.getRider()) &&
                getRoute().equals(otherConfirmedRequest.getRoute()) &&
                getPrice() == otherConfirmedRequest.getPrice();
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
