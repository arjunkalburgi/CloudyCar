package com.cloudycrew.cloudycar;

import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requestinteractions.AcceptRequest;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by George on 2016-10-12.
 */
@RunWith(MockitoJUnitRunner.class)
public class AcceptingTests {
    @Mock
    private IRequestStore requestStore;

    private User rider;
    private User driver;

    private AcceptRequest acceptRequest;

    private Request request1;
    private Request request2;
    private AcceptedRequest acceptedRequest1;
    private ConfirmedRequest confirmedRequest1;
    private CompletedRequest completedRequest1;

    @Before
    public void set_up() {
        acceptRequest = new AcceptRequest();

        rider = new User("janedoedoe");
        driver = new User("driverdood");

        Point startingPoint = new Point(48.1472373, 11.5673969);
        Point endingPoint = new Point(48.1258551, 11.5121003);

        Route route = new Route();
        route.addPoint(startingPoint);
        route.addPoint(endingPoint);

        request1 = new PendingRequest();
        request1.setId("request-1");
        request1.setRider(rider);
        request1.setRoute(route);

        request2 = new PendingRequest();
        request2.setId("request-2");
        request2.setRider(rider);
        request2.setRoute(route);

        acceptedRequest1 = new AcceptedRequest();
        acceptedRequest1.setId("request-1");
        acceptedRequest1.setRider(rider);
        acceptedRequest1.setDriver(driver);
        acceptedRequest1.setRoute(route);

        confirmedRequest1 = new ConfirmedRequest();
        confirmedRequest1.setId("request-1");
        confirmedRequest1.setRider(rider);
        confirmedRequest1.setDriver(driver);
        confirmedRequest1.setRoute(route);

        completedRequest1 = new CompletedRequest();
        completedRequest1.setId("request-1");
        completedRequest1.setRider(rider);
        completedRequest1.setDriver(driver);
        completedRequest1.setRoute(route);
    }

    @Test
    public void test_acceptRequest_ifRequestExistsAndIsPending_thenStoreIsUpdatedWithTheAcceptedRequest() {
        when(requestStore.getRequest("request-1")).thenReturn(request1);
        acceptRequest.accept("request-1");

        verify(requestStore).updateRequest(acceptedRequest1);
    }

    @Test
    public void test_getAcceptedRequests_ifDriverHasNoAcceptedRequests_thenReturnsAnEmptyList() {
        when(requestStore.getRequests()).thenReturn(new ArrayList<Request>(Arrays.asList(acceptedRequest1)));

        List<Request> acceptedRequests = requestStore.getAcceptedRequests();

        assertTrue(acceptedRequests.isEmpty());
    }

    @Test
    public void test_getAcceptedRequests_ifDriverHasAcceptedRequests_thenReturnsAcceptedRequests() {
        when(requestStore.getRequests()).thenReturn(new ArrayList<Request>(Arrays.asList(acceptedRequest1)));

        List<Request> expectedAcceptedRequests = new ArrayList<Request>(Arrays.asList(acceptedRequest1));
        List<Request> actualAcceptedRequests = requestStore.getAcceptedRequests();

        assertEquals(expectedAcceptedRequests, actualAcceptedRequests);
    }
}
