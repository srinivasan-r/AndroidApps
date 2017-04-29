package com.vs.myapps2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vs.myapps2.emi.AsyncListener;
import com.vs.myapps2.emi.EmiCalcAsync;
import com.vs.myapps2.support.EMIData;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class EmiTableActivity extends AppCompatActivity implements AsyncListener {
    private final String TAG = "EmiTableActivity";
    private final Locale locale = new Locale("hi", "IN");
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    private EditText mETLoan;
    private EditText mETInterest;
    private EditText mETTenure;
    private ToggleButton mTBTenure;
    private TextView mTVDate;
    private TextView mTVEmi;
    private TextView mTVTotalInterest;
    private TableLayout mEmiDetailsTable;
    private HashMap<Integer, ArrayList<EMIData>> mEmiDetailsMap;


    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;
    private DatePickerDialog.OnDateSetListener dateListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    setDate(year, monthOfYear, dayOfMonth);
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("OnResume", "Resume");
        setInputData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyFormatter.setMaximumFractionDigits(0);

        setContentView(R.layout.activity_emi_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public void onPostExecute(HashMap<Integer, ArrayList<EMIData>> emiDetailsMap) {
        mEmiDetailsMap = emiDetailsMap;
        mEmiDetailsTable.removeAllViews();
        mEmiDetailsTable.addView(getLayoutInflater().inflate(R.layout.emi_details_tablerow, null));
        Set<Integer> keySet = new TreeSet<>(emiDetailsMap.keySet());
        {
            EMIData emiData = emiDetailsMap.get(keySet.iterator().next()).get(0);
            mTVEmi.setText(currencyFormatter.format(emiData.interest + emiData.principal));
        }
        double totalInterest = 0;
        for (Integer key : keySet) {
            ArrayList<EMIData> emiDataArrayList = emiDetailsMap.get(key);
            EMIData data = new EMIData();
            for (EMIData emiData : emiDataArrayList) {
                totalInterest += emiData.interest;
                data = data.add(emiData);
            }
            View tableRow = getLayoutInflater().inflate(R.layout.emi_details_tablerow, null);
            ((TextView) tableRow.findViewById(R.id.tvYear)).setText(String.valueOf(key));
            ((TextView) tableRow.findViewById(R.id.tvPrincipal)).setText(data.getPrincipal());
            ((TextView) tableRow.findViewById(R.id.tvInterest)).setText(data.getInterest());
            ((TextView) tableRow.findViewById(R.id.tvTotal)).setText(data.getRemaining());
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View rowView) {
                    if (rowView.getTag() == null || rowView.getTag() == TableRowConstants.COLLAPSED) {
                        rowView.setTag(TableRowConstants.EXPANDED);
                        int pos = 0;
                        for (; pos < mEmiDetailsTable.getChildCount(); pos++) {
                            View v = mEmiDetailsTable.getChildAt(pos);
                            if (v == rowView) {
                                break;
                            }
                        }
                        ArrayList<EMIData> emiDataArrayList =
                                mEmiDetailsMap.get(Integer.parseInt((String) ((TextView) rowView.findViewById(R.id.tvYear)).getText()));
                        for (EMIData data : emiDataArrayList) {
                            View tableRow = getLayoutInflater().inflate(R.layout.emi_details_tablerow, null);
                            tableRow.setTag(TableRowConstants.CHILD);
                            ((TextView) tableRow.findViewById(R.id.tvYear)).setText(data.getMonth());
                            ((TextView) tableRow.findViewById(R.id.tvPrincipal)).setText(data.getPrincipal());
                            ((TextView) tableRow.findViewById(R.id.tvInterest)).setText(data.getInterest());
                            ((TextView) tableRow.findViewById(R.id.tvTotal)).setText(data.getRemaining());
                            mEmiDetailsTable.addView(tableRow, ++pos);
                        }
                    } else if (rowView.getTag() == TableRowConstants.EXPANDED) {
                        rowView.setTag(TableRowConstants.COLLAPSED);
                        boolean reachedClickPos = false;
                        for (int pos = 0; pos < mEmiDetailsTable.getChildCount(); pos++) {
                            View v = mEmiDetailsTable.getChildAt(pos);
                            if (v == rowView) {
                                reachedClickPos = true;
                                continue;
                            }
                            if (reachedClickPos) {
                                if (v.getTag() != null && v.getTag() == TableRowConstants.CHILD)
                                    v.setVisibility(View.GONE);
                                else
                                    break;
                            }
                        }
                    }
                }
            });
            mEmiDetailsTable.addView(tableRow);
        }
        mTVTotalInterest.setText(currencyFormatter.format(totalInterest));
    }

    private void init() {
        preferences = getPreferences(MODE_PRIVATE);
        prefEditor = preferences.edit();
        mETLoan = ((EditText) findViewById(R.id.etLoan));
        mETInterest = ((EditText) findViewById(R.id.etInterest));
        mETTenure = ((EditText) findViewById(R.id.etTenure));
        mTBTenure = (ToggleButton) findViewById(R.id.tbTenure);
        mTVDate = ((TextView) findViewById(R.id.tvDate));
        mTVEmi = ((TextView) findViewById(R.id.tvEmi));
        mTVTotalInterest = ((TextView) findViewById(R.id.tvTotalInterest));
        mEmiDetailsTable = (TableLayout) findViewById(R.id.emiDetailsTable);

        mETLoan.setOnKeyListener(new EmiTableActivity.KeyListener());
        mETInterest.setOnKeyListener(new EmiTableActivity.KeyListener());
        mETTenure.setOnKeyListener(new EmiTableActivity.KeyListener());
        mTBTenure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleButton tb = (ToggleButton) v;
                prefEditor.putBoolean(String.valueOf(v.getId()), tb.isChecked());
                prefEditor.commit();
                startAsyncTask();
            }
        });
        findViewById(R.id.emiDetailsLayout).setNestedScrollingEnabled(false);

        setInputData();
    }

    private void setInputData() {
        mETLoan.setText(String.valueOf(preferences.getInt(String.valueOf(R.id.etLoan), 2000000)));
        mETInterest.setText(String.valueOf(preferences.getFloat(String.valueOf(R.id.etInterest), (float) 9.1)));
        mETTenure.setText(String.valueOf(preferences.getInt(String.valueOf(R.id.etTenure), 20)));
        mTBTenure.setChecked(preferences.getBoolean(String.valueOf(R.id.tbTenure), false));
        mTVDate.setText(preferences.getString(String.valueOf(R.id.tvDate), "1-1-2017"));
        startAsyncTask();
    }

    public void showDateDialog(View view) {
        createDatePickerDialog().show();
    }

    private Dialog createDatePickerDialog() {
        Calendar calendar;
        int year, month, day;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(preferences.getString(String.valueOf(R.id.tvDate), "1-1-2017")));
        } catch (ParseException e) {
        }
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(this, dateListener, year, month, day);
    }

    private void setDate(int year, int monthOfYear, int dayOfMonth) {
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        tvDate.setText(date);
        prefEditor.putString(String.valueOf(R.id.tvDate), date);
        prefEditor.commit();
        startAsyncTask();
    }

    private void startAsyncTask() {
        new EmiCalcAsync(this, Long.parseLong(mETLoan.getText().toString()),
                Float.parseFloat(mETInterest.getText().toString()),
                Long.parseLong(mETTenure.getText().toString()), !mTBTenure.isChecked(), mTVDate.getText().toString()).execute();
    }

    enum TableRowConstants {EXPANDED, COLLAPSED, CHILD}

    class KeyListener implements View.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.i("Keycode", String.valueOf(keyCode));
            if (keyCode < 7 || keyCode > 16 && keyCode != 67)
                return false;
            try {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("Keycode", String.valueOf(keyCode));
                    EditText textView = (EditText) v;
                    CharSequence text = textView.getText();
                    switch (v.getId()) {
                        case R.id.etLoan:
                        case R.id.etTenure: {
                            prefEditor.putInt(String.valueOf(v.getId()),
                                    Integer.parseInt(String.valueOf(text)));
                            break;
                        }
                        case R.id.etInterest: {
                            prefEditor.putFloat(String.valueOf(v.getId()),
                                    Float.parseFloat(String.valueOf(text)));
                            break;
                        }
                    }
                    prefEditor.commit();
                    startAsyncTask();
                    Log.i("pref", "commit");
                }
            } catch (Exception e) {
            }
            return false;
        }
    }
}
