package com.acrutchfield.natashashairstudio.service;

import com.acrutchfield.natashashairstudio.utils.AppExecutors;
import com.acrutchfield.natashashairstudio.utils.AppointmentReminderTask;
import com.acrutchfield.natashashairstudio.utils.AppointmentReminderUtils;
import com.acrutchfield.natashashairstudio.utils.SharedPrefs;
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
