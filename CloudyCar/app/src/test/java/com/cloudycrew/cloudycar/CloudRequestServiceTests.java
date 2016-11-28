package com.cloudycrew.cloudycar;

import com.cloudycrew.cloudycar.elasticsearch.IElasticSearchService;
import com.cloudycrew.cloudycar.models.Location;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requeststorage.CloudRequestService;
import com.cloudycrew.cloudycar.search.SearchContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by George on 2016-11-28.
 */

@RunWith(MockitoJUnitRunner.class)
public class CloudRequestServiceTests {
    @Mock
    private IElasticSearchService<Request> elasticSearchService;

    private PendingRequest request1;
    private PendingRequest request2;

    private CloudRequestService cloudRequestService;

    @Before
    public void set_up() {
        String locationDescription = "test description";
        String requestDescription = "description";

        String riderUsername1 = "rider1";
        String riderUsername2 = "rider2";

        Location startLocation = new Location(48.1472373, 11.5673969, locationDescription);
        Location endLocation = new Location(48.1258551, 11.5121003, locationDescription);
        Route route = new Route(startLocation, endLocation);
        double price = 2.5;

        request1 = new PendingRequest(riderUsername1, route, price, requestDescription);
        request2 = new PendingRequest(riderUsername2, route, price, requestDescription);

        cloudRequestService = new CloudRequestService(elasticSearchService);
    }

    @Test
    public void test_createRequest_callsCreateInElasticSearch() {
        cloudRequestService.createRequest(request1);

        verify(elasticSearchService).create(request1);
    }

    @Test
    public void test_updateRequest_callsUpdateInElasticSearch() {
        cloudRequestService.updateRequest(request1);

        verify(elasticSearchService).update(request1);
    }

    @Test
    public void test_deleteRequest_callsDeleteInElasticSearch() {
        cloudRequestService.deleteRequest(request1.getId());

        verify(elasticSearchService).delete(request1.getId());
    }

    @Test
    public void test_getRequests_returnsRequestsInElasticSearch() {
        when(elasticSearchService.getAll()).thenReturn(Arrays.<Request>asList(request1, request2));

        List<Request> expectedRequests = Arrays.<Request>asList(request1, request2);
        List<Request> actualRequests = cloudRequestService.getRequests();

        assertEquals(expectedRequests, actualRequests);
    }

    @Test
    public void test_searchRequests_returnsSearchResultsFromElasticSearch() {
        when(elasticSearchService.search(any(String.class))).thenReturn(Arrays.<Request>asList(request2));

        List<Request> expectedRequests = Arrays.<Request>asList(request2);
        List<Request> actualRequests = cloudRequestService.search(new SearchContext());

        assertEquals(expectedRequests, actualRequests);
    }
}
