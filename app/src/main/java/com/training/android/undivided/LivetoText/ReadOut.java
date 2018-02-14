package com.training.android.undivided.LivetoText;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Hillary Briones on 2/13/2018.
 */

public class ReadOut extends Service implements TextToSpeech.OnInitListener {
    public final String tag="yo";
    private String str;
    private TextToSpeech mTts;
    private static final String TAG="TTSService";
    public static int a=1;

    public void onCreate(){
        Log.d(tag,"onCreate");
        Toast.makeText(getApplicationContext(),"onCreate", Toast.LENGTH_LONG).show();
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(tag,"onStartCommand");
        mTts = new TextToSpeech(this,
                this  // OnInitListener
        );
        //Toast.makeText(getApplicationContext(),"onStartCommand", Toast.LENGTH_LONG).show();
        mTts.setSpeechRate(1.0f);
        str=intent.getStringExtra("noti");
        Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();
        return Service.START_NOT_STICKY;
    }

    public IBinder onBind(Intent arg0) {
        Log.d(tag,"onStartCommand");
        //Toast.makeText(getApplicationContext(),"onStartCommand", Toast.LENGTH_LONG).show();
        // TODO Auto-generated method stub
        return null;
    }

    private void sayHello(String str) {
        //Toast.makeText(getApplicationContext(), "Reached  itini itni door", Toast.LENGTH_LONG).show();
        mTts.speak(str,TextToSpeech.QUEUE_FLUSH,null);
    }

    public void onInit(int status) {
        Log.d(TAG, "oninit");
        //Toast.makeText(getApplicationContext(),"onInit", Toast.LENGTH_LONG).show();
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d(TAG, "Language is not available.");
                Toast.makeText(getApplicationContext(), "Bhai nahi ho paega", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), str+ " ye hai", Toast.LENGTH_LONG).show();
                sayHello(str);
                try{
                    //Toast.makeText(getApplicationContext(), "Delay dena hai 6 sec ka", Toast.LENGTH_LONG).show();
                    Thread.sleep(str.length()*160);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Intent Sent", Toast.LENGTH_LONG).show();
                Intent i = new Intent("android.intent.action.check");
                this.sendBroadcast(i);
                this.stopSelf();
            }
        } else {
            Log.d(tag, "Could not initialize TextToSpeech.");
        }
    }

    public void onStart(Intent intent, int startId) {
        sayHello(str);
        Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onstart_service");
        super.onStart(intent, startId);
    }

    public void onDestroy() {
        // TODO Auto-generated method stub
        //Toast.makeText(getApplicationContext(), "Destorying Service", Toast.LENGTH_LONG).show();
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

}
