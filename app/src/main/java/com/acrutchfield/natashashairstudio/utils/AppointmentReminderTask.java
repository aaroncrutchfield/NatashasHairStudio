package com.acrutchfield.natashashairstudio.utils;

import android.content.Context;

public class AppointmentReminderTask {
    public static final String ACTION_APPOINTMENT_REMINDER = "appointment_reminder";

    static void execute(Context context, String action) {
        if(action.equals(ACTION_APPOINTMENT_REMINDER)) {
            sendAppointmentReminder(context);
        }
    }

    private static void sendAppointmentReminder(Context context) {
        NotificationUtils.createReminder(context);
    }
}
