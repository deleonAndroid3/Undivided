package com.training.android.undivided.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

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
    private int count = 0;
    private mPhoneStateListener phoneStateListener;

    @Override

    public void onReceive(final Context context, final Intent intent) {

        try {
            if (phoneStateListener == null) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                dbHandler = new DBHandler(context);
                phoneStateListener = new mPhoneStateListener(context);

                Class c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                telephonyService = (ITelephony) m.invoke(tm);
                tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void replySMS(String num) {


        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num, null, gmodel.getGroupMessage(), null, null);
            count = 1;
        } catch (Exception e) {
            Log.e("ERROR", e.getLocalizedMessage());
        }

    }

    private class mPhoneStateListener extends PhoneStateListener {

        private Context mContext;

        private mPhoneStateListener(Context c) {
            mContext = c;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);


            incomingNumber = incomingNumber.replace(" ", "");
            gmodel = dbHandler.getGroup(incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:

                    //End Call
                    telephonyService.endCall();

                    if (gmodel.getRule2() == 1 && gmodel != null && count == 0)
                        replySMS(incomingNumber);

                    break;
            }
        }
    }
}

