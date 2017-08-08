package com.training.android.undivided.Utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.training.android.undivided.BaseActivity;

import java.util.ArrayList;

/**
 * Created by Hillary Briones on 8/8/2017.
 */

public class SmsUtils {
    private static String ACTION_SMS_SENT = "SMS_SENT";
    private static String ACTION_SMS_DELIVERED = "SMS_DELIVERED";

    /**
     * Send SMS.
     *
     * @param context
     * @param phoneNumber
     * @param message
     */
    public static void sendSms(String id, Context context, String phoneNumber, String message) {

        Intent sendIntent = new Intent(ACTION_SMS_SENT);
        sendIntent.putExtra("id", id);

        Intent deliveredIntent = new Intent(ACTION_SMS_DELIVERED);
        deliveredIntent.putExtra("id", id);

        PendingIntent piSent = PendingIntent.getBroadcast(context, BaseActivity.REQUEST_CODE, sendIntent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, BaseActivity.REQUEST_CODE, deliveredIntent, PendingIntent.FLAG_ONE_SHOT);
        BaseActivity.REQUEST_CODE++;

        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<String> messagelist = smsManager.divideMessage(message);
        ArrayList<PendingIntent> pendingSentList = new ArrayList<>();
        pendingSentList.add(piSent);
        ArrayList<PendingIntent> pendingDeliveredList = new ArrayList<>();
        pendingDeliveredList.add(piDelivered);
        smsManager.sendMultipartTextMessage(phoneNumber, null, messagelist, pendingSentList, pendingDeliveredList);
    }
}
