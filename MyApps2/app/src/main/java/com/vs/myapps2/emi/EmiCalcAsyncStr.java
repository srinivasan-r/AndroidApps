package com.vs.myapps2.emi;

import android.os.AsyncTask;

import com.vs.myapps2.support.Data4Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by 305022193 on 1/16/2017.
 */

public class EmiCalcAsyncStr extends AsyncTask<Void, String, ArrayList<Data4Strings>> {

    AsyncListenerStr mAsyncListener;
    long mPrincipal;
    double mRate;
    long mTenure;
    Calendar cal;

    public EmiCalcAsyncStr(AsyncListenerStr asyncListener, long p, double r, long tenure, boolean tenureInYears) {
        this.mAsyncListener = asyncListener;
        this.mPrincipal = p;
        this.mRate = (r / 12.0) / 100.0;
        if (tenureInYears)
            this.mTenure = tenure * 12;
        else
            this.mTenure = tenure;
        String dt = "01-01-2017";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(dt));
        } catch (ParseException e) {
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected ArrayList<Data4Strings> doInBackground(Void... params) {
        ArrayList<Data4Strings> EMIDetailsList = new ArrayList<Data4Strings>();
        double temp = Math.pow((1.0 + mRate), mTenure);
        final double EMI = mPrincipal * mRate * (temp / (temp - 1));
        double principal;
        double interest;
        double remaining = mPrincipal;
        EMIDetailsList.add(new Data4Strings("Year", "Interest", "Principal", "Remaining"));
        for (int i = 0; i < mTenure; i++) {
            interest = remaining * mRate;
            principal = EMI - interest;
            remaining -= principal;
            EMIDetailsList.add(new Data4Strings(cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1), String.format("%.2f", interest),
                    String.format("%.2f", principal), String.format("%.2f", mPrincipal - remaining)));
            cal.add(Calendar.MONTH, 1);
        }
        return EMIDetailsList;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Data4Strings> emiDetailsList) {
        mAsyncListener.onPostExecute(emiDetailsList);
//        super.onPostExecute(EMIDetailsList);
    }

}
