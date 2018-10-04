package com.linh.wiinav.view.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.linh.wiinav.R;
import com.linh.wiinav.view.InfoProblemReportActivity;
import com.linh.wiinav.view.adapter.PlaceAutocompleteAdapter;
import com.linh.wiinav.view.model.ReportedData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity
        extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = "MapsActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );

    private LatLng oriLatLng;
    private LatLng desLatLng;

    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView iwMyLocation;
    private FloatingActionButton mFloatingActionButton, fab_report1, fab_report2, fab_report3, fab_report4;
    private NavigationView navigationView;
    private boolean showHide = false;
    private RelativeLayout rlDirection;
    private ImageView iwSearch1, iwSearch2, iwDirection;
    private AutoCompleteTextView mSearchDestinationText;


    //vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GeoDataClient mGeoDataClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLocationPermission();
        addControls();
        addEvents();
    }

    private void addEvents() {
        navigationView.setNavigationItemSelectedListener(this);
        iwMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
        hideFabLayout();
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showHide == false) {
                    showFabLayout();
                    showHide = true;
                } else {
                    hideFabLayout();
                    showHide = false;
                }
            }
        });
        rlDirection.setVisibility(View.GONE);
        iwDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rlDirection.isShown()) {
                    rlDirection.setVisibility(View.VISIBLE);

                } else {
                    rlDirection.setVisibility(View.GONE);

                }
            }
        });

        mSearchDestinationText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //searching
                    geoLocate(1);
                    hideKeyboard();
                }

                return false;
            }
        });
    }

    private void direction() {
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(oriLatLng, desLatLng);
    }

    private void addControls() {
        mSearchText = findViewById(R.id.input_search);
        iwMyLocation = findViewById(R.id.iwMyLocation);
        mFloatingActionButton = findViewById(R.id.floatingActionButton);
        fab_report1 = findViewById(R.id.fab_report1);
        fab_report2 = findViewById(R.id.fab_report2);
        fab_report3 = findViewById(R.id.fab_report3);
        fab_report4 = findViewById(R.id.fab_report4);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        rlDirection = findViewById(R.id.relLayout2);
        iwDirection = findViewById(R.id.iwDirection);
        iwSearch1 = findViewById(R.id.iwSearch1);
        iwSearch2 = findViewById(R.id.iwSearch2);
        mSearchDestinationText = findViewById(R.id.input_search_destination);
        navigationView = findViewById(R.id.nav_view);
    }


    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Toast.makeText(MapsActivity.this, "Map is read ", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is right here!");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        addNewMarker(mMap, "problem", "Hư xe", "Tôi bị hư xe", marker1, null);
        addNewMarker(mMap, "problem", "Hết xăng", "Tôi bị gãy chân, không có xe", marker2, null);
        addNewMarker(mMap, "problem", "Cần quá giang", "Tôi bị lủng lốp", marker3, null);
        init();
    }

    private void init() {
        Log.d(TAG, "init: initializing");
        mGeoDataClient = Places.getGeoDataClient(this);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,
                mGeoDataClient,
                LAT_LNG_BOUNDS,
                null);

        mSearchDestinationText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //searching
                    geoLocate(0);
                    hideKeyboard();
                }
                return false;
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(MapsActivity.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void geoLocate(int i) {
        Log.d(TAG, "geoLocate: geolocating");
        String searchString = "";
        switch (i) {
            case 0:
                searchString = mSearchText.getText().toString();
                break;
            case 1:
                searchString = mSearchDestinationText.getText().toString();
                break;
        }
        Log.d(TAG, "geoLocate: ");
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException", e);
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a locaiton: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            switch (i) {
                case 0:
                    oriLatLng = latLng;
                    break;
                case 1:
                    desLatLng = latLng;
                    direction();
                    break;
            }
            moveCamera(latLng, DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting device current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull final Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),
                                            currentLocation.getLongitude()), DEFAULT_ZOOM,
                                    "My location");
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Sercurity Exeption" + e.getMessage());
        }

    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving camera to: lat: " + latLng.latitude + ", lng " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        if (!title.equals("My location")) {
            mMap.addMarker(options);
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Log.d(TAG, "initMap: initializing map");
        mapFragment.getMapAsync(this);
    }

    public void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COURSE_LOCATION};
        Log.d(TAG, "getLocationPermission: getting location permision");
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermission: fine location permission granted");
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                Log.d(TAG, "getLocationPermission: course location permission granted!");
                initMap();
            } else {
                Log.d(TAG, "getLocationPermission: request course permision ");
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            Log.d(TAG, "getLocationPermission: request fine permision ");
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        Log.d(TAG, "onRequestPermissionsResult: called.");
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed.");
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Log.d(TAG, "onRequestPermissionsResult: permission granted.");
                    initMap();
                }
            }

        }
    }

    private static final LatLng marker1 = new LatLng(16.132669, 108.119502);
    private static final LatLng marker2 = new LatLng(15.996625, 108.258672);
    private static final LatLng marker3 = new LatLng(16.060654, 108.209443);

    public void addNewMarker(GoogleMap googleMap, String type, String problem, String description, LatLng position, ReportedData reportedData) {
        mMap = googleMap;
        MarkerOptions markerOptions = new MarkerOptions().title(problem)
                .snippet(description).position(position).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_problem));

        //Set reportedData --> This is temporary
//        ReportedData reportedData = reportedData;
        reportedData = new ReportedData();

        //Set type of problem
        reportedData.setType(type);

        //Set user's information for InfoWindowData -->This is temporary (static data)
        reportedData.setTitle(markerOptions.getTitle());
        reportedData.setSnippet(markerOptions.getSnippet());
        reportedData.setPhoneNumber("01288446176");

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        Marker marker = mMap.addMarker(markerOptions);
        mMap.setOnInfoWindowClickListener(this);
        marker.setTag(reportedData);
        marker.showInfoWindow();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this, InfoProblemReportActivity.class);
        ReportedData reportedData = (ReportedData) marker.getTag();
        intent.putExtra("reportedData",reportedData);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_place) {

        } else if (id == R.id.nav_contribution) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_setting) {
            Intent settingActivity = new Intent(getApplicationContext(), SettingActivity.class);
            displayNextScreen(settingActivity);
        } else if (id == R.id.nav_feedback) {
            Intent feedbackActivity = new Intent(getApplicationContext(), FeedbackActivity.class);
            displayNextScreen(feedbackActivity);
        } else if (id == R.id.nav_term) {
            Intent termActivity = new Intent(getApplicationContext(), TermActivity.class);
            displayNextScreen(termActivity);
        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFabLayout()
    {
        fab_report1.show();
        fab_report2.show();
        fab_report3.show();
        fab_report4.show();
    }

    private void hideFabLayout()
    {
        fab_report1.hide();
        fab_report2.hide();
        fab_report3.hide();
        fab_report4.hide();
    }

    protected void displayNextScreen(final Intent nextScreen)
    {
        startActivity(nextScreen);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
