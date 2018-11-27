package com.linh.wiinav.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.linh.wiinav.R;
import com.linh.wiinav.adapters.PlaceAutocompleteAdapter;
import com.linh.wiinav.models.AskHelp;
import com.linh.wiinav.models.PlaceInfo;
import com.linh.wiinav.models.Report;
import com.linh.wiinav.models.Route;
import com.linh.wiinav.modules.DirectionFinder;
import com.linh.wiinav.modules.DirectionFinderListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity
        extends BaseActivity
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


    private boolean isTrafficOn = false;
    private boolean mapType = false;
    private boolean isDirectionPressed = false;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Route> routes = new ArrayList<>();
    private List<AskHelp> askHelps = new ArrayList<>();
    private List<Report> reports = new ArrayList<>();

    //widgets
    private DrawerLayout mapLayout;
    private Dialog dialogSelectAction;
    private Dialog dialogInfoReport;
    private Dialog dialogDirectionDisplaySetting;
    private ImageView ivCloseSelectActionDialog;
    private ImageView ivAskHelpSelectActionDialog;
    private ImageView ivReportSelectActionDialog;
    private ImageView ivDirectionDisplaySetting;
    private CheckBox cbPetrol, cbRestaurant, cbHospital, cbPopularTourist;
    private TextView tvTitleInfoReport;
    private TextView tvDescriptionInfoReport;
    private TextView tvDownVoteInfoReport;
    private TextView tvUpVoteInfoReport;
    private TextView tvReporterNameInfoReport;
    private TextView tvRemainingTimeInfoReport;
    private TextView tvPostedDateInfoReport;
    private ImageView ivUpVoteInfoReport, ivDownVoteInfoReport;

    private AutoCompleteTextView mSearchText;
    private ImageView ivSearch, ivDirection, ivMyLocation;
    private TextView tvDuration;
    private TextView tvDistance;
    private Snackbar snackbar;
    private ImageView mFloatingActionButton, trafficStatusButton;

    private ImageView fabMapType;
    private NavigationView navigationView;

    //vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GeoDataClient mGeoDataClient;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private PlaceInfo mPlace;
    private Handler refreshHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        sharedPreferences = getSharedPreferences("location", Context.MODE_PRIVATE);

        getLocationPermission();
        addControls();
        addEvents();
    }

    @Override
    protected void addEvents() {
        navigationView.setNavigationItemSelectedListener(this);
        ivMyLocation.setOnClickListener((v) ->{
            moveToDeviceLocation();
        });

        mFloatingActionButton.setOnClickListener((v) -> {
            dialogSelectAction.show();
            moveToDeviceLocation();
        });

        fabMapType.setOnClickListener((v) -> {
            mapType = !mapType;
            // refresh map here
            if(mapType == false)
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            else
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        });

        trafficStatusButton.setOnClickListener((v) -> {
            isTrafficOn = !isTrafficOn;
            // refresh map here
            if(isTrafficOn == false)
                mMap.setTrafficEnabled(false);
            else
                mMap.setTrafficEnabled(true);


        });

        ivDirection.setOnClickListener((v) -> {
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

        //Add event for Selecting Action Dialog
        ivCloseSelectActionDialog.setOnClickListener((v -> dialogSelectAction.dismiss()));
        ivAskHelpSelectActionDialog.setOnClickListener((v ->{
            startActivity(new Intent(MapsActivity.this, AskHelpActivity.class));
        }));
        //
        ivReportSelectActionDialog.setOnClickListener((v -> {
            Intent reportActivity = new Intent(MapsActivity.this, ReportActivity.class);
            startActivity(reportActivity);
        }));
        //
        ivDirectionDisplaySetting.setOnClickListener(v -> dialogDirectionDisplaySetting.show());
        cbPetrol.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

            } else {

            }
        });
        cbHospital.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

            } else {

            }
        });
        cbRestaurant.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

            } else {

            }
        });
        cbPopularTourist.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

            } else {

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

    @Override
    protected void addControls() {
        mapLayout = findViewById(R.id.map_layout);

        mSearchText = findViewById(R.id.input_search);
        ivMyLocation = findViewById(R.id.iwMyLocation);
        mFloatingActionButton = findViewById(R.id.iv_action_button);
        ivDirection = findViewById(R.id.iwDirection);
        ivSearch = findViewById(R.id.iwSearch);
        navigationView = findViewById(R.id.nav_view);

        fabMapType = findViewById(R.id.iv_map_type);

        navigationView = findViewById(R.id.nav_view);

        //Dialog Select Action
        dialogSelectAction = new Dialog(this);
        dialogSelectAction.setContentView(R.layout.dialog_select_action);
        ivCloseSelectActionDialog = dialogSelectAction.findViewById(R.id.ivCloseDialog);
        ivAskHelpSelectActionDialog = dialogSelectAction.findViewById(R.id.ivAskHelp);

        //Dialog info report
        ivReportSelectActionDialog = dialogSelectAction.findViewById(R.id.ivReport);
        dialogInfoReport = new Dialog(this);
        dialogInfoReport.setContentView(R.layout.dialog_info_report);
        tvTitleInfoReport = dialogInfoReport.findViewById(R.id.info_report_title);
        tvDescriptionInfoReport = dialogInfoReport.findViewById(R.id.info_report_description);
        tvDownVoteInfoReport = dialogInfoReport.findViewById(R.id.info_report_down_vote);
        tvUpVoteInfoReport = dialogInfoReport.findViewById(R.id.info_report_up_vote);
        tvReporterNameInfoReport = dialogInfoReport.findViewById(R.id.info_report_reporter_name);
        tvRemainingTimeInfoReport = dialogInfoReport.findViewById(R.id.info_report_remain_time);
        ivUpVoteInfoReport = dialogInfoReport.findViewById(R.id.info_report_iv_up_vote);
        ivDownVoteInfoReport = dialogInfoReport.findViewById(R.id.info_report_iv_down_vote);
        tvPostedDateInfoReport = dialogInfoReport.findViewById(R.id.info_report_post_time);

        //Dialog info route
        snackbar = Snackbar.make(mapLayout,"",Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.findViewById(android.support.design.R.id.snackbar_text).setVisibility(View.INVISIBLE);

        //Dialog direction display setting
        ivDirectionDisplaySetting = findViewById(R.id.iv_direction_display_setting);
        dialogDirectionDisplaySetting = new Dialog(this);
        dialogDirectionDisplaySetting.setContentView(R.layout.dialog_direction_display_setting);
        cbPetrol = dialogDirectionDisplaySetting.findViewById(R.id.cb_petrol);
        cbRestaurant = dialogDirectionDisplaySetting.findViewById(R.id.cb_restaurant);
        cbHospital = dialogDirectionDisplaySetting.findViewById(R.id.cb_hospital);
        cbPopularTourist = dialogDirectionDisplaySetting.findViewById(R.id.cb_popular_tourist);

        View snackView = LayoutInflater.from(snackbar.getContext()).inflate(R.layout.snackbar_info_route, null);
        tvDuration = snackView.findViewById(R.id.sbDuration);
        tvDistance = snackView.findViewById(R.id.sbDistance);

        snackbarLayout.addView(snackView, 0);

        trafficStatusButton = findViewById(R.id.iv_traffic_status_button);
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
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.setTrafficEnabled(true);
            mMap.setOnInfoWindowClickListener(this);
            mMap.setOnPolylineClickListener((polyline -> {
                String distance = null, duration = null;
                Route oldRoute = null;
                if (polyline.getTag() != null) {
                    oldRoute = (Route) polyline.getTag();
                    distance = oldRoute.getDistance().getText();
                    duration = oldRoute.getDuration().getText();
                }
                if (polyline.getColor() == getResources().getColor(R.color.colorBestPolyline)) {
                    if (distance != null && duration != null) {
                        tvDistance.setText(distance);
                        tvDuration.setText(duration);
                        snackbar.show();
                        return;
                    }
                }
                else {
                    mMap.clear();
                    destinationMarkers.clear();
                    originMarkers.clear();
                    Route routeTmp = null;
                    for (Route route: routes) {
                        addDirectionMaker(route);

                        if (oldRoute.getDuration().compareTo(route.getDuration()) != 0
                                && oldRoute.getDistance().compareTo(route.getDistance()) != 0) {
                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .geodesic(true)
                                    .color(getResources()
                                            .getColor(R.color.colorNormalPolyline))
                                    .width(10).clickable(true);

                            polylineOptions.add(route.getStartLocation());
                            for (int i = 0; i < route.getPoints().size(); i++)
                                polylineOptions.add(route.getPoints().get(i));
                            polylineOptions.add(route.getEndLocation());

                            mMap.addPolyline(polylineOptions).setTag(route);
                        }
                        else routeTmp = route;

                    }
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .geodesic(true)
                            .color(getResources()
                                    .getColor(R.color.colorBestPolyline))
                            .width(10)
                            .clickable(true);

                    polylineOptions.add(routeTmp.getStartLocation());
                    for (int i = 0; i < routeTmp.getPoints().size(); i++)
                        polylineOptions.add(routeTmp.getPoints().get(i));
                    polylineOptions.add(routeTmp.getEndLocation());

                    mMap.addPolyline(polylineOptions).setTag(routeTmp);
                }
            }));
        }
        init();
        refreshHandler = new Handler();
        refreshHandler.postDelayed(reloadMapRunnable, 5000);
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

                        saveLocation(currentLocation);

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

    private void saveLocation(Location currentLocation) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LAT", String.valueOf(currentLocation.getLatitude()));
        Log.i(TAG, "moveToDeviceLocation: " + currentLocation.getLatitude() );
        editor.putString("LONG", String.valueOf(currentLocation.getLongitude()));
        editor.apply();
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

    /*This method adds asking help marker into the map (displayToMap Marker)
     *
     * Version: 1.0
     *
     * Date: 13/11/2018
     *
     * Author: Nghiên
     */
    public void displayAskHelpMarker(AskHelp askHelp){
        MarkerOptions markerOptions = new MarkerOptions();
        //Set attribute for marker;
        markerOptions.title(askHelp.getTitle());
        markerOptions.snippet(askHelp.getContent());
        LatLng position = new LatLng(askHelp.getLatitude(), askHelp.getLongitude());
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_problem));

        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(askHelp);
    }

    private void displayReportMarker(final Report report)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(report.getTitle());
        markerOptions.snippet(report.getContent());
        markerOptions.position(new LatLng(report.getLatitude(), report.getLongitude()));
        Drawable icon = getResources().getDrawable(report.getReportType().getReportIcon());
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 24, 24, true));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(((BitmapDrawable) drawable).getBitmap()));

        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(report);
    }

    private void reloadMap() {
        getAskHelpData();
        getReportData();
        displayToMap();
    }

    private void getReportData()
    {
        databaseReference.child("reports").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> reportChildren = dataSnapshot.getChildren();

                reports.clear();

                for (DataSnapshot data: reportChildren) {
                    Report report = data.getValue(Report.class);
                    if (report.getRemainingTime() != 0) {
                        reports.add(report);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError databaseError)
            {
                Log.d(TAG, "onCancelled: get ask help fail " + databaseError.getMessage());
                Log.e(TAG, "onCancelled: ", databaseError.toException());
            }
        });
    }

    private void displayToMap() {
        for (AskHelp askHelp : askHelps) {
            displayAskHelpMarker(askHelp);
        }

        for (Report report : reports) {
            displayReportMarker(report);
        }
    }



    private final Runnable reloadMapRunnable = new Runnable() {
        @Override
        public void run() {
            reloadMap();

            refreshHandler.postDelayed(reloadMapRunnable, 5000);
        }
    };

    private void getAskHelpData() {
        databaseReference.child("askHelps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> askHelpChildren = dataSnapshot.getChildren();

                askHelps.clear();

                for (DataSnapshot ask : askHelpChildren) {
                    AskHelp askHelp = ask.getValue(AskHelp.class);
                    Log.d(TAG, "onDataChange: " + askHelp.getId());
                    Log.d(TAG, "onDataChange: complete " + askHelp.isCompleted());
                    if (!askHelp.isCompleted())
                        askHelps.add(askHelp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: get ask help fail " + databaseError.getMessage());
                Log.e(TAG, "onCancelled: ", databaseError.toException());
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTag()!= null)
        {
            if (marker.getTag() instanceof AskHelp) {
                AskHelp askHelp = (AskHelp) marker.getTag();
                Intent intent = new Intent(MapsActivity.this, InfoAskHelpActivity.class);
                intent.putExtra("askHelp", askHelp);
                startActivity(intent);
            }
            if (marker.getTag() instanceof  Report) {
                Report report = (Report) marker.getTag();
                boolean isVoted = false;
                tvTitleInfoReport.setText(report.getTitle());
                tvDescriptionInfoReport.setText(report.getContent());
                StringBuilder builder = new StringBuilder();
                builder.append(report.getRemainingTime()/3600);
                builder.append(" ");
                builder.append(getString(R.string.mins));
                tvRemainingTimeInfoReport.setText(builder.toString());
                tvReporterNameInfoReport.setText(report.getReporter().getUsername());
                tvUpVoteInfoReport.setText(String.valueOf(report.getUpVote()));
                tvDownVoteInfoReport.setText(String.valueOf(report.getDownVote()));
                tvPostedDateInfoReport.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(report.getPostDate()));
                ivDownVoteInfoReport.setOnClickListener(l -> {
                    int currentUpVote = report.getUpVote();
                    int currentDownVote = report.getDownVote();
                    if (ivDownVoteInfoReport.isEnabled()) {
                        if (currentUpVote > 0) {
                            report.setUpVote(--currentUpVote);
                            tvUpVoteInfoReport.setText(String.valueOf(currentUpVote));
                        }
                        report.setDownVote(++currentDownVote);
                        tvDownVoteInfoReport.setText(String.valueOf(currentDownVote));
                        ivDownVoteInfoReport.setEnabled(false);
                        ivUpVoteInfoReport.setEnabled(true);
                        Map<String, Object> vote = new HashMap<>();
                        vote.put("downVote", currentDownVote);
                        vote.put("upVote", currentUpVote);
                        databaseReference.child("reports")
                                .child(report.getId())
                                .updateChildren(vote)
                                .addOnCompleteListener(task -> {
                                    showToastMessage("Vote down successfully!");
                                });
                    }
                });
                ivUpVoteInfoReport.setOnClickListener(l -> {
                    int currentUpVote = report.getUpVote();
                    int currentDownVote = report.getDownVote();
                    if (ivUpVoteInfoReport.isEnabled()) {
                        if (currentDownVote > 0) {
                            report.setDownVote(--currentDownVote);
                            tvDownVoteInfoReport.setText(String.valueOf(currentDownVote));
                        }
                        report.setUpVote(++currentUpVote);
                        tvUpVoteInfoReport.setText(String.valueOf(currentUpVote));
                        ivUpVoteInfoReport.setEnabled(false);
                        ivDownVoteInfoReport.setEnabled(true);
                        Map<String, Object> vote = new HashMap<>();
                        vote.put("downVote", currentDownVote);
                        vote.put("upVote", currentUpVote);
                        databaseReference.child("reports")
                                .child(report.getId())
                                .updateChildren(vote)
                                .addOnCompleteListener(task -> {
                                    showToastMessage("Vote up successfully!");
                                });
                    }
                });
                dialogInfoReport.show();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.map_layout);
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

        DrawerLayout drawer = findViewById(R.id.map_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        this.routes = routes;
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        Route bestRoute = getBestRoute(routes);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bestRoute.getStartLocation(), 16));

        for (Route route : routes) {
            addDirectionMaker(route);
            if (route.compareTo(bestRoute) != 0) {
                PolylineOptions polylineOptions = new PolylineOptions().
                        geodesic(true).
                        color(getResources().getColor(R.color.colorNormalPolyline)).
                        width(10)
                        .clickable(true);

                polylineOptions.add(route.getStartLocation());
                for (int i = 0; i < route.getPoints().size(); i++)
                    polylineOptions.add(route.getPoints().get(i));
                polylineOptions.add(route.getEndLocation());

                mMap.addPolyline(polylineOptions).setTag(route);
            }
        }

        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true).
                color(getResources().getColor(R.color.colorBestPolyline)).
                width(10)
                .clickable(true);

        polylineOptions.add(bestRoute.getStartLocation());
        for (int i = 0; i < bestRoute.getPoints().size(); i++)
            polylineOptions.add(bestRoute.getPoints().get(i));
        polylineOptions.add(bestRoute.getEndLocation());

        mMap.addPolyline(polylineOptions).setTag(bestRoute);
    }

    private void addDirectionMaker(Route route) {
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_start))
                .title(route.getStartAddress())
                .position(route.getStartLocation())));
        destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_end))
                .title(route.getEndAddress())
                .position(route.getEndLocation())
        ));
    }

    private Route getBestRoute(List<Route> routes) {
        Collections.sort(routes);
        return routes.get(0);
    }

    AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id)
        {
            hideKeyboard(view);

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
