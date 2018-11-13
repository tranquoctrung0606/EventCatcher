package com.linh.wiinav.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linh.wiinav.R;
import com.linh.wiinav.models.Report;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportDetailActivity
        extends AppCompatActivity
{
    private static final String TAG = ReportDetailActivity.class.getSimpleName();

    private TextView txtReportDetailTitle;
    private ImageView imgViewReportThumbnail;
    private ImageView reportSubmit;
    private TextView reportDescriptions;

    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
    private SharedPreferences mPreferences;

    private int reportedId;
    private String reportedType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        mPreferences = getSharedPreferences("location", Context.MODE_PRIVATE);

        addControls();
        addEvents();
    }

    private void addEvents()
    {
        getIncomingIntent();
        reportDescriptions.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus)
            {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        reportSubmit.setOnClickListener(new View.OnClickListener()
        {
            Date postDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Report report = new Report();

            @Override
            public void onClick(final View v)
            {
                report.setReportType(reportedType);
                report.setReporterId(FirebaseAuth.getInstance().getUid());
                report.setPostDate(formatter.format(postDate));
                report.setContent(reportDescriptions.getText().toString());
                report.setVerifyDate("");
                report.setLatitude(Double.parseDouble(mPreferences.getString("LAT", "0.0")));
                report.setLongitude(Double.parseDouble(mPreferences.getString("LONG", "0.0")));
                report.setInspector("");
                report.setInspectorId("");

                mReference.child("reports").child(report.getId().toString()).setValue(report);
                Intent intent = new Intent(ReportDetailActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void hideKeyboard(final View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void addControls()
    {

        txtReportDetailTitle = findViewById(R.id.txtReportDetailTitle);
        imgViewReportThumbnail = findViewById(R.id.imgViewReportThumbnail);

        reportSubmit = findViewById(R.id.reportSubmit);
        reportSubmit.setClickable(true);
        reportSubmit.bringToFront();

        reportDescriptions = findViewById(R.id.reportDescriptions);
    }

    private void getIncomingIntent(){

        if(getIntent().hasExtra("REPORT_TYPE"))
        {
            reportedType = getIntent().getStringExtra("REPORT_TYPE");
            reportedId = getIntent().getIntExtra("REPORT_ID", 0);
            txtReportDetailTitle.setText(reportedType);
            setThumbnail(reportedId);
        }
    }

    private void setThumbnail(final int reportedId)
    {
        switch (reportedId) {
            case 1: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_traffic_moderate);
                break;
            }
            case 2: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_traffic_heavy);
                break;
            }
            case 3: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_traffic_stuck);
                break;
            }
            case 5: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_police_visible);
                break;
            }
            case 6: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_police_hidden);
                break;
            }
            case 8: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_crash_major);
                break;
            }
            case 9: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_crash_minor);
                break;
            }
            case 11: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_flood);
                break;
            }
            case 12: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_pothole);
                break;
            }
            case 13: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_construction);
                break;
            }
            case 14: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_missingsign);
                break;
            }
            case 15: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_objectonroad);
                break;
            }
            case 16: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_brokentrafficlight);
                break;
            }
            case 17: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_stoppedvehicle);
                break;
            }
            case 18: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_animal);
                break;
            }
            case 20: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_missingroad);
                break;
            }
            case 21: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_turnnotallowed);
                break;
            }
            case 22: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_speedlimit);
                break;
            }
            case 23: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_wrongaddress);
                break;
            }
            case 24: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_missingexit);
                break;
            }
            case 25: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_oneway);
                break;
            }
            case 26: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_closure);
                break;
            }
            case 28: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_camera_speed);
                break;
            }
            case 29: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_camera_redlight);
                break;
            }
            case 30: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_camera_fake);
                break;
            }
            case 32: {
                imgViewReportThumbnail.setImageResource(R.drawable.ic_report_roadsidehelp);
                break;
            }
            default: {

            }
        }
    }
}
