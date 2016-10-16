package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;

import java.util.UUID;

/**
 * Created by George on 2016-10-13.
 */

public abstract class Request {
    private User rider;
    private Route route;
    protected UUID id;
    public Request(User rider, Route route){
        this.rider=rider;
        this.route=route;
    }
    public User getRider() {
        return rider;
    }
    public Route getRoute() {
        return route;
    }
    public UUID getId(){
        return this.id;
    }
}
