package com.vs.myapps2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.vs.myapps2.emi.AsyncListenerStr;
import com.vs.myapps2.emi.EmiCalcAsyncStr;
import com.vs.myapps2.support.Data4Strings;
import com.vs.myapps2.support.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerEmiActivity extends AppCompatActivity implements AsyncListenerStr {
    public static String ARG_ITEM_POS = "ARG_ITEM_POS";
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mrecyclerAdapter;
    private EditText mETLoan;
    private EditText mETInterest;
    private EditText mETTenure;
    private TextView mTVDate;

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
    protected void onResume() {
        super.onResume();
        Log.i("OnResume", "Resume");
        setInputData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Intent intent = getIntent();
        int itemPos = intent.getIntExtra(ARG_ITEM_POS, 0);
        if (itemPos == 0)
            collapsingToolbarLayout.setTitle("EMI Calculator ");
        init();
    }

    private void init() {
        preferences = getPreferences(MODE_PRIVATE);
        prefEditor = preferences.edit();
        mETLoan = ((EditText) findViewById(R.id.etLoan));
        mETInterest = ((EditText) findViewById(R.id.etInterest));
        mETTenure = ((EditText) findViewById(R.id.etTenure));
        mTVDate = ((TextView) findViewById(R.id.tvDate));

        mETLoan.setOnKeyListener(new KeyListener());
        mETInterest.setOnKeyListener(new KeyListener());
        mETTenure.setOnKeyListener(new KeyListener());
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerGridView);
        mrecyclerAdapter = new RecyclerAdapter(new ArrayList<Data4Strings>());
        mRecyclerView.setAdapter(mrecyclerAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

        setInputData();
    }

    private void setInputData() {
        mETLoan.setText(String.valueOf(preferences.getInt(String.valueOf(R.id.etLoan), 2000000)));
        mETInterest.setText(String.valueOf(preferences.getFloat(String.valueOf(R.id.etInterest), (float) 9.1)));
        mETTenure.setText(String.valueOf(preferences.getInt(String.valueOf(R.id.etTenure), 20)));
        mTVDate.setText(preferences.getString(String.valueOf(R.id.tvDate), "1-1-2017"));
        startAsyncTask();
    }

    public void showDateDialog(View view) {
        createDatePickerDialog().show();
    }

    private Dialog createDatePickerDialog() {
        Calendar calendar;
        int year;

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        return new DatePickerDialog(this, dateListener, year, 0, 1);
    }

    private void setDate(int year, int monthOfYear, int dayOfMonth) {
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        tvDate.setText(date);
        prefEditor.putString(String.valueOf(R.id.tvDate), date);
        prefEditor.commit();
    }

    private void startAsyncTask() {
        new EmiCalcAsyncStr(this, Long.parseLong(mETLoan.getText().toString()),
                Float.parseFloat(mETInterest.getText().toString()),
                Long.parseLong(mETTenure.getText().toString()), true).execute();
    }

    @Override
    public void onPostExecute(ArrayList<Data4Strings> emiDetailsList) {
        mrecyclerAdapter.setData(emiDetailsList);
        mrecyclerAdapter.notifyDataSetChanged();
    }

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
//                e.printStackTrace();
                prefEditor.clear();
            }
            return false;
        }
    }
}

//        Snackbar.make(toolbar, "Replace with your own action" + itemPos, Snackbar.LENGTH_LONG)
// .setAction("Action", null).show();
//        Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();