package com.training.android.undivided.GroupSender.Utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.training.android.undivided.GroupSender.App;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class Toasts {

    private Toasts() {}

    public static boolean sShow = true;


    public static void showShort(CharSequence message) {
        if (sShow)
            Toast.makeText(App.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showShort( @StringRes int message) {
        if (sShow)
            Toast.makeText(App.getInstance(), message, Toast.LENGTH_SHORT).show();
    }


    public static void showLong(CharSequence message) {
        if (sShow)
            Toast.makeText(App.getInstance(), message, Toast.LENGTH_LONG).show();
    }


    public static void showLong(@StringRes int message) {
        if (sShow)
            Toast.makeText(App.getInstance(), message, Toast.LENGTH_LONG).show();
    }


    public static void show(CharSequence message, int duration) {
        if (sShow)
            Toast.makeText(App.getInstance(), message, duration).show();
    }


    public static void show(@StringRes int message, int duration) {
        if (sShow)
            Toast.makeText(App.getInstance(), message, duration).show();
    }

    private static Toast mToast;
    private static long time = 0;


    public static void showToast(String content) {
        long temp = System.currentTimeMillis();
        if (mToast == null) {
            mToast = Toast.makeText(App.getInstance(), content, Toast.LENGTH_SHORT);
        }
        mToast.setText(content);
        if (temp - time < 2000) {
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        time = temp;
        mToast.show();
    }


    public static void showToast(@StringRes int resId) {
        long temp = System.currentTimeMillis();
        if (mToast == null) {
            mToast = Toast.makeText(App.getInstance(), resId, Toast.LENGTH_SHORT);
        }
        mToast.setText(resId);
        if (temp - time < 2000) {
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        time = temp;
        mToast.show();
    }

}
