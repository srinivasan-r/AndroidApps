package com.vs.autowhatsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class SendMessage extends AppCompatActivity {
    public final String EXTRA_CONTACT_NO = "com.vs.autowhatsapp.CONTACTNO";
    public final String EXTRA_CONTACT_NAME = "com.vs.autowhatsapp.CONTACTNAME";
    public final String EXTRA_MESSAGE = "com.vs.autowhatsapp.MESSAGE";
    private final String TAG = "SendMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoMsgService autoMsgService = AutoMsgService.getInstance();
        if (autoMsgService == null)
            Toast.makeText(this, "Service is not running", Toast.LENGTH_LONG).show();
        else {
            Intent intent = getIntent();
            autoMsgService.sActive = true;
            autoMsgService.sContact = intent.getStringExtra(EXTRA_CONTACT_NAME);
            autoMsgService.sMsg = intent.getStringExtra(EXTRA_MESSAGE);
            long contactNo = intent.getLongExtra(EXTRA_CONTACT_NO, 0);
            if (autoMsgService.sContact == null || autoMsgService.sMsg == null || contactNo == 0)
                Toast.makeText(this, "Invalid data - " + autoMsgService.sContact + ";" + autoMsgService.sMsg + ";" + contactNo, Toast.LENGTH_LONG).show();
            else {
                Uri uri = Uri.parse("smsto:" + contactNo + "@s.whatsapp.net");
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(i);
            }
        }
        finish();
    }
}