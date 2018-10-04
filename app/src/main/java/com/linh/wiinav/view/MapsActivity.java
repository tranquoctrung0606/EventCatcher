package com.linh.wiinav.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.linh.wiinav.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity
        extends AppCompatActivity
    implements OnMapReadyCallback,
               GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = "MapsActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168), new LatLng(71,136)
    );

    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView iwMyLocation;
    private FloatingActionButton mFloatingActionButton;
    private NavigationView navigationView;

    //vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GeoDataClient mGeoDataClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText = findViewById(R.id.input_search);
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

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void addControls() {
        mSearchText = findViewById(R.id.input_search);
        iwMyLocation = findViewById(R.id.iwMyLocation);
        mFloatingActionButton = findViewById(R.id.floatingActionButton);
        navigationView= (NavigationView) findViewById(R.id.nav_view);
    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult)
    {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
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
        addNewMarker(mMap,"Hư xe","Tôi bị hư xe",marker1);
        addNewMarker(mMap,"Hết xăng","Tôi bị gãy chân, không có xe",marker2);
        addNewMarker(mMap,"Cần quá giang","Tôi bị lủng lốp",marker3);
        init();
    }

    private void init(){
        Log.d(TAG, "init: initializing");
        mGeoDataClient = Places.getGeoDataClient(this);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,
                                                                 mGeoDataClient,
                                                                 LAT_LNG_BOUNDS,
                                                                 null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction( TextView textView,int actionId, KeyEvent keyEvent)
            {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    //searching
                    geoLocate();
                }
                return false;
            }
        });
    }

    private void geoLocate()
    {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();
        Log.d(TAG, "geoLocate: ");
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString,1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException", e);
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a locaiton: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                       address.getAddressLine(0));
        }
    }

    private void getDeviceLocation()
    {
        Log.d(TAG, "getDeviceLocation: getting device current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull final Task task)
                        {
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

    private void moveCamera(LatLng latLng, float zoom, String title)
    {
        Log.d(TAG, "moveCamera: moving camera to: lat: " + latLng.latitude + ", lng " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        if(!title.equals("My location")){
            mMap.addMarker(options);
        }
    }

    private void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Log.d(TAG, "initMap: initializing map");
        mapFragment.getMapAsync(this);
    }

    public void getLocationPermission(){
        String[] permissions = {FINE_LOCATION, COURSE_LOCATION};
        Log.d(TAG, "getLocationPermission: getting location permision");
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "getLocationPermission: fine location permission granted");
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                Log.d(TAG, "getLocationPermission: course location permission granted!");
                initMap();
            }else {
                Log.d(TAG, "getLocationPermission: request course permision ");
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
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
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
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
    public void addNewMarker(GoogleMap googleMap, String problem,String description,LatLng position){
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().title(problem).snippet(description).position(position).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_problem)));
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//
//        if (id == R.id.nav_place) {
//            Toast.makeText(MapsActivity.this, "got it", Toast.LENGTH_LONG).show();
//        } else if (id == R.id.nav_contribution) {
//
//        } else if (id == R.id.nav_profile) {
//
//        } else if (id == R.id.nav_contact) {
//
//        } else if (id == R.id.nav_setting) {
//
//        } else if (id == R.id.nav_feedback) {
//
//        } else if (id == R.id.nav_term) {
//
//        } else if (id == R.id.nav_logout) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
