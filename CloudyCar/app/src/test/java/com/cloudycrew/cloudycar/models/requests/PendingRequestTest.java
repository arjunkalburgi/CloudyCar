package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Harley Vanselow on 2016-10-16.
 */
public class PendingRequestTest {
    User rider;
    User driver;
    Route route;
    Point startingPoint;
    Point endingPoint;
    @Before
    public void setUp(){
        startingPoint = new Point(48.1472373, 11.5673969);
        endingPoint = new Point(48.1258551, 11.5121003);
        driver = new User("TheDriver");
        rider = new User("TheRider");
        route = new Route(startingPoint,endingPoint);

    }
    @Test
    public void acceptRequest() throws Exception {
        PendingRequest request = new PendingRequest(rider,route);
        AcceptedRequest acceptedRequest = request.acceptRequest(driver);
        Assert.assertEquals("Did not retrieve request route correctly",route,acceptedRequest.getRoute());
        Assert.assertEquals("Did not retrieve request rider correctly",rider,acceptedRequest.getRider());
        Assert.assertEquals("Did not retrieve request driver correctly",driver,acceptedRequest.getDriver());
        Assert.assertEquals("Did not carry over ID correctly",request.getId(),acceptedRequest.getId());
    }


}