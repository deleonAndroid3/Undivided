package com.training.android.undivided.LivetoText;

import android.app.Activity;
import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.*;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.training.android.undivided.R;

import java.util.ArrayList;

/**
 * Created by Hillary Briones on 2/13/2018.
 */

public class DictateandSend extends Activity {
    private static final int REQUEST_CODE = 1234;
    private BroadcastReceiver check;
    IntentFilter filtercheck= new IntentFilter("android.intent.action.check");

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Dialog);
        this.setContentView(R.layout.dictate);
        check = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(),"check", Toast.LENGTH_SHORT).show();
                startVoiceRecognitionActivity();
            }
        };

        this.registerReceiver(this.check,filtercheck);

        Toast.makeText(getApplicationContext(), "Starting Voice", Toast.LENGTH_SHORT).show();
        this.startService(new Intent(this,ReadOut.class).putExtra("noti", "Please Dictate your Message"));
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(check);
    }

    protected void sendSMSMessage(String phoneNo, String message) {
        Log.i("Send SMS", "");

        Toast.makeText(getApplicationContext(),"Attempting to send message to: "+ phoneNo + " with details: "+message,
                Toast.LENGTH_SHORT).show();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again.",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Dictate Your Message!");

        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {

            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            sendSMSMessage(MyApp.number,matches.get(0).toLowerCase());

        }
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
