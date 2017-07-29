package com.training.android.undivided;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class AutoReplyActivity extends AppCompatActivity {
    private int notification_id = 555;
    private BroadcastReceiver sent_message_receiver;
    private BroadcastReceiver timer_receiver;
    private BroadcastReceiver finished_receiver;
    private static ReplyTimer timer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_reply);
        final AutoReply auto_reply = AutoReply.getInstance();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        auto_reply.setIncludeEta(preferences.getBoolean("pref_eta", false));
        auto_reply.setIncludeSignature(preferences.getBoolean("pref_signature", true));
        auto_reply.setCurrentMessage(preferences.getString("pref_message", "Sorry, I am driving right now.  I'll call you back soon."));
    }
    protected void onResume() {
        super.onResume();
        Log.d("Lifecycle", "onResume");

        // Refresh Views
        updateMessage();
     //   updateHistory();
        updateTimer();
        updateState();

        // Listen for live history updates
        registerServiceReceiver();
    }
    protected void onPause() {
        super.onPause();
        Log.d("Lifecycle", "onPause");

        // Stop listening for live history updates
        unregisterServiceReceiver();
    }

    protected void onStop() {
        super.onStop();
        Log.d("Lifecycle", "onStop");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d("Lifecycle", "onRestart");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("Lifecycle", "onDestroy");
        // Further Clean Up
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onMessageClick(View v) {
        Intent edit_reply_message = new Intent(this, EditReplyMessage.class);
        startActivity(edit_reply_message);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onTimerClick(View v) {
        AutoReply auto_reply = AutoReply.getInstance();
        int milliseconds = auto_reply.defaultDuration();
        int minutes = ((milliseconds / (1000*60)) % 60);
        int hours   = ((milliseconds / (1000*60*60)) % 24);

        TimePickerDialog timer_picker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int minute) {
                cancelTimer();
                int milliseconds = ((hour * 60) + minute) * 60 * 1000;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AutoReplyActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("pref_duration", milliseconds);
                AutoReply auto_reply = AutoReply.getInstance();
                auto_reply.setDuration(milliseconds);
                auto_reply.setDefaultDuration(milliseconds);
                editor.commit();
                updateTimer();
                if (auto_reply.activated()) startTimer();
            }
        }, hours, minutes, true);
        timer_picker.setTitle("Hours : Minutes");
        timer_picker.show();
    }

    public void onToggleActivation(View v) {
        boolean on = ((ToggleButton) v).isChecked();
        if (on)
            activate();
        else
            deactivate();
    }
    private void activate() {
        AutoReply auto_reply = AutoReply.getInstance();

        // Activate the app
        auto_reply.setActivated(true);
        updateState();

        // Clear history
        auto_reply.clearHistory();
       // updateHistory();

        // Start the timer
        startTimer();

        // Send the notification
        sendNotification();
    }

    public void cancelNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(notification_id);
    }

    public void cancelTimer() {
        Log.d("Timer", "Cancel timer");
        if (timer != null) {
            timer.interrupt();
        }
    }

    private void deactivate() {
        AutoReply auto_reply = AutoReply.getInstance();

        // Deactivate the app
        auto_reply.setActivated(false);
        updateState();

        // Stop the timer
        cancelTimer();

        // Update the timer
        updateTimer();

        // Clear the notification
        cancelNotification();
    }

    // Look up contact information from phone number
    private String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = number;

        ContentResolver contentResolver = getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }
        return name;
    }
    private void registerServiceReceiver() {
        // History Updates
        this.sent_message_receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                updateHistory();
            }

            private void updateHistory() {
            }
        };
        IntentFilter autoreplied = new IntentFilter("android.intent.action.AUTOREPLIED");
        this.registerReceiver(sent_message_receiver, autoreplied);

        // Timer updates
        this.timer_receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {updateTimer();
            }
        };
        IntentFilter autoreplytick = new IntentFilter("android.intent.action.AUTOREPLYTICK");
        this.registerReceiver(timer_receiver, autoreplytick);

        // Timer finished updates
        this.finished_receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {deactivate();
            }
        };
        IntentFilter autoreplyfinished = new IntentFilter("android.intent.action.AUTOREPLYFINISHED");
        this.registerReceiver(finished_receiver, autoreplyfinished);
    }



    private void sendNotification() {
        NotificationCompat.Builder notification;
        notification = new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.drawable.ic_notification);
        notification.setContentTitle("Undivided Activated");
        notification.setTicker("Undivided Activated");
        notification.setContentText("Auto Reply is now on.");
        notification.setPriority(2);
        Intent resultIntent = new Intent(this, AutoReplyActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(resultPendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(notification_id, notification.build());
    }

    private void startTimer() {
        Log.d("Timer", "Start Timer");
        timer = new ReplyTimer();
        timer.start();
    }

    private void unregisterServiceReceiver() {
        this.unregisterReceiver(this.sent_message_receiver);
        this.unregisterReceiver(this.timer_receiver);
        this.unregisterReceiver(this.finished_receiver);
    }

    private void updateMessage() {
        AutoReply auto_reply = AutoReply.getInstance();
        TextView message = (TextView) findViewById(R.id.message);
        message.setText(auto_reply.currentMessage());
    }

    private void updateTimer() {
        AutoReply auto_reply = AutoReply.getInstance();
        TimeTicker tick = new TimeTicker(auto_reply.duration());
        TextView hours = (TextView) findViewById(R.id.hours);
        TextView minutes = (TextView) findViewById(R.id.minutes);
        TextView seconds = (TextView) findViewById(R.id.seconds);
        hours.setText(tick.formatHours());
        minutes.setText(tick.formatMinutes());
        seconds.setText(tick.formatSeconds());
    }

    private void updateState() {
        AutoReply auto_reply = AutoReply.getInstance();
        ToggleButton toggle_activation = (ToggleButton) findViewById(R.id.toggle_activation);
        RelativeLayout form_background = (RelativeLayout) findViewById(R.id.form_background);
        TextView message_label = (TextView) findViewById(R.id.message_label);
        TextView timer_label = (TextView) findViewById(R.id.timer_label);
        Resources resources = getResources();

        if (auto_reply.activated()) {
            toggle_activation.setChecked(true);
            toggle_activation.setBackgroundColor(resources.getColor(R.color.red));
            form_background.setBackgroundColor(resources.getColor(R.color.dark_gray));
            message_label.setTextColor(Color.WHITE);
            message_label.setText(R.string.message_label_active);
            timer_label.setTextColor(Color.WHITE);
            timer_label.setText(R.string.timer_label_active);

        } else {
            toggle_activation.setChecked(false);
            toggle_activation.setBackgroundColor(resources.getColor(R.color.green));
            form_background.setBackgroundColor(resources.getColor(R.color.light_gray));
            message_label.setTextColor(Color.BLACK);
            message_label.setText(R.string.message_label_inactive);
            timer_label.setTextColor(Color.BLACK);
            timer_label.setText(R.string.timer_label_inactive);
            // Gavin's gifs go here
        }
    }

    private class ReplyTimer extends Thread {
        private static final String TAG = "ReplyTimerThread";
        private static final int DELAY = 1000;

        @Override
        public void run() {
            AutoReply auto_reply = AutoReply.getInstance();
            while (auto_reply.duration() > 0) {
                auto_reply.setDuration(auto_reply.duration() - 1000);
                Intent tick = new Intent("android.intent.action.AUTOREPLYTICK");
                sendBroadcast(tick);
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    Log.v(TAG, "Interrupting and stopping timer");
                    return;
                }
            }

            // The timer is now finished
            auto_reply.setDuration(auto_reply.defaultDuration());
            auto_reply.setActivated(false);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(notification_id);
            Intent finished = new Intent("android.intent.action.AUTOREPLYFINISHED");
            sendBroadcast(finished);
        }
    }





}

