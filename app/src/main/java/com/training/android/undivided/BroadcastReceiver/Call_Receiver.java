package com.training.android.undivided.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.Group.Model.GroupModel;

import java.lang.reflect.Method;

/**
 * Created by Dyste on 2/22/2018.
 */

public class Call_Receiver extends BroadcastReceiver {

    private ITelephony telephonyService;
    private DBHandler dbHandler;
    private GroupModel gmodel;


    @Override
    public void onReceive(Context context, Intent intent) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        dbHandler = new DBHandler(context);


        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(tm);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            //End Call
            telephonyService.endCall();

        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            gmodel = dbHandler.getMessageContact(incomingNumber);

            //TODO: AUTOREPLY CALLS
            if (gmodel.getRule2() == 1) {
                replySMS(context, incomingNumber);
            }

        }
    }

    private void replySMS(Context context, String num) {

        Toast.makeText(context, gmodel.getGroupMessage(), Toast.LENGTH_SHORT).show();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num, null, gmodel.getGroupMessage(), null, null);

        } catch (Exception e) {
            Log.e("ERROR", e.getLocalizedMessage());
        }

    }
}

