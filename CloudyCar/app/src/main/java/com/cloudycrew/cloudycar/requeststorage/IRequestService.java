package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.models.requests.AcceptedRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.List;

/**
 * Created by George on 2016-10-13.
 */

public interface IRequestService {
    List<Request> getRequests();
    List<AcceptedRequest> getAcceptedRequests();

    void addRequest(Request request);
    void updateRequest(Request request);
}
