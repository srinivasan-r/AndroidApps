package com.vs.tablelayoutsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.mainTable);
        TableRow headerRow = (TableRow) tableLayout.findViewById(R.id.headerRow);
        TextView textView = (TextView) headerRow.findViewById(R.id.textViewCol2);
        textView.setText("Hello");

        for (int i = 0; i < 20; i++) {
            TableRow tableRow = (TableRow) this.getLayoutInflater().inflate(R.layout.table_row, null);
            TextView textView1 = (TextView) tableRow.findViewById(R.id.textViewCol1);
            textView1.setText(String.valueOf(i));
            tableLayout.addView(tableRow);
        }
    }
}
