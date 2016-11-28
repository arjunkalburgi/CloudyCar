package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.Identifiable;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;

import java.util.Date;
import java.util.UUID;

/**
 * Created by George on 2016-10-13.
 */
public abstract class Request implements Identifiable {
    private String requestType;
    private String riderUsername;
    private Route route;
    private double price;
    private double pricePerKm;
    private Date lastUpdated;
    private transient boolean hasBeenReadByUser;
    private String description;

    /**
     * The Id.
     */
    protected String id;

    /**
     * Instantiates a new Request.
     *
     * @param type          the type
     * @param riderUsername the rider username
     * @param route         the route
     * @param price         the price
     */
    public Request(String type, String riderUsername, Route route, double price, String description) {
        this.requestType = type;
        this.riderUsername = riderUsername;
        this.route = route;
        this.price = price;
        this.description = description;
        this.pricePerKm = price / route.getKilometers();
        this.lastUpdated = new Date();
    }

    /**
     * Gets request type.
     *
     * @return the request type
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Gets rider.
     *
     * @return the rider
     */
    public String getRider() {
        return riderUsername;
    }

    /**
     * Gets route.
     *
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }
    public String getId(){
        return this.id.toString();
    }

    /**
     * Sets rider username.
     *
     * @param name the name
     */
    public void setRiderUsername(String name) {
        this.riderUsername = name;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean hasBeenReadByUser() {
        return hasBeenReadByUser;
    }

    public void setHasBeenReadByUser(boolean hasBeenReadByUser) {
        this.hasBeenReadByUser = hasBeenReadByUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }
}
