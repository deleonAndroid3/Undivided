package com.training.android.undivided.SmsGroup.Receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.training.android.undivided.SmsGroup.Activity.BaseActivity;
import com.training.android.undivided.SmsGroup.Model.MessageStatus;

/**
 * Created by Hillary Briones on 2/7/2018.
 */

public class DeliveryBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_SMS_DELIVERED = "SMS_DELIVERED";

    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals(ACTION_SMS_DELIVERED)) {

            String id = intent.getStringExtra("id");

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    sendBroadast(context, id, MessageStatus.SMS_DELIVERED);
                    break;
                case Activity.RESULT_CANCELED:
                    sendBroadast(context, id, MessageStatus.SMS_CANCELED);
                    break;
            }
        }
    }

    private void sendBroadast(Context context, String id, MessageStatus status) {
        Intent resourceIntent = new Intent(context.getApplicationContext().getPackageName() + "-" + BaseActivity.SMS_STATUS);
        resourceIntent.putExtra("status", status.toString());
        resourceIntent.putExtra("id", id);
        context.sendBroadcast(resourceIntent);
    }
}
