package com.cloudycrew.cloudycar.scheduling;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by George on 2016-11-08.
 */

public class TestSchedulerProvider implements ISchedulerProvider {
    @Override
    public Scheduler mainThreadScheduler() {
        return Schedulers.immediate();
    }

    @Override
    public Scheduler ioScheduler() {
        return Schedulers.immediate();
    }

    @Override
    public Scheduler computationScheduler() {
        return Schedulers.immediate();
    }
}
