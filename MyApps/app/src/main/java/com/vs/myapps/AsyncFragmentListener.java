package com.vs.myapps;

import java.util.ArrayList;

/**
 * Created by 305022193 on 9/12/2016.
 */
public interface AsyncFragmentListener {
    public void onProgressUpdate(LoanDetails loanDetails);

    public void onComplete(ArrayList<LoanDetails> loanDetailsList);
}
