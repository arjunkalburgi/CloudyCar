package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.elasticsearch.IElasticSearchService;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.search.ElasticSearchQueryBuilder;
import com.cloudycrew.cloudycar.search.SearchContext;
import com.cloudycrew.cloudycar.users.IUserPreferences;

import java.util.Collection;
import java.util.List;


/**
 * Created by George on 2016-10-13.
 */

public class CloudRequestService implements IRequestService {

    private IElasticSearchService<Request> elasticSearchService;

    public CloudRequestService(IElasticSearchService<Request> elasticSearchService) {
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
    public void batchUpdateRequests(Collection<? extends Request> requests) {
        for (Request request: requests) {
            updateRequest(request);
        }
    }

    @Override
    public void deleteRequest(String requestId) {

        elasticSearchService.delete(requestId);
    }

    @Override
    public List<Request> search(SearchContext searchContext) {
        String query = getQueryBuilder().buildQuery(searchContext);
        return elasticSearchService.search(query);
    }

    private ElasticSearchQueryBuilder getQueryBuilder() {
        return new ElasticSearchQueryBuilder();
    }
}
