package com.vs.autowhatsapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getApplication(), AutoMsgService.class));
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    public void copyToClipboard(View view) {
        String copyText = "";
        switch (view.getId()) {
            case R.id.editText1:
            case R.id.editText2:
            case R.id.editText3:
                copyText = ((TextView) view).getText().toString();
                break;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", copyText);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, copyText + " copied to clipboard", Toast.LENGTH_LONG).show();
    }
}
