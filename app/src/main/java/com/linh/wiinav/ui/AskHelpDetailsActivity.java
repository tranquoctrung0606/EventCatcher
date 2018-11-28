package com.linh.wiinav.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linh.wiinav.R;
import com.linh.wiinav.adapters.CommentsAdapter;
import com.linh.wiinav.models.AskHelp;
import com.linh.wiinav.models.Comment;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AskHelpDetailsActivity extends BaseActivity {
    private static final String TAG = "AskHelpDetailsActivity";

    private Button btnCall;
    private EditText edtAskHelpCommentText;
//    private Button btnComment;
    private TextView tvTitle;
    private TextView tvContent;

    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    private List<Comment> comments;
    private CommentsAdapter commentsAdapter;
    private RecyclerView rcvComments;

    //Ask Help variable
    private AskHelp currentAskHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_help_details);
        addControls();
        addEvents();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void addEvents() {
        /* This method makes a phone call
         *
         * Version: 1.0
         *
         * Date: 23/11/2018
         *
         * Author: Nghiên
         */
        btnCall.setOnClickListener(v -> {
            if (isPermissionGranted()) {
                callAction();
            }
        });

        /* This method sends a comment which user type
         *
         * Version: 1.0
         *
         * Date: 23/11/2018
         *
         * Author: Nghiên
         */
        edtAskHelpCommentText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtAskHelpCommentText.getRight() - edtAskHelpCommentText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                    Comment comment = new Comment();
                    comment.setCommentId(UUID.randomUUID().toString());
                    comment.setContent(edtAskHelpCommentText.getText().toString());
                    comment.setCommentator(getUser());
                    comment.setCommentDate(Calendar.getInstance().getTime());

                    mDatabaseReference.child("askHelps")
                            .child(currentAskHelp.getId())
                            .child("comments")
                            .child(String.valueOf(comments.size()))
                            .setValue(comment).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    edtAskHelpCommentText.setText("");
                                } else {
                                    Log.d(TAG, "addEvents: ");
                                    showToastMessage("Comments failed. Try it later!");
                                }
                            });
                    return true;
                }
            }
            return false;
        });
        mDatabaseReference.child("askHelps").child(currentAskHelp.getId()).child("comments")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> commentsSnapshot = dataSnapshot.getChildren();
                comments.clear();
                for (DataSnapshot data: commentsSnapshot) {
                    Comment comment = data.getValue(Comment.class);
                    comments.add(comment);
                }
                commentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled:  ", databaseError.toException());
                showToastMessage("Can not load data. Please check your connection.");
            }
        });
    }
    private boolean isPermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    private void callAction(){
        String phnum = "";//currentAskHelp.getReporter().getPhoneNumber();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    callAction();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void addControls() {
//        btnComment = findViewById(R.id.btn_comment);
        btnCall = findViewById(R.id.btn_call);
        edtAskHelpCommentText = findViewById(R.id.et_comment_text);
        //Get information from ask help infoWindow
        currentAskHelp = (AskHelp) getIntent().getSerializableExtra("currentAskHelp");
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(currentAskHelp.getTitle());
        tvContent = findViewById(R.id.tv_content);
        tvContent.setText(currentAskHelp.getContent());

        rcvComments = findViewById(R.id.rcvComments);
        rcvComments.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvComments.setLayoutManager(linearLayoutManager);
        rcvComments.setItemAnimator(new DefaultItemAnimator());

        comments = currentAskHelp.getComments();

        commentsAdapter = new CommentsAdapter(comments, this, currentAskHelp);

        rcvComments.setAdapter(commentsAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
