package com.linh.wiinav.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.linh.wiinav.R;
import com.linh.wiinav.adapters.UploadImageAdapter;
import com.linh.wiinav.models.Report;
import com.linh.wiinav.models.ReportType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import pl.aprilapps.easyphotopicker.DefaultCallback;
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

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public static int countImage = 0;
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
            if (countImage == imageNames.size()) {
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
                report.setReporter(getUser());
                report.setImageName(imageNames);

                sendReport(report);
                backToMapsScreen();
            }else {
                showToastMessage("Waiting for uploading image done.");
            }
        });
        ivUploadImage.setOnClickListener(l -> {
            EasyImage.configuration(this)
                    .setAllowMultiplePickInGallery(true);
            EasyImage.openChooserWithGallery(ReportDetailActivity.this,"Choose Image",0);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> list, EasyImage.ImageSource imageSource, int i) {
                if(imageNames.size()<6 && list.size()<6-imageNames.size()) {
                    countImage = list.size();
                    for (int j = 0; j < list.size(); j++) {
                        Uri path = Uri.fromFile(list.get(j));
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(list.get(j)), null, options);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();
                            uploadImage(list.get(j), data, path);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    showToastMessage("Allow maximum 5 pictures");
                }
            }
        });

    }

    private void uploadImage(File file, byte[] data, Uri path) {
        storageReference.child("images/"+path.getLastPathSegment())
                .putBytes(data)
                .addOnCompleteListener(task -> {
                    imageNames.add(file.getPath());
                    uploadImageAdapter.notifyDataSetChanged();
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        rvUploadImage.setLayoutManager(gridLayoutManager);
        uploadImageAdapter = new UploadImageAdapter(imageNames);
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