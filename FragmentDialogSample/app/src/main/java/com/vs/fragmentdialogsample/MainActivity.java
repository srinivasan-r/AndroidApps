package com.vs.fragmentdialogsample;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        String[] listValues = getResources().getStringArray(R.array.AppList);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listValues));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(MainActivity.this, (String) getListAdapter().getItem(position), Toast.LENGTH_SHORT).show();
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.show(getFragmentManager(),"InfoFragment");
    }
}
