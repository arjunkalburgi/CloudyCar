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
}
