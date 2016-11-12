package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.elasticsearch.IElasticSearchService;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.users.IUserPreferences;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by George on 2016-10-13.
 */

public class CloudRequestService implements IRequestService {
    private IUserPreferences userPreferences;
    private IElasticSearchService<Request> elasticSearchService;

    public CloudRequestService(IUserPreferences userPreferences, IElasticSearchService<Request> elasticSearchService) {
        this.userPreferences = userPreferences;
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    public List<Request> getRequests() {
        return elasticSearchService.getAll();
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
