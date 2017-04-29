package com.vs.pregnancyweekwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 305022193 on 1/16/2017.
 */

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        Log.i("Widget","OnUpdate");
        for (int i = 0; i < count; i++) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String LMPStr = context.getSharedPreferences("PregnancyWidget", Context.MODE_PRIVATE).getString("Date", "Error");
            try {
                Date date = format.parse(LMPStr);
                Calendar today = Calendar.getInstance();
                Calendar LMP = Calendar.getInstance();
                LMP.setTime(date);
                long diff = today.getTimeInMillis() - LMP.getTimeInMillis(); //result in millis
                long days = diff / (24 * 60 * 60 * 1000);
                long weeks = days / 7;
                days = days % 7;
                remoteViews.setTextViewText(R.id.textView, weeks + "W + " + days + "D");
            } catch (Exception e) {
                e.printStackTrace();
                remoteViews.setTextViewText(R.id.textView, "Error");
            }

            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.textView, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
    }
}
