package com.training.android.undivided.NavigationMode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.training.android.undivided.MainActivity;
import com.training.android.undivided.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Navigation extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    protected static final int REQUEST_CHECK_SETTINGS = 2;

    private static float angle;
    ArrayList<LatLng> mPathPolygonPoints = null;
    int mIndexCurrentPoint = 0;
    Bitmap mMarkerIcon;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private LinearLayout mSpeedometer;
    private TextView mtvSpeed, mtvTotalDistance, mtvDestination, mtvPlace;

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        showSearch();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mSpeedometer = findViewById(R.id.Speedometer);
        mtvSpeed = findViewById(R.id.tvSpeedometer);
        mtvTotalDistance = findViewById(R.id.tvTotalDistance);
        mtvDestination = findViewById(R.id.tvDestination);
        mtvPlace = findViewById(R.id.tvPlaceName);

        mSpeedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Navigation.this, "Gwapo", Toast.LENGTH_SHORT).show();
            }
        });
//        markerIconBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.user_location);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_navigation);
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
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        settingsRequest();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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

            mLastLocation = location;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            // KM/S
            double currentSpeed = location.getSpeed() * 3.6;
            int speed = (int) currentSpeed;
            mtvSpeed.setText(String.valueOf(speed));

            if (mCurrLocationMarker == null) {
                mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Your Location"));

            } else {
                mCurrLocationMarker.setPosition(latLng);
            }


            CameraPosition cameraPosition = new CameraPosition.Builder().
                    target(latLng).
                    zoom(16).
                    bearing(location.getBearing()).
                    build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


//            locationUpdate(location);
//            if (mLastLocation != null) {
//                double bearing = angleFromCoordinate(mLastLocation.getLatitude(), mLastLocation.getLongitude(), location.getLatitude(), location.getLongitude());
//                changeMarkerPosition(bearing);
//            }
//            mLastLocation = location;

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
                            Place place = PlaceAutocomplete.getPlace(this, data);
                            LatLng startLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                            //Adds Marker to the users destination
                            mMap.addMarker(new MarkerOptions()
                                    .position(place.getLatLng())
                                    .title(place.getAddress().toString()));

                            //Adds Marker to the users first Location
                            mMap.addMarker(new MarkerOptions()
                                    .position(startLatLng)
                                    .title("Starting Location")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                            //Get The Distance from starting position to Destination
                            //NAA ra sa API makuha ang total distance

                            mtvDestination.setText(place.getAddress());
                            mtvPlace.setText(place.getName());

                            String url = getDirectionsUrl(startLatLng, place.getLatLng());
                            DownloadTask downloadTask = new DownloadTask();
                            downloadTask.execute(url);
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

    /**
     * Map Navigation
     */

//    private void locationUpdate(Location location) {
//        LatLng latLng = new LatLng((location.getLatitude()), (location.getLongitude()));
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
////        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_right));
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
     * JSON ROUTE PARSING
     */

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
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                mPathPolygonPoints = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    mPathPolygonPoints.add(position);

                    // Drawing polyline in the Google Map for the i-th route
                    mMap.addPolyline(new PolylineOptions()
                            .addAll(mPathPolygonPoints)
                            .width(10)
                            .geodesic(true)
                            .clickable(true)
                            .color(R.color.colorPrimary));

                }
            }

        }
    }

}
