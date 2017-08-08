package com.training.android.undivided.BroadcastReceivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.training.android.undivided.BaseActivity;

/**
 * Created by Hillary Briones on 8/8/2017.
 */

public class SendBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_SMS_SENT = "SMS_SENT";

    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals(ACTION_SMS_SENT)) {

            String id = intent.getStringExtra("id");

            switch (getResultCode()) {

                case Activity.RESULT_OK:
                    sendBroadcast(context, id, MessageStatus.SMS_SENT);
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    sendBroadcast(context, id, MessageStatus.SMS_FAILURE);
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    sendBroadcast(context, id, MessageStatus.SMS_ERROR_SERVICE);
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    sendBroadcast(context, id, MessageStatus.SMS_ERROR_NULL_PDU);
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    sendBroadcast(context, id, MessageStatus.SMS_ERROR_RADIO_OFF);
                    break;
            }
        }
    }

    private void sendBroadcast(Context context, String id, MessageStatus status) {
        Intent resourceIntent = new Intent(context.getApplicationContext().getPackageName() + "-" + BaseActivity.SMS_STATUS);
        resourceIntent.putExtra("status", status.toString());
        resourceIntent.putExtra("id", id);
        context.sendBroadcast(resourceIntent);
    }
}
