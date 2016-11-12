package com.cloudycrew.cloudycar.models;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Harley Vanselow on 2016-10-14.
 */
public class PointTest {
    private Point point;
    double longitude;
    double latitude;
    String testDescription;
    @Before
    public void setUp() throws Exception {
        longitude = 48.1472373;
        latitude  = 11.5673969;
        testDescription = "test description";
        point= new Point(longitude, latitude,testDescription);

    }

    @Test
    public void getLongitude() throws Exception {
        Assert.assertEquals("Did not retrieve longitude correctly",longitude,point.getLongitude());
    }

    @Test
    public void getLatitude() throws Exception {
        Assert.assertEquals("Did not retrieve latitude correctly",latitude,point.getLatitude());
    }


}