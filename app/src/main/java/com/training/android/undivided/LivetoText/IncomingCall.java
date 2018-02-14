package com.training.android.undivided.LivetoText;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.training.android.undivided.R;

import java.util.ArrayList;

/**
 * Created by Hillary Briones on 2/13/2018.
 */

public class IncomingCall extends AppCompatActivity {
    private BroadcastReceiver mReceiver;

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    protected void onResume(Bundle savedInstanceState) {

        super.onResume();
    }

    protected void onPause(Bundle savedInstanceState) {
        super.onPause();

        this.unregisterReceiver(this.mReceiver);

    }

    public String returnoti(String number){
        Toast.makeText(getApplicationContext(), "onClick has been launched", Toast.LENGTH_SHORT).show();
        String ew=new String();
        for(int h=3;h<number.length();h++){
            ew=number;
        }
        String send=getContactName(this,number);
        if(send==null){
            return ew;
        }
        else{
            return send;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Dialog);
        setContentView(R.layout.dictate);


        IntentFilter intentF = new IntentFilter("android.intent.action.MAI");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String msg_for_me = intent.getStringExtra("some_msg");

                Log.i("Aa dugaya intent", msg_for_me);
                startVoiceRecognitionActivity();
            }
        };

        this.registerReceiver(this.mReceiver, intentF);
        startService(new Intent(this,separatereadout.class).putExtra("oti", "You have a call from" + returnoti(getIntent().getStringExtra("phone") + "Answer or Disconnect?")));
    }

    public static void acceptCall(Context context)
    {
        Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
        buttonUp.putExtra(Intent.EXTRA_KEY_EVENT,
                new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
        context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
    }

    private void rejectCall(Context context)
    {
        Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON); buttonDown.putExtra(Intent.EXTRA_KEY_EVENT,
            new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
        context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");}

    private static final int REQUEST_CODE = 1234;

    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Answer or Disconnect");

        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {

            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(getApplicationContext(), matches.get(0), Toast.LENGTH_SHORT).show();
            if(matches.get(0).toLowerCase().compareTo("answer")==0){
                acceptCall(this);
                this.onDestroy();

            }else{
                rejectCall(this);
                this.onDestroy();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
