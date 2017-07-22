package com.training.android.undivided;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by Maouusama on 7/20/2017.
 */

public class BackgroundIntentService extends IntentService {

    public BackgroundIntentService() {
        this(BackgroundIntentService.class.getName());
    }

    public BackgroundIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        showToast("Starting Intent Service");

        try{
            Thread.sleep(10000);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

        showToast("Finishing Intent Service");
    }

    protected void showToast(final String msg){
        //gets the main thread
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // run this code in the main thread
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

