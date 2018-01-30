package com.training.android.undivided.GroupSender;

import android.app.Application;

import com.training.android.undivided.GroupSender.Utils.CrashHandler;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class App extends Application{
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        CrashHandler.getInstance().init(this);
    }

    public static App getInstance() {
        return sInstance;
    }
}
