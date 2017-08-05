package com.training.android.undivided;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsSender extends IntentService {

    private SmsManager sms_manager;

    public SmsSender(String name) {
        super(name);
    }
    public SmsSender() {

        super("SmsSender");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String number  = intent.getStringExtra("number");
        String message = intent.getStringExtra("message");
        SmsManager manager = SmsManager.getDefault();
        manager.sendMultipartTextMessage(number, null, manager.divideMessage(message), null, null);
        addMessageToSentFolder(number, message);
        Intent broacast = new Intent("android.intent.action.AUTOREPLIED");
        sendBroadcast(broacast);
    }

    private void addMessageToSentFolder(String number, String message) {
        ContentValues values = new ContentValues();
        values.put("address", number);
        values.put("body", message);
        values.put("read", false);
        getContentResolver().insert(Uri.parse("content://sms/sent"), values);
        Log.d("SmsSender", "Saved to sent folder of: " + number);
    }

}
