package com.acrutchfield.natashashairstudio.utils;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class AppointmentReminderUtils {

    private static final int REMINDER_INTERVAL_DAYS = 7;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.DAYS.toSeconds(REMINDER_INTERVAL_DAYS));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS + 120;

    private static final String REMINDER_JOB_TAG = "apppointment_reminder_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleAppointmentReminder(Context context) {
        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintRemiderJob = dispatcher.newJobBuilder()
                .setService(AppointmentFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintRemiderJob);

        sInitialized = true;
    }

    synchronized public static void cancelAppointmentReminder(Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        dispatcher.cancel(REMINDER_JOB_TAG);
    }
}
