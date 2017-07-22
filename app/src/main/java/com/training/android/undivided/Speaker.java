package com.training.android.undivided;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.*;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Hillary Briones on 7/22/2017.
 */

public class Speaker implements android.speech.tts.TextToSpeech.OnInitListener {

    private android.speech.tts.TextToSpeech tts;

    private boolean ready = false;

    private boolean allowed = false;

    public Speaker(Context context){
        tts = new android.speech.tts.TextToSpeech(context, this);
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed){
        this.allowed = allowed;
    }

    @Override
    public void onInit(int status) {
        if(status == android.speech.tts.TextToSpeech.SUCCESS){
            // Change this to match your
            // locale
            tts.setLanguage(Locale.US);
            ready = true;
        }else{
            ready = false;
        }
    }
    public void speak(String text){

        // Speak only if the TTS is ready
        // and the user has allowed speech

        if(ready && allowed) {
            HashMap<String, String> hash = new HashMap<String,String>();
            hash.put(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_NOTIFICATION));
            tts.speak(text, android.speech.tts.TextToSpeech.QUEUE_ADD, hash);
        }
    }
    public void pause(int duration){
        tts.playSilence(duration, android.speech.tts.TextToSpeech.QUEUE_ADD, null);
    }
    // Free up resources
    public void destroy(){
        tts.shutdown();
    }
}

