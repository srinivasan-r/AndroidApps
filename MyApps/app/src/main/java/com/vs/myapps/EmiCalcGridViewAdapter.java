package com.vs.myapps;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by 305022193 on 9/12/2016.
 */
public class EmiCalcGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private int[] icons = {
            android.R.drawable.btn_star_big_off,
            android.R.drawable.btn_star_big_on,
            android.R.drawable.alert_light_frame,
            android.R.drawable.alert_dark_frame,
            android.R.drawable.arrow_down_float,
            android.R.drawable.gallery_thumb,
            android.R.drawable.ic_dialog_map,
            android.R.drawable.ic_popup_disk_full,
            android.R.drawable.star_big_on,
            android.R.drawable.star_big_off,
            android.R.drawable.star_big_on
    };

    public EmiCalcGridViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 200;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Adapter", "position " + position);
        TextView textView;
        if (convertView == null) {
            textView = new TextView(mContext);
            textView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Medium);
            }
        } else {
            textView = (TextView) convertView;
        }
        textView.setText("cell " + position);
        return textView;
/*        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(10, 10, 10, 10);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(icons[position]);
        return imageView;*/
    }
}
