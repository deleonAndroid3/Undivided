package com.training.android.undivided.SafeMode;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.training.android.undivided.AutoReply.Widget.RuleWidgetProvider;
import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.Emergency;
import com.training.android.undivided.MainActivity;
import com.training.android.undivided.R;
import com.training.android.undivided.DriveHistory.Model.DriveModel;
import com.training.android.undivided.Speaker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class SafeMode extends AppCompatActivity implements android.speech.tts.TextToSpeech.OnInitListener {
    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;

    private TextToSpeech myTTS;

    private Button button;
    DBHandler dbHandler;

    private Speaker speaker;

    DrawerLayout drawerLayout;
    ImageView imgView;
    private String start_time="0";
    private String end_time="0";

    private static LayoutInflater inflater=null;
    private boolean speaker_flag = false;
    private boolean mBool = true;

    private BroadcastReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_mode);

        dbHandler = new DBHandler(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       start_time  = dateFormat.format(new Date());

        myTTS = new TextToSpeech(this, this);
        speaker = new Speaker(this);
        button = findViewById(R.id.btnTTS);

//        Intent checkTTSIntent = new Intent();
//
//        checkTTSIntent
//                .setAction(android.speech.tts.TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//        startActivityForResult(checkTTSIntent, CHECK_CODE);
//
//        Thread logoTimer = new Thread() {
//            public void run() {
//                try {
//                    speaker.allow(true);
//                    speaker.speak("Good Morning!");
//
//                }
//
//                finally {
//                    finish();
//                }
//            }
//
//        };
//        logoTimer.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker.allow(true);
                speaker.speak(getString(R.string.start_speaking));
//                myTTS.speak("Hello, Good Morning!", TextToSpeech.QUEUE_FLUSH, null);
                Log.d("SPEAKER SPOKE", "The speaker spoke.");
            }
        });

//        toggleListener = new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
//                if(isChecked){
//                    speaker.allow(true);
//                    speaker.speak(getString(R.string.start_speaking));
//                    Log.d("SPEAKER SPOKE", "The speaker spoke.");
//                }else{
//                    speaker.speak(getString(R.string.stop_speaking));
//                    speaker.allow(false);
//                }
//            }
//        };
//        toggle.setOnCheckedChangeListener(toggleListener);


        SharedPreferences.Editor threshold_editor = getSharedPreferences("com.example.thresholdCounter", MODE_PRIVATE).edit();
        threshold_editor.putString("thresholdCounter", String.valueOf(0));
        threshold_editor.commit();



        new Runnable() {
            @Override
            public void run() {
                Intent updateWidgetIntent = new Intent(getApplicationContext(), RuleWidgetProvider.class);
                updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{0} ).setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                getApplicationContext().sendBroadcast(updateWidgetIntent);
            }
        }.run();

        imgView = findViewById(R.id.ivDriveStop);
        drawerLayout = findViewById(R.id.drawer_layout);

        drawerLayout.getBackground().setAlpha(80);

        Toast.makeText(SafeMode.this, "Safe Mode Selected", Toast.LENGTH_SHORT).show();


//        ComponentName mDeviceAdmin;
//        DevicePolicyManager myDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        mDeviceAdmin = new ComponentName(SafeMode.this, MainActivity.class);
//
//        if (myDevicePolicyManager.isDeviceOwnerApp(SafeMode.this.getPackageName())) {
//            // Device owner
//            String[] packages = {SafeMode.this.getPackageName()};
//            myDevicePolicyManager.setLockTaskPackages(mDeviceAdmin, packages);
//        } else {
//            Log.d("INFO","IS NOT APP OWNER");
//        }
//
//        if (myDevicePolicyManager.isLockTaskPermitted(SafeMode.this.getPackageName())) {
//            // Lock allowed
//            // NOTE: locking device also disables notification
//            startLockTask();
//        } else {
//            Log.d("INFO","lock not permitted");
//            startLockTask();
//        }

        // OLD DISABLE ATTACHTOWINDOW
        onAttachedToWindow();

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLockTask();
//                speakerStop();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                end_time  = dateFormat.format(new Date());

                DriveModel dm = new DriveModel();
                dm.setDriveType("SafeMode");
                dm.setStart_time(start_time);
                dm.setEnd_time(end_time);

                dbHandler.addDrive(dm);

                Intent i = new Intent (SafeMode.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i = new Intent(SafeMode.this, Emergency.class);
                startActivity(i);
                return false;
            }
        });

//        speakerStart();
        checkTTS();
        initializeSMSReceiver();
        registerSMSReceiver();
//        toggle.toggle();
//        try {
//            speaker.allow(true);
//            Log.e("SPEAK ERROR", "Error on speaker.speak");
//            speaker.speak(getString(R.string.start_speaking));
//            Log.d("SPEAKER SPOKE", "The speaker spoke.");
//        }catch (Exception e){
//            Log.e("SPEAKER ERROR", "Error on starting speaker.");
//        }

//        new Runnable() {
//            @Override
//            public void run() {
//        try {
//            speaker.allow(true);
//            Log.e("SPEAK ERROR", "Error on speaker.speak");
//            speaker.speak(getString(R.string.start_speaking));
//            Log.d("SPEAKER SPOKE", "The speaker spoke.");
//        }catch (Exception e){
//            Log.e("SPEAKER ERROR", "Error on starting speaker.");
//        }
//            }
//      }.run();

    }

//    private void speakerStart(){
//        if(speaker_flag == false) {
//            speaker.allow(true);
//            speaker.speak(getString(R.string.start_speaking));
//            speaker_flag = true;
//        } else {
//            speakerStop();
//        }
//    }
//
//    private void speakerStop(){
//        if(speaker_flag == true) {
//            speaker.allow(false);
//        } else {
//            speakerStart();
//        }
//    }

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(android.speech.tts.TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if ( (event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {
            Toast.makeText(this, "You pressed home button", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Toast.makeText(this, "You pressed the back button!", Toast.LENGTH_SHORT).show();
            return true;
        }

        if ( (keyCode == KeyEvent.KEYCODE_HOME)) {
            Toast.makeText(this, "You pressed the home button.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_MENU)) {
            Toast.makeText(this, "You pressed the menu button.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == android.speech.tts.TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new Speaker(this);
            }else {
                Intent install = new Intent();
                install.setAction(android.speech.tts.TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    @Override
    public void onAttachedToWindow()
    {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


//            this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//            super.onAttachedToWindow();

//            this.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);
//            super.onAttachedToWindow();

    }

    private void initializeSMSReceiver(){
        smsReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
//                speaker.allow(true);
//                speaker.speak(getString(R.string.start_speaking));
                Bundle bundle = intent.getExtras();
                if(bundle!=null){
                    Object[] pdus = (Object[])bundle.get("pdus");
                    for(int i=0;i<pdus.length;i++){
                        byte[] pdu = (byte[])pdus[i];
                        SmsMessage message = SmsMessage.createFromPdu(pdu);
                        String text = message.getDisplayMessageBody();
                        String sender = getContactName(message.getOriginatingAddress());

                        speaker.pause(LONG_DURATION);
                        speaker.speak("You have a new message from" + sender + "!");
                        speaker.pause(LONG_DURATION);
                        speaker.speak(text);
                        speaker.speak("Do you want to reply to?"+sender+"!");


                        //Intent replyIntent = new Intent(TextToSpeech.this, SpeechToText.class);
//                        startActivity(replyIntent);

                        // Intent reIntent = getIntent();

                      /*  if( reIntent.getStringExtra("reply").equals("yes") ){
                                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                        checkSMSPermission();
                                        if (ContextCompat.checkSelfPermission(TextToSpeech.this, Manifest.permission.SEND_SMS)
                                                == PackageManager.PERMISSION_GRANTED) {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage("09568635884", null, "Gwapa", null, null);
                                        }
                                    } else {
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage("09568635884", null, "Gwapa", null, null);
                                    }
                        }*/

                    }
                }

            }
        };
    }

//    public void addHistory(String start, String end){
//
//        DriveModel dm = new DriveModel();
//        dm.setDriveType("SafeMode");
//        dm.setStart_time(start);
//        dm.setEnd_time(end);
//
//        dbHandler.addDrive(dm);
//    }

    @Override
    public void onBackPressed() {
        Log.i("Back Pressed","Back button is disabled");
    }

    private String getContactName(String phone){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }else {
            return "unknown number";
        }
    }
    private void registerSMSReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }
    @Override
    protected void onDestroy() {
        speaker.speak(getString(R.string.stop_speaking));
        speaker.allow(false);
        unregisterReceiver(smsReceiver);
        speaker.destroy();
        super.onDestroy();
    }

    @Override
    public void onInit(int i) {
        if (i == android.speech.tts.TextToSpeech.SUCCESS) {

            int result = myTTS.setLanguage(Locale.US);

            // tts.setPitch(5); // set pitch level

            // tts.setSpeechRate(2); // set speech speed rate

            if (result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA
                    || result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            } else {
                button.setEnabled(true);
            }

        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }
}
