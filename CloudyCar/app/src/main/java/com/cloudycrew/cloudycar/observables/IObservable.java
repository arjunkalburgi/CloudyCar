package com.cloudycrew.cloudycar.observables;

/**
 * Created by George on 2016-10-27.
 */

public interface IObservable<T> {
    void addObserver(IObserver<? super T> observer);
    void removeObserver(IObserver<? super T> observer);
    void removeAllObservers();
}
