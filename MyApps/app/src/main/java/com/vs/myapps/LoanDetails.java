package com.vs.myapps;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 305022193 on 9/12/2016.
 */
public class LoanDetails implements Parcelable {
    public static final Creator<LoanDetails> CREATOR = new Creator<LoanDetails>() {
        @Override
        public LoanDetails createFromParcel(Parcel in) {
            return new LoanDetails(in);
        }

        @Override
        public LoanDetails[] newArray(int size) {
            return new LoanDetails[size];
        }
    };

    private int sNo;
    private double interest;
    private double principle;
    private double remaining;

    protected LoanDetails(Parcel in) {
        sNo = in.readInt();
        interest = in.readDouble();
        principle = in.readDouble();
        remaining = in.readDouble();
    }

    public LoanDetails(int sNo, double interest, double principle, double remaining) {
        this.sNo = sNo;
        this.interest = interest;
        this.principle = principle;
        this.remaining = remaining;
    }

    public int getsNo() {
        return sNo;
    }

    public double getInterest() {
        return interest;
    }

    public double getPrinciple() {
        return principle;
    }

    public double getRemaining() {
        return remaining;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sNo);
        dest.writeDouble(interest);
        dest.writeDouble(principle);
        dest.writeDouble(remaining);
    }
}
