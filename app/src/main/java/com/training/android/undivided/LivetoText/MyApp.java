package com.training.android.undivided.LivetoText;

import android.app.Application;

/**
 * Created by Hillary Briones on 2/13/2018.
 */

public class MyApp extends Application {
    public static String number = "919971496664";
    public static int flag=0;
    public static int smsflag=0;
    private static MyApp singleton;
    public static MyApp getInstance() {
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public String getState(){
        return number;
    }
    public void setState(String s){
        number = s;
    }
}
