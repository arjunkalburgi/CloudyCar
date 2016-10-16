package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;

import java.util.UUID;

/**
 * Created by George on 2016-10-13.
 */

public class PendingRequest extends Request {
    public PendingRequest(User rider, Route route) {
        super(rider, route);
        this.id = UUID.randomUUID();
    }
    public AcceptedRequest acceptRequest(User driver){
        return new AcceptedRequest(this,driver);
    }
}
