package com.example.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        views.setOnClickPendingIntent(R.id.np, getPendingSelfIntent(context, "np", 0, appWidgetId));
        views.setOnClickPendingIntent(R.id.br, getPendingSelfIntent(context, "br", 1, appWidgetId));
        views.setOnClickPendingIntent(R.id.yc, getPendingSelfIntent(context, "yc", 2, appWidgetId));
        views.setOnClickPendingIntent(R.id.cc, getPendingSelfIntent(context, "cc", 3, appWidgetId));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    protected static PendingIntent getPendingSelfIntent(Context context, String action, int rc, int appID) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(action);
        return PendingIntent.getService(context, rc, intent, 0);
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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getStringExtra("response") != null) {

            String response = intent.getStringExtra("response");

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, com.example.bakingapp.IngredientsWidget.class);

            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for(int widgetId : allWidgetIds){
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
                remoteViews.setTextViewText(R.id.ingredientItem, response);
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
    }
}

