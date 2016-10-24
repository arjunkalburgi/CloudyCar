package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;

import java.util.UUID;

/**
 * Created by George on 2016-10-13.
 */

public abstract class Request {
    private String requestType;
    private String riderUsername;
    private Route route;
    protected UUID id;

    public Request(String type, String riderUsername, Route route) {
        this.requestType = type;
        this.riderUsername = riderUsername;
        this.route = route;
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

    public UUID getId(){
        return this.id;
    }
}
