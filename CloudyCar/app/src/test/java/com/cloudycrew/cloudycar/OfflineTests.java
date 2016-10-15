package com.cloudycrew.cloudycar;

import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requeststorage.CloudRequestService;
import com.cloudycrew.cloudycar.requeststorage.IRequestService;
import com.cloudycrew.cloudycar.requeststorage.LocalRequestService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by George on 2016-10-12.
 */

@RunWith(MockitoJUnitRunner.class)
public class OfflineTests {
    @Mock
    private LocalRequestService localRequestService;
    @Mock
    private CloudRequestService cloudRequestService;
    @Mock
    private IRequestService requestService;
    @Mock
    private InternetConnectivityProvider internetConnectivityProvider;

    private User rider;
    private User driver;
    private Request request1;
    private Request request2;
    private Request newRequest;
    private AcceptedRequest acceptedRequest1;
    private AcceptedRequest newAcceptedRequest;


    @Before
    public void set_up() {
        set_up_requests();

        when(localRequestService.getRequests()).thenReturn(Arrays.asList(request1, request2));
        when(localRequestService.getAcceptedRequests()).thenReturn(Arrays.asList(acceptedRequest1));
    }

    private void set_up_requests() {
        rider = new User("janedoedoe");
        driver = new User("driverdood");

        Point startingPoint = new Point(48.1472373, 11.5673969);
        Point endingPoint = new Point(48.1258551, 11.5121003);

        Route route = new Route(startingPoint,endingPoint);

        request1 = new PendingRequest();
        request1.setId("request-1");
        request1.setRider(rider);
        request1.setRoute(route);

        request2 = new PendingRequest();
        request2.setId("request-2");
        request2.setRider(rider);
        request2.setRoute(route);

        newRequest = new PendingRequest();
        newRequest.setId("request-2");
        newRequest.setRider(rider);
        newRequest.setRoute(route);

        acceptedRequest1 = new AcceptedRequest();
        acceptedRequest1.setId("request-1");
        acceptedRequest1.setRider(rider);
        acceptedRequest1.setDriver(driver);
        acceptedRequest1.setRoute(route);

        newAcceptedRequest = new AcceptedRequest();
        newAcceptedRequest.setId("request-2");
        newAcceptedRequest.setRider(rider);
        newAcceptedRequest.setDriver(driver);
        newAcceptedRequest.setRoute(route);
    }

    @Test
    public void test_getPendingRequests_ifTheDeviceIsOffline_getsLocalRequests() {
        internetConnectivityProvider.setInternetAvailable(false);

        List<Request> expectedRequests = Arrays.asList(request1, request2);
        List<Request> actualRequests = requestService.getRequests();

        assertEquals(expectedRequests, actualRequests);
    }

    @Test
    public void test_createRequest_ifTheDeviceIsOffline_thenSendsRequestWhenDeviceRegainsConnectivity() {
        internetConnectivityProvider.setInternetAvailable(false);

        requestService.addRequest(newRequest);
        verify(cloudRequestService, never()).addRequest(newRequest);

        internetConnectivityProvider.setInternetAvailable(true);

        verify(cloudRequestService).addRequest(newRequest);
    }

    @Test
    public void test_getAcceptedRequests_ifTheDeviceIsOffline_getsLocalAcceptedRequests() {
        internetConnectivityProvider.setInternetAvailable(false);

        List<AcceptedRequest> expectedRequests = Arrays.asList(acceptedRequest1);
        List<AcceptedRequest> actualRequests = requestService.getAcceptedRequests();

        assertEquals(expectedRequests, actualRequests);
    }

    @Test
    public void test_acceptRequest_ifTheDeviceIsOffline_thenSendsAcceptedRequestWhenDeviceRegainsConnectivity() {
        internetConnectivityProvider.setInternetAvailable(false);

        requestService.addRequest(newAcceptedRequest);
        verify(cloudRequestService, never()).updateRequest(newAcceptedRequest);

        internetConnectivityProvider.setInternetAvailable(true);

        verify(cloudRequestService).updateRequest(newAcceptedRequest);
    }
}
