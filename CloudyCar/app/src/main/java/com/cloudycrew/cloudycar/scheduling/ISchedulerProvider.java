package com.cloudycrew.cloudycar.scheduling;

import rx.Scheduler;

/**
 * Created by George on 2016-11-08.
 */
public interface ISchedulerProvider {
    /**
     * Gets a Main thread scheduler.
     *
     * @return the scheduler
     */
    Scheduler mainThreadScheduler();

    /**
     * Gets an Io scheduler scheduler.
     *
     * @return the scheduler
     */
    Scheduler ioScheduler();

    /**
     * Gets a Computation scheduler.
     *
     * @return the scheduler
     */
    Scheduler computationScheduler();
}
