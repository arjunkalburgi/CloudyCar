package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.elasticsearch.IElasticSearchService;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by George on 2016-10-13.
 */

public class CloudRequestService implements IRequestService {
    private String username;
    private IElasticSearchService<Request> elasticSearchService;

    public CloudRequestService(String username, IElasticSearchService<Request> elasticSearchService) {
        this.username = username;
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    public List<Request> getRequests() {
        return Observable.from(elasticSearchService.getAll())
                         .filter(r -> r.getRider().equals(username))
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }

    @Override
    public void createRequest(Request request) {
        elasticSearchService.create(request);
    }

    @Override
    public void updateRequest(Request request) {
        elasticSearchService.update(request);
    }

    @Override
    public void deleteRequest(String requestId) {
        elasticSearchService.delete(requestId);
    }
}
