package com.training.android.undivided.BackgroundService;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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

import com.google.android.gms.maps.LocationSource;
import com.training.android.undivided.MainActivity;

/**
 * Created by Maouusama on 7/20/2017.
 */

public class BackgroundService extends Service implements LocationSource.OnLocationChangedListener {
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
        super.onDestroy();
        Toast.makeText(this, "Destroyed Service", Toast.LENGTH_SHORT).show();
    }

    public void launchApp(){
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

    @Override
    public void onLocationChanged(Location location) {
        if (location!=null){

            double currentSpeed = location.getSpeed() * 3.6;
            int speed = (int) currentSpeed;

            if(speed>=20){
                launchApp();
                Toast.makeText(this, "App is Starting!", Toast.LENGTH_SHORT).show();
            }

        }
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


        }
    }
}
