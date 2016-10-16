package com.cloudycrew.cloudycar;

import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
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

    private PendingRequest pendingRequest1;
    private PendingRequest pendingRequest2;

    private AcceptedRequest acceptedRequest1;
    private AcceptedRequest confirmedRequest1;
    private AcceptedRequest completedRequest1;
    private AcceptRequest acceptRequest;

    @Before
    public void set_up() {
        rider = new User("janedoedoe");
        driver = new User("driverdood");

        Point startingPoint = new Point(48.1472373, 11.5673969);
        Point endingPoint = new Point(48.1258551, 11.5121003);

        Route route = new Route(startingPoint,endingPoint);
        
        pendingRequest1 = new PendingRequest(rider,route);
        pendingRequest2 = new PendingRequest(rider,route);

        acceptedRequest1 = pendingRequest1.acceptRequest(driver);

        confirmedRequest1 = new AcceptedRequest(pendingRequest1, driver);
        confirmedRequest1.setConfirmed();

        completedRequest1 = new AcceptedRequest(pendingRequest1, driver);
        completedRequest1.setConfirmed();
        completedRequest1.setCompleted();
    }

    @Test
    public void test_acceptRequest_ifRequestExistsAndIsPending_thenStoreIsUpdatedWithTheAcceptedRequest() {
        when(requestStore.getRequest(acceptedRequest1.getId())).thenReturn(acceptedRequest1);
        acceptRequest.accept(acceptedRequest1.getId());

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
