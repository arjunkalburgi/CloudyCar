package com.cloudycrew.cloudycar.requestinteractions;

import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.requeststorage.CloudRequestService;
import com.cloudycrew.cloudycar.requeststorage.IRequestService;

/**
 * Created by George on 2016-10-13.
 */

public class CreateRequest {
    private IRequestService cloudRequestService = new CloudRequestService();

    public void create(Point startingPoint, Point endingPoint, User rider) {
        Route route= new Route(startingPoint,endingPoint);
        //cloudRequestService.addRequest(new PendingRequest(rider,route));
    }
}
