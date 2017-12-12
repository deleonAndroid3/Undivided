package com.training.android.undivided.BackgroundService;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.LocationSource;
import com.training.android.undivided.MainActivity;
import com.training.android.undivided.Settings;

import java.util.concurrent.TimeUnit;

/**
 * Created by Maouusama on 7/20/2017.
 */

public class BackgroundService extends Service implements LocationSource.OnLocationChangedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    boolean status;
    LocationManager locationManager;
    static double distance=0;
    private static final long INTERVAL = 1000*2;
    private static final long FASTEST_INTERVAL = 1000*2;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation,lStart, lEnd;
    private final IBinder mBinder = new BackgroundService.LocalBinder();



    public void onCreate(){
    Log.i("EXISTING", "THIS SERVICE HAS STARTED");
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
        stopSelf();
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
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        return mBinder;
    }

    public boolean onUnbind(Intent intent){
        stopLocationUpdates();
        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        lStart = lEnd = null;
        distance = 0;
        return super.onUnbind(intent);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        distance=0;
    }

    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if(lStart == null)
        {
            lStart = lEnd = mCurrentLocation;
        }
        else
            lEnd = mCurrentLocation;


        updateStatus();
    }

    private void updateStatus() {
        if(Settings.p==0) {
            distance = distance + (lStart.distanceTo(lEnd) / 1000.00);
            Settings.endTime = System.currentTimeMillis();
            long diff = Settings.endTime - Settings.startTime;
            diff = TimeUnit.MILLISECONDS.toMinutes(diff);

            lStart = lEnd;

            double speed = distance / (diff*60);


              if(speed>10)
            launchApp();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        } catch (SecurityException e){
            Log.i("SECURITY_EXCEPTION", "Security exception on connected");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    public class LocalBinder extends Binder {
        public BackgroundService getService(){
            return BackgroundService.this;
        }
    }
}
