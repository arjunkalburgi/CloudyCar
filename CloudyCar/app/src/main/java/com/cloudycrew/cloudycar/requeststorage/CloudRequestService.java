package com.cloudycrew.cloudycar.requeststorage;

import android.util.Log;

import com.cloudycrew.cloudycar.elasticsearch.IElasticSearchService;
import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

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
