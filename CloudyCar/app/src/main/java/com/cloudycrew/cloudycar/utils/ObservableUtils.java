package com.cloudycrew.cloudycar.utils;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Action2;
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

    public static <T1, T2> Observable<Void> fromAction(Action2<T1, T2> action, T1 t1, T2 t2) {
        return Observable.<Void>just(null)
                         .doOnNext(nothing -> action.call(t1, t2));
    }

    public static <T, R> Observable<R> fromFunction(Func1<T, R> function, T input) {
        return Observable.just(input)
                         .map(function);
    }
}
