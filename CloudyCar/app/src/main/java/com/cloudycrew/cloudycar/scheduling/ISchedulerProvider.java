package com.cloudycrew.cloudycar.scheduling;

import rx.Scheduler;

/**
 * Created by George on 2016-11-08.
 */

public interface ISchedulerProvider {
    Scheduler mainThreadScheduler();
    Scheduler ioScheduler();
    Scheduler computationScheduler();
}
