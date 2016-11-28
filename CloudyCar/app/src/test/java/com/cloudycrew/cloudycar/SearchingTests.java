package com.cloudycrew.cloudycar;

/**
 * Created by George on 2016-10-12.
 */

import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.models.Location;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.requests.CancelledRequest;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requeststorage.IRequestService;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.search.SearchContext;
import com.cloudycrew.cloudycar.search.SearchService;
import com.cloudycrew.cloudycar.users.IUserPreferences;

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

@RunWith(MockitoJUnitRunner.class)
public class SearchingTests {
    @Mock
    private IUserPreferences userPreferences;
    @Mock
    private RequestController requestController;

    private PendingRequest request1;

    private PendingRequest request2;
    private PendingRequest acceptedRequest2;
    private ConfirmedRequest confirmedRequest2;
    private CompletedRequest completedRequest2;
    private CancelledRequest cancelledRequest2;

    private String locationDescription;
    private String requestDescription;

    private String riderUsername1;
    private String riderUsername2;
    private String driverUsername;

    private SearchContext searchContext;
    private SearchService searchService;

    @Before
    public void set_up() {
        locationDescription = "test description";
        requestDescription = "description";

        riderUsername1 = "rider1";
        riderUsername2 = "rider2";
        driverUsername = "driver";

        Location startLocation = new Location(48.1472373, 11.5673969, locationDescription);
        Location endLocation = new Location(48.1258551, 11.5121003, locationDescription);
        Route route = new Route(startLocation, endLocation);
        double price = 2.5;

        request1 = new PendingRequest(riderUsername1, route, price, requestDescription);

        request2 = new PendingRequest(riderUsername2, route, price, requestDescription);
        acceptedRequest2 = request2.accept(driverUsername);
        confirmedRequest2 = acceptedRequest2.confirmRequest(driverUsername);
        completedRequest2 = confirmedRequest2.completeRequest();
        cancelledRequest2 = request2.cancel();

        searchContext = new SearchContext();

        when(userPreferences.getUserName()).thenReturn(riderUsername1);
        searchService = new SearchService(userPreferences, requestController);
    }

    @Test
    public void test_ifSearchOnlyContainsRequestsForTheCurrentUser_thenReturnsAnEmptyList() {
        List<Request> requestsMatchingSearch = Arrays.<Request>asList(request1);
        when(requestController.searchRequests(any(SearchContext.class))).thenReturn(requestsMatchingSearch);

        List<PendingRequest> expectedResults = new ArrayList<>();
        List<PendingRequest> actualResults = searchService.search(searchContext);

        assertEquals(expectedResults, actualResults);
    }

    @Test
    public void test_ifSearchContainsRequestsForOtherUsers_thenReturnsThoseRequests() {
        List<Request> requestsMatchingSearch = Arrays.<Request>asList(request1, request2);
        when(requestController.searchRequests(any(SearchContext.class))).thenReturn(requestsMatchingSearch);

        List<PendingRequest> expectedResults = Arrays.asList(request2);
        List<PendingRequest> actualResults = searchService.search(searchContext);

        assertEquals(expectedResults, actualResults);
    }

    @Test
    public void test_ifSearchContainsAcceptedRequestsForOtherUsers_thenReturnsThoseRequests() {
        List<Request> requestsMatchingSearch = Arrays.<Request>asList(request1, request2, acceptedRequest2);
        when(requestController.searchRequests(any(SearchContext.class))).thenReturn(requestsMatchingSearch);

        List<PendingRequest> expectedResults = Arrays.asList(request2, acceptedRequest2);
        List<PendingRequest> actualResults = searchService.search(searchContext);

        assertEquals(expectedResults, actualResults);
    }

    @Test
    public void test_searchDoesNotReturnConfirmedCompletedOrCanceledRequests() {
        List<Request> requestsMatchingSearch = Arrays.<Request>asList(request1,
                request2, acceptedRequest2, confirmedRequest2, completedRequest2, cancelledRequest2);

        when(requestController.searchRequests(any(SearchContext.class))).thenReturn(requestsMatchingSearch);

        List<PendingRequest> expectedResults = Arrays.asList(request2, acceptedRequest2);
        List<PendingRequest> actualResults = searchService.search(searchContext);

        assertEquals(expectedResults, actualResults);
    }
}
