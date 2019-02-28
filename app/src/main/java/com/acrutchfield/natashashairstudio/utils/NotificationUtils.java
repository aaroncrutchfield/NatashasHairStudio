package com.acrutchfield.natashashairstudio.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.activity.MainActivity;

import androidx.core.app.NotificationCompat;

class NotificationUtils {

    private static final String APPOINTMENT_NOTIFICATION_CHANNEL_ID = "appointment_notification_channel_id";
    private static final String APPOINTMENT_CHANNEL_NAME = "appointmentChannelName";
    private static final String CONTENT_TITLE = "Book Now";
    private static final String CONTENT_TEXT = "Don't forget to book your weekly appointment.";

    static void createReminder(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(APPOINTMENT_NOTIFICATION_CHANNEL_ID,
                    APPOINTMENT_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, APPOINTMENT_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(CONTENT_TITLE)
                .setContentText(CONTENT_TEXT)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(getContentIntent(context))
                .setSmallIcon(R.drawable.ic_book)
                        .setAutoCancel(true);

        manager.notify(1, notificationBuilder.build());
    }

    private static PendingIntent getContentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(AppointmentReminderTask.ACTION_APPOINTMENT_REMINDER);

        return PendingIntent.getActivity(context, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
