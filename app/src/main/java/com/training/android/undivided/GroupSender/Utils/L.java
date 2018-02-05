package com.training.android.undivided.GroupSender.Utils;

import android.util.Log;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class L {
    private static final String TAG = "Log";

    private L() {}


    public static boolean sDebug = true;

    public static void v(String msg) {
        if (sDebug)
            Log.v(TAG, msg);
    }

    public static void d(String msg) {
        if (sDebug)
            Log.d(TAG, msg);
    }


    public static void i(String msg) {
        if (sDebug)
            Log.i(TAG, msg);
    }

    public static void e(String msg) {
        if (sDebug)
            Log.e(TAG, msg);
    }

    public static void w(String msg) {
        if (sDebug)
            Log.w(TAG, msg);
    }


    public static void v(String tag, String msg) {
        if (sDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (sDebug)
            Log.i(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (sDebug)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (sDebug)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (sDebug)
            Log.i(tag, msg);
    }



}
