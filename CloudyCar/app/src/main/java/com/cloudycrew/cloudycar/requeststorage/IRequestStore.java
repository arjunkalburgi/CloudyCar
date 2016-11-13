package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.observables.IObservable;

import java.util.Collection;
import java.util.List;

/**
 * Created by George on 2016-10-13.
 */
public interface IRequestStore extends IObservable<IRequestStore> {
    /**
     * Gets request.
     *
     * @param id the id
     * @return the request
     */
    Request getRequest(String id);

    /**
     * Gets request.
     *
     * @param <T>          the end of the request
     * @param id           the id
     * @param requestClass the request type being queried
     * @return the request
     */
    <T extends Request> T getRequest(String id, Class<T> requestClass);

    /**
     * Gets requests.
     *
     * @return the requests
     */
    List<Request> getRequests();

    /**
     * Gets requests.
     *
     * @param <T>          the end of the request
     * @param requestClass the request type being queried
     * @return the requests
     */
    <T extends Request> List<T> getRequests(Class<T> requestClass);

    /**
     * Sets all requests in the store.
     *
     * @param requests the requests
     */
    void setAll(Collection<? extends Request> requests);

    /**
     * Add all requests in the store.
     *
     * @param requests the requests
     */
    void addAll(Collection<? extends Request> requests);

    /**
     * Add request.
     *
     * @param request the request
     */
    void addRequest(Request request);

    /**
     * Update request.
     *
     * @param request the request
     */
    void updateRequest(Request request);

    /**
     * Delete request.
     *
     * @param id the id
     */
    void deleteRequest(String id);

    /**
     * Checks if store contains a request.
     *
     * @param request the request
     * @return the boolean
     */
    boolean contains(Request request);
}
