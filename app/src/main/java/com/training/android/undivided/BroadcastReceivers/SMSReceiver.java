package com.training.android.undivided.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import  com.training.android.undivided.Database.DatabaseManager;
import  com.training.android.undivided.Objects.Rule;
import  com.training.android.undivided.Objects.SMS;

import java.util.HashMap;

/**
 * Created by Hillary Briones on 8/3/2017.
 */

public class SMSReceiver extends BroadcastReceiver {

    private static long delay = 2000; // 2 secs delay before responding
    private String logTag = "SMSReceiver";
    private SmsManager smsManager;
    private Context context;
    private DatabaseManager dbManager;

    @Override
    public void onReceive(final Context c, Intent intent) {
        context = c;

        String phoneNo = "";

        Bundle bundle = intent.getExtras();
        SmsMessage[] msg;

        if (bundle != null) {
            Log.i(logTag, "Non-null intent received");

            dbManager = new DatabaseManager(c);

            Object[] pdus = (Object[]) bundle.get("pdus");
            msg = new SmsMessage[pdus.length];
            for (int i=0; i<msg.length; i++) {
                msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                //get the phoneNo of the sender
                phoneNo = msg[i].getOriginatingAddress();

            }

            //REPLY
            phoneNo = phoneNo.replaceAll("[()\\-\\s]", "");//re-create phone no string, to make it final


        }



    }

    private void arrayToHash(HashMap hm, String[] array) {
        for (int i = 0; i < array.length; i++) {
            hm.put(array[i], 0);
        }
    }


    private void sendSMS(Rule r, String phoneNo) {
        // Reply
        String replyText = r.getText();
        smsManager.sendMultipartTextMessage(phoneNo, null, smsManager.divideMessage(replyText), null, null);

        // Add the reply to the Outbox DB
        dbManager.addSMS(new SMS(System.currentTimeMillis(), replyText, String.valueOf(phoneNo), r.getName()));

        //documentation & feedback
        Toast.makeText(context, "Replied to " + phoneNo + ": " + replyText.substring(0,80) + "...", Toast.LENGTH_SHORT).show();
        Log.i(logTag, "Sent out an SMS to " + String.valueOf(phoneNo));
    }


    private boolean inContacts(String no) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(no));
        //	    String name = "?";

        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri,
                new String[] {BaseColumns._ID }, //ContactsContract.PhoneLookup.DISPLAY_NAME }
                null, null, null);

        if (contactLookup != null)
        {
            try {
                if (contactLookup.getCount() > 0) {
                    Log.i(logTag, contactLookup.getCount() + " contact(s) found with the senders no");
                    return true;
                    //name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                    //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
                }
            } finally {
                contactLookup.close();
            }
        }
        return false;
    }
}
