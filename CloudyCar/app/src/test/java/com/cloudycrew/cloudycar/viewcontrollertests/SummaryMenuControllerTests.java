package com.cloudycrew.cloudycar.viewcontrollertests;

import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.requeststorage.RequestStore;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.scheduling.TestSchedulerProvider;
import com.cloudycrew.cloudycar.summarycontainer.ISummaryMenuView;
import com.cloudycrew.cloudycar.summarycontainer.SummaryMenuController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by George on 2016-11-24.
 */
@RunWith(MockitoJUnitRunner.class)
public class SummaryMenuControllerTests {
    @Mock
    private UserController userController;
    @Mock
    private ISummaryMenuView summaryMenuView;

    private IRequestStore requestStore;
    private ISchedulerProvider schedulerProvider;
    private SummaryMenuController summaryMenuController;

    private String riderUsername;
    private String driverUsername;

    private Route route;
    private double price;

    @Before
    public void set_up() {
        riderUsername = "rider";
        driverUsername = "driver";

        route = new Route(new Point(0, 0, "start"), new Point(0, 0, "end"));
        price = 20.30;

        schedulerProvider = new TestSchedulerProvider();
        requestStore = new RequestStore(schedulerProvider);

        summaryMenuController = new SummaryMenuController(userController, requestStore);
    }

    @Test
    public void test_ifRiderHasNoRequests_thenDisplayZeroUnreadRiderRequests() {
        requestStore.setAll(new ArrayList<>());
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadRiderRequests(0);
    }

    @Test
    public void test_ifRiderNoHasUnreadRequests_thenDisplayZeroUnreadRiderRequests() {
        PendingRequest pendingRequest = new PendingRequest(riderUsername, route, price);

        ConfirmedRequest confirmedRequest = new PendingRequest(riderUsername, route, price)
                .accept(driverUsername)
                .confirmRequest(driverUsername);

        pendingRequest.setLastUpdated(new Date(1));
        confirmedRequest.setLastUpdated(new Date(1));

        List<Request> requests = Arrays.asList(pendingRequest, confirmedRequest);

        when(userController.getCurrentUser()).thenReturn(new User(riderUsername));
        when(userController.getLastReadTime(pendingRequest.getId())).thenReturn(new Date(2));
        when(userController.getLastReadTime(confirmedRequest.getId())).thenReturn(new Date(2));

        requestStore.setAll(requests);
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadRiderRequests(0);
    }

    @Test
    public void test_ifRiderHasRequestsThatHaveNeverBeenRead_thenDisplaysTotalUnread() {
        PendingRequest pendingRequest = new PendingRequest(riderUsername, route, price);

        ConfirmedRequest confirmedRequest = new PendingRequest(riderUsername, route, price)
                .accept(driverUsername)
                .confirmRequest(driverUsername);

        pendingRequest.setLastUpdated(new Date(1));
        confirmedRequest.setLastUpdated(new Date(1));

        List<Request> requests = Arrays.asList(pendingRequest, confirmedRequest);

        when(userController.getCurrentUser()).thenReturn(new User(riderUsername));
        when(userController.getLastReadTime(pendingRequest.getId())).thenReturn(new Date(2));
        when(userController.getLastReadTime(confirmedRequest.getId())).thenReturn(null);

        requestStore.setAll(requests);
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadRiderRequests(1);
    }

    @Test
    public void test_ifRiderHasRequestsThatHaveBeenReadBeforeAnUpdate_thenDisplaysThoseRequestsAsUnread() {
        PendingRequest pendingRequest = new PendingRequest(riderUsername, route, price);

        ConfirmedRequest confirmedRequest = new PendingRequest(riderUsername, route, price)
                .accept(driverUsername)
                .confirmRequest(driverUsername);

        pendingRequest.setLastUpdated(new Date(3));
        confirmedRequest.setLastUpdated(new Date(3));

        List<Request> requests = Arrays.asList(pendingRequest, confirmedRequest);

        when(userController.getCurrentUser()).thenReturn(new User(riderUsername));
        when(userController.getLastReadTime(pendingRequest.getId())).thenReturn(new Date(2));
        when(userController.getLastReadTime(confirmedRequest.getId())).thenReturn(new Date(5));

        requestStore.setAll(requests);
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadRiderRequests(1);
    }

    @Test
    public void test_onlyCountUnreadRiderRequestsForRequestsARiderIsInvolvedWith() {
        PendingRequest pendingRequest = new PendingRequest(driverUsername, route, price);

        ConfirmedRequest confirmedRequest = new PendingRequest(riderUsername, route, price)
                .accept(driverUsername)
                .confirmRequest(driverUsername);

        pendingRequest.setLastUpdated(new Date(3));
        confirmedRequest.setLastUpdated(new Date(3));

        List<Request> requests = Arrays.asList(pendingRequest, confirmedRequest);

        when(userController.getCurrentUser()).thenReturn(new User(riderUsername));
        when(userController.getLastReadTime(pendingRequest.getId())).thenReturn(null);
        when(userController.getLastReadTime(confirmedRequest.getId())).thenReturn(null);

        requestStore.setAll(requests);
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadRiderRequests(1);
    }

    @Test
    public void test_ifDriverHasNoRequests_thenDisplayZeroUnreadDriverRequests() {
        requestStore.setAll(new ArrayList<>());
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadDriverRequests(0);
    }

    @Test
    public void test_ifDriverHasNoUnreadRequests_thenDisplayZeroUnreadDriverRequests() {
        PendingRequest acceptedRequest = new PendingRequest(riderUsername, route, price)
                .accept(driverUsername);

        ConfirmedRequest confirmedRequest = new PendingRequest(riderUsername, route, price)
                .accept(driverUsername)
                .confirmRequest(driverUsername);

        acceptedRequest.setLastUpdated(new Date(1));
        confirmedRequest.setLastUpdated(new Date(1));

        List<Request> requests = Arrays.asList(acceptedRequest, confirmedRequest);

        when(userController.getCurrentUser()).thenReturn(new User(driverUsername));
        when(userController.getLastReadTime(acceptedRequest.getId())).thenReturn(new Date(2));
        when(userController.getLastReadTime(confirmedRequest.getId())).thenReturn(new Date(2));

        requestStore.setAll(requests);
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadDriverRequests(0);
    }

    @Test
    public void test_doNotIncludePendingRequests_whenDeterminingDriverUnreadCount() {
        PendingRequest acceptedRequest = new PendingRequest(riderUsername, route, price)
                .accept(driverUsername);

        acceptedRequest.setLastUpdated(new Date(5));

        List<Request> requests = Arrays.asList(acceptedRequest);

        when(userController.getCurrentUser()).thenReturn(new User(driverUsername));
        when(userController.getLastReadTime(acceptedRequest.getId())).thenReturn(new Date(2));

        requestStore.setAll(requests);
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadDriverRequests(0);
    }

    @Test
    public void test_doNotIncludeCompletedRequests_whenDeterminingDriverUnreadCount() {
        CompletedRequest acceptedRequest = new PendingRequest(riderUsername, route, price)
                .accept(driverUsername)
                .confirmRequest(driverUsername)
                .completeRequest();

        acceptedRequest.setLastUpdated(new Date(5));

        List<Request> requests = Arrays.asList(acceptedRequest);

        when(userController.getCurrentUser()).thenReturn(new User(driverUsername));
        when(userController.getLastReadTime(acceptedRequest.getId())).thenReturn(new Date(2));

        requestStore.setAll(requests);
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadDriverRequests(0);
    }

    @Test
    public void test_doNotIncludeDriverRequestsThatDoNotBelongToTheCurrentUser() {
        ConfirmedRequest confirmedRequest = new PendingRequest(driverUsername, route, price)
                .accept(riderUsername)
                .confirmRequest(riderUsername);

        confirmedRequest.setLastUpdated(new Date(1));

        List<Request> requests = Arrays.asList(confirmedRequest);

        when(userController.getCurrentUser()).thenReturn(new User(driverUsername));
        when(userController.getLastReadTime(confirmedRequest.getId())).thenReturn(null);

        requestStore.setAll(requests);
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadDriverRequests(0);
    }

    @Test
    public void test_ifDriverHasUnreadRequests_thenCallsDisplayTotalUnreadRequestsWithTotalUnread() {
        ConfirmedRequest confirmedRequest = new PendingRequest(riderUsername, route, price)
                .accept(driverUsername)
                .confirmRequest(driverUsername);

        confirmedRequest.setLastUpdated(new Date(1));

        List<Request> requests = Arrays.asList(confirmedRequest);

        when(userController.getCurrentUser()).thenReturn(new User(driverUsername));
        when(userController.getLastReadTime(confirmedRequest.getId())).thenReturn(null);

        requestStore.setAll(requests);
        summaryMenuController.attachView(summaryMenuView);

        verify(summaryMenuView).displayTotalUnreadDriverRequests(1);
    }

}
