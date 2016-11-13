package com.cloudycrew.cloudycar.observables;

/**
 * Created by George on 2016-10-27.
 *
 * @param <T> the type being observed
 */
public interface IObservable<T> {
    /**
     * Add observer.
     *
     * @param observer the observer
     */
    void addObserver(IObserver<? super T> observer);

    /**
     * Remove observer.
     *
     * @param observer the observer
     */
    void removeObserver(IObserver<? super T> observer);

    /**
     * Remove all observers.
     */
    void removeAllObservers();
}
