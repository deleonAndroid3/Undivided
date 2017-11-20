package com.training.android.undivided.NavigationMode;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.exceptions.InvalidLatLngBoundsException;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.Constants;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.services.android.navigation.ui.v5.R.style.NavigationMapRoute;
import static com.mapbox.services.android.telemetry.location.LocationEnginePriority.HIGH_ACCURACY;

public class NavigationMode extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapLongClickListener, LocationEngineListener, Callback<DirectionsResponse> {

    private static final int CAMERA_ANIMATION_DURATION = 1000;


    private LocationLayerPlugin locationLayer;
    private LocationEngine locationEngine;
    private NavigationMapRoute mapRoute;
    private MapboxMap mapboxMap;
    private MapView mapView;

    private Button mbtnStart;
    private ProgressBar loading;
    private Marker currentMarker;
    private Position currentPosition;
    private Position destination;
    private DirectionsRoute route;

    private boolean locationFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_navigation_mode);

        mapView = findViewById(R.id.mapView);
        loading = findViewById(R.id.loading);
        mbtnStart = findViewById(R.id.btnStart);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    //LifeCycles
    @SuppressWarnings({"MissingPermission"})
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        if (locationLayer != null) {
            locationLayer.onStart();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (locationEngine != null) {
            locationEngine.addLocationEngineListener(this);
            if (!locationEngine.isConnected()) {
                locationEngine.activate();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationLayer != null) {
            locationLayer.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
            locationEngine.deactivate();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.mapboxMap.setOnMapLongClickListener(this);
        initLocationEngine();
        initLocationLayer();

    }

    @Override
    public void onMapLongClick(@NonNull LatLng point) {
        destination = Position.fromCoordinates(point.getLongitude(), point.getLatitude());
        mbtnStart.setEnabled(true);
        loading.setVisibility(View.VISIBLE);
        setCurrentMarkerPosition(point);
        if (currentPosition != null) {
            fetchRoute();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentPosition = Position.fromCoordinates(location.getLongitude(), location.getLatitude());
        onLocationFound(location);
    }

    @Override
    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        if (validRouteResponse(response)) {
            route = response.body().getRoutes().get(0);
            mbtnStart.setEnabled(true);
            initMapRoute();
            mapRoute.addRoute(route);
            boundCameraToRoute();
            hideLoading();
        }
    }

    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
        Log.e("TAG" , t.getMessage());
    }

    @SuppressWarnings({"MissingPermission"})
    private void initLocationEngine() {
        locationEngine = new LocationSource(this);
        locationEngine.setPriority(HIGH_ACCURACY);
        locationEngine.setInterval(0);
        locationEngine.setFastestInterval(1000);
        locationEngine.addLocationEngineListener(this);
        locationEngine.activate();

        if (locationEngine.getLastLocation() != null) {
            Location lastLocation = locationEngine.getLastLocation();
            currentPosition = Position.fromCoordinates(lastLocation.getLongitude(), lastLocation.getLatitude());
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void initLocationLayer() {
        locationLayer = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
        locationLayer.setLocationLayerEnabled(LocationLayerMode.COMPASS);
    }

    private void initMapRoute() {
        mapRoute = new NavigationMapRoute(null,mapView, mapboxMap, R.style.NavigationMapRoute, null);

    }

    private void fetchRoute() {
        NavigationRoute.builder()
                .accessToken(getString(R.string.access_token))
                .origin(currentPosition)
                .destination(destination)
                .build()
                .getRoute(this);
        loading.setVisibility(View.VISIBLE);
    }

    private void launchNavigationWithRoute() {
        if (route != null) {
            NavigationLauncher.startNavigation(this, route, null, false);
        }
    }

    private boolean validRouteResponse(Response<DirectionsResponse> response) {
        return response.body() != null
                && response.body().getRoutes() != null
                && response.body().getRoutes().size() > 0;
    }

    private void hideLoading() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.INVISIBLE);
        }
    }

    private void onLocationFound(Location location) {
        if (!locationFound) {
            animateCamera(new LatLng(location.getLatitude(), location.getLongitude()));
            locationFound = true;
            hideLoading();
        }
    }

    public void boundCameraToRoute() {
        if (route != null) {
            List<Position> routeCoords = LineString.fromPolyline(route.getGeometry(),
                    Constants.PRECISION_6).getCoordinates();
            List<LatLng> bboxPoints = new ArrayList<>();
            for (Position position : routeCoords) {
                bboxPoints.add(new LatLng(position.getLatitude(), position.getLongitude()));
            }
            if (bboxPoints.size() > 1) {
                try {
                    LatLngBounds bounds = new LatLngBounds.Builder().includes(bboxPoints).build();
                    animateCameraBbox(bounds, CAMERA_ANIMATION_DURATION, new int[]{50, 500, 50, 335});
                } catch (InvalidLatLngBoundsException exception) {
                    Toast.makeText(this, "Valid route not found.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void animateCameraBbox(LatLngBounds bounds, int animationTime, int[] padding) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,
                padding[0], padding[1], padding[2], padding[3]), animationTime);
    }

    private void animateCamera(LatLng point) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16), CAMERA_ANIMATION_DURATION);
    }

    private void setCurrentMarkerPosition(LatLng position) {
        if (position != null) {
            if (currentMarker == null) {
                MarkerOptions markerOptions = new MarkerOptions().position(position);
                currentMarker = mapboxMap.addMarker(markerOptions);
            } else {
                currentMarker.setPosition(position);
            }
        }
    }
}


//
//import java.util.List;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
//// classes needed to initialize map
//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.maps.MapView;
//
//// classes needed to add location layer
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import android.location.Location;
//import com.mapbox.mapboxsdk.geometry.LatLng;
//import android.support.annotation.NonNull;
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
//import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
//import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
//import com.mapbox.services.android.location.LostLocationEngine;
//import com.mapbox.services.android.telemetry.location.LocationEngine;
//import com.mapbox.services.android.telemetry.location.LocationEngineListener;
//import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
//import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
//import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
//
//// classes needed to add a marker
//import com.mapbox.mapboxsdk.annotations.Marker;
//import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
//
//// classes to calculate a route
//import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
//import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
//import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
//import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
//import com.mapbox.services.commons.models.Position;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import android.util.Log;
//
//// classes needed to launch navigation UI
//import android.view.View;
//import android.widget.Button;
//import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
//import com.training.android.undivided.R;
//
//
//public class NavigationMode extends AppCompatActivity implements LocationEngineListener, PermissionsListener {
//
//    private MapView mapView;
//
//    // variables for adding location layer
//    private MapboxMap map;
//    private PermissionsManager permissionsManager;
//    private LocationLayerPlugin locationPlugin;
//    private LocationEngine locationEngine;
//
//    // variables for adding a marker
//    private Marker destinationMarker;
//    private LatLng originCoord;
//    private LatLng destinationCoord;
//    private Location originLocation;
//
//    // variables for calculating and drawing a route
//    private Position originPosition;
//    private Position destinationPosition;
//    private DirectionsRoute currentRoute;
//    private static final String TAG = "DirectionsActivity";
//    private NavigationMapRoute navigationMapRoute;
//
//    private Button button;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Mapbox.getInstance(this, getString(R.string.access_token));
//        setContentView(R.layout.activity_navigation_mode);
//        mapView = findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//
//        // Add user location to the map
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(final MapboxMap mapboxMap) {
//                map = mapboxMap;
//                enableLocationPlugin();
//                originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
//
//                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(@NonNull LatLng point) {
//
//                        if (destinationMarker != null) {
//                            mapboxMap.removeMarker(destinationMarker);
//                        }
//
//                        destinationCoord = point;
//
//                        destinationMarker = mapboxMap.addMarker(new MarkerViewOptions()
//                                .position(destinationCoord)
//                        );
//                        destinationPosition = Position.fromCoordinates(destinationCoord.getLongitude(), destinationCoord.getLatitude());
//                        originPosition = Position.fromCoordinates(originCoord.getLongitude(), originCoord.getLatitude());
//                        getRoute(originPosition, destinationPosition);
//                        button.setEnabled(true);
////                        button.setBackgroundResource(R.color.mapboxBlue);
//                    };
//                });
//
//                button = findViewById(R.id.btnStart);
//                button.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        Position origin = originPosition;
//                        Position destination = destinationPosition;
//
//                        // Pass in your Amazon Polly pool id for speech synthesis using Amazon Polly
//                        // Set to null to use the default Android speech synthesizer
//                        String awsPoolId = null;
//
//                        boolean simulateRoute = true;
//
//                        // Call this method with Context from within an Activity
//                        NavigationLauncher.startNavigation(NavigationMode.this, origin, destination,
//                                awsPoolId, simulateRoute);
//                    }
//                });
//            };
//        });
//    }
//
//    private void getRoute(Position origin, Position destination) {
//        NavigationRoute.builder()
//                .accessToken(Mapbox.getAccessToken())
//                .origin(origin)
//                .destination(destination)
//                .build()
//                .getRoute(new Callback<DirectionsResponse>() {
//                    @Override
//                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//                        // You can get the generic HTTP info about the response
//                        Log.d(TAG, "Response code: " + response.code());
//                        if (response.body() == null) {
//                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
//                            return;
//                        } else if (response.body().getRoutes().size() < 1) {
//                            Log.e(TAG, "No routes found");
//                            return;
//                        }
//
//                        currentRoute = response.body().getRoutes().get(0);
//
//                        // Draw the route on the map
//                        if (navigationMapRoute != null) {
//                            navigationMapRoute.removeRoute();
//                        } else {
//                            navigationMapRoute = new NavigationMapRoute( mapView, map);
//                        }
//                        navigationMapRoute.addRoute(currentRoute);
//                    }
//
//                    @Override
//                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
//                        Log.e(TAG, "Error: " + throwable.getMessage());
//                    }
//                });
//    }
//
//    @SuppressWarnings( {"MissingPermission"})
//    private void enableLocationPlugin() {
//        // Check if permissions are enabled and if not request
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//            // Create an instance of LOST location engine
//            initializeLocationEngine();
//
//            locationPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
//            locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
//        } else {
//            permissionsManager = new PermissionsManager(this);
//            permissionsManager.requestLocationPermissions(this);
//        }
//    }
//
//    @SuppressWarnings( {"MissingPermission"})
//    private void initializeLocationEngine() {
//        locationEngine = new LostLocationEngine(NavigationMode.this);
//        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
//        locationEngine.activate();
//
//        Location lastLocation = locationEngine.getLastLocation();
//        if (lastLocation != null) {
//            originLocation = lastLocation;
//            setCameraPosition(lastLocation);
//        } else {
//            locationEngine.addLocationEngineListener(this);
//        }
//    }
//
//    private void setCameraPosition(Location location) {
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(location.getLatitude(), location.getLongitude()), 13));
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            enableLocationPlugin();
//        } else {
//            finish();
//        }
//    }
//
//    @Override
//    @SuppressWarnings( {"MissingPermission"})
//    public void onConnected() {
//        locationEngine.requestLocationUpdates();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        if (location != null) {
//            originLocation = location;
//            setCameraPosition(location);
//            locationEngine.removeLocationEngineListener(this);
//        }
//    }
//
//    @Override
//    @SuppressWarnings( {"MissingPermission"})
//    protected void onStart() {
//        super.onStart();
//        if (locationEngine != null) {
//            locationEngine.requestLocationUpdates();
//        }
//        if (locationPlugin != null) {
//            locationPlugin.onStart();
//        }
//        mapView.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (locationEngine != null) {
//            locationEngine.removeLocationUpdates();
//        }
//        if (locationPlugin != null) {
//            locationPlugin.onStop();
//        }
//        mapView.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//        if (locationEngine != null) {
//            locationEngine.deactivate();
//        }
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//}
