package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.observables.IObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;

/**
 * Created by George on 2016-11-07.
 */

public class RequestStore implements IRequestStore {
    private Map<String, Request> requestMap;
    private Set<IObserver<? super IRequestStore>> observers;

    public RequestStore() {
        this.requestMap = new LinkedHashMap<>();
        this.observers = new HashSet<>();
    }

    @Override
    public Request getRequest(String id) {
        return getRequest(id, Request.class);
    }

    public <T extends Request> T getRequest(String requestId, Class<T> requestClass) {
        return Observable.from(requestMap.values())
                         .filter(r -> r.getId().equals(requestId))
                         .filter(r -> r.getClass().isAssignableFrom(requestClass))
                         .cast(requestClass)
                         .toBlocking()
                         .firstOrDefault(null);
    }

    @Override
    public List<Request> getRequests() {
        return getRequests(Request.class);
    }

    public <T extends Request> List<T> getRequests(Class<T> requestClass) {
        return Observable.from(requestMap.values())
                         .filter(r -> r.getClass().isAssignableFrom(requestClass))
                         .cast(requestClass)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }

    @Override
    public void setAll(Collection<? extends Request> requests) {
        requestMap.clear();
        for (Request request: requests) {
            requestMap.put(request.getId(), request);
        }
    }

    @Override
    public void addRequest(Request request) {
        if (!requestMap.containsKey(request.getId())) {
            requestMap.put(request.getId(), request);
            notifyObservers();
        }
    }

    @Override
    public void updateRequest(Request request) {
        if (requestMap.containsKey(request.getId())) {
            requestMap.put(request.getId(), request);
            notifyObservers();
        }
    }

    @Override
    public void deleteRequest(String id) {
        if (requestMap.containsKey(id)) {
            requestMap.remove(id);
            notifyObservers();
        }
    }

    @Override
    public boolean contains(Request request) {
        return requestMap.containsKey(request.getId());
    }

    protected void notifyObservers() {
        for (IObserver<? super IRequestStore> observer: observers) {
            observer.notifyUpdate(this);
        }
    }

    @Override
    public void addObserver(IObserver<? super IRequestStore> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver<? super IRequestStore> observer) {
        observers.remove(observer);
    }

    @Override
    public void removeAllObservers() {
        observers.clear();
    }
}
