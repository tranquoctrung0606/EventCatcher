package com.linh.wiinav.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linh.wiinav.R;
import com.linh.wiinav.view.adapter.CommentsAdapter;
import com.linh.wiinav.view.model.Comment;
import com.linh.wiinav.view.model.ReportedData;

import java.util.ArrayList;
import java.util.List;

public class InfoProblemReportActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvSnipper;
    private Button btnCall;
    private ReportedData reportedData;
    RecyclerView commentsRecyclerView;

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
                intent.setData(Uri.parse("tel:" + reportedData.getReporter().getPhoneNumber()));
                try {
                    if (ActivityCompat.checkSelfPermission(InfoProblemReportActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
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
        tvSnipper.setText(reportedData.getDescription());
        btnCall = findViewById(R.id.btnCall);
        commentsRecyclerView = findViewById(R.id.comments);
        commentsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsRecyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<Comment> comments = reportedData.getComments();
        CommentsAdapter commentsAdapter = new CommentsAdapter(comments);
        commentsRecyclerView.setAdapter(commentsAdapter);

    }

}
