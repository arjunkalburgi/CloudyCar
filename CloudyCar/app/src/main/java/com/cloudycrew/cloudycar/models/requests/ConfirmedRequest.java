package com.cloudycrew.cloudycar.models.requests;

/**
 * Created by George on 2016-10-23.
 */
public class ConfirmedRequest extends Request {
    /**
     * The constant TYPE_NAME.
     */
    public static final String TYPE_NAME = "confirmed";

    private String driverUsername;

    /**
     * Instantiates a new Confirmed request.
     *
     * @param pendingRequest the pending request
     * @param driverUsername the driver username
     */
    public ConfirmedRequest(PendingRequest pendingRequest, String driverUsername) {
        super(TYPE_NAME, pendingRequest.getRider(), pendingRequest.getRoute(), pendingRequest.getPrice(), pendingRequest.getDescription());
        this.id = pendingRequest.getId();
        this.driverUsername = driverUsername;
    }

    /**
     * Completes a confirmed request.
     *
     * @return the completed request
     */
    public CompletedRequest completeRequest() {
        return new CompletedRequest(this);
    }

    /**
     * Gets driver username.
     *
     * @return the driver username
     */
    public String getDriverUsername() {
        return driverUsername;
    }

    /**
     * Sets driver username.
     *
     * @param driverUsername the driver username
     */
    public void setDriverUsername(String driverUsername) {
        this.driverUsername = driverUsername;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !getClass().equals(obj.getClass())) return false;

        ConfirmedRequest otherConfirmedRequest = (ConfirmedRequest) obj;

        return getId().equals(otherConfirmedRequest.getId()) &&
                getDescription().equals(otherConfirmedRequest.getDescription()) &&
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
