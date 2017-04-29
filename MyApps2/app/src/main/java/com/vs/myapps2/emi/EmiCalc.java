package com.vs.myapps2.emi;

import android.util.Log;

import com.vs.myapps2.support.Data4Strings;

import java.util.ArrayList;

/**
 * Created by 305022193 on 1/16/2017.
 */
public class EmiCalc {

    public static ArrayList<Data4Strings> calculate(long principal, double rate, long tenure) {
        ArrayList<Data4Strings> EMIDetailsList = new ArrayList<Data4Strings>();
        Log.d("Fragment", "Started preparing list");
        double temp = Math.pow((1.0 + rate), tenure);
        final double EMI = principal * rate * (temp / (temp - 1));
        double principle;
        double interest;
        double remaining = principal;

        for (int i = 0; i < tenure; i++) {
            interest = remaining * rate;
            principle = EMI - interest;
            remaining -= principle;
            EMIDetailsList.add(new Data4Strings(String.valueOf(i), String.format("%.2f", interest), String.format("%.2f", principle), String.format("%.2f", remaining)));
        }
        return EMIDetailsList;
    }

    public static ArrayList<String> getDataSet1() {
        ArrayList<String> EMIDetailsList = new ArrayList<String>();
        Log.d("Fragment", "Started preparing list");
        long p = 2200000;
        double r = (10.0 / 12.0) / 100.0;
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
            EMIDetailsList.add(String.valueOf(i));
            EMIDetailsList.add(String.format("%.2f", interest));
            EMIDetailsList.add(String.format("%.2f", principle));
            EMIDetailsList.add(String.format("%.2f", remaining));
        }
        return EMIDetailsList;
    }
}
