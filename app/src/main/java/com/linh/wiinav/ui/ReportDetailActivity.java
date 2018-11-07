package com.linh.wiinav.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.linh.wiinav.R;

public class ReportDetailActivity
        extends AppCompatActivity
{
    private static final String TAG = ReportDetailActivity.class.getSimpleName();

    private TextView txtReportDetailTitle;
    private ImageView imgViewReportThumbnail;
    private ImageButton reportSubmit;
    private TextView reportDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        addControls();
        addEvents();
    }

    private void addEvents()
    {
        getIncomingIntent();
        setThumbnail();

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
            @Override
            public void onClick(final View v)
            {
                Log.i(TAG, "onClick: report submit");
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

        if(getIntent().hasExtra("TITLE"))
        {
            String title = getIntent().getStringExtra("TITLE");
            txtReportDetailTitle.setText(title);
        }
    }

    private void setThumbnail()
    {
        if(txtReportDetailTitle.getText().toString().equals("Moderate")){
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_traffic_moderate);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Heavy")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_traffic_heavy);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Stuck")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_traffic_stuck);
        } else
        if(txtReportDetailTitle.getText().toString().equals("On Road")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_police_visible);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Hidden")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_police_hidden);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Major")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_crash_major);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Minor")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_crash_minor);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Flood")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_flood);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Pothole")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_pothole);
        }else
        if(txtReportDetailTitle.getText().toString().equals("Construction")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_construction);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Missing Sign")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_missingsign);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Object on Road")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_objectonroad);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Broken Traffic Light")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_brokentrafficlight);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Stopped Vehicle")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_stoppedvehicle);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Animal")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_hazard_animal);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Missing Road")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_missingroad);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Turn not Allowed")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_turnnotallowed);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Speed limit")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_speedlimit);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Wrong Address")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_wrongaddress);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Missing Exit")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_missingexit);
        } else
        if(txtReportDetailTitle.getText().toString().equals("One Way")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_oneway);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Closure")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_mapissue_closure);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Speed")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_camera_speed);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Redlight")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_camera_redlight);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Fake")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_camera_fake);
        } else
        if(txtReportDetailTitle.getText().toString().equals("Send Help")) {
            imgViewReportThumbnail.setImageResource(R.drawable.ic_report_roadsidehelp);
        }
    }
}
