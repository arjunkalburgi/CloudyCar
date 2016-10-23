package com.cloudycrew.cloudycar.requeststorage;

import android.util.Log;

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
    private final String ELASTIC_SEARCH_INDEX = "cmput301f16t12";
    private final String ELASTIC_SEARCH_TYPE = "request";

    private JestClient jestClient;

    public CloudRequestService(JestClient jestClient) {
        this.jestClient = jestClient;
    }

    @Override
    public List<Request> getRequests() {
        Search search = new Search.Builder("")
                .addIndex(ELASTIC_SEARCH_INDEX)
                .addType(ELASTIC_SEARCH_TYPE)
                .build();

        try {
            SearchResult result = jestClient.execute(search);

            if (result.isSucceeded()) {
                return extractRequestsFromSearchHits(result.getHits(Request.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private List<Request> extractRequestsFromSearchHits(List<SearchResult.Hit<Request, Void>> searchHits) {
        List<Request> out = new ArrayList<>();
        for(SearchResult.Hit<Request, Void> hit : searchHits) {
            out.add(hit.source);
        }

        return out;
    }

    @Override
    public List<AcceptedRequest> getAcceptedRequests() {
        return new ArrayList<>();
    }

    @Override
    public void createRequest(Request request) {
        Index index = new Index.Builder(request)
                .index(ELASTIC_SEARCH_INDEX)
                .type(ELASTIC_SEARCH_TYPE)
                .id(request.getId().toString())
                .build();

        try {
            DocumentResult result = jestClient.execute(index);

            if (result.isSucceeded()) {
                Log.i("Good", "We made the thing");
            }
            else {
                Log.i("Error", "Elastic search was not able to add the tweet.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRequest(Request request) {
        Update update = new Update.Builder(request)
                .index(ELASTIC_SEARCH_INDEX)
                .type(ELASTIC_SEARCH_TYPE)
                .id(request.getId().toString())
                .build();

        try {
            jestClient.execute(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRequest(String requestId) {
        Delete delete = new Delete.Builder(requestId)
                .index(ELASTIC_SEARCH_INDEX)
                .type(ELASTIC_SEARCH_TYPE)
                .build();

        try {
            jestClient.execute(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
