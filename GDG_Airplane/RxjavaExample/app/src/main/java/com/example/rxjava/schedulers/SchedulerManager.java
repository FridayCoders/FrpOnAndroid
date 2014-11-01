package com.example.rxjava.schedulers;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulerManager {

    private static Scheduler main = AndroidSchedulers.mainThread();
    private static Scheduler io = Schedulers.io();

    public static void setMain(Scheduler scheduler) {
        main = scheduler;
    }

    public static void setIO(Scheduler scheduler) {
        io = scheduler;
    }

    public static Scheduler main() {
        return main;
    }

    public static Scheduler io() {
        return io;
    }
}
