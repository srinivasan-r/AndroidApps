package com.vs.myapps.TableLayout;

import android.os.AsyncTask;
import android.util.Log;

import com.vs.myapps.AppDetailFragment;
import com.vs.myapps.AsyncFragmentListener;
import com.vs.myapps.LoanDetails;

import java.util.ArrayList;

/**
 * Created by 305022193 on 9/12/2016.
 */
public class EmiCalc extends AsyncTask<Void, LoanDetails, ArrayList<LoanDetails>> {

    AsyncFragmentListener mAsyncFragmentListener;

    public EmiCalc(AsyncFragmentListener AsyncFragmentListener) {
        mAsyncFragmentListener = AsyncFragmentListener;
    }

    @Override
    protected ArrayList<LoanDetails> doInBackground(Void... params) {
        ArrayList<LoanDetails> loanDetailsList = new ArrayList<LoanDetails>();
        Log.d("Fragment", "Started preparing list");
        long p = 2200000;
        double r = (10.0 / 12.0) / 100.0;
        Log.d(AppDetailFragment.class.getName(), "r " + r);
        long years = 20;
        long n = years * 12;
        double temp = Math.pow((1.0 + r), n);
        final double EMI = p * r * (temp / (temp - 1));
        double principle;
        double interest;
        double remaining = p;

        for (int i = 0; i < n; i++) {
            interest = remaining * r;
            principle = EMI - interest;
            remaining -= principle;
            loanDetailsList.add(new LoanDetails(i, interest, principle, remaining));
//                publishProgress(loanParams);
        }
        return loanDetailsList;
    }

    @Override
    protected void onProgressUpdate(LoanDetails... values) {
        for (LoanDetails value : values) {
            mAsyncFragmentListener.onProgressUpdate(value);
        }
    }

    @Override
    protected void onPostExecute(ArrayList<LoanDetails> s) {
        Log.d("fragment", "completed list");
        mAsyncFragmentListener.onComplete(s);
    }
}
