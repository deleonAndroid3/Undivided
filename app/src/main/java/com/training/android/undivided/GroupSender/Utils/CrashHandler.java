package com.training.android.undivided.GroupSender.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.training.android.undivided.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler  {

    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";

    private String mPath;
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler() {
    }


    public static CrashHandler getInstance() {
        return SingletonHolder.sInstance;
    }


    private static class SingletonHolder {
        private static final CrashHandler sInstance = new CrashHandler();
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mPath = Environment.getExternalStorageDirectory().getPath()
                + "/"
                + mContext.getResources().getString(R.string.app_name)
                + "/log/";
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {

            dumpExceptionToSDCard(ex);

            uploadExceptionToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dumpExceptionToSDCard(Throwable ex) throws IOException {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                L.w(TAG, "sdcard unmounted, skip dump exception");
                return;
            }
        }

        File dir = new File(mPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.CHINA)
                .format(new Date(current));
        File file = new File(mPath + FILE_NAME + time + FILE_NAME_SUFFIX);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            L.e(TAG, "dump crash info failed");
        }
    }

    @SuppressWarnings("deprecation")
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);


        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print('_');
        pw.println(SDK_INT);


        pw.print("Vendor: ");
        pw.println(Build.MODEL);


        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }


    private void uploadExceptionToServer() {
        /*Upload Exception SMS To Your Web Server*/
    }
}
