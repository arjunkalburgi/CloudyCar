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
     * Gets requests within a point
     *
     * @param point the point
     * @return the requests
     */
    List<PendingRequest> searchWithPoint(Point point);

    /**
     * Gets requests close to a keyword
     *
     * @param keyword the keyword
     * @return the list
     */
    List<PendingRequest> searchWithKeyword(String keyword);
}
