package com.linh.wiinav.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linh.wiinav.R;
import com.linh.wiinav.adapters.UploadImageAdapter;
import com.linh.wiinav.models.Report;
import com.linh.wiinav.models.ReportType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import pl.aprilapps.easyphotopicker.EasyImage;

public class ReportDetailActivity
        extends BaseActivity
{
    private static final String TAG = "ReportDetailActivity";

    private TextView tvReportDetailTitle;
    private ImageView ivViewReportThumbnail;
    private ImageView bReportSubmit;
    private ImageView ivUploadImage;
    private TextView tvReportDescriptions;
    private RecyclerView rvUploadImage;
    private UploadImageAdapter uploadImageAdapter;
    private List<String> imageNames;

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
        tvReportDescriptions.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        bReportSubmit.setOnClickListener(v ->
        {
            Report report = new Report();
            report.setId(UUID.randomUUID().toString());
            report.setReportType(reportedType);
            report.setPostDate(Calendar.getInstance().getTime());
            report.setContent(tvReportDescriptions.getText().toString());
            report.setLatitude(Double.parseDouble(sharedPreferences.getString("LAT", "0.0")));
            report.setLongitude(Double.parseDouble(sharedPreferences.getString("LONG", "0.0")));
            report.setDownVote(0);
            report.setUpVote(0);
            report.setRemainingTime(3600L);
            report.setTitle(tvReportDetailTitle.getText().toString());
            report.setReporter(getUser());sendReport(report);

            backToMapsScreen();
        });
        ivUploadImage.setOnClickListener(l -> {

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

        tvReportDetailTitle = findViewById(R.id.txtReportDetailTitle);
        ivViewReportThumbnail = findViewById(R.id.imgViewReportThumbnail);

        bReportSubmit = findViewById(R.id.reportSubmit);
        bReportSubmit.setClickable(true);
        bReportSubmit.bringToFront();

        tvReportDescriptions = findViewById(R.id.reportDescriptions);
        ivUploadImage = findViewById(R.id.iv_report_upload_image);
        rvUploadImage = findViewById(R.id.rv_upload_image);
        imageNames = new ArrayList<>();
        uploadImageAdapter = new UploadImageAdapter(imageNames, this);
        rvUploadImage.setAdapter(uploadImageAdapter);
    }

    private void getIncomingIntent(){

        if(getIntent().hasExtra("REPORT_TYPE"))
        {
            reportedType = new ReportType();
            reportedType.setName(getIntent().getStringExtra("REPORT_TYPE"));
            reportedType.setId(String.valueOf(getIntent().getIntExtra("REPORT_ID", 0)));
            reportedType.setDuration(18000L);
            tvReportDetailTitle.setText(reportedType.getName());
            setThumbnail(Integer.parseInt(reportedType.getId()));
        }
    }

    private void setThumbnail(final int reportedId)
    {
        switch (reportedId) {
            case 1: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_traffic_moderate);
                reportedType.setReportIcon(R.drawable.ic_report_traffic_moderate);
                break;
            }
            case 2: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_traffic_heavy);
                reportedType.setReportIcon(R.drawable.ic_report_traffic_heavy);
                break;
            }
            case 3: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_traffic_stuck);
                reportedType.setReportIcon(R.drawable.ic_report_traffic_stuck);
                break;
            }
            case 5: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_police_visible);
                reportedType.setReportIcon(R.drawable.ic_report_police_visible);
                break;
            }
            case 6: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_police_hidden);
                reportedType.setReportIcon(R.drawable.ic_report_police_hidden);
                break;
            }
            case 8: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_crash_major);
                reportedType.setReportIcon(R.drawable.ic_report_crash_major);
                break;
            }
            case 9: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_crash_minor);
                reportedType.setReportIcon(R.drawable.ic_report_crash_minor);
                break;
            }
            case 11: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_flood);
                reportedType.setReportIcon(R.drawable.ic_report_hazard_flood);
                break;
            }
            case 12: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_pothole);
                reportedType.setReportIcon(R.drawable.ic_report_hazard_pothole);
                break;
            }
            case 13: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_construction);
                reportedType.setReportIcon(R.drawable.ic_report_hazard_construction);
                break;
            }
            case 14: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_missingsign);
                reportedType.setReportIcon(R.drawable.ic_report_hazard_missingsign);
                break;
            }
            case 15: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_objectonroad);
                reportedType.setReportIcon(R.drawable.ic_report_hazard_objectonroad);
                break;
            }
            case 16: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_brokentrafficlight);
                reportedType.setReportIcon(R.drawable.ic_report_hazard_brokentrafficlight);
                break;
            }
            case 17: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_stoppedvehicle);
                reportedType.setReportIcon(R.drawable.ic_report_hazard_stoppedvehicle);
                break;
            }
            case 18: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_animal);
                reportedType.setReportIcon(R.drawable.ic_report_hazard_animal);
                break;
            }
            case 20: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_missingroad);
                reportedType.setReportIcon(R.drawable.ic_report_mapissue_missingroad);
                break;
            }
            case 21: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_turnnotallowed);
                reportedType.setReportIcon(R.drawable.ic_report_mapissue_turnnotallowed);
                break;
            }
            case 22: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_speedlimit);
                reportedType.setReportIcon(R.drawable.ic_report_mapissue_speedlimit);
                break;
            }
            case 23: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_wrongaddress);
                reportedType.setReportIcon(R.drawable.ic_report_mapissue_wrongaddress);
                break;
            }
            case 24: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_missingexit);
                reportedType.setReportIcon(R.drawable.ic_report_mapissue_missingexit);
                break;
            }
            case 25: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_oneway);
                reportedType.setReportIcon(R.drawable.ic_report_mapissue_oneway);
                break;
            }
            case 26: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_closure);
                reportedType.setReportIcon(R.drawable.ic_report_mapissue_closure);
                break;
            }
            case 28: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_camera_speed);
                reportedType.setReportIcon(R.drawable.ic_report_camera_speed);
                break;
            }
            case 29: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_camera_redlight);
                reportedType.setReportIcon(R.drawable.ic_report_camera_redlight);
                break;
            }
            case 30: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_camera_fake);
                reportedType.setReportIcon(R.drawable.ic_report_camera_fake);
                break;
            }
            case 32: {
                ivViewReportThumbnail.setImageResource(R.drawable.ic_report_roadsidehelp);
                reportedType.setReportIcon(R.drawable.ic_report_roadsidehelp);
                break;
            }
            default: {

            }
        }
    }
}