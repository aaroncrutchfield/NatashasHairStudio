package com.acrutchfield.natashashairstudio.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


// https://github.com/udacity/ud851-Exercises/blob/student/Lesson09b-ToDo-List-AAC/
// T09b.04-Solution-Executors/app/src/main/java/com/example/android/todolist/AppExecutors.java

public class AppExecutors {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;

    private AppExecutors(Executor diskIO) {
        this.diskIO = diskIO;

    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }
}