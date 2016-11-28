package com.cloudycrew.cloudycar.utils;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;

/**
 * Created by George on 2016-11-09.
 */

public class ObservableUtils {
    public static <R> Observable<R> create(final Func0<? extends R> resultFactory) {
        return Observable.<Void>just(null)
                         .map(new Func1<Void, R>() {
                             @Override
                             public R call(Void nothing) {
                                 return resultFactory.call();
                             }
                         });
    }

    public static Observable<Void> create(final Action0 action) {
        return Observable.<Void>just(null)
                         .doOnNext(new Action1<Void>() {
                             @Override
                             public void call(Void nothing) {
                                 action.call();
                             }
                         });
    }
}
