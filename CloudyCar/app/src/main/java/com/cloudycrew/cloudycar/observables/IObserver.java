package com.cloudycrew.cloudycar.observables;

/**
 * Created by George on 2016-10-27.
 */

public interface IObserver<T> {
    void notifyUpdate(T observable);
}
