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

    private static final String ACTION_SHOP = "shop";
    private static final String ACTION_BOOK = "book";
    private static final String ACTION_REVIEW = "review";
    private static final String ACTION_SOCIAL = "social";
    private static final int RC_SHOP = 101;
    private static final int RC_BOOK = 102;
    private static final int RC_REVIEW = 103;
    private static final int RC_SOCIAL = 104;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.launcher_app_widget);

        views.setOnClickPendingIntent(R.id.ShopButton, getPendingIntent(context, ACTION_SHOP, RC_SHOP));
        views.setOnClickPendingIntent(R.id.BookButton, getPendingIntent(context, ACTION_BOOK, RC_BOOK));
        views.setOnClickPendingIntent(R.id.ReviewButton, getPendingIntent(context, ACTION_REVIEW, RC_REVIEW));
        views.setOnClickPendingIntent(R.id.SocialButton, getPendingIntent(context, ACTION_SOCIAL, RC_SOCIAL));

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

