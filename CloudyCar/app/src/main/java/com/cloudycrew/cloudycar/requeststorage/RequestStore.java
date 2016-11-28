package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.observables.IObserver;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by George on 2016-11-07.
 */

public class RequestStore implements IRequestStore {
    private Map<String, Request> requestMap;
    private Set<IObserver<? super IRequestStore>> observers;
    private ISchedulerProvider schedulerProvider;

    public RequestStore(ISchedulerProvider schedulerProvider) {
        this.requestMap = new LinkedHashMap<>();
        this.observers = new HashSet<>();
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Request getRequest(String id) {
        return getRequest(id, Request.class);
    }

    public <T extends Request> T getRequest(final String requestId, final Class<T> requestClass) {
        return Observable.from(requestMap.values())
                         .filter(new Func1<Request, Boolean>() {
                             @Override
                             public Boolean call(Request r) {
                                 return r.getId().equals(requestId) &&
                                         requestClass.isAssignableFrom(r.getClass());
                             }
                         })
                         .cast(requestClass)
                         .toBlocking()
                         .firstOrDefault(null);
    }

    @Override
    public List<Request> getRequests() {
        return getRequests(Request.class);
    }

    public <T extends Request> List<T> getRequests(final Class<T> requestClass) {
        return Observable.from(requestMap.values())
                         .filter(new Func1<Request, Boolean>() {
                             @Override
                             public Boolean call(Request r) {
                                 return requestClass.isAssignableFrom(r.getClass());
                             }
                         })
                         .cast(requestClass)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<T>());
    }

    @Override
    public void setAll(Collection<? extends Request> requests) {
        requestMap.clear();
        for (Request request: requests) {
            requestMap.put(request.getId(), request);
        }
        notifyObservers();
    }

    @Override
    public void addAll(Collection<? extends Request> requests) {
        for (Request request: requests) {
            requestMap.put(request.getId(), request);
        }
        notifyObservers();
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
        Observable.from(observers)
                  .observeOn(schedulerProvider.mainThreadScheduler())
                  .forEach(new Action1<IObserver<? super IRequestStore>>() {
                      @Override
                      public void call(IObserver<? super IRequestStore> observer) {
                          observer.notifyUpdate(RequestStore.this);
                      }
                  });
    }

    @Override
    public void addObserver(IObserver<? super IRequestStore> observer) {
        observers.add(observer);
        observer.notifyUpdate(this);
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
