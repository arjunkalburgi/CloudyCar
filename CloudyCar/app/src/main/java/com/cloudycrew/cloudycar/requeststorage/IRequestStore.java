package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.List;
import java.util.UUID;

/**
 * Created by George on 2016-10-13.
 */

public interface IRequestStore {
    List<Request> getRequests();
    List<Request> getAcceptedRequests();

    Request getRequest(UUID id);

    void deleteRequest(UUID id);
    void updateRequest(Request request);

    boolean contains(Request request);
}
