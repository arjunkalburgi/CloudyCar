package com.cloudycrew.cloudycar.models.requests;


/**
 * Created by George on 2016-10-23.
 */

public class CompletedRequest extends Request {
    private String driverUsername;

    public CompletedRequest(ConfirmedRequest confirmedRequest) {
        super("completed", confirmedRequest.getRider(), confirmedRequest.getRoute());
        this.id = confirmedRequest.getId();
        this.driverUsername = confirmedRequest.getDriverUsername();
    }

    public String getDriverUsername() {
        return driverUsername;
    }

    public void setDriverUsername(String driverUsername) {
        this.driverUsername = driverUsername;
    }
}
