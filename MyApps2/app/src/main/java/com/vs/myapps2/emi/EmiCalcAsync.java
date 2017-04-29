package com.vs.myapps2.emi;

import android.os.AsyncTask;

import com.vs.myapps2.support.EMIData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by 305022193 on 1/16/2017.
 */

public class EmiCalcAsync extends AsyncTask<Void, String, HashMap<Integer, ArrayList<EMIData>>> {

    AsyncListener mAsyncListener;
    long mPrincipal;
    double mRate;
    long mTenure;
    Calendar mCal;

    public EmiCalcAsync(AsyncListener asyncListener, long p, double r, long tenure, boolean tenureInYears, String date) {
        this.mAsyncListener = asyncListener;
        this.mPrincipal = p;
        this.mRate = (r / 12.0) / 100.0;
        if (tenureInYears)
            this.mTenure = tenure * 12;
        else
            this.mTenure = tenure;
        String defDate = "01-01-2017";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        mCal = Calendar.getInstance();
        try {
            mCal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            try {
                mCal.setTime(sdf.parse(defDate));
            } catch (ParseException e1) {
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected HashMap<Integer, ArrayList<EMIData>> doInBackground(Void... params) {
        HashMap<Integer, ArrayList<EMIData>> integerEMIDataMap = new HashMap();
        double temp = Math.pow((1.0 + mRate), mTenure);
        final double EMI = mPrincipal * mRate * (temp / (temp - 1));
        double principal;
        double interest;
        double remaining = mPrincipal;
        for (int i = 0; i < mTenure; i++) {
            interest = remaining * mRate;
            principal = EMI - interest;
            remaining -= principal;
            if (i == mTenure - 1)
                remaining = 0;
            int year = mCal.get(Calendar.YEAR);
            if (!integerEMIDataMap.containsKey(year))
                integerEMIDataMap.put(year, new ArrayList<EMIData>());
            integerEMIDataMap.get(year).add(new EMIData(mCal.get(Calendar.MONTH), interest, principal, remaining));
            mCal.add(Calendar.MONTH, 1);
        }
        return integerEMIDataMap;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(HashMap<Integer, ArrayList<EMIData>> emiDetailsMap) {
        mAsyncListener.onPostExecute(emiDetailsMap);
//        super.onPostExecute(EMIDetailsList);
    }

}
