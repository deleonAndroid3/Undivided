package com.training.android.undivided.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.Group.Model.GroupModel;
import com.training.android.undivided.TTS;

import java.lang.reflect.Method;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Dyste on 2/22/2018.
 */

public class Call_Receiver extends BroadcastReceiver {

    private ITelephony telephonyService;
    private DBHandler dbHandler;
    private GroupModel gmodel;
    private int count = 0;
    private int count1 = 0;
    private mPhoneStateListener phoneStateListener;

    private String logTag = "Call_Receiver";


    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.i(logTag, "Received call intent");
        count = 0;
        count1 = 0;

        AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        //TODO: HILLARY REPLY MESSAGE FOR CALL
        SharedPreferences replySharedPrefs = context.getSharedPreferences("com.example.ReplyMessage", Context.MODE_PRIVATE);
        String unknown_number_message = replySharedPrefs.getString("replyMessage","I'm currently driving");


        try {
            if (phoneStateListener == null) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                dbHandler = new DBHandler(context);
                phoneStateListener = new mPhoneStateListener(context);

                SharedPreferences.Editor editor = context.getSharedPreferences("com.example.ringing", MODE_PRIVATE).edit();
                editor.putBoolean("ringing", false);
                editor.commit();

                Class c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                telephonyService = (ITelephony) m.invoke(tm);
                tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

    }

    private void Threshold(Context context, String incomingNumber) {

        SharedPreferences thresholdPrefs = context.getSharedPreferences("com.example.threshold", MODE_PRIVATE);
        SharedPreferences thresholdCounterPrefs = context.getSharedPreferences("com.example.thresholdCounter" + incomingNumber, MODE_PRIVATE);


        if (!(thresholdPrefs.getString("threshold", String.valueOf(0)).equals(thresholdCounterPrefs.getString("thresholdCounter"
                + incomingNumber, String.valueOf(0))))) {

            SharedPreferences.Editor threshold_editor = context.getSharedPreferences("com.example.thresholdCounter" + incomingNumber, MODE_PRIVATE).edit();
            threshold_editor.putString("thresholdCounter" + incomingNumber, String.valueOf(Integer.parseInt(thresholdCounterPrefs.getString("thresholdCounter"
                    + incomingNumber, String.valueOf(0))) + 1));
            threshold_editor.commit();
            Log.i(logTag, "COUNTING" + thresholdCounterPrefs.getString("thresholdCounter" + incomingNumber, String.valueOf(0)));
        }

        if (thresholdPrefs.getString("threshold", String.valueOf(0)).equals(thresholdCounterPrefs.getString("thresholdCounter"
               + incomingNumber, String.valueOf(0)))) {
            Log.i(logTag, "MESSAGE ME!");

            SharedPreferences.Editor threshold_editor = context.getSharedPreferences("com.example.thresholdCounter"
                    + incomingNumber, MODE_PRIVATE).edit();
            threshold_editor.putString("thresholdCounter" + incomingNumber, String.valueOf(0));

            threshold_editor.commit();

//            context.startService(new Intent(context, TTS.class));
        }

        count1 = 1;
    }

    public boolean contactExists(Context context, String number) {
/// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
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

//            SharedPreferences thresholdCounterPrefs = mContext.getSharedPreferences("com.example.thresholdCounter"
//                    + incomingNumber, MODE_PRIVATE);
//            SharedPreferences thresholdPrefs = mContext.getSharedPreferences("com.example.threshold", MODE_PRIVATE);
//
//            if((Integer.parseInt(thresholdCounterPrefs.getString("com.example.thresholdCounter"
//                    + incomingNumber, String.valueOf(0)))) >=
//                    (Integer.parseInt(thresholdPrefs.getString("com.example.threshold",
//                            String.valueOf(0))))) {
//                SharedPreferences.Editor threshold_editor = mContext.getSharedPreferences("com.example.thresholdCounter"
//                        + incomingNumber, MODE_PRIVATE).edit();
//                threshold_editor.putString("thresholdCounter" + incomingNumber, String.valueOf(0));
//
//            threshold_editor.commit();
//
//            }


            incomingNumber = incomingNumber.replace(" ", "");
            gmodel = dbHandler.getGroup(incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    //End Call
                    telephonyService.silenceRinger();
                    telephonyService.endCall();

                    SharedPreferences sharedPrefs = mContext.getSharedPreferences("com.example.ringing", MODE_PRIVATE);
                    if (!sharedPrefs.getBoolean("ringing", true)) {

                        if (contactExists(mContext, incomingNumber)) {
                            if (gmodel.getRule2() == 1 && count == 0)
                                replySMS(incomingNumber);

                    if (gmodel.getRule4() == 1 && count1 == 0)
                        Threshold(mContext , incomingNumber);
                        Threshold(mContext, incomingNumber);
                            if (gmodel.getRule4() == 1 && count1 == 0)
                                Threshold(mContext, incomingNumber);

                        } else {

                            SharedPreferences replySharedPrefs = mContext.getSharedPreferences("com.example.ReplyMessage", Context.MODE_PRIVATE);
                            String unknown_number_message = replySharedPrefs.getString("replyMessage", "I'm currently driving");
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(incomingNumber, null, unknown_number_message, null, null);

                            } catch (Exception e) {
                                Log.e("ERROR", e.getLocalizedMessage());
                            }
                        }

                        SharedPreferences.Editor editor = mContext.getSharedPreferences("com.example.ringing", MODE_PRIVATE).edit();
                        editor.putBoolean("ringing", true);
                        editor.commit();
                        break;

                    }
            }
        }

    }
}


