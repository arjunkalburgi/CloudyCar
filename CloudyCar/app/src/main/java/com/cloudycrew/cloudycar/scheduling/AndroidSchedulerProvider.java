package com.cloudycrew.cloudycar.scheduling;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by George on 2016-11-08.
 */

public class AndroidSchedulerProvider implements ISchedulerProvider {

    @Override
    public Scheduler mainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler ioScheduler() {
        return Schedulers.io();
    }

    @Override
    public Scheduler computationScheduler() {
        return Schedulers.computation();
    }
}
