package com.cloudycrew.cloudycar;

/**
 * Created by George on 2016-11-05.
 */

public class ViewController<T> {
    private T view;

    public T getView() {
        return view;
    }

    public boolean isViewAttached() {
        return getView() != null;
    }
    
    public void attachView(T view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }
}
