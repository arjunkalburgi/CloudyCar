package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.Identifiable;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;

import java.util.UUID;

/**
 * Created by George on 2016-10-13.
 */

public abstract class Request implements Identifiable {
    private String requestType;
    private String riderUsername;
    private Route route;
    private double price;
    protected String id;

    public Request(String type, String riderUsername, Route route, double price) {
        this.requestType = type;
        this.riderUsername = riderUsername;
        this.route = route;
        this.price = price;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getRider() {
        return riderUsername;
    }

    public Route getRoute() {
        return route;
    }

    public double getPrice() {
        return price;
    }
    public String getId(){
        return this.id.toString();
    }

    public void setRiderUsername(String name) {
        this.riderUsername = name;
    }
}
