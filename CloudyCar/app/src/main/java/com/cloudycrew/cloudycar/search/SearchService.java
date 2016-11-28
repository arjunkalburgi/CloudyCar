package com.cloudycrew.cloudycar.search;

import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requeststorage.IRequestService;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.users.IUserPreferences;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by George on 2016-11-12.
 */

public class SearchService implements ISearchService {
    private IUserPreferences userPreferences;
    private RequestController requestController;

    public SearchService(IUserPreferences userPreferences, RequestController requestController) {
        this.userPreferences = userPreferences;
        this.requestController = requestController;
    }

    private boolean doesRequestBelongToCurrentUser(Request request) {
        return request.getRider().equals(userPreferences.getUserName());
    }

    /**
     * Call to start searching based on the provided searchContext
     * @param searchContext the object containing the search parameters to use for searching
     * @return The list of PendingRequests that meet the search criteria
     */
    @Override
    public List<PendingRequest> search(SearchContext searchContext) {
        List<Request> requests = requestController.searchRequests(searchContext);
        return getPendingRequestsThatDoNotBelongToTheCurrentUser(requests);
    }

    /**
     * Format and filter the requests returned by the requestService
     * @param requests The list of requests returned by the requestService
     * @return A further filtered list of PendingRequests
     */
    private List<PendingRequest> getPendingRequestsThatDoNotBelongToTheCurrentUser(List<Request> requests) {
        return Observable.from(requests)
                         .filter(new Func1<Request, Boolean>() {
                             @Override
                             public Boolean call(Request r) {
                                 return r instanceof PendingRequest &&
                                         !doesRequestBelongToCurrentUser(r);
                             }
                         })
                        .cast(PendingRequest.class)
                        .toList()
                        .toBlocking()
                        .firstOrDefault(new ArrayList<PendingRequest>());
    }
}
