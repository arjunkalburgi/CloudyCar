package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.models.requests.Request;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by George on 2016-11-07.
 */

public class RequestStore extends Store<Request> {
    public <T extends Request> T getRequest(String requestId, Class<T> requestClass) {
        return Observable.from(getItems())
                         .filter(r -> r.getId().equals(requestId))
                         .filter(r -> r.getClass().isAssignableFrom(requestClass))
                         .cast(requestClass)
                         .toBlocking()
                         .firstOrDefault(null);
    }

    public <T extends Request> List<T> getRequests(Class<T> requestClass) {
        return Observable.from(getItems())
                         .filter(r -> r.getClass().isAssignableFrom(requestClass))
                         .cast(requestClass)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }
}
