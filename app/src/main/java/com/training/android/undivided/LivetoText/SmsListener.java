package com.training.android.undivided.LivetoText;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.Group.Model.GroupModel;

/**
 * Created by Hillary Briones on 2/13/2018.
 */

public class SmsListener extends Service {

    IntentFilter intentFilter2 = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    private BroadcastReceiver SMSReceiver;
    private DBHandler dbHandler;
    private GroupModel gmodel;
    private String phoneNumber;

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    public void onCreate() {
        super.onCreate();

        dbHandler = new DBHandler(getApplicationContext());

        SMSReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                MyApp.smsflag = 1;
                Bundle myBundle = intent.getExtras();
                SmsMessage messages = null;
                String strMessage = "";

                if (myBundle != null) {
                    Object[] pdus = (Object[]) myBundle.get("pdus");

                    for (int i = 0; i < pdus.length; i++) {
                        messages = SmsMessage.createFromPdu((byte[]) pdus[i]);

                        phoneNumber = messages.getOriginatingAddress();
                        phoneNumber = "0" + phoneNumber.substring(3);
                        phoneNumber = phoneNumber.replace(" ", "");
                        gmodel = dbHandler.getGroup(phoneNumber);

                    }

                    if (gmodel.getRule3() == 1) {
                        String ew = "";
                        for (int h = 3; h < messages.getOriginatingAddress().length(); h++) {
                            ew = ew + messages.getOriginatingAddress().charAt(h) + " ";
                        }

                        String send = getContactName(getApplicationContext(), messages.getOriginatingAddress());
                        if (send == null) {
                            strMessage += "SMS From: " + ew;
                        } else {
                            strMessage += "SMS From: " + send;
                        }
                        strMessage += "It says";
                        strMessage += " : ";
                        strMessage += messages.getMessageBody();
                        strMessage += "\n";
                        strMessage += " Do you wish to reply?";
                        Intent in = new Intent("android.intent.action.MAIN2");
                        in.putExtra("sms_event", strMessage);
                        in.putExtra("com.training.android.undivided.LivetoText.number", messages.getOriginatingAddress());
                        sendBroadcast(in);
                    }
                }
            }
        };
        this.registerReceiver(this.SMSReceiver, intentFilter2);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
