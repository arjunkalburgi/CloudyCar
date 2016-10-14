package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;

/**
 * Created by George on 2016-10-13.
 */

public class Request {
    private String id;
    private User rider;
    private Route route;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getRider() {
        return rider;
    }

    public void setRider(User rider) {
        this.rider = rider;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
