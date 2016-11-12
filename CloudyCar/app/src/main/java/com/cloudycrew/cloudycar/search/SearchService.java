package com.cloudycrew.cloudycar.search;

import com.cloudycrew.cloudycar.elasticsearch.IElasticSearchService;
import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.users.IUserPreferences;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by George on 2016-11-12.
 */

public class SearchService implements ISearchService {
    private IUserPreferences userPreferences;
    private IElasticSearchService<Request> requestElasticSearchService;

    public SearchService(IUserPreferences userPreferences, IElasticSearchService<Request> requestElasticSearchService) {
        this.userPreferences = userPreferences;
        this.requestElasticSearchService = requestElasticSearchService;
    }

    private boolean doesRequestBelongToCurrentUser(Request request) {
        return request.getRider().equals(userPreferences.getUserName());
    }

    private List<PendingRequest> getPendingRequestsThatDoNotBelongToTheCurrentUser(List<Request> requests) {
        return Observable.from(requests)
                         .filter(r -> r instanceof PendingRequest)
                         .filter(r -> !doesRequestBelongToCurrentUser(r))
                         .cast(PendingRequest.class)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }

    @Override
    public List<PendingRequest> searchWithPoint(Point point) {
        return getPendingRequestsThatDoNotBelongToTheCurrentUser(requestElasticSearchService.getAll());
    }

    @Override
    public List<PendingRequest> searchWithKeyword(String keyword) {
        return getPendingRequestsThatDoNotBelongToTheCurrentUser(requestElasticSearchService.getAll());
    }
}
