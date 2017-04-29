package com.vs.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

/**
 * Created by 305022193 on 7/26/2016.
 */
public class MyAdapter extends ArrayAdapter<String> {
    private LayoutInflater layoutInflater;
    private String items;

    public MyAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }
}
