package com.vs.myapps2.support;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by 305022193 on 1/18/2017.
 */

public class EMIData {
    public int month;
    public double interest;
    public double principal;
    public double remaining;
    private Locale locale = new Locale("hi", "IN");
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    private String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public EMIData() {
    }

    public EMIData(int month, double interest, double principal, double remaining) {
        this.month = month;
        this.interest = interest;
        this.principal = principal;
        this.remaining = remaining;
        currencyFormatter.setMaximumFractionDigits(0);
    }

    public EMIData add(EMIData emiData) {
        double remaining;
        if (this.remaining == 0)
            remaining = emiData.remaining;
        else if (this.remaining < emiData.remaining)
            remaining = this.remaining;
        else
            remaining = emiData.remaining;
        return new EMIData(0, this.interest + emiData.interest, this.principal + emiData.principal,
                remaining
        );
    }

    public String getMonth() {
        return monthName[month];
    }

    public String getInterest() {
        return currencyFormatter.format(interest);
    }

    public String getPrincipal() {
        return currencyFormatter.format(principal);
    }

    public String getRemaining() {
        return currencyFormatter.format(remaining);
    }
}
