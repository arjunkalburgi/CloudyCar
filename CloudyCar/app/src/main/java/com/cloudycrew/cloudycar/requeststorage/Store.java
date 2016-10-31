package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.Identifiable;
import com.cloudycrew.cloudycar.observables.IObservable;
import com.cloudycrew.cloudycar.observables.IObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by George on 2016-10-28.
 */

public class Store<T extends Identifiable> implements IObservable<Store<T>> {
    private Map<String, T> itemsMap;
    private Set<IObserver<? super Store<T>>> observers;

    public Store() {
        itemsMap = new LinkedHashMap<>();
        observers = new HashSet<>();
    }

    public List<T> getItems() {
        return new ArrayList<>(itemsMap.values());
    }

    public void setAll(Collection<? extends T> items) {
        items.clear();
        for (T item: items) {
            itemsMap.put(item.getId(), item);
        }
        notifyObservers();
    }

    public void addItem(T item) {
        itemsMap.put(item.getId(), item);
        notifyObservers();
    }

    public void updateItem(T item) {
        if (itemsMap.containsKey(item.getId())) {
            itemsMap.put(item.getId(), item);
            notifyObservers();
        }
    }

    public void deleteItem(String itemId) {
        if (itemsMap.containsKey(itemId)) {
            itemsMap.remove(itemId);
            notifyObservers();
        }
    }

    protected void notifyObservers() {
        for (IObserver<? super Store<T>> observer: observers) {
            observer.notifyUpdate(this);
        }
    }

    @Override
    public void addObserver(IObserver<? super Store<T>> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver<? super Store<T>> observer) {
        observers.remove(observer);
    }

    @Override
    public void removeAllObservers() {
        observers.clear();
    }
}
