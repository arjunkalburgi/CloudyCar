package com.cloudycrew.cloudycar.observables;

/**
 * Created by George on 2016-10-27.
 *
 * @param <T> the type being observed
 */
public interface IObserver<T> {
    /**
     * Callback called when the observable has changed
     *
     * @param observable the observable
     */
    void notifyUpdate(T observable);
}
