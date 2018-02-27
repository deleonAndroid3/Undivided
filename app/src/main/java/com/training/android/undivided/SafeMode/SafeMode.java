package com.training.android.undivided.SafeMode;

import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.training.android.undivided.AutoReply.Widget.RuleWidgetProvider;
import com.training.android.undivided.BroadcastReceiver.Call_Receiver;
import com.training.android.undivided.BroadcastReceiver.SMS_Receiver;
import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.DriveHistory.Model.DriveModel;
import com.training.android.undivided.Emergency;
import com.training.android.undivided.LivetoText.DictateandSend;
import com.training.android.undivided.LivetoText.MyApp;
import com.training.android.undivided.LivetoText.ReadOutAndSignal;
import com.training.android.undivided.LivetoText.SmsListener;
import com.training.android.undivided.MainActivity;
import com.training.android.undivided.R;
import com.training.android.undivided.Speaker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SafeMode extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    private static LayoutInflater inflater = null;
    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;
    DBHandler dbHandler;
    DrawerLayout drawerLayout;
    ImageView imgView;
    private TextToSpeech myTTS;
    private Button button;
    private Speaker speaker;
    private String start_time = "0";
    private String end_time = "0";
    private boolean speaker_flag = false;
    private boolean mBool = true;

    private BroadcastReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_mode);

        dbHandler = new DBHandler(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        start_time = dateFormat.format(new Date());

//        myTTS = new TextToSpeech(this, this);
//        speaker = new Speaker(this);
//        button = findViewById(R.id.btnTTS);

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

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                speaker.allow(true);
//                speaker.speak(getString(R.string.start_speaking));
////                myTTS.speak("Hello, Good Morning!", TextToSpeech.QUEUE_FLUSH, null);
//                Log.d("SPEAKER SPOKE", "The speaker spoke.");
//            }
//        });

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
                updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{0}).setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                getApplicationContext().sendBroadcast(updateWidgetIntent);
            }
        }.run();

        imgView = findViewById(R.id.ivDriveStop);
        drawerLayout = findViewById(R.id.drawer_layout);

        drawerLayout.getBackground().setAlpha(80);

        Toast.makeText(SafeMode.this, "Safe Mode Selected", Toast.LENGTH_SHORT).show();
        ComponentName mDeviceAdmin;
        DevicePolicyManager myDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdmin = new ComponentName(SafeMode.this, MainActivity.class);

        if (myDevicePolicyManager.isDeviceOwnerApp(SafeMode.this.getPackageName())) {
            // Device owner
            String[] packages = {SafeMode.this.getPackageName()};
            myDevicePolicyManager.setLockTaskPackages(mDeviceAdmin, packages);
        } else {
            Log.d("INFO", "IS NOT APP OWNER");
        }

        if (myDevicePolicyManager.isLockTaskPermitted(SafeMode.this.getPackageName())) {
            // Lock allowed
            // NOTE: locking device also disables notification
            startLockTask();
        } else {
            Log.d("INFO", "lock not permitted");
            startLockTask();
            enableCallBroadcastReceiver();
            enableSMSBroadcastReceiver();
            this.startService(new Intent(this, SmsListener.class));
        }

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLockTask();
                disableSMSBroadcastReceiver();
                disableCallBroadcastReceiver();

//                speakerStop();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                end_time = dateFormat.format(new Date());

                DriveModel dm = new DriveModel();
                dm.setDriveType("SafeMode");
                dm.setStart_time(start_time);
                dm.setEnd_time(end_time);

                dbHandler.addDrive(dm);

                Intent i = new Intent(SafeMode.this, MainActivity.class);
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

    private void checkTTS() {
        Intent check = new Intent();
        check.setAction(android.speech.tts.TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_CODE) {
            if (resultCode == android.speech.tts.TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                speaker = new Speaker(this);
            } else {
                Intent install = new Intent();
                install.setAction(android.speech.tts.TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            //Toast.makeText(getApplicationContext(), matches.get(0), Toast.LENGTH_SHORT).show();
            if (matches.get(0).toLowerCase().compareTo("yes") == 0) {

                startActivity(new Intent(this, DictateandSend.class).putExtra("number", MyApp.number));

            } else {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        Log.i("Back Pressed", "Back button is disabled");
    }

    private String getContactName(String phone) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        } else {
            return "unknown number";
        }
    }

    @Override
    protected void onDestroy() {
        speaker.speak(getString(R.string.stop_speaking));
        speaker.allow(false);
        unregisterReceiver(smsReceiver);
        speaker.destroy();
        super.onDestroy();
    }

    private void initializeSMSReceiver() {

        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String msg_for_me = intent.getStringExtra("sms_event");
                MyApp.number = intent.getStringExtra("com.training.android.undivided.LivetoText.number");
                context.startService(new Intent(SafeMode.this, ReadOutAndSignal.class).putExtra("noti", msg_for_me));

                Intent intent2 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent2.putExtra(RecognizerIntent.EXTRA_PROMPT, "Reply Back?");
                startActivityForResult(intent2, REQUEST_CODE);
            }
        };
    }

    private void registerSMSReceiver() {
        IntentFilter intentFilter2 = new IntentFilter("android.intent.action.MAIN2");
        registerReceiver(smsReceiver, intentFilter2);
    }

    public void enableCallBroadcastReceiver() {
        ComponentName receiver = new ComponentName(this, Call_Receiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

    public void enableSMSBroadcastReceiver() {
        ComponentName receiver = new ComponentName(this, SMS_Receiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

    public void disableCallBroadcastReceiver() {
        ComponentName receiver = new ComponentName(this, Call_Receiver.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    public void disableSMSBroadcastReceiver() {
        ComponentName receiver = new ComponentName(this, SMS_Receiver.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }
}
