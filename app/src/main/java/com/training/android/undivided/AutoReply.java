package com.training.android.undivided;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.HashMap;

public class AutoReply {
    private boolean                 activated;
    private String                  app_signature;
    private String                  current_message;
    private int                     default_duration;
    private int                     duration;
    private ArrayList<Message> history;
    private boolean                 include_eta;
    private boolean                 include_signature;
    private static AutoReply        instance = null;
    private HashMap<String, String> sent_messages;

    private AutoReply() {
        this.activated        = false;
        this.app_signature    = "Auto-sent via Text & Drive";
        this.current_message  = "Sorry, I am driving right now.  I'll call you back soon.";
        this.duration         = 6000; // 15 Minutes
        this.default_duration = 6000; // 15 Minutes
        this.history          = new ArrayList<Message>();

        this.sent_messages    = new HashMap<String, String>();
    }

    public static AutoReply getInstance() {
        if (instance == null) {
            instance = new AutoReply();
            return instance;
        } else {
            return instance;
        }
    }
    public boolean activated() {
        return this.activated;
    }

    public String appSignature() {
        return this.app_signature;
    }

    public String currentMessage() {
        return current_message;
    }

    public int defaultDuration() {
        return this.default_duration;
    }

    public int duration() {
        return this.duration;
    }

    public ArrayList<Message> history() {
        return this.history;
    }
    public void setActivated(boolean status) {
        if (this.activated != status) {
            this.activated = status;
            if (this.activated == true) {
                clearHistory();
            }
        }
    }

    public void setCurrentMessage(String message) {
        this.current_message = message;
    }

    public void setDefaultDuration(int new_duration) {
        this.default_duration = new_duration;
    }

    public void setDuration(int milliseconds) {
        this.duration = milliseconds;
    }

    public void setIncludeEta(boolean setting) {
        this.include_eta = setting;
    }

    public void setIncludeSignature(boolean setting) {
        this.include_signature = setting;
    }
    public void addMessageToHistory(Context context, String phone, String message) {
        this.history.add(new Message(phone, message));
        this.sent_messages.put(phone, message);
    }

    public void clearHistory() {
        this.history.clear();
        this.sent_messages.clear();
    }

    public String eta() {
        String eta = "ETA:";

        // Format the hours
        if (remainingHours() == 1) {
            eta += " 1 hour";
        } else if (remainingHours() > 0) {
            eta += String.format(" %d hours", remainingHours());
        }

        // Format the minutes
        if (remainingMinutes() == 1) {
            eta += " 1 minute";
        } else if (remainingMinutes() > 0) {
            eta += String.format(" %d minutes", remainingMinutes());
        }

        return eta;
    }

    public boolean isInSentHistory(String number) {
        return this.sent_messages.containsKey(number);
    }

    public String message() {
        String message = this.current_message;
        if (this.include_eta || this.include_signature) message += "\n";
        if (this.include_eta) message += "\n" + this.eta();
        if (this.include_signature) message += "\n" + this.app_signature;
        return message;
    }

    public int remainingHours() {
        return (this.duration / (1000*60*60)) % 24;
    }

    public int remainingMinutes() {
        return (this.duration / (1000*60)) % 60;
    }

    public int remainingSeconds() {
        return (this.duration / 1000) % 60 ;
    }

    public void replyTo(Context context, String number) {
        if (this.activated && !isInSentHistory(number)) {
            Log.d("AutoReply", "Replying to: " + number);
            Intent send_intent = new Intent(context, SmsSender.class);
            send_intent.putExtra("number", number);
            send_intent.putExtra("message", message());
            context.startService(send_intent);
            addMessageToHistory(context, number, "Reply sent!");
        }
    }

}
