package com.training.android.undivided.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.Group.Model.GroupModel;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.training.android.undivided.LivetoText.SmsListener.getContactName;

/**
 * Created by Dyste on 2/22/2018.
 */

public class SMS_Receiver extends BroadcastReceiver {

    private DBHandler dbHandler;
    private GroupModel gmodel;
    private String phoneNumber;
    private SmsMessage sms;
    private String strMessage = "";
    private int count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        dbHandler = new DBHandler(context);
        Bundle bundle = intent.getExtras();
        count = 0;


        //TODO: HILLARY REPLY MESSAGE FOR SMS
//        SharedPreferences replySharedPrefs = context.getSharedPreferences("com.example.ReplyMessage", Context.MODE_PRIVATE);
//        String unknown_number_message = replySharedPrefs.getString("replyMessage","I'm currently driving");

        try {

            Object[] pdusObj = (Object[]) bundle.get("pdus");

            for (int i = 0; i < pdusObj.length; i++) {
                // This will create an SmsMessage object from the received pdu
                sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                phoneNumber = sms.getOriginatingAddress();
                phoneNumber = "0" + phoneNumber.substring(3);
                phoneNumber = phoneNumber.replace(" ", "");
                gmodel = dbHandler.getGroup(phoneNumber);

            }

            if (gmodel.getRule1() == 1) {
                replySMS(context, phoneNumber);
            }
            if (contactExists(context,phoneNumber)) {
                if (gmodel.getRule3() == 1 && count == 0) {
                    count = 1;
//                    String ew = "";
//                for (int h = 3; h < sms.getOriginatingAddress().length(); h++) {
//                    ew = ew + sms.getOriginatingAddress().charAt(h) + " ";
//                }

                    String send = getContactName(getApplicationContext(), sms.getOriginatingAddress());

                    if (send == null) {
                        // strMessage += "SMS From: " + ew


                    } else {
                        strMessage += "SMS From: " + send;
                    }

                    strMessage += "It says";
                    strMessage += " : ";
                    strMessage += sms[i].getMessageBody();
                    strMessage += "\n";
                    strMessage += " Do you wish to reply?";

                    Intent in = new Intent("android.intent.action.MAIN2");
                    in.putExtra("sms_event", strMessage);
                    in.putExtra("com.training.android.undivided.LivetoText.number", sms.getOriginatingAddress());
                    context.sendBroadcast(in);

                }


            }else{
                SharedPreferences replySharedPrefs = context.getSharedPreferences("com.example.ReplyMessage", Context.MODE_PRIVATE);
                String unknown_number_message = replySharedPrefs.getString("replyMessage", "I'm currently driving");
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, unknown_number_message, null, null);

                } catch (Exception e) {
                    Log.e("ERROR", e.getLocalizedMessage());
                }
            }

        }catch(Exception e)

            {
                e.printStackTrace();
            }
        }


    public boolean contactExists(Context context, String number) {
/// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
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

    private void replySMS(Context context, String num) {


        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num, null, gmodel.getGroupMessage(), null, null);

        } catch (Exception e) {
            Log.e("ERROR", e.getLocalizedMessage());
        }

    }
}
