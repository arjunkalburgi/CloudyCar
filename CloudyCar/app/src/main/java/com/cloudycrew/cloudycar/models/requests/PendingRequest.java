package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by George on 2016-10-13.
 */

public class PendingRequest extends Request {
    public static final String TYPE_NAME = "pending";

    private List<String> driversWhoAccepted;

    public PendingRequest(String riderUsername, Route route, double price) {
        super(TYPE_NAME, riderUsername, route, price);
        this.id = UUID.randomUUID().toString();
        this.driversWhoAccepted = new ArrayList<>();
    }

    public ConfirmedRequest confirmRequest(String driverUsername) {
        if (!getDriversWhoAccepted().contains(driverUsername)) {
            throw new ConfirmingDriverWhoHasNotAcceptedException();
        }

        return new ConfirmedRequest(this, driverUsername);
    }

    public boolean hasBeenAccepted() {
        return !getDriversWhoAccepted().isEmpty();
    }

    public boolean hasBeenAcceptedBy(String driverUsername) {
        return getDriversWhoAccepted().contains(driverUsername);
    }

    public List<String> getDriversWhoAccepted() {
        if (driversWhoAccepted == null) {
            driversWhoAccepted = new ArrayList<>();
        }
        return driversWhoAccepted;
    }

    public PendingRequest accept(String driverUsername) {
        if (hasBeenAcceptedBy(driverUsername)) {
            throw new DriverAlreadyAcceptedException();
        }

        PendingRequest pendingRequest = new PendingRequest(getRider(), getRoute(), getPrice());
        pendingRequest.id = getId();
        pendingRequest.driversWhoAccepted.addAll(getDriversWhoAccepted());
        pendingRequest.driversWhoAccepted.add(driverUsername);

        return pendingRequest;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !getClass().equals(obj.getClass())) return false;

        PendingRequest otherPendingRequest = (PendingRequest) obj;

        return getId().equals(otherPendingRequest.getId()) &&
                getRider().equals(otherPendingRequest.getRider()) &&
                getRoute().equals(otherPendingRequest.getRoute()) &&
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
