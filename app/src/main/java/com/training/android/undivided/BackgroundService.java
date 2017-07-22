package com.training.android.undivided;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Maouusama on 7/20/2017.
 */

public class BackgroundService extends Service{
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    public void onCreate(){

        // to avoid cpu-blocking, handler for running the service
        HandlerThread thread = new HandlerThread("TestService", Process.THREAD_PRIORITY_BACKGROUND);

        // Starts the handler thread
        thread.start();

        mServiceLooper = thread.getLooper();

        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    public void onDestroy(){
//         DISABLED FOR NOW: ANNOYING AUTO START SINCE EVERYTIME THE SERVICE FINISHES IT DESTROYS AUTOMATICALLY

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void restartService(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Starting Service(onStartCommand)", Toast.LENGTH_SHORT).show();

        // call a new service handler. The service ID can be used to identify the service
        Message message = mServiceHandler.obtainMessage();
        message.arg1 = startId;
        mServiceHandler.sendMessage(message);
        Notification notif = new Notification();
        startForeground(1, notif);
        return START_STICKY;

    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        /*Intent reService = new Intent(getApplicationContext(), this.getClass());
        reService.setPackage(getPackageName());
        PendingIntent reServicePendingIntent = PendingIntent.getService(getApplicationContext(),
                1,reService,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000,
                reServicePendingIntent);

*/

        super.onTaskRemoved(rootIntent);
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

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    // Object responsible for
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Well calling mServiceHandler.sendMessage(message); from onStartCommand,
            // this method will be called.

            // Add your cpu-blocking activity here

            try {

                Thread.sleep(5000);
                showToast("Service is currently running (TEST#0001)");
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }



            showToast("Auto Starting UNDIVIDED, id: " + msg.arg1);
            restartService();

            stopSelf(msg.arg1);
        }
    }
}
