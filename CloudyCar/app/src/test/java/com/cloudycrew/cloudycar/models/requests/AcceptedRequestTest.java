package com.cloudycrew.cloudycar.models.requests;

import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Harley Vanselow on 2016-10-16.
 */
public class AcceptedRequestTest {
    private PendingRequest request;
    private AcceptedRequest acceptedRequest;
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
        request = new PendingRequest(rider,route);
        acceptedRequest = request.acceptRequest(driver);
    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();


    @Test
    public void setConfirmed() throws Exception {
        Assert.assertTrue("Set Confirmed did not work",acceptedRequest.setConfirmed().isConfirmed());
    }

    @Test
    public void setCompleted() throws Exception {
        Assert.assertTrue("Set Completed did not work",acceptedRequest.setConfirmed().setCompleted().isCompleted());
    }

    @Test
    public void completeBeforeConfirmThrowsException(){
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("A request cannot be completed until it is confirmed");
        acceptedRequest.setCompleted();
    }

    @Test
    public void getDriver() throws Exception {
        Assert.assertEquals("Could not retrieve driver from accepted request",driver,acceptedRequest.getDriver());
    }

}