package com.linh.wiinav.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linh.wiinav.R;
import com.linh.wiinav.view.model.ReportedData;

public class InfoProblemReportActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvSnipper;
    private Button btnCall;
    private ReportedData reportedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_report);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + reportedData.getPhoneNumber()));
                try{
                    startActivity(intent);
                }
                catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(InfoProblemReportActivity.this,"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addControls() {
        reportedData = (ReportedData) getIntent().getSerializableExtra("reportedData");
        tvTitle = findViewById(R.id.title);
        tvTitle.setText(reportedData.getTitle());
        tvSnipper = findViewById(R.id.snippet);
        tvSnipper.setText(reportedData.getSnippet());
        btnCall = findViewById(R.id.btnCall);
    }

}
