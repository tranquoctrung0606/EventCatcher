package com.linh.wiinav.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linh.wiinav.R;
import com.linh.wiinav.InfoProblemReportActivity;
import com.linh.wiinav.adapters.PlaceAutocompleteAdapter;
import com.linh.wiinav.models.Comment;
import com.linh.wiinav.models.PlaceInfo;
import com.linh.wiinav.models.ReportedData;
import com.linh.wiinav.models.Route;
import com.linh.wiinav.modules.DirectionFinder;
import com.linh.wiinav.modules.DirectionFinderListener;
import com.linh.wiinav.models.User;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity
        extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnInfoWindowClickListener,
        DirectionFinderListener
{
    private static final String TAG = "MapsActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );

    private boolean showHide2 = false;
    private boolean isDirectionPressed = false;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView iwMyLocation;
    private ImageView iwSearch, iwDirection;

    private FloatingActionButton mFloatingActionButton, fab_maptype, fab_satellitetype, fab_roadtype ;
    private NavigationView navigationView;

    //vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GeoDataClient mGeoDataClient;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
    private SharedPreferences mPreferences;

    private PlaceInfo mPlace;

    public static final String TITLE = "title";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mPreferences = getSharedPreferences("location", Context.MODE_PRIVATE);

        getLocationPermission();
        addControls();
        addEvents();
    }

    private void addEvents() {
        navigationView.setNavigationItemSelectedListener(this);
        iwMyLocation.setOnClickListener((v) ->{
            moveToDeviceLocation();
        });

        mFloatingActionButton.setOnClickListener((v) -> {
            Intent intent = new Intent(MapsActivity.this, ReportActivity.class);
            startActivity(intent);
        });

        fab_maptype.setOnClickListener((v) -> {
            if(showHide2 == false)
                showFabLayout2();
            else
                hideFabLayout2();
            showHide2 = !showHide2;
        });

        fab_satellitetype.setOnClickListener((v) -> {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        });

        fab_roadtype.setOnClickListener((v) -> {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        });

        iwSearch.setOnClickListener((v) -> {

        });

        iwDirection.setOnClickListener((v) -> {
            if(!isDirectionPressed || !mSearchText.getText().toString().trim().isEmpty()) {
                isDirectionPressed = true;
                makeDirection();
            }
            else {
                mMap.clear();
                mSearchText.setText("");
                isDirectionPressed = false;
            }
        });

    }

    private void makeDirection()
    {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                 mFusedLocationProviderClient.getLastLocation()
                                             .addOnCompleteListener((task) -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Log.d(TAG, "onComplete: found location");
                        Location currentLocation = (Location) task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(),
                                              currentLocation.getLongitude()), DEFAULT_ZOOM,
                                   "My Location");
                        StringBuilder origin = new StringBuilder();
                        origin.append(currentLocation.getLatitude());
                        origin.append(",");
                        origin.append(currentLocation.getLongitude());
                        String destination = mSearchText.getText().toString();
                        sendRequest(origin.toString(), destination);
                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT)
                             .show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Sercurity Exeption" + e.getMessage());
        }
    }

    private void sendRequest(final String origin, final String destination) {
        Log.d(TAG, "sendRequest: sending...............");
        if(destination.isEmpty()) {
            return;
        }
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void addControls() {
        mSearchText = findViewById(R.id.input_search);
        iwMyLocation = findViewById(R.id.iwMyLocation);
        mFloatingActionButton = findViewById(R.id.floatingActionButton);
        iwDirection = findViewById(R.id.iwDirection);
        iwSearch = findViewById(R.id.iwSearch);
        navigationView = findViewById(R.id.nav_view);

        fab_maptype = findViewById(R.id.fab_maptype);
        fab_satellitetype = findViewById(R.id.fab_satellitetype);
        fab_roadtype = findViewById(R.id.fab_roadtype);
        hideFabLayout2();
        navigationView = findViewById(R.id.nav_view);
    }


    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is right here!");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            moveToDeviceLocation();
            if (ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setTrafficEnabled(true);
        }
        //addNewMarker(mMap, "problem", "Hư xe", "Tôi bị hư xe", marker1, null);
        //addNewMarker(mMap, "problem", "Hết xăng", "Tôi bị gãy chân, không có xe", marker2, null);
        //addNewMarker(mMap, "problem", "Cần quá giang", "Tôi bị lủng lốp", marker3, null);
        init();
    }

    private void init() {
        Log.d(TAG, "init: initializing");
        mGeoDataClient = Places.getGeoDataClient(this);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,
                mGeoDataClient,
                LAT_LNG_BOUNDS,
                null);

        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //searching
                    geoLocate();
                }
                return true;
            }
        });
    }

    private void hideKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");
        String searchString;
        List<Address> list = new ArrayList<>();

        searchString = mSearchText.getText().toString();
        Log.d(TAG, "geoLocate: ");
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException", e);
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a locaiton: " + address.toString());
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            moveCamera(latLng, DEFAULT_ZOOM,
                       address.getAddressLine(0));
        }
    }

    private void moveToDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting device current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.getLastLocation().addOnCompleteListener((task) -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Log.d(TAG, "onComplete: found location");
                        Location currentLocation = (Location) task.getResult();

                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString("LAT", String.valueOf(currentLocation.getLatitude()));
                        Log.i(TAG, "moveToDeviceLocation: " + currentLocation.getLatitude() );
                        editor.putString("LONG", String.valueOf(currentLocation.getLongitude()));
                        editor.commit();

                        moveCamera(new LatLng(currentLocation.getLatitude(),
                                              currentLocation.getLongitude()), DEFAULT_ZOOM,
                                   "My Location");
                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT)
                             .show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Sercurity Exeption" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String name) {
        Log.d(TAG, "moveCamera: moving camera to: lat: " + latLng.latitude + ", lng " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(name)
                .snippet(name);

        if (!name.equals("My Location")) {
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

    public void addNewMarker(GoogleMap googleMap,String type, String problem, String description, LatLng position, ReportedData reportedData) {
        mMap = googleMap;
        MarkerOptions markerOptions = new MarkerOptions().title(problem)
                .snippet(description).position(position).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_problem));

        //Set reportedData --> This is temporary
//        ReportedData reportedData = reportedData;
        reportedData = new ReportedData();

        //Set type of problem
        reportedData.setType(type);
        Comment cmt1 = new Comment("cmt01",null,"Where Are U?","03-18-2018");
        Comment cmt2 = new Comment("cmt02",null,"Anybody help u?","03-18-2018");
        Comment cmt3 = new Comment("cmt03",null,"I'm on my way","03-18-2018");
        ArrayList<Comment> listComments = new ArrayList<>();
        listComments.add(cmt1);
        listComments.add(cmt2);
        listComments.add(cmt3);
        reportedData.setComments(listComments);
//        reportedData.getComments().add(cmt1);
//        reportedData.getComments().add(cmt2);
//        reportedData.getComments().add(cmt3);
        //Set problem's reporter -->Teporary --> Needn't set user here if reported data already has user information
        User reporter = new User();
        reportedData.setReporter(reporter);
        //Set problem's information for InfoWindowData -->This is temporary (static data)
        reportedData.setTitle(markerOptions.getTitle());
        reportedData.setDescription(markerOptions.getSnippet());

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);

        mMap.setInfoWindowAdapter(customInfoWindow);
        Marker marker = mMap.addMarker(markerOptions);

        marker.setTag(reportedData);
        marker.showInfoWindow();
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTag()!=null)
        {
            ReportedData reportedData = (ReportedData) marker.getTag();
            Intent intent = new Intent(MapsActivity.this, InfoProblemReportActivity.class);
            intent.putExtra("reportedData",reportedData);
            startActivity(intent);
        }
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

    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_place:
                break;
            case R.id.nav_contribution:
                break;
            case R.id.nav_profile:
                Intent userProfileActivity = new Intent(getApplicationContext(), UserprofileActivity.class);
                displayNextScreen(userProfileActivity);
                break;
            case R.id.nav_contact:
                break;
            case R.id.nav_setting:
                Intent settingActivity = new Intent(getApplicationContext(), SettingActivity.class);
                displayNextScreen(settingActivity);
                break;
            case R.id.nav_feedback:
                Intent feedbackActivity = new Intent(getApplicationContext(), FeedbackActivity.class);
                displayNextScreen(feedbackActivity);
                break;
            case R.id.nav_term:
                Intent termActivity = new Intent(getApplicationContext(), TermActivity.class);
                displayNextScreen(termActivity);
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFabLayout2()
    {
        fab_satellitetype.show();
        fab_roadtype.show();
    }
    private void hideFabLayout2()
    {
        fab_satellitetype.hide();
        fab_roadtype.hide();
    }

    protected void displayNextScreen(final Intent nextScreen)
    {
        startActivity(nextScreen);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onDirectionFinderStart() {
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_start))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_end))
                    .title(route.endAddress)
                    .position(route.endLocation)
                                                  .snippet("Duration: ")
            ));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
            break;
        }
    }

    AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id)
        {
            hideKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            mGeoDataClient.getPlaceById(placeId).addOnCompleteListener((task -> {
                if (task.isSuccessful()) {
                    PlaceBufferResponse places = task.getResult();
                    try {
                        Place myPlace = places.get(0);

                        mPlace = new PlaceInfo();

                        mPlace.setId(myPlace.getId());
                        mPlace.setAddress(myPlace.getAddress().toString());
                        //mPlace.setAttribution(myPlace.getAttributions().toString());
                        mPlace.setName(myPlace.getName().toString());
                        mPlace.setPhoneNumber(myPlace.getPhoneNumber().toString());
                        mPlace.setRating(myPlace.getRating());
                        mPlace.setWebsiteUri(myPlace.getWebsiteUri());
                        mPlace.setLatLng(myPlace.getLatLng());

                        Log.i(TAG, "Place found: " + myPlace.getName());
                        moveCamera(new LatLng(myPlace.getViewport().getCenter().latitude,
                                              myPlace.getViewport().getCenter().longitude),
                                   DEFAULT_ZOOM, mPlace.getName());
                        places.release();
                    } catch (NullPointerException e) {
                        Log.e(TAG, "onItemClick: ", e);
                    }
                } else {
                    Log.e(TAG, "Place not found.");
                }
            }));
        }
    };


}
