package com.cloudycrew.cloudycar.models.requests;


/**
 * Created by George on 2016-10-23.
 */
public class CompletedRequest extends Request {
    /**
     * The constant TYPE_NAME.
     */
    public static final String TYPE_NAME = "completed";

    private String driverUsername;

    /**
     * Instantiates a new Completed request.
     *
     * @param confirmedRequest the confirmed request
     */
    public CompletedRequest(ConfirmedRequest confirmedRequest) {
        super(TYPE_NAME, confirmedRequest.getRider(), confirmedRequest.getRoute(), confirmedRequest.getPrice());
        this.id = confirmedRequest.getId();
        this.driverUsername = confirmedRequest.getDriverUsername();
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

        CompletedRequest otherCompletedRequest = (CompletedRequest) obj;

        return getId().equals(otherCompletedRequest.getId()) &&
                getDriverUsername().equals(otherCompletedRequest.getDriverUsername()) &
                getRider().equals(otherCompletedRequest.getRider()) &&
                getRoute().equals(otherCompletedRequest.getRoute()) &&
                getPrice() == otherCompletedRequest.getPrice();
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
