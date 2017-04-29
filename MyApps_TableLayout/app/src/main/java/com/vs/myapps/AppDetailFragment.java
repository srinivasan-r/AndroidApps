package com.vs.myapps;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

interface AsyncListener {
    public void onProgressUpdate(LoanParams loanParams);

    public void onComplete(ArrayList<LoanParams> loanParamsList);
}

/**
 * A fragment representing a single App detail screen.
 * This fragment is either contained in a {@link AppListActivity}
 * in two-pane mode (on tablets) or a {@link AppDetailActivity}
 * on handsets.
 */
public class AppDetailFragment extends Fragment implements AsyncListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
//    private DummyContent.DummyItem mItem;
    private String mItem;
    private TextView mEmiTextView;
    private ArrayList<LoanParams> mLoanParamsList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AppDetailFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("LoanParamsList", mLoanParamsList);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (null != savedInstanceState) {
            mLoanParamsList = savedInstanceState.getParcelableArrayList("LoanParamsList");
            updateView();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        savedInstanceState.put
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

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
        View rootView = inflater.inflate(R.layout.table_layout, container, false);
        TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.mainTable);
        long p = 2200000;
        double r = (10.0 / 12.0) / 100.0;
        Log.d(AppDetailFragment.class.getName(), "r " + r);
        long years = 20;
        long n = years * 12;
        double temp = Math.pow((1.0 + r), n);
        Log.d(this.getClass().getName(), String.valueOf(temp));
        final double EMI = p * r * (temp / (temp - 1));
        Log.d(this.getClass().getName(), String.valueOf(EMI));
        mEmiTextView = (TextView) rootView.findViewById(R.id.emiText);
        mEmiTextView.setText("EMI = " + String.format("%.2f", EMI));
        double principle;
        double interest;
        double remaining = p;

        {
            TableRow tableRow = (TableRow) inflater.inflate(R.layout.table_row, null);
            TextView yearCol = (TextView) tableRow.findViewById(R.id.yearCol);
            yearCol.setText("S.No");
            TextView interestCol = (TextView) tableRow.findViewById(R.id.interestCol);
            interestCol.setText("Interest");
            TextView principalCol = (TextView) tableRow.findViewById(R.id.principalCol);
            principalCol.setText("Principle");
            TextView totalCol = (TextView) tableRow.findViewById(R.id.totalCol);
            totalCol.setText("Remaining");
            tableLayout.addView(tableRow);
        }
        if (null == savedInstanceState) {
            EmiCalc emiCalc = new EmiCalc(this);
            emiCalc.execute();
        }
        return rootView;
    }

    @Override
    public void onProgressUpdate(LoanParams loanParams) {
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.mainTable);
        TableRow tableRow = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.table_row, null);
        TextView yearCol = (TextView) tableRow.findViewById(R.id.yearCol);
        yearCol.setText(String.valueOf(loanParams.sNo));
        TextView interestCol = (TextView) tableRow.findViewById(R.id.interestCol);
        interestCol.setText(String.format("%.2f", loanParams.interest));
        TextView principalCol = (TextView) tableRow.findViewById(R.id.principalCol);
        principalCol.setText(String.format("%.2f", loanParams.principle));
        TextView totalCol = (TextView) tableRow.findViewById(R.id.totalCol);
        totalCol.setText(String.format("%.2f", loanParams.remaining));

        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView totalCol = (TextView) v.findViewById(R.id.totalCol);
                String remaining = (String) totalCol.getText();
                Toast.makeText(getContext(), remaining, Toast.LENGTH_SHORT).show();
            }
        });
        tableLayout.addView(tableRow);
//        mEmiTextView.setText(String.valueOf(loanParams.sNo));
//        tableLayout.addView(tableRow);
    }

    private void updateView() {
        View view = getView();
        if (null == view) {
            Log.e("Fragment", "view null");
            return;
        }
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.mainTable);
        for (LoanParams loanParams : mLoanParamsList) {
            TableRow tableRow = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.table_row, null);
            TextView yearCol = (TextView) tableRow.findViewById(R.id.yearCol);
            yearCol.setText(String.valueOf(loanParams.sNo));
            TextView interestCol = (TextView) tableRow.findViewById(R.id.interestCol);
            interestCol.setText(String.format("%.2f", loanParams.interest));
            TextView principalCol = (TextView) tableRow.findViewById(R.id.principalCol);
            principalCol.setText(String.format("%.2f", loanParams.principle));
            TextView totalCol = (TextView) tableRow.findViewById(R.id.totalCol);
            totalCol.setText(String.format("%.2f", loanParams.remaining));

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
    public void onComplete(ArrayList<LoanParams> loanParamsList) {
        mLoanParamsList = loanParamsList;
        updateView();
    }

    public class EmiCalc extends AsyncTask<Void, LoanParams, ArrayList<LoanParams>> {

        AsyncListener mAsyncListener;

        EmiCalc(AsyncListener asyncListener) {
            mAsyncListener = asyncListener;
        }

        @Override
        protected ArrayList<LoanParams> doInBackground(Void... params) {
            ArrayList<LoanParams> loanParamsList = new ArrayList<LoanParams>();
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
                loanParamsList.add(new LoanParams(i, interest, principle, remaining));
//                publishProgress(loanParams);
            }
            return loanParamsList;
        }

        @Override
        protected void onProgressUpdate(LoanParams... values) {
            for (LoanParams value : values) {
                mAsyncListener.onProgressUpdate(value);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<LoanParams> s) {
            Log.d("fragment", "completed list");
            mAsyncListener.onComplete(s);
        }
    }

}

class LoanParams implements Parcelable {
    public static final Creator<LoanParams> CREATOR = new Creator<LoanParams>() {
        @Override
        public LoanParams createFromParcel(Parcel in) {
            return new LoanParams(in);
        }

        @Override
        public LoanParams[] newArray(int size) {
            return new LoanParams[size];
        }
    };
    public int sNo;
    public double interest;
    public double principle;
    public double remaining;

    protected LoanParams(Parcel in) {
        sNo = in.readInt();
        interest = in.readDouble();
        principle = in.readDouble();
        remaining = in.readDouble();
    }

    public LoanParams(int sNo, double interest, double principle, double remaining) {
        this.sNo = sNo;
        this.interest = interest;
        this.principle = principle;
        this.remaining = remaining;
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
