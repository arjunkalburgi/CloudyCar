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
public class RequestTest {
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
    public void getRider() throws Exception {
        Request request = new PendingRequest(rider,route);
        Assert.assertEquals("Did not retrieve request rider correctly",rider,request.getRider());
    }

    @Test
    public void getRoute() throws Exception {
        Request request = new PendingRequest(rider,route);
        Assert.assertEquals("Did not retrieve request route correctly",route,request.getRoute());
    }
}