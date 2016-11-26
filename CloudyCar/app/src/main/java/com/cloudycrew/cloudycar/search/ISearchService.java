package com.cloudycrew.cloudycar.search;

import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.List;

/**
 * Created by George on 2016-10-13.
 */
public interface ISearchService {
    /**
     * Gets requests that match the search context
     *
     * @param searchContext the context of the search
     * @return the matching requests
     */
    List<PendingRequest> search(SearchContext searchContext);
}
