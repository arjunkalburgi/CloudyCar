package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.observables.IObservable;

import java.util.Collection;
import java.util.List;

/**
 * Created by George on 2016-10-13.
 */

public interface IRequestStore extends IObservable<IRequestStore> {
    <T extends Request> T getRequest(String id, Class<T> requestClass);
    <T extends Request> List<T> getRequests(Class<T> requestClass);

    void setAll(Collection<? extends Request> requests);

    void addRequest(Request request);
    void updateRequest(Request request);
    void deleteRequest(String id);
    boolean contains(Request request);
}
