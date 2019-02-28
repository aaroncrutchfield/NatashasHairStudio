package com.acrutchfield.natashashairstudio.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.activity.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class LauncherAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.launcher_app_widget);

        views.setOnClickPendingIntent(R.id.ShopButton, getPendingIntent(context, "shop", 101));
        views.setOnClickPendingIntent(R.id.BookButton, getPendingIntent(context, "book", 102));
        views.setOnClickPendingIntent(R.id.ReviewButton, getPendingIntent(context, "review", 103));
        views.setOnClickPendingIntent(R.id.SocialButton, getPendingIntent(context, "social", 104));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static PendingIntent getPendingIntent(Context context, String action, int requestCode) {
        Intent intent = new Intent(context , MainActivity.class);
        intent.setAction(action);

        return PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

