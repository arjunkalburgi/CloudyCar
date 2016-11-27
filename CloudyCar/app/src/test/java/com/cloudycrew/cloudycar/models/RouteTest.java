package com.cloudycrew.cloudycar.models;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Harley Vanselow on 2016-10-14.
 */
public class RouteTest {
    private Route route;
    private Location endingLocation;
    private Location startingLocation;
    private String testDescription = "test description";

    @Before
    public void setUp(){
        startingLocation = new Location(48.1472373, 11.5673969,testDescription);
        endingLocation = new Location(48.1258551, 11.5121003,testDescription);
        route = new Route(startingLocation, endingLocation);
        route.setMeters(1000);
    }
    @Test
    public void getStartingPoint() throws Exception {
        Assert.assertEquals("Did not retrieve correct starting point", startingLocation,route.getStartingPoint());
    }

    @Test
    public void getEndingPoint() throws Exception {
        Assert.assertEquals("Did not retrieve correct ending point", endingLocation,route.getEndingPoint());
    }

    @Test
    public void getMeters(){
        Assert.assertEquals("Did not retrieve correct distance",1000,route.getMeters());
    }

}