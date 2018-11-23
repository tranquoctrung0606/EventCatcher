package com.linh.wiinav.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linh.wiinav.R;
import com.linh.wiinav.models.Report;
import com.linh.wiinav.models.ReportType;
import com.linh.wiinav.models.User;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ReportDetailActivity
        extends BaseActivity
{
    private static final String TAG = "ReportDetailActivity";

    private TextView txtReportDetailTitle;
    private ImageView imgViewReportThumbnail;
    private ImageView reportSubmit;
    private TextView reportDescriptions;

    private ReportType reportedType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        addControls();
        addEvents();
    }

    @Override
    protected void addEvents()
    {
        getIncomingIntent();
        reportDescriptions.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        reportSubmit.setOnClickListener(new View.OnClickListener()
        {
            Report report = new Report();

            @Override
            public void onClick(final View v)
            {
                report.setId(UUID.randomUUID().toString());
                report.setReportType(reportedType);
                report.setPostDate(Calendar.getInstance().getTime());
                report.setContent(reportDescriptions.getText().toString());
                report.setLatitude(Double.parseDouble(sharedPreferences.getString("LAT", "0.0")));
                report.setLongitude(Double.parseDouble(sharedPreferences.getString("LONG", "0.0")));
                report.setDownVote(0);
                report.setUpVote(0);

                report.setReporter(getUser());
                sendReport(report);

                backToMapsScreen();
            }
        });
    }

    private void sendReport(Report report) {
        databaseReference.child("reports").child(report.getId())
                .setValue(report).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "sendReport: post report successfully");
            showToastMessage("Report is sent.");
        }).addOnFailureListener(e -> {
            Log.e(TAG, "sendReport: ", e);
            showToastMessage("Report fail");
        });
    }

    @Override
    protected void addControls()
    {
        sharedPreferences = getSharedPreferences("location", Context.MODE_PRIVATE);

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
            reportedType = new ReportType();
            reportedType.setName(getIntent().getStringExtra("REPORT_TYPE"));
            reportedType.setId(String.valueOf(getIntent().getIntExtra("REPORT_ID", 0)));
            reportedType.setDuration(18000L);
            txtReportDetailTitle.setText(reportedType.getName());
            setThumbnail(Integer.parseInt(reportedType.getId()));
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
