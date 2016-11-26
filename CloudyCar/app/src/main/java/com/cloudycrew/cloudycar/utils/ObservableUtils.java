package com.cloudycrew.cloudycar.utils;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;

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

    public static <T1, T2, R> Observable<R> fromFunction(Func2<T1, T2, R> function, T1 input1, T2 input2) {
        return Observable.just(null)
                         .map(nothing -> function.call(input1, input2));
    }

    public static <T1, T2, T3, R> Observable<R> fromFunction(Func3<T1, T2, T3, R> function, T1 input1, T2 input2, T3 input3) {
        return Observable.just(null)
                .map(nothing -> function.call(input1, input2, input3));
    }
}
