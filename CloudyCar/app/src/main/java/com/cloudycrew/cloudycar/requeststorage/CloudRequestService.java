package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2016-10-13.
 */

public class CloudRequestService implements IRequestService {
    @Override
    public List<Request> getRequests() {
        return new ArrayList<>();
    }

    @Override
    public List<AcceptedRequest> getAcceptedRequests() {
        return new ArrayList<>();
    }

    @Override
    public void addRequest(Request request) {

    }

    @Override
    public void updateRequest(Request request) {

    }
}
