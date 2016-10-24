package com.cloudycrew.cloudycar.models.requests;

/**
 * Created by George on 2016-10-23.
 */

public class ConfirmedRequest extends Request {
    private String driverUsername;

    public ConfirmedRequest(AcceptedRequest acceptedRequest) {
        super("confirmed", acceptedRequest.getRider(), acceptedRequest.getRoute());
        this.id = acceptedRequest.getId();
        this.driverUsername = acceptedRequest.getDriverUsername();
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
}
