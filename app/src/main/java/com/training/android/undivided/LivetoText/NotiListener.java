package com.training.android.undivided.LivetoText;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hillary Briones on 2/13/2018.
 */


public class NotiListener extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "Notification Services Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static String getpackagename(StatusBarNotification s){
        return s.getPackageName().split(".")[s.getPackageName().split(".").length -1];
    }

    public static List<String> getText(Notification notification)
    {

        RemoteViews views = notification.bigContentView;
        if (views == null) views = notification.contentView;
        if (views == null) return null;


        List<String> text = new ArrayList<String>();
        try
        {
            Field field = views.getClass().getDeclaredField("mActions");
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            ArrayList<Parcelable> actions = (ArrayList<Parcelable>) field.get(views);


            for (Parcelable p : actions)
            {
                Parcel parcel = Parcel.obtain();
                p.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);


                int tag = parcel.readInt();
                if (tag != 2) continue;


                parcel.readInt();
                String methodName = parcel.readString();
                if (methodName == null) continue;


                else if (methodName.equals("setText"))
                {

                    parcel.readInt();


                    String t = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel).toString().trim();
                    text.add(t);
                }


                else if (methodName.equals("setTime"))
                {

                }

                parcel.recycle();
            }
        }


        catch (Exception e)
        {
            Log.e("NotificationClassifier", e.toString());
        }

        return text;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNotificationPosted");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" +  "\t" + sbn.getPackageName());
        Intent i = new Intent("com.training.android.undivided.LivetoText.newnoti");
        i.putExtra("notification_event",getText(sbn.getNotification()) + "\n");
        sendBroadcast(i);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
        Intent i = new Intent("com.training.android.undivided.LivetoText.oldnoti");
        i.putExtra("notification_even","onNotificationRemoved :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);
    }
}
