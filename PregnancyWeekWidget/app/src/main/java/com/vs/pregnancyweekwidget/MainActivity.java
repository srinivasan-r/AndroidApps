package com.vs.pregnancyweekwidget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;
    private DatePickerDialog.OnDateSetListener dateListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Log.i("Date ", "setting date" + monthOfYear);
                    setDate(year, monthOfYear, dayOfMonth);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("PregnancyWidget",MODE_PRIVATE);
        prefEditor = preferences.edit();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!preferences.contains("Date")) {
            Calendar calendar;
            calendar = Calendar.getInstance();
            setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        } else {
            TextView tvDate = (TextView) findViewById(R.id.date);
            tvDate.setText(preferences.getString("Date", "Error"));
        }
    }

    public void showDateDialog(View view) {
        createDatePickerDialog().show();
    }

    private Dialog createDatePickerDialog() {
        int year = 0, month = 0, day = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String LMPStr = preferences.getString("Date", "Error");
        try {
            Date date = format.parse(LMPStr);
            Calendar LMP = Calendar.getInstance();
            LMP.setTime(date);
            year = LMP.get(Calendar.YEAR);
            month = LMP.get(Calendar.MONTH);
            day = LMP.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        return new DatePickerDialog(this, dateListener, year, month, day);
    }

    private void setDate(int year, int monthOfYear, int dayOfMonth) {
        TextView tvDate = (TextView) findViewById(R.id.date);
        String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        tvDate.setText(date);
        prefEditor.putString("Date", date);
        prefEditor.commit();
        Log.i("Pref", "Commit" + date);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.i(TAG, "widgetid " + mAppWidgetId + ", invalidid " + AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
            appWidgetManager.updateAppWidget(mAppWidgetId, views);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        } else
            Log.i(TAG, "intent null");
    }
}
