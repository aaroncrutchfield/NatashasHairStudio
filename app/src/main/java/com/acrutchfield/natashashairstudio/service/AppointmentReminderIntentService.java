package com.acrutchfield.natashashairstudio.service;

import android.app.IntentService;
import android.content.Intent;

import com.acrutchfield.natashashairstudio.utils.AppointmentReminderTask;


public class AppointmentReminderIntentService extends IntentService {


    public AppointmentReminderIntentService() {
        super("AppointmentReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        assert action != null;
        AppointmentReminderTask.execute(this, action);
    }
}
