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

/**
 * Created by Dyste on 2/22/2018.
 */

public class SMS_Receiver extends BroadcastReceiver {

    private DBHandler dbHandler;
    private GroupModel gmodel;

    @Override
    public void onReceive(Context context, Intent intent) {
        dbHandler = new DBHandler(context);
        Bundle bundle = intent.getExtras();

        try {

                Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    // This will create an SmsMessage object from the received pdu
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                    String phoneNumber = sms.getDisplayOriginatingAddress();
                    gmodel = dbHandler.getMessageContact(phoneNumber);



                        replySMS(context, phoneNumber);



                    if (gmodel.getRule1() == 1)
                        replySMS(context, phoneNumber);

                }

            }
        } catch (Exception e) {
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
