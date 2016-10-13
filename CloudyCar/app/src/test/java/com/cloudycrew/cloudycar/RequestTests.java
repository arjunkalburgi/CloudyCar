package com.cloudycrew.cloudycar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
/**
 * Created by George on 2016-10-12.
 */

public class RequestTests {
    private User user;
    private IRequestStore requestStore;
    private CreateRequest createRequest;

    @Before
    public void set_up() {
        user = new User("janedoedoe");
        createRequest = new CreateRequest(requestStore);
    }

    @Test
    public void test_createRequest_thenStoreContainsNewPendingRequest() {
        Point startingPoint = new Point(48.1472373, 11.5673969);
        Point endingPoint = new Point(48.1258551, 11.5121003);

        createRequest.create(startingPoint, endingPoint);

        Route expectedRoute = new Route();
        expectedRoute.addPoint(startingPoint);
        expectedRoute.addPoint(endingPoint);

        Request expectedRequest = new PendingRequest();
        expectedRequest.setUser(user);
        expectedRequest.setRoute(expectedRoute);

        assertTrue(requestStore.contains(expectedRequest));
    }

    @Test
    public void test_getCurrentRequests_ifUserHasNoRequests_thenReturnsAnEmptyList() {
        List<Request> requests = requestStore.getRequests();

        assertTrue(requests.isEmpty());
    }

    @Test
    public void test_getCurrentRequests_ifUserHasRequests_thenReturnsUsersRequests() {
        Point startingPoint = new Point(48.1472373, 11.5673969);
        Point endingPoint = new Point(48.1258551, 11.5121003);

        Route route = new Route();
        route.addPoint(startingPoint);
        route.addPoint(endingPoint);

        Request request1 = new PendingRequest();
        request1.setUser(user);
        request1.setRoute(route);

        Request request2 = new PendingRequest();
        request2.setUser(user);
        request2.setRoute(route);

        List<Request> expectedRequests = Arrays.asList(request1, request2);
        List<Request> actualRequests = requestStore.getRequests();

        assertEquals(expectedRequests, actualRequests);
    }

}
