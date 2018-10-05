package com.linh.wiinav.view.ui;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.linh.wiinav.R;

public class ReportActivity
        extends AppCompatActivity
{

    private TextView txtReportName;
    private ImageView imgViewReport;
    private ImageButton imgButtonCamera;
    private Button btnCloseReport;
    private Button btnSendReport;

    private static final int CAMERA_PIC_REQUEST = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        addControls();
        addEvents();
    }

    private void addEvents()
    {
        Intent intent = getIntent();
        txtReportName.setText(intent.getStringExtra(MapsActivity.TITLE));

        if(txtReportName.getText().toString().equals("Places")){
            imgViewReport.setImageResource(R.drawable.ic_places);
        } else
        if(txtReportName.getText().toString().equals("Police man")) {
            imgViewReport.setImageResource(R.drawable.ic_policeman);
        } else
        if(txtReportName.getText().toString().equals("Map Issues")) {
            imgViewReport.setImageResource(R.drawable.ic_mapissues);
        } else
        if(txtReportName.getText().toString().equals("Traffic")) {
            imgViewReport.setImageResource(R.drawable.ic_traffic);
        }

        imgButtonCamera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
        btnCloseReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                onBackPressed();
            }
        });
        btnSendReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                Toast.makeText(getApplicationContext(), "Report Sent", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void addControls()
    {
        txtReportName = findViewById(R.id.txtReportName);
        imgViewReport = findViewById(R.id.imgViewReport);
        imgButtonCamera = findViewById(R.id.imgButtonCamera);
        btnCloseReport = findViewById(R.id.btnCloseReport);
        btnSendReport = findViewById(R.id.btnSendReport);
    }
}
