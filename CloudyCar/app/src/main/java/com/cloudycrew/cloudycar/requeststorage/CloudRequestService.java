package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.elasticsearch.IElasticSearchService;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2016-10-13.
 */

public class CloudRequestService implements IRequestService {
    IElasticSearchService<Request> elasticSearchService;

    public CloudRequestService(IElasticSearchService<Request> elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    public List<Request> getRequests() {
        return elasticSearchService.getAll();
    }

    @Override
    public List<AcceptedRequest> getAcceptedRequests() {
        return new ArrayList<>();
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
