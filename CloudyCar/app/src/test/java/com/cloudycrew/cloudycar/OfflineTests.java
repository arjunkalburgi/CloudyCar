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
    private PendingRequest request1;
    private PendingRequest request2;
    private PendingRequest newRequest;

    private PendingRequest acceptedRequest1;
    private PendingRequest newAcceptedRequest;


    @Before
    public void set_up() {
        set_up_requests();

        when(localRequestService.getRequests()).thenReturn(Arrays.<Request>asList(request1, request2));
        //  when(localRequestService.getAcceptedRequests()).thenReturn(Arrays.asList(acceptedRequest1));
    }

    private void set_up_requests() {
        rider = new User("janedoedoe");
        driver = new User("driverdood");

        Point startingPoint = new Point(48.1472373, 11.5673969);
        Point endingPoint = new Point(48.1258551, 11.5121003);

        Route route = new Route(startingPoint,endingPoint);

        request1 = new PendingRequest(rider.getUsername(), route);
        request2 = new PendingRequest(rider.getUsername(), route);

        newRequest = new PendingRequest(rider.getUsername(), route);

        acceptedRequest1 = request1.accept(driver.getUsername());
        newAcceptedRequest = newRequest.accept(driver.getUsername());
    }

    @Test
    public void test_getPendingRequests_ifTheDeviceIsOffline_getsLocalRequests() {
        internetConnectivityProvider.setInternetAvailable(false);

        List<Request> expectedRequests = Arrays.<Request>asList(request1, request2);
        List<Request> actualRequests = requestService.getRequests();

        assertEquals(expectedRequests, actualRequests);
    }

    @Test
    public void test_createRequest_ifTheDeviceIsOffline_thenSendsRequestWhenDeviceRegainsConnectivity() {
        internetConnectivityProvider.setInternetAvailable(false);

        requestService.createRequest(newRequest);
        verify(cloudRequestService, never()).createRequest(newRequest);

        internetConnectivityProvider.setInternetAvailable(true);

        verify(cloudRequestService).createRequest(newRequest);
    }

    @Test
    public void test_getAcceptedRequests_ifTheDeviceIsOffline_getsLocalAcceptedRequests() {
        internetConnectivityProvider.setInternetAvailable(false);

        List<PendingRequest> expectedRequests = Arrays.asList(acceptedRequest1);
        //List<AcceptedRequest> actualRequests = requestService.getAcceptedRequests();

        //assertEquals(expectedRequests, actualRequests);
    }

    @Test
    public void test_acceptRequest_ifTheDeviceIsOffline_thenSendsAcceptedRequestWhenDeviceRegainsConnectivity() {
        internetConnectivityProvider.setInternetAvailable(false);

        requestService.createRequest(newAcceptedRequest);
        verify(cloudRequestService, never()).updateRequest(newAcceptedRequest);

        internetConnectivityProvider.setInternetAvailable(true);

        verify(cloudRequestService).updateRequest(newAcceptedRequest);
    }
}
