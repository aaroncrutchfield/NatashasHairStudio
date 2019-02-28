package com.acrutchfield.natashashairstudio.utils;

import android.app.IntentService;
import android.content.Intent;



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
