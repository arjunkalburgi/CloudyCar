package com.cloudycrew.cloudycar;

/**
 * Created by George on 2016-10-12.
 */

import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.search.ISearchService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchingTests {
    @Mock
    private IRequestStore requestStore;
    @Mock
    private ISearchService searchService;

    private Request request1;
    private Request request2;

    @Before
    public void set_up() {
        Point startingPoint1 = new Point(48.1472373, 11.5673969);
        Point endingPoint1 = new Point(48.1258551, 11.5121003);

        Route route1 = new Route();
        route1.addPoint(startingPoint1);
        route1.addPoint(endingPoint1);

        Point startingPoint2 = new Point(53.5225, 113.6242);
        Point endingPoint2 = new Point(53.5232, 113.5263);

        Route route2 = new Route();
        route2.addPoint(startingPoint2);
        route2.addPoint(endingPoint2);

        request1 = new PendingRequest();
        request1.setId("request-1");
        request1.setRoute(route1);

        request2 = new PendingRequest();
        request2.setId("request-2");
        request2.setRoute(route2);

        when(requestStore.getRequests()).thenReturn(Arrays.asList(request1, request2));
    }

    @Test
    public void test_searchByGeoLocation_ifThereAreNoMatchingResults_thenReturnsEmptyList() {
        Point pointFarFromAllRequests = new Point(0, 0);

        List<Request> searchResults = searchService.searchWithPoint(pointFarFromAllRequests);

        assertTrue(searchResults.isEmpty());
    }

    @Test
    public void test_searchByGeoLocation_ifThereAreMatchingResults_thenReturnsResults() {
        Point pointFarFromAllRequests = new Point(48.1472373, 11.5673969);

        List<Request> expectedSearchResults = Arrays.asList(request1);
        List<Request> actualSearchResults = searchService.searchWithPoint(pointFarFromAllRequests);

        assertEquals(expectedSearchResults, actualSearchResults);
    }

    @Test
    public void test_searchByKeyword_ifThereAreNoMatchingResults_thenReturnsEmptyList() {
        String keyword = "pacific ocean";

        List<Request> searchResults = searchService.searchWithKeyword(keyword);

        assertTrue(searchResults.isEmpty());
    }

    @Test
    public void test_searchByKeyword_ifThereAreMatchingResults_thenReturnsResults() {
        String keyword = "west edmonton mall";

        List<Request> expectedSearchResults = Arrays.asList(request2);
        List<Request> actualSearchResults = searchService.searchWithKeyword(keyword);

        assertEquals(expectedSearchResults, actualSearchResults);
    }
}
