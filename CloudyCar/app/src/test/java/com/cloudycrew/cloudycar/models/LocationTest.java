package com.cloudycrew.cloudycar.models;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Harley Vanselow on 2016-10-14.
 */
public class LocationTest {
    private Location location;
    double longitude;
    double latitude;
    String testDescription;
    @Before
    public void setUp() throws Exception {
        longitude = 48.1472373;
        latitude  = 11.5673969;
        testDescription = "test description";
        location = new Location(longitude, latitude,testDescription);

    }

    @Test
    public void getLongitude() throws Exception {
        Assert.assertEquals("Did not retrieve longitude correctly",longitude, location.getLongitude());
    }

    @Test
    public void getLatitude() throws Exception {
        Assert.assertEquals("Did not retrieve latitude correctly",latitude, location.getLatitude());
    }


}