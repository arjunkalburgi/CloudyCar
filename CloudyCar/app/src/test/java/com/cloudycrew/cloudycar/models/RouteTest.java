package com.cloudycrew.cloudycar.models;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Harley Vanselow on 2016-10-14.
 */
public class RouteTest {
    private Route route;
    private Point endingPoint;
    private Point startingPoint;
    private String testDescription = "test description";

    @Before
    public void setUp(){
        startingPoint = new Point(48.1472373, 11.5673969,testDescription);
        endingPoint = new Point(48.1258551, 11.5121003,testDescription);
        route = new Route(startingPoint,endingPoint);
    }
    @Test
    public void getStartingPoint() throws Exception {
        Assert.assertEquals("Did not retrieve correct starting point",startingPoint,route.getStartingPoint());
    }

    @Test
    public void getEndingPoint() throws Exception {
        Assert.assertEquals("Did not retrieve correct ending point",endingPoint,route.getEndingPoint());
    }

}