/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.vs.myapps2.support;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vs.myapps2.R;

import java.util.ArrayList;


/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    ArrayList<Data4Strings> mData;

    public RecyclerAdapter(ArrayList<Data4Strings> dataSet) {
        mData = dataSet;
    }

    public void setData(ArrayList<Data4Strings> mData) {
        this.mData = mData;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.emi_details_row, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        View view = viewHolder.getView();//.setText(mData.get(position));
        ((TextView) view.findViewById(R.id.tvYear)).setText(mData.get(position).a);
        ((TextView) view.findViewById(R.id.tvPrincipal)).setText(mData.get(position).b);
        ((TextView) view.findViewById(R.id.tvInterest)).setText(mData.get(position).c);
        Log.d(TAG, "Element " + position + " set." + ((TextView) view.findViewById(R.id.tvInterest)).getLayoutParams().width);
        ((TextView) view.findViewById(R.id.tvTotal)).setText(mData.get(position).d);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
//        Log.d(TAG, "Data size " + mData.size());
        return mData.size();
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            mView = v.findViewById(R.id.emiDetailsRow);
        }

        public View getView() {
            return mView;
        }
    }

}
