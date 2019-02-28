package com.acrutchfield.natashashairstudio.utils;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import androidx.annotation.NonNull;

public class AppointmentFirebaseJobService extends JobService {


    @Override
    public boolean onStartJob(@NonNull JobParameters job) {

        if (SharedPrefs.getReminder(getBaseContext())) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                AppointmentReminderTask.execute(
                        AppointmentFirebaseJobService.this,
                        AppointmentReminderTask.ACTION_APPOINTMENT_REMINDER
                );

                jobFinished(job, false);
            });
            return true;
        } else {
            AppointmentReminderUtils.cancelAppointmentReminder(getBaseContext());
            return false;
        }
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
        return false;
    }


}
