package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by George on 2016-10-13.
 */
public class PendingRequest extends Request {
    /**
     * The constant TYPE_NAME.
     */
    public static final String TYPE_NAME = "pending";

    private List<String> driversWhoAccepted;

    /**
     * Instantiates a new Pending request.
     *
     * @param riderUsername the rider username
     * @param route         the route
     * @param price         the price
     */
    public PendingRequest(String riderUsername, Route route, double price, String description) {
        super(TYPE_NAME, riderUsername, route, price, description);
        this.id = UUID.randomUUID().toString();
        this.driversWhoAccepted = new ArrayList<>();
    }

    /**
     * Confirm a request for a given driver
     *
     * @param driverUsername the driver username
     * @throws ConfirmingDriverWhoHasNotAcceptedException if the driver has not accepted the request
     * @return the confirmed request
     */
    public ConfirmedRequest confirmRequest(String driverUsername) {
        if (!getDriversWhoAccepted().contains(driverUsername)) {
            throw new ConfirmingDriverWhoHasNotAcceptedException();
        }

        return new ConfirmedRequest(this, driverUsername);
    }

    /**
     * Has been accepted boolean.
     *
     * @return the boolean
     */
    public boolean hasBeenAccepted() {
        return !getDriversWhoAccepted().isEmpty();
    }

    /**
     * Has been accepted by boolean.
     *
     * @param driverUsername the driver username
     * @return the boolean
     */
    public boolean hasBeenAcceptedBy(String driverUsername) {
        return getDriversWhoAccepted().contains(driverUsername);
    }

    /**
     * Gets drivers who accepted.
     *
     * @return the drivers who accepted
     */
    public List<String> getDriversWhoAccepted() {
        if (driversWhoAccepted == null) {
            driversWhoAccepted = new ArrayList<>();
        }
        return driversWhoAccepted;
    }

    /**
     * Accept pending request.
     *
     * @param driverUsername the driver username
     * @throws DriverAlreadyAcceptedException if the driver has already accepted the requesr
     * @return the pending request
     */
    public PendingRequest accept(String driverUsername) {
        if (hasBeenAcceptedBy(driverUsername)) {
            throw new DriverAlreadyAcceptedException();
        }

        PendingRequest pendingRequest = new PendingRequest(getRider(), getRoute(), getPrice(), getDescription());
        pendingRequest.id = getId();
        pendingRequest.driversWhoAccepted.addAll(getDriversWhoAccepted());
        pendingRequest.driversWhoAccepted.add(driverUsername);

        return pendingRequest;
    }

    public CancelledRequest cancel() {
        return new CancelledRequest(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !getClass().equals(obj.getClass())) return false;

        PendingRequest otherPendingRequest = (PendingRequest) obj;

        return getId().equals(otherPendingRequest.getId()) &&
                getRider().equals(otherPendingRequest.getRider()) &&
                getRoute().equals(otherPendingRequest.getRoute()) &&
                getDescription().equals(otherPendingRequest.getDescription()) &&
                getDriversWhoAccepted().equals(otherPendingRequest.getDriversWhoAccepted()) &&
                getPrice() == otherPendingRequest.getPrice();
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public static class DriverAlreadyAcceptedException extends RuntimeException {

    }

    public static class ConfirmingDriverWhoHasNotAcceptedException extends RuntimeException {

    }

}
