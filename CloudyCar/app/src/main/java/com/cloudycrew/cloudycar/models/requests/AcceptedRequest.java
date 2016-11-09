package com.cloudycrew.cloudycar.models.requests;

/**
 * Created by George on 2016-10-13.
 */

public class AcceptedRequest extends Request {
    public static final String TYPE_NAME = "accepted";

    private String driverUsername;

    public AcceptedRequest(PendingRequest request, String driverUsername) {
        super(TYPE_NAME, request.getRider(), request.getRoute());
        this.driverUsername = driverUsername;
        this.id = request.getId();
    }

    public ConfirmedRequest confirmRequest() {
        return new ConfirmedRequest(this);
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

        AcceptedRequest otherAcceptedRequest = (AcceptedRequest) obj;

        return getId().equals(otherAcceptedRequest.getId()) &&
                getDriverUsername().equals(otherAcceptedRequest.getDriverUsername()) &
                getRider().equals(otherAcceptedRequest.getRider()) &&
                getRoute().equals(otherAcceptedRequest.getRoute());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
