package com.acrutchfield.natashashairstudio.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    private static final String PREFS_NAME = "prefs";
    private static final String REMINDER = "reminder";

    public static void setReminder(Context context, boolean willRemind) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.putBoolean(REMINDER, willRemind);

        prefs.apply();
    }

    public static boolean getReminder(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(REMINDER, false);
    }
}