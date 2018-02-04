package com.training.android.undivided.GroupSender.Utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class ActivityCollector {

    private static final String TAG = "ActivityCollector";

    private static List<Activity> sActivityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        sActivityList.add(activity);
    }

    public static void removeActivty(Activity activity) {
        sActivityList.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity: sActivityList) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }

    public static void finish(int count) {
        for (int i = 0; i < count; i++) {
            sActivityList.get(sActivityList.size() - 1 - i).finish();
        }
    }

    public static Activity getTopActivity() {
        if (sActivityList.isEmpty()) {
            return null;
        }
        return sActivityList.get(sActivityList.size() - 1);
    }

}
