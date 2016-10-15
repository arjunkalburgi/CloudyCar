package com.cloudycrew.cloudycar;

import com.cloudycrew.cloudycar.email.EmailMessage;
import com.cloudycrew.cloudycar.email.IEmailService;
import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requestinteractions.AcceptRequest;
import com.cloudycrew.cloudycar.requestinteractions.CancelRequest;
import com.cloudycrew.cloudycar.requestinteractions.CompleteRequest;
import com.cloudycrew.cloudycar.requestinteractions.ConfirmRequest;
import com.cloudycrew.cloudycar.requestinteractions.CreateRequest;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;

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
public class RequestTests {
    @Mock
    private IEmailService emailService;
    @Mock
    private IRequestStore requestStore;

    private CreateRequest createRequest;
    private AcceptRequest acceptRequest;
    private CancelRequest cancelRequest;
    private CompleteRequest completeRequest;
    private ConfirmRequest confirmRequest;

    private User rider;
    private User driver;

    private Request request1;
    private Request request2;
    private AcceptedRequest acceptedRequest1;
    private ConfirmedRequest confirmedRequest1;
    private CompletedRequest completedRequest1;

    @Before
    public void set_up() {
        createRequest = new CreateRequest();
        completeRequest = new CompleteRequest();
        acceptRequest = new AcceptRequest();
        cancelRequest = new CancelRequest();
        confirmRequest = new ConfirmRequest();

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
    public void test_createRequest_thenStoreContainsNewPendingRequest() {
        Point startingPoint = request1.getRoute().getStartingPoint();
        Point endingPoint = request1.getRoute().getEndingPoint();

        createRequest.create(startingPoint, endingPoint,rider);

        assertTrue(requestStore.contains(request1));
    }

    @Test
    public void test_getCurrentRequests_ifUserHasNoRequests_thenReturnsAnEmptyList() {
        List<Request> requests = requestStore.getRequests();

        assertTrue(requests.isEmpty());
    }

    @Test
    public void test_getCurrentRequests_ifUserHasRequests_thenReturnsUsersRequests() {
        List<Request> expectedRequests = Arrays.asList(request1, request2);
        List<Request> actualRequests = requestStore.getRequests();

        assertEquals(expectedRequests, actualRequests);
    }

    @Test
    public void test_acceptRequest_sendsEmailToIntendedUser() {
        EmailMessage expectedMessage = new EmailMessage();
        expectedMessage.setTo(rider.getEmail());
        expectedMessage.setFrom(rider.getEmail());
        expectedMessage.setSubject(String.format("%s has accepted your ride request", driver.getFirstName()));

        when(requestStore.getRequest("request-1")).thenReturn(request1);
        acceptRequest.accept("request-1");

        verify(emailService).sendEmail(expectedMessage);
    }

    @Test
    public void test_cancelRequest_deleteRequestIsCalledWithCorrectRequestId() {
        when(requestStore.getRequest("request-1")).thenReturn(request1);
        cancelRequest.cancel("request-1");

        verify(requestStore).deleteRequest("request-1");
    }

    @Test
    public void test_completeRequest_ifStoreContainsRequest_thenUpdateRequestIsCalledWithTheExpectedCompletedRequest() {
        when(requestStore.getRequest("request-1")).thenReturn(request1);
        completeRequest.complete("request-1");

        verify(requestStore).updateRequest(completedRequest1);
    }

    @Test
    public void test_confirmRequest_ifStoreContainsRequest_thenUpdateRequestIsCalledWithTheExpectedConfirmedRequest() {
        when(requestStore.getRequest("request-1")).thenReturn(request1);

        confirmRequest.confirm("request-1");

        verify(requestStore).updateRequest(confirmedRequest1);
    }

    @Test
    public void test_acceptRequest_ifRequestExistsAndIsPending_thenStoreIsUpdatedWithTheAcceptedRequest() {
        when(requestStore.getRequest("request-1")).thenReturn(request1);
        acceptRequest.accept("request-1");

        verify(requestStore).updateRequest(acceptedRequest1);
    }
}
