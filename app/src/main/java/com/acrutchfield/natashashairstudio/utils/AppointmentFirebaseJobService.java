package com.acrutchfield.natashashairstudio.utils;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import androidx.annotation.NonNull;

public class AppointmentFirebaseJobService extends JobService {

    AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(@NonNull JobParameters job) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            AppointmentReminderTask.execute(
                    AppointmentFirebaseJobService.this,
                    AppointmentReminderTask.ACTION_APPOINTMENT_REMINDER
            );

            jobFinished(job, false);
        });

        return true;
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
//        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return false;
    }


}
