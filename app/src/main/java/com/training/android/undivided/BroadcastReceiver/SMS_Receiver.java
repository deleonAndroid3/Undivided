package com.training.android.undivided.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    public void onReceive(Context context, Intent intent) {
        dbHandler = new DBHandler(context);
        Bundle bundle = intent.getExtras();

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
                Toast.makeText(context, "Send Reply to " + phoneNumber, Toast.LENGTH_SHORT).show();
            }

            if (gmodel.getRule3() == 1) {
                String ew = "";
                for (int h = 3; h < sms.getOriginatingAddress().length(); h++) {
                    ew = ew + sms.getOriginatingAddress().charAt(h) + " ";
                }

                String send = getContactName(getApplicationContext(), sms.getOriginatingAddress());

                if (send == null) {
                    strMessage += "SMS From: " + ew;
                } else {
                    strMessage += "SMS From: " + send;
                }
                strMessage += "It says";
                strMessage += " : ";
                strMessage += sms.getMessageBody();
                strMessage += "\n";
                strMessage += " Do you wish to reply?";

                Intent in = new Intent("android.intent.action.MAIN2");
                in.putExtra("sms_event", strMessage);
                in.putExtra("com.training.android.undivided.LivetoText.number", sms.getOriginatingAddress());
                context.sendBroadcast(in);

            } else
                Toast.makeText(context, "Not Found", Toast.LENGTH_SHORT).show();


        } catch (Exception e)

        {
            e.printStackTrace();
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
