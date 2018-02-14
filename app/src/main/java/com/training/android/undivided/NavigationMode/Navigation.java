package com.training.android.undivided.NavigationMode;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.VectorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ag.floatingactionmenu.OptionsFabLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.Group.Model.ContactsModel;
import com.training.android.undivided.MainActivity;
import com.training.android.undivided.R;
import com.training.android.undivided.Speaker;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Navigation extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private static final int PROXIMITY_RADIUS = 10000;
    private static final int CHECK_CODE = 3;

    ArrayList<LatLng> mPathPolygonPoints = null;
    ArrayList<ContactsModel> cmodel;
    Polyline polyline;
    boolean start = false;
    String type = "";
    Handler handler = new Handler();
    DecimalFormat form = new DecimalFormat("0.00");
    DistanceTraveledService mDistanceTraveledService;
    boolean bound = false;
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DistanceTraveledService.DistanceTravelBinder distanceTravelBinder = (DistanceTraveledService.DistanceTravelBinder) service;
            mDistanceTraveledService = distanceTravelBinder.getBinder();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };
    private double tdistance, dremaining;
    private int routeCounter = -1;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mLastLatLng, mNextLatLng, DestLatlng;
    private Location mLastLocation;
    private Marker mCurrLocationMarker, DestMarker, SpotsMarker;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private Button mBtnChangeRoute, mbtnSetMarker;
    private RelativeLayout mBtnStartDriving;
    private LinearLayout mSpeedometer;
    private TextView mtvSpeed, mtvTotalDistance, mtvDestination, mtvPlace;
    private OptionsFabLayout famServices;
    private ArrayList<Marker> mMarkers = new ArrayList<>();
    private DBHandler dbHandler;
    private Speaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHandler = new DBHandler(this);
        cmodel = new ArrayList<>();

        final String message = dbHandler.getMessage("Emergency").getGroupMessage();

        setContentView(R.layout.activity_navigation);
        showSearch();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mSpeedometer = findViewById(R.id.Speedometer);
        mBtnStartDriving = findViewById(R.id.btnStartDriving);
        mbtnSetMarker = findViewById(R.id.btnSetMarker);
        mBtnChangeRoute = findViewById(R.id.btnChangeRoute);
        mtvSpeed = findViewById(R.id.tvSpeedometer);
        mtvTotalDistance = findViewById(R.id.tvTotalDistance);
        mtvDestination = findViewById(R.id.tvDestination);
        mtvPlace = findViewById(R.id.tvPlaceName);
        famServices = findViewById(R.id.fab_services);

        checkTTS();

        mbtnSetMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpeedometer.setClickable(true);
                mSpeedometer.setFocusable(true);
                DestMarker.setDraggable(false);
                mbtnSetMarker.setVisibility(View.GONE);
                drawCircle(DestMarker.getPosition());
                LatlngBounds(mLastLatLng, DestMarker.getPosition());

                String url = getDirectionsUrl(mLastLatLng, DestMarker.getPosition());
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }
        });

        mBtnStartDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                start = true;
                mBtnChangeRoute.setVisibility(View.INVISIBLE);
                mBtnStartDriving.setVisibility(View.INVISIBLE);
            }
        });

        mSpeedometer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                cmodel = dbHandler.getEmergencyContacts();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;

                        while (i != cmodel.size()) {
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(cmodel.get(i).getContactNumber(), null, message, null, null);

                                Toast.makeText(Navigation.this, "Sent!", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(Navigation.this, "Failed.", Toast.LENGTH_SHORT).show();
                            }
                            i++;
                        }
                    }
                });


                String phone = "+639234152360";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

                return true;
            }
        });

        famServices.setMiniFabsColors(R.color.colorPrimaryDark,
                R.color.colorPrimaryDark,
                R.color.colorPrimaryDark,
                R.color.colorPrimaryDark);

        famServices.setMainFabOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (famServices.isOptionsMenuOpened())
                    famServices.closeOptionsMenu();
            }
        });

        famServices.setMiniFabSelectedListener(new OptionsFabLayout.OnMiniFabSelectedListener() {
            @Override
            public void onMiniFabSelected(MenuItem fabItem) {
                switch (fabItem.getItemId()) {

                    case R.id.mHospital:
                        String Hospital = "hospital";
                        getServiceMarkers(Hospital);
                        famServices.closeOptionsMenu();
                        Toast.makeText(Navigation.this, "Nearby Hospitals", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.mPolice:
                        String Police = "police";
                        getServiceMarkers(Police);
                        famServices.closeOptionsMenu();
                        Toast.makeText(Navigation.this, "Nearby Police Stations", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.mRepair:
                        String repair = "car_repair";
                        getServiceMarkers(repair);
                        famServices.closeOptionsMenu();
                        Toast.makeText(Navigation.this, "Nearby Car Repair Shops", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.mTowing:
                        String Towing = "towing";
                        getServiceMarkers(Towing);
                        famServices.closeOptionsMenu();
                        Toast.makeText(Navigation.this, "Nearby Towing Services", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(true);
        //        mMap.setMapStyle(mapStyleOptions);


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.setMyLocationEnabled(false);
            }
        } else {
            buildGoogleApiClient();
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMyLocationEnabled(false);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });

    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        settingsRequest();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {

            mNextLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            /*
              GET SPEED
              */
            getSpeed(location);

            if (mNextLatLng != null && mLastLatLng != null && start) {
                CameraPosition cameraPosition = new CameraPosition.Builder().
                        target(mNextLatLng).
                        zoom(17).
                        bearing((float) bearingBetweenLocations(mLastLatLng, mNextLatLng)).
                        build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                if (mCurrLocationMarker == null) {
                    mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(mNextLatLng)
                            .title("Current Location")
                            .anchor(0.5f, 1f)
                            .icon(getBitmapDescriptor(R.drawable.ic_navigation)));
                } else {
                    animateMarker(mLastLatLng, mNextLatLng, false);
                    displayDistance();
                }
            }
            mLastLatLng = mNextLatLng;


            if (DestLatlng != null && mLastLatLng != null) {
                if (SphericalUtil.computeDistanceBetween(DestLatlng, mLastLatLng) < 50) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    speaker.allow(true);
                    speaker.speak("You have arrived at your destination");


                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                switch (resultCode) {
                    case RESULT_OK:

                        if (mLastLocation != null) {
                            mLastLocation.setLatitude(mLastLocation.getLatitude());
                            mLastLocation.setLongitude(mLastLocation.getLongitude());
                            Place place = PlaceAutocomplete.getPlace(this, data);
                            DestLatlng = place.getLatLng();

                            //Adds Marker to the users destination
                            DestMarker = mMap.addMarker(new MarkerOptions()
                                    .position(DestLatlng)
                                    .draggable(true));

                            CameraPosition cameraPosition = new CameraPosition.Builder().
                                    target(DestLatlng).
                                    zoom(16).
                                    build();

                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            //Adds Marker to the users first Location
                            mMap.addMarker(new MarkerOptions()
                                    .position(mLastLatLng)
                                    .title("Starting Location")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                            mtvDestination.setText(place.getAddress());
                            mtvPlace.setText(place.getName());

                            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                @Override
                                public void onMarkerDragStart(Marker marker) {
                                    Toast.makeText(Navigation.this, "Place the marker on your desired location", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onMarkerDrag(Marker marker) {

                                }

                                @Override
                                public void onMarkerDragEnd(Marker marker) {
                                    getCompleteAddressString(marker.getPosition().latitude, marker.getPosition().longitude);
                                }
                            });

                        } else
                            Toast.makeText(this, "Unable to locate your current location", Toast.LENGTH_SHORT).show();
                        break;

                    case PlaceAutocomplete.RESULT_ERROR:
                        Status status = PlaceAutocomplete.getStatus(this, data);
                        Log.i("TAG", status.getStatusMessage());

                        Toast.makeText(this, "Error Retrieving Location", Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_CANCELED:
                        // The user canceled the operation.
                        startActivity(new Intent(Navigation.this, MainActivity.class));
                        break;
                }
                break;

            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Toast.makeText(this, "GPS Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        settingsRequest();//keep asking if imp or do whatever
                        break;
                }
                break;

            case CHECK_CODE:
                if (resultCode == android.speech.tts.TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    speaker = new Speaker(this);
                } else {
                    Intent install = new Intent();
                    install.setAction(android.speech.tts.TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(install);
                }
                break;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public void showSearch() {
        try {
            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(Place.TYPE_COUNTRY)
                    .setCountry("PH")
                    .build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(autocompleteFilter)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.e("Error", e.getMessage());
        }
    }

    public void settingsRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        final PendingResult<LocationSettingsResult> pendingResult =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(Navigation.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable vectorDrawable = (VectorDrawable) getDrawable(id);

            int h = vectorDrawable.getIntrinsicHeight();
            int w = vectorDrawable.getIntrinsicWidth();

            vectorDrawable.setBounds(0, 0, w, h);

            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            vectorDrawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bm);

        } else {
            return BitmapDescriptorFactory.fromResource(id);
        }
    }

    /**
     * Map Navigation
     */

    private void checkTTS() {
        Intent check = new Intent();
        check.setAction(android.speech.tts.TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    private void getServiceMarkers(String stype) {
        Log.d("onClick", "Button is Clicked");

        if (mMarkers != null && SpotsMarker != null) {
            removeMarkers();
        }
        String url = getUrl(DestLatlng.latitude, DestLatlng.longitude, stype);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);

    }

    private void drawCircle(LatLng point) {

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        circleOptions.visible(false);

        // Radius of the circle
        circleOptions.radius(50);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);
    }

    public void LatlngBounds(LatLng startPosition, LatLng DestPosition) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startPosition);
        builder.include(DestPosition);

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (height * 0.15);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        mMap.animateCamera(cu);

    }

    public void getSpeed(Location location) {
        double currentSpeed = location.getSpeed() * 3.6;
        int speed = (int) currentSpeed;
        mtvSpeed.setText(String.valueOf(speed));
    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;

    }

    public void rotateMarker(final Marker marker, final float toRotation, final float st) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = st;
        final long duration = 1555;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;

                marker.setRotation(-rot > 180 ? rot / 2 : rot);
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    public void animateMarker(final LatLng startPosition, final LatLng toPosition,
                              final boolean hideMarker) {


        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();

        final long duration = 1000;
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startPosition.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startPosition.latitude;

                mCurrLocationMarker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        mCurrLocationMarker.setVisible(false);
                    } else {
                        mCurrLocationMarker.setVisible(true);
                    }
                }
            }
        });
    }

    private void getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);

                String Address = returnedAddress.getAddressLine(0);
                String PlaceName = returnedAddress.getFeatureName();

                mtvDestination.setText(Address);
                mtvPlace.setText(PlaceName);

            } else {
                Log.w("My Current location address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current location address", "Canont get Address!");
        }
    }

    private void displayDistance() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double distance = 0;
                if (mDistanceTraveledService != null) {
                    distance = mDistanceTraveledService.getDistanceTraveled();
                    dremaining = tdistance - distance;
                    if (dremaining < 1000)
                        mtvTotalDistance.setText(form.format(dremaining) + " m");
                    else
                        mtvTotalDistance.setText(form.format(dremaining) + " Km");

                    if (dremaining < 0)
                        mtvTotalDistance.setText("0 m");
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

//    private void locationUpdate(Location location) {
//        LatLng latLng = new LatLng((location.getLatitude()), (location.getLongitude()));
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.compass));
//
//        if (mCurrLocationMarker == null) {
//            mCurrLocationMarker = mMap.addMarker(markerOptions);
//        }else
//        {
//            mCurrLocationMarker.setPosition(latLng);
//        }
//
//        CameraPosition position = CameraPosition.builder()
//                .target(new LatLng(location.getLatitude(), location.getLongitude()))
//                .zoom(18)
//                .build();
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
//    }
//
//    private void changeMarkerPosition(double position) {
//        float direction = (float) position;
//        Log.e("LocationBearing", "" + direction);
//
//        if (direction == 360.0) {
//            //default
//            mCurrLocationMarker.setRotation(angle);
//        } else {
//            mCurrLocationMarker.setRotation(direction);
//            angle = direction;
//        }
//    }
//
//    private double angleFromCoordinate(double lat1, double long1, double lat2,
//                                       double long2) {
//        double dLon = (long2 - long1);
//
//        double y = Math.sin(dLon) * Math.cos(lat2);
//        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
//                * Math.cos(lat2) * Math.cos(dLon);
//
//        double brng = Math.atan2(x, y);
//
//        brng = Math.toDegrees(brng);
//        brng = (brng + 360) % 360;
//        brng = 360 - brng;
//        return brng;
//    }


//    private void animateCarMove(final Marker marker, final LatLng beginLatLng, final LatLng endLatLng, final long duration) {
//        final Handler handler = new Handler();
//        final long startTime = SystemClock.uptimeMillis();
//
//        final Interpolator interpolator = new LinearInterpolator();
//
//        // set car bearing for current part of path
//        float angleDeg = (float) (180 * getAngle(beginLatLng, endLatLng) / Math.PI);
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angleDeg);
//        marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true)));
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                // calculate phase of animation
//                long elapsed = SystemClock.uptimeMillis() - startTime;
//                float t = interpolator.getInterpolation((float) elapsed / duration);
//                // calculate new position for marker
//                double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
//                double lngDelta = endLatLng.longitude - beginLatLng.longitude;
//
//                if (Math.abs(lngDelta) > 180) {
//                    lngDelta -= Math.signum(lngDelta) * 360;
//                }
//                double lng = lngDelta * t + beginLatLng.longitude;
//
//                marker.setPosition(new LatLng(lat, lng));
//
//                // if not end of line segment of path
//                if (t < 1.0) {
//                    // call next marker position
//                    handler.postDelayed(this, 16);
//                } else {
//                    // call turn animation
//                    nextTurnAnimation();
//                }
//            }
//        });
//    }
//
//    private double getAngle(LatLng beginLatLng, LatLng endLatLng) {
//        double f1 = Math.PI * beginLatLng.latitude / 180;
//        double f2 = Math.PI * endLatLng.latitude / 180;
//        double dl = Math.PI * (endLatLng.longitude - beginLatLng.longitude) / 180;
//        return Math.atan2(Math.sin(dl) * Math.cos(f2), Math.cos(f1) * Math.sin(f2) - Math.sin(f1) * Math.cos(f2) * Math.cos(dl));
//    }
//
//    private void nextTurnAnimation() {
//        mIndexCurrentPoint++;
//
//        if (mIndexCurrentPoint < mPathPolygonPoints.size() - 1) {
//            LatLng prevLatLng = mPathPolygonPoints.get(mIndexCurrentPoint - 1);
//            LatLng currLatLng = mPathPolygonPoints.get(mIndexCurrentPoint);
//            LatLng nextLatLng = mPathPolygonPoints.get(mIndexCurrentPoint + 1);
//
//            float beginAngle = (float) (180 * getAngle(prevLatLng, currLatLng) / Math.PI);
//            float endAngle = (float) (180 * getAngle(currLatLng, nextLatLng) / Math.PI);
//
//            animateCarTurn(mCurrLocationMarker, beginAngle, endAngle, 1);
//        }
//    }
//
//    private void animateCarTurn(final Marker marker, final float startAngle, final float endAngle, final long duration) {
//        final Handler handler = new Handler();
//        final long startTime = SystemClock.uptimeMillis();
//        final Interpolator interpolator = new LinearInterpolator();
//
//        final float dAndgle = endAngle - startAngle;
//
//        Matrix matrix = new Matrix();
//        matrix.postRotate(startAngle);
//        Bitmap rotatedBitmap = Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true);
//        marker.setIcon(BitmapDescriptorFactory.fromBitmap(rotatedBitmap));
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//                long elapsed = SystemClock.uptimeMillis() - startTime;
//                float t = interpolator.getInterpolation((float) elapsed / duration);
//
//                Matrix m = new Matrix();
//                m.postRotate(startAngle + dAndgle * t);
//                marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), m, true)));
//
//                if (t < 1.0) {
//                    handler.postDelayed(this, 16);
//                } else {
//                    nextMoveAnimation();
//                }
//            }
//        });
//    }
//
//    private void nextMoveAnimation() {
//        if (mIndexCurrentPoint < mPathPolygonPoints.size() - 1) {
//            animateCarMove(mCurrLocationMarker, mPathPolygonPoints.get(mIndexCurrentPoint), mPathPolygonPoints.get(mIndexCurrentPoint + 1), 1);
//        }
//    }


    /**
     * Activity Lifecycle
     */

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, DistanceTraveledService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        if (bound) {
            unbindService(mServiceConnection);
            bound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * JSON ROUTE PARSING
     */

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        type = nearbyPlace;
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + getString(R.string.google_maps_key));
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        //Units: metric
        String unit = "units=metric";

        // Alternate Routes
        String route = "alternatives=true";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + unit + "&" + route;

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        Log.i("TAG", url);
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.e("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void removeMarkers() {
        for (Marker marker : mMarkers) {
            marker.remove();
        }
        mMarkers.clear();

    }

    /**
     * A class to download data from Google Directions URL
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Directions in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(final List<List<HashMap<String, String>>> result) {

            mBtnChangeRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    routeCounter++;
                    mPathPolygonPoints = new ArrayList<>();

                    if (polyline != null) {
                        polyline.remove();
                    }

                    // Fetching i-th route
                    try {
                        List<HashMap<String, String>> path = result.get(routeCounter);

                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            if (j == 0) {    // Get distance from the list
                                tdistance = Double.valueOf(point.get("distance"));
                                continue;
                            }
//
//                        else if (j == 3) {
//                            sDist = point.get("sdist");
//                            continue;
//                        } else if (j == 4) {
//                            slat = Double.parseDouble(point.get("sLat"));
//                            continue;
//                        } else if (j == 5) {
//                            slng = Double.parseDouble(point.get("sLat"));
//                            continue;
//                        }
//                        LatLng sPos = new LatLng(slat, slng);
//
//                        mMap.addMarker(new MarkerOptions().
//                                position(sPos).
//                                title(sDist));

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            mPathPolygonPoints.add(position);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        Log.e("Route", "Route", e.getCause());
                    }

                    // Drawing polyline in the Google Map for the i-th route
                    polyline = mMap.addPolyline(new PolylineOptions()
                            .addAll(mPathPolygonPoints)
                            .width(10)
                            .geodesic(true)
                            .clickable(true)
                            .color(Color.GREEN));

                    mtvTotalDistance.setText(tdistance / 1000 + " Km");

                    if (routeCounter + 1 == result.size()) {
                        routeCounter = -1;
                    }
                }
            });

            mBtnChangeRoute.performClick();
        }
    }

    public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

        String googlePlacesData;
        GoogleMap mMap;
        String url;

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d("GetNearbyPlacesData", "doInBackground entered");
                mMap = (GoogleMap) params[0];
                url = (String) params[1];
                googlePlacesData = downloadUrl(url);
                Log.d("GooglePlacesReadTask", "doInBackground Exit");
            } catch (Exception e) {
                Log.d("GooglePlacesReadTask", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("GooglePlacesReadTask", "onPostExecute Entered");
            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList = dataParser.parse(result);
            ShowNearbyPlaces(nearbyPlacesList);
            Log.d("GooglePlacesReadTask", "onPostExecute Exit");
        }

        private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
            for (int i = 0; i < nearbyPlacesList.size(); i++) {
                Log.d("onPostExecute", "Entered into showing locations");

                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                LatLng latLng = new LatLng(lat, lng);

                SpotsMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(placeName)
                        .snippet(vicinity));


                switch (type) {
                    case "hospital":
                        SpotsMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        break;
                    case "police":
                        SpotsMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        break;
                    case "car_repair":
                        SpotsMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                        break;
                }

                mMarkers.add(SpotsMarker);
            }
        }
    }
}
