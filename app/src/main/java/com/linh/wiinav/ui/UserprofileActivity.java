package com.linh.wiinav.ui;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.linh.wiinav.R;
import com.linh.wiinav.models.User;
import com.squareup.picasso.Picasso;

public class UserprofileActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgV_avatar;
    private TextView tv_repName, tv_acctiveAcc, tv_name, tv_phoneNumber, tv_email, tv_facebook;
    private Button btn_changeName, btn_changeNumber, btn_changeEmail, btn_changeFacebook,
            btn_changePass, btn_favorite, btn_reported, btn_refleted;
    private DatabaseReference databaseReference;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth firebaseAuth;
    private AlertDialog.Builder builder;
    private User userF;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        addControls();

        loadActivity();

        addEvents();
    }


    //add control
    public void addControls() {
        imgV_avatar = findViewById(R.id.imgV_avatar);

        tv_repName = findViewById(R.id.tv_repName);
        tv_acctiveAcc = findViewById(R.id.tv_activeAcc);
        tv_name = findViewById(R.id.tv_name);
        tv_phoneNumber = findViewById(R.id.tv_phonenumber);
        tv_email = findViewById(R.id.tv_email);
        tv_facebook = findViewById(R.id.tv_facebook);

        btn_changeName = findViewById(R.id.btn_changeName);
        btn_changeNumber = findViewById(R.id.btn_changeNumber);
        btn_changeEmail = findViewById(R.id.btn_changeEmail);
        btn_changeFacebook = findViewById(R.id.btn_changeFacebook);
        btn_changePass = findViewById(R.id.btn_changePass);
        btn_favorite = findViewById(R.id.btn_favorite);
        btn_reported = findViewById(R.id.btn_reported);
        btn_refleted = findViewById(R.id.btn_reflected);
    }

    public void loadActivity() {

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        //get id current user
        userId = LoginActivity.mAuth.getCurrentUser().getUid();

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userF = user;
                tv_name.setText(user.getUsername());
                tv_phoneNumber.setText(user.getPhoneNumber());
                tv_repName.setText(user.getUsername());
                tv_email.setText(user.getEmail());

                if(user.getImageName() != "") {
                    storageReference.child("images/" + user.getImageName())
                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).fit().centerCrop().into(imgV_avatar);
                        }
                    });
                }
                if (user.isBan()) {
                    tv_acctiveAcc.setText("Your account is ban");
                } else {
                    if (user.isVerifiedEmail())
                        tv_acctiveAcc.setText("Your account has been verify ");
                    else
                        tv_acctiveAcc.setText("Active your account");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void addEvents() {

        btn_changeName.setOnClickListener(this);
        btn_changePass.setOnClickListener(this);
        btn_changeEmail.setOnClickListener(this);
        btn_changeNumber.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText edt = new EditText(this);
        builder.setView(edt);

        switch (v.getId())
        {
            case R.id.btn_changeName:
                builder.setTitle("Change name");
                builder.setMessage("Input new name");
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_name.setText(edt.getText().toString());
                        userF.setUsername(tv_name.getText().toString());
                        databaseReference.child(userId).setValue(userF);
                    }
                });
                break;
            case R.id.btn_changeEmail:
                builder.setTitle("Change email");
                builder.setMessage("Input new email");
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_email.setText(edt.getText().toString());
                        userF.setEmail(tv_email.getText().toString());
                        databaseReference.child(userId).setValue(userF);
                    }
                });
                break;
            case R.id.btn_changeNumber:
                builder.setTitle("Change phone number");
                builder.setMessage("Input new phone number");
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_phoneNumber.setText(edt.getText().toString());
                        userF.setPhoneNumber(tv_phoneNumber.getText().toString());
                        databaseReference.child(userId).setValue(userF);
                    }
                });
                break;
        }
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        /*userF.setUsername(tv_name.getText().toString());
        userF.setEmail(tv_email.getText().toString());
        userF.setPhoneNumber(tv_phoneNumber.getText().toString());
        databaseReference.child(userId).setValue(userF);*/
    }
}
