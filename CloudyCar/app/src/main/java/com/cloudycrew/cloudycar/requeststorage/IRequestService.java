package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.search.SearchContext;

import java.util.Collection;
import java.util.List;

/**
 * Created by George on 2016-10-13.
 */
public interface IRequestService {
    /**
     * Gets requests.
     *
     * @return the requests
     */
    List<Request> getRequests();

    /**
     * Create request.
     *
     * @param request the request
     */
    void createRequest(Request request);

    /**
     * Update request.
     *
     * @param request the request
     */
    void updateRequest(Request request);

    /**
     * Update request.
     *
     * @param requests the batch to update
     */
    void batchUpdateRequests(Collection<? extends Request> requests);

    /**
     * Delete request.
     *
     * @param requestId the request id
     */
    void deleteRequest(String requestId);


    /**
     * Search for requests.
     *
     * @param searchContext the search context
     */
    List<Request> search(SearchContext searchContext);
}
