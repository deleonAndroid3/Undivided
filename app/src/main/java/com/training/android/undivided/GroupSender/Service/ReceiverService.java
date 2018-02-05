package com.training.android.undivided.GroupSender.Service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class ReceiverService extends BroadcastReceiver {

    public static final String SMS_SEND_ACTION = "com.training.android.undivided.GroupSender.Service.sms_send_action";
    public static final String SMS_DELIVERED_ACTION = "com.training.android.undivided.GroupSender.Service.sms_delivered_action";



    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_SEND_ACTION)){
            switch (getResultCode()){
                case Activity.RESULT_OK:
                    Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:

                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:

                    break;
            }
        } else if (intent.getAction().equals(SMS_DELIVERED_ACTION)){
            switch (getResultCode()){
                case Activity.RESULT_OK:
                    Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:

                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:

                    break;
            }
        }
    }
}
