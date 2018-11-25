package com.linh.wiinav.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linh.wiinav.R;
import com.linh.wiinav.models.Report;
import com.linh.wiinav.models.ReportType;
import com.linh.wiinav.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linh.wiinav.enums.User.BIRTHDAY;
import static com.linh.wiinav.enums.User.EMAIL;
import static com.linh.wiinav.enums.User.IDENTIFY_CARD;
import static com.linh.wiinav.enums.User.IS_BANNED;
import static com.linh.wiinav.enums.User.IS_VERIFIED_EMAIL;
import static com.linh.wiinav.enums.User.IS_VERIFIED_PHONE_NUMBER;
import static com.linh.wiinav.enums.User.NUMBER_ASK;
import static com.linh.wiinav.enums.User.PASSWORD;
import static com.linh.wiinav.enums.User.PHONE_NUMBER;
import static com.linh.wiinav.enums.User.USERNAME;
import static com.linh.wiinav.helpers.ValidationHelper.isEmptyField;
import static com.linh.wiinav.helpers.ValidationHelper.isValidEmail;
import static com.linh.wiinav.helpers.ValidationHelper.isValidPassword;

public class LoginActivity
        extends BaseActivity
        implements View.OnClickListener
{
    private static final String TAG = "LoginActivity";

    private static final int REQUEST_SIGN_UP_BY_EMAIL = 9001;

    private FirebaseAuth mAuth;
    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();

    private EditText edtEmail, edtPass;
    private Button btnSignInByEmail, btnSignUpByEmail;

    @Override
    protected void addControls(){
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("location", Context.MODE_PRIVATE);

        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_password);
        btnSignInByEmail = findViewById(R.id.btn_signin);
        btnSignUpByEmail = findViewById(R.id.btn_signupEmail);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if(mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (!user.isEmailVerified()) {
                user.reload();
                if (user.isEmailVerified()) {
                    Map<String, Object> updateUser = new HashMap<>();
                    updateUser.put("verifiedEmail", true);
                    databaseReference.child("users").child(getUid()).updateChildren(updateUser);
                }
            }
            displayMainActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addControls();
        addEvents();
        initData();
    }

    private void initData()
    {
        mReference.child("report_types").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot)
            {
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError databaseError)
            {
            }
        });
    }

    protected void addEvents()
    {
        btnSignInByEmail.setOnClickListener(this);
        btnSignUpByEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v)
    {
        switch (v.getId()) {
            case R.id.btn_signin:
                if (validation()) {
                    signIn(edtEmail.getText().toString(), edtPass.getText().toString());
                }
                break;
            case R.id.btn_signupEmail:
                signUpByEmail();
                break;
        }
    }

    private void signUpByEmail()
    {
    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
    startActivityForResult(intent, REQUEST_SIGN_UP_BY_EMAIL);
}

    private void signIn(final String email, final String password)
    {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((authResultTask) -> {
            Log.d(TAG, "signIn: " + authResultTask.isSuccessful());

            if (authResultTask.isSuccessful()) {
                saveUserProfile();
                displayMainActivity();
            } else {
                showToastMessage("Sign in failed. Please check your email and password.");
            }
        });
    }

    private void saveUserProfile() {
        String userId = getUid();
        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    Log.d(TAG, "onDataChange: can not get user");
                    showToastMessage("Can not get user");
                } else {
                    writeUserDataToPreference(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ", databaseError.toException());
            }
        });
    }

    private void writeUserDataToPreference(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(IS_BANNED.name(), user.isBan());
        editor.putBoolean(IS_VERIFIED_EMAIL.name(), user.isVerifiedEmail());
        editor.putBoolean(IS_VERIFIED_PHONE_NUMBER.name(), user.isVerifiedPhoneNumber());
        editor.putString(EMAIL.name(), user.getEmail());
        editor.putString(USERNAME.name(), user.getUsername());
        editor.putString(PHONE_NUMBER.name(), user.getPhoneNumber());
        editor.putString(BIRTHDAY.name(), user.getBirthday());
        editor.putLong(IDENTIFY_CARD.name(), user.getIdentifyCard());
        editor.putInt(NUMBER_ASK.name(), user.getNumberAsk());
        editor.putString(PASSWORD.name(), user.getPassword());

        editor.apply();
    }

    private void displayMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        if (requestCode == REQUEST_SIGN_UP_BY_EMAIL) {
            if (resultCode == RESULT_OK) {
                if (validation()) {
                    signIn(data.getStringExtra(EMAIL.name()),data.getStringExtra(PASSWORD.name()));
                }
            }
        }
    }

    private boolean validation()
    {
        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();

        if (isEmptyField(email)) {
            edtEmail.setError(getString(R.string.error_field_required));
            return false;
        }

        if (isEmptyField(password)) {
            edtPass.setError(getString(R.string.error_field_required));
            return false;
        }

        if (!isValidEmail(email)) {
            edtEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }

        if (!isValidPassword(password)) {
            edtPass.setError(getString(R.string.error_invalid_password));
            return false;
        }

        return true;
    }
}
