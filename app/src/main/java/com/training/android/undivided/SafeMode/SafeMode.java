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
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.training.android.undivided.AutoReply.Widget.RuleWidgetProvider;
import com.training.android.undivided.BroadcastReceiver.Call_Receiver;
import com.training.android.undivided.BroadcastReceiver.SMS_Receiver;
import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.DriveHistory.Model.DriveModel;
import com.training.android.undivided.Group.Model.ContactsModel;
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
    DrawerLayout drawerLayout;
    ImageView imgView;
    ArrayList<ContactsModel> cmodel;
    private TextToSpeech myTTS;
    private Button button;
    private Speaker speaker;
    private String start_time = "0";
    private String end_time = "0";
    private boolean speaker_flag = false;
    private boolean mBool = true;
    private DBHandler dbHandler;
    private String message;
    private String msg;
    private BroadcastReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_mode);

        dbHandler = new DBHandler(this);
        cmodel = new ArrayList<>();

        message = dbHandler.getMessage("Emergency").getGroupMessage();

        Intent startingIntent = this.getIntent();
        msg = startingIntent.getStringExtra("MESSAGE");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        start_time = dateFormat.format(new Date());

        SharedPreferences.Editor threshold_editor = getSharedPreferences("com.example.thresholdCounter", MODE_PRIVATE).edit();
        threshold_editor.putString("thresholdCounter", String.valueOf(0));
        threshold_editor.apply();

        SharedPreferences.Editor threshold_editorMK2 = getSharedPreferences("com.example.selectedMode", MODE_PRIVATE).edit();
        threshold_editor.putString("selectedMode", "SafeMode");
        threshold_editor.apply();


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

        // OLD DISABLE ATTACHTOWINDOW
        onAttachedToWindow();

//        Broadcast with smartlock
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
//            this.startService(new Intent(this, SmsListener.class));
        }

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLockTask();
                disableSMSBroadcastReceiver();
                disableCallBroadcastReceiver();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                end_time = dateFormat.format(new Date());

                DriveModel dm = new DriveModel();
                dm.setDriveType("SafeMode");
                dm.setStart_time(start_time);
                dm.setEnd_time(end_time);

                dbHandler.addDrive(dm);

                SharedPreferences.Editor editor = getSharedPreferences("com.example.bgService", MODE_PRIVATE).edit();
                editor.putBoolean("bgService", false);
                editor.commit();

                Intent i = new Intent(SafeMode.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                disableSMSBroadcastReceiver();
                disableCallBroadcastReceiver();

                cmodel = dbHandler.getEmergencyContacts();

                 int i = 0;

                while (i != cmodel.size()) {
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(cmodel.get(i).getContactNumber(), null, message, null, null);

                        Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
                    }
                    i++;
                }

                String phone = "+639234152360";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

                return false;
            }
        });

        initializeSMSReceiver();
        registerSMSReceiver();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {
            Toast.makeText(this, "You pressed home button", Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
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
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onAttachedToWindow() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    private void speakPullOver() {

    }

    @Override
    protected void onDestroy() {
//        speaker.speak(getString(R.string.stop_speaking));
//        speaker.allow(false);
//        speaker.destroy();
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
