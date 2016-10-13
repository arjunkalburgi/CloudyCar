package com.cloudycrew.cloudycar;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by George on 2016-10-12.
 */

public class OfflineTests {
    private LocalRequestService localRequestService;
    private CloudRequestService cloudRequestService;
    private IRequestService requestService;

    private InternetConnectivityProvider internetConnectivityProvider;

    private User rider;
    private User driver;
    private Request request1;
    private Request request2;
    private Request newRequest;
    private Request acceptedRequest1;
    private Request newAcceptedRequest;


    @Before
    public void set_up() {
        set_up_requests();

        when(localRequestService.getRequests()).thenReturns(Arrays.asList(request1, request2));
        when(localRequestService.getAcceptedRequests()).thenReturns(Arrays.asList(acceptedRequest1));

        requestService = new RequestService(localRequestService, cloudRequestService, internetConnectivityProvider);
    }

    private void set_up_requests() {
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

        List<Request> expectedRequests = Arrays.asList(acceptedRequest1);
        List<Request> actualRequests = requestService.getAcceptedRequests();

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
