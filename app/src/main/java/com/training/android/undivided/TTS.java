package com.training.android.undivided;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Dyste on 2/28/2018.
 */

public class TTS extends Service implements android.speech.tts.TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    private TextToSpeech mTts;
    private Handler handler;


    @Override
    public void onCreate() {
      super.onCreate();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTts.speak("There is an incoming emergency call. You are advised to pull over", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }, 5000);

            }
        }
    }

    @Override
    public void onUtteranceCompleted(String s) {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mTts = new TextToSpeech(this, this);
        handler = new Handler();

        return super.onStartCommand(intent, flags, startId);
    }
}
