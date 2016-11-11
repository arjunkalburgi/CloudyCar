package com.cloudycrew.cloudycar;

import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.email.EmailMessage;
import com.cloudycrew.cloudycar.email.IEmailService;
import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.requeststorage.IRequestService;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.scheduling.TestSchedulerProvider;
import com.cloudycrew.cloudycar.users.IUserPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by George on 2016-10-12.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestControllerTests {
    @Mock
    private IEmailService emailService;
    @Mock
    private IRequestStore requestStore;
    @Mock
    private IRequestService requestService;
    @Mock
    private IUserPreferences userPreferences;

    private ISchedulerProvider schedulerProvider;

    private RequestController requestController;

    private String riderUsername;
    private String driverUsername;

    private User rider;
    private User driver;

    private PendingRequest request1;
    private PendingRequest request2;
    private AcceptedRequest acceptedRequest1;
    private ConfirmedRequest confirmedRequest1;
    private CompletedRequest completedRequest1;

    @Before
    public void set_up() {
        riderUsername = "janedoedoe";
        driverUsername = "driverdood";

        rider = new User(riderUsername);
        driver = new User(driverUsername);

        Point startingPoint = new Point(48.1472373, 11.5673969);
        Point endingPoint = new Point(48.1258551, 11.5121003);

        Route route = new Route(startingPoint,endingPoint);

        request1 = new PendingRequest(riderUsername, route);
        request2 = new PendingRequest(riderUsername, route);

        acceptedRequest1 = request1.acceptRequest(driverUsername);
        confirmedRequest1 = acceptedRequest1.confirmRequest();
        completedRequest1 = confirmedRequest1.completeRequest();

        schedulerProvider = new TestSchedulerProvider();
        requestController = new RequestController(userPreferences, requestStore, requestService, schedulerProvider);
    }

    @Test
    public void test_createRequest_thenStoreContainsNewPendingRequest() {
        requestController.createRequest(request1.getRoute());

        verify(requestStore).addRequest(request1);
        verify(requestService).createRequest(request1);
    }

    @Test
    public void test_cancelRequest_deleteRequestIsCalledWithCorrectRequestId() {
        when(requestStore.getRequest(request1.getId())).thenReturn(request1);
        when(userPreferences.getUserName()).thenReturn(riderUsername);

        requestController.cancelRequest(request1.getId());

        verify(requestStore).deleteRequest(request1.getId());
        verify(requestService).deleteRequest(request1.getId());
    }

    @Test
    public void test_completeRequest_ifStoreDoesNotContainRequest_thenNothingHappens() {
        when(userPreferences.getUserName()).thenReturn(riderUsername);

        requestController.completeRequest(confirmedRequest1.getId());

        verify(requestStore, never()).updateRequest(anyObject());
        verify(requestService, never()).updateRequest(anyObject());
    }

    @Test
    public void test_completeRequest_ifStoreContainsRequest_thenUpdateRequestIsCalledWithTheExpectedCompletedRequest() {
        when(requestStore.getRequest(confirmedRequest1.getId(), ConfirmedRequest.class)).thenReturn(confirmedRequest1);
        when(userPreferences.getUserName()).thenReturn(riderUsername);

        requestController.completeRequest(confirmedRequest1.getId());

        verify(requestStore).updateRequest(completedRequest1);
        verify(requestService).updateRequest(completedRequest1);
    }

    @Test
    public void test_confirmRequest_ifStoreDoesNotContainRequest_thenNothingHappens() {
        when(userPreferences.getUserName()).thenReturn(riderUsername);

        requestController.confirmRequest(acceptedRequest1.getId());

        verify(requestStore, never()).updateRequest(anyObject());
        verify(requestService, never()).updateRequest(anyObject());
    }

    @Test
    public void test_confirmRequest_ifStoreContainsRequest_thenUpdateRequestIsCalledWithTheExpectedConfirmedRequest() {
        when(requestStore.getRequest(acceptedRequest1.getId(), AcceptedRequest.class)).thenReturn(acceptedRequest1);
        when(userPreferences.getUserName()).thenReturn(riderUsername);

        requestController.confirmRequest(acceptedRequest1.getId());

        verify(requestStore).updateRequest(confirmedRequest1);
        verify(requestService).updateRequest(confirmedRequest1);
    }

    @Test
    public void test_acceptRequest_ifStoreDoesNotContainRequest_thenNothingHappens() {
        when(userPreferences.getUserName()).thenReturn(riderUsername);

        requestController.acceptRequest(acceptedRequest1.getId());

        verify(requestStore, never()).addRequest(anyObject());
        verify(requestService, never()).createRequest(anyObject());
    }

    @Test
    public void test_acceptRequest_ifRequestExistsAndIsPending_thenStoreIsUpdatedWithTheAcceptedRequest() {
        when(requestStore.getRequest(request1.getId(), PendingRequest.class)).thenReturn(request1);
        when(userPreferences.getUserName()).thenReturn(driverUsername);

        requestController.acceptRequest(request1.getId());

        verify(requestStore).addRequest(acceptedRequest1);
        verify(requestService).createRequest(acceptedRequest1);
    }

    @Test
    public void test_acceptRequest_sendsEmailToIntendedUser() {
        EmailMessage expectedMessage = new EmailMessage();
        expectedMessage.setTo(rider.getEmail());
        expectedMessage.setFrom(rider.getEmail());
        expectedMessage.setSubject(String.format("%s has accepted your ride request", driver.getFirstName()));

        when(requestStore.getRequest(request1.getId())).thenReturn(request1);
        when(userPreferences.getUserName()).thenReturn(driverUsername);

        requestController.acceptRequest(request1.getId());

        verify(emailService).sendEmail(expectedMessage);
    }

}
