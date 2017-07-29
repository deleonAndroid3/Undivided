package com.training.android.undivided;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();

    public SmsReceiver() {
        // TODO Auto-generated constructor stub
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdus_obj = (Object[]) bundle.get("pdus");

                for(int i = 0; i < pdus_obj.length; ++i) {
                    SmsMessage current_message = SmsMessage.createFromPdu((byte[]) pdus_obj[i]);
                    String phone_number = current_message.getDisplayOriginatingAddress();
                    String message = current_message.getDisplayMessageBody();
                    AutoReply.getInstance().replyTo(context, phone_number);
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception: " + e);
        }
    }
    }

