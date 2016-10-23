package com.cloudycrew.cloudycar.models.requests;

/**
 * Created by George on 2016-10-13.
 */

public class AcceptedRequest extends Request {
    private String driverUsername;

    public AcceptedRequest(PendingRequest request, String driverUsername) {
        super(request.getRider(), request.getRoute());
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
