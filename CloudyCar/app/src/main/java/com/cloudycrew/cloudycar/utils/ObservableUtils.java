package com.cloudycrew.cloudycar.utils;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by George on 2016-11-09.
 */

public class ObservableUtils {
    public static <T> Observable<Void> fromAction(Action1<T> action, T input) {
        return Observable.just(input)
                         .doOnNext(action)
                         .map(t -> null);
    }

    public static <T, R> Observable<R> fromFunction(Func1<T, R> function, T input) {
        return Observable.just(input)
                         .map(function);
    }
}
