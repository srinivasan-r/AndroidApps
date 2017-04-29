package com.vs.myapps;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.vs.myapps.TableLayout.EmiCalc;

import java.util.ArrayList;

/**
 * A fragment representing a single App detail screen.
 * This fragment is either contained in a {@link AppListActivity}
 * in two-pane mode (on tablets) or a {@link AppDetailActivity}
 * on handsets.
 */
public class AppDetailFragment extends Fragment implements AsyncFragmentListener {
    public static final String ARG_ITEM_ID = "item_id";

    private String mItem;
    private TextView mEmiTextView;
    private ArrayList<LoanDetails> mLoanDetailsList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AppDetailFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList("LoanParamsList", mLoanDetailsList);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
/*        if (null != savedInstanceState) {
            mLoanDetailsList = savedInstanceState.getParcelableArrayList("LoanParamsList");
            updateView();
        }*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = getResources().getStringArray(R.array.AppList)[getArguments().getInt(ARG_ITEM_ID)];
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid_view, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(new EmiCalcGridViewAdapter(this.getActivity()));
        long p = 2200000;
        double r = (10.0 / 12.0) / 100.0;
        Log.d(AppDetailFragment.class.getName(), "r " + r);
        long years = 20;
        long n = years * 12;
        double temp = Math.pow((1.0 + r), n);
        Log.d(this.getClass().getName(), String.valueOf(temp));
        final double EMI = p * r * (temp / (temp - 1));
        Log.d(this.getClass().getName(), String.valueOf(EMI));

//        mEmiTextView = (TextView) rootView.findViewById(R.id.emiText);
//        mEmiTextView.setText("EMI = " + String.format("%.2f", EMI));

        double principle;
        double interest;
        double remaining = p;

/*        {
            TableRow tableRow = (TableRow) inflater.inflate(R.layout.table_row, null);
            TextView yearCol = (TextView) tableRow.findViewById(R.id.yearCol);
            yearCol.setText("S.No");
            TextView interestCol = (TextView) tableRow.findViewById(R.id.interestCol);
            interestCol.setText("Interest");
            TextView principalCol = (TextView) tableRow.findViewById(R.id.principalCol);
            principalCol.setText("Principle");
            TextView totalCol = (TextView) tableRow.findViewById(R.id.totalCol);
            totalCol.setText("Remaining");
        }
        if (null == savedInstanceState) {
            EmiCalc emiCalc = new EmiCalc(this);
            emiCalc.execute();
        }*/
        return rootView;
    }

    @Override
    public void onProgressUpdate(LoanDetails loanDetails) {
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.mainTable);
        TableRow tableRow = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.table_row, null);
        TextView yearCol = (TextView) tableRow.findViewById(R.id.yearCol);
        yearCol.setText(String.valueOf(loanDetails.getsNo()));
        TextView interestCol = (TextView) tableRow.findViewById(R.id.interestCol);
        interestCol.setText(String.format("%.2f", loanDetails.getInterest()));
        TextView principalCol = (TextView) tableRow.findViewById(R.id.principalCol);
        principalCol.setText(String.format("%.2f", loanDetails.getPrinciple()));
        TextView totalCol = (TextView) tableRow.findViewById(R.id.totalCol);
        totalCol.setText(String.format("%.2f", loanDetails.getRemaining()));

        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView totalCol = (TextView) v.findViewById(R.id.totalCol);
                String remaining = (String) totalCol.getText();
                Toast.makeText(getContext(), remaining, Toast.LENGTH_SHORT).show();
            }
        });
        tableLayout.addView(tableRow);
    }

    private void updateView() {
        View view = getView();
        if (null == view) {
            Log.e("Fragment", "view null");
            return;
        }
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.mainTable);
        for (LoanDetails loanDetails : mLoanDetailsList) {
            TableRow tableRow = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.table_row, null);
            TextView yearCol = (TextView) tableRow.findViewById(R.id.yearCol);
            yearCol.setText(String.valueOf(loanDetails.getsNo()));
            TextView interestCol = (TextView) tableRow.findViewById(R.id.interestCol);
            interestCol.setText(String.format("%.2f", loanDetails.getInterest()));
            TextView principalCol = (TextView) tableRow.findViewById(R.id.principalCol);
            principalCol.setText(String.format("%.2f", loanDetails.getPrinciple()));
            TextView totalCol = (TextView) tableRow.findViewById(R.id.totalCol);
            totalCol.setText(String.format("%.2f", loanDetails.getRemaining()));

            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView totalCol = (TextView) v.findViewById(R.id.totalCol);
                    String remaining = (String) totalCol.getText();
                    Toast.makeText(getContext(), remaining, Toast.LENGTH_SHORT).show();
                }
            });
            tableLayout.addView(tableRow);
        }
        Log.d("Fragment", "Completed view update");
    }

    @Override
    public void onComplete(ArrayList<LoanDetails> loanDetailsList) {
        mLoanDetailsList = loanDetailsList;
        updateView();
    }

}

