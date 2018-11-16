package com.linh.wiinav.ui;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linh.wiinav.R;
import com.linh.wiinav.helpers.ValidationHelper;
import com.linh.wiinav.models.User;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.linh.wiinav.enums.User.EMAIL;
import static com.linh.wiinav.enums.User.PASSWORD;

public class SignUpActivity
        extends BaseActivity
        implements View.OnClickListener
{
    private static final String TAG = "SignUpActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Button btnSignUp;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private EditText edtPhoneNumber;

    private String phoneVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        addControls();
        addEvents();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

    @Override
    protected void addEvents()
    {
        btnSignUp.setOnClickListener(this);
    }

    @Override
    protected void addControls()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        btnSignUp = findViewById(R.id.btn_signupEmail);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirmpassword);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
    }

    private void displayNextScreen(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        intent.putExtra(EMAIL.name(), edtEmail.getText().toString());
        intent.putExtra(PASSWORD.name(),edtPassword.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void validateFormField(){
        if (ValidationHelper.isEmptyField(edtEmail.getText().toString())) {
            edtEmail.setText("Required");
            return;
        }

        if (ValidationHelper.isEmptyField(edtPassword.getText().toString())) {
            edtEmail.setText("Required");
            return;
        }

        if (ValidationHelper.isEmptyField(edtConfirmPassword.getText().toString())) {
            edtEmail.setText("Required");
            return;
        }

        if (ValidationHelper.isEmptyField(edtPhoneNumber.getText().toString())) {
            edtEmail.setText("Required");
            return;
        }
    }

    private void signUp()
    {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener(this, task -> {
                 Log.d(TAG, "signUp: " + task.isSuccessful());

                 if(task.isSuccessful()) {
                     onAuthSuccess(task.getResult().getUser());
                 } else {
                     Toast.makeText(this, "Sign Up failed", Toast.LENGTH_SHORT).show();
                 }
             });
    }

    private void onAuthSuccess(final FirebaseUser user)
    {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail());

        verifyUserEmail(user);
        verifyUserPhoneNumber(user);

        displayNextScreen();
    }

    private void verifyUserPhoneNumber(FirebaseUser user) {
        Log.d(TAG, "verifyUserPhoneNumber: " + edtPhoneNumber.getText().toString());
        PhoneAuthProvider.getInstance().verifyPhoneNumber(edtPhoneNumber.getText().toString(),
                60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallback);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            phoneVerificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            verifySmsCode(code);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e(TAG, "onVerificationFailed: phone verification fail", e);
        }
    };

    private void verifySmsCode(String code) {
        PhoneAuthProvider.getCredential(phoneVerificationId, code);
    }

    private void verifyUserEmail(final FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener((task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "verifyUserEmail: Verification email is sent to " + user.getEmail() );
            } else {
                Log.d(TAG, "verifyUserEmail: fail");
            }
        }));
    }

    private void writeNewUser(final String uid, final String username, final String email)
    {
        Log.d(TAG, "writeNewUser: " + uid);

        User user = new User(uid, email, username, edtPhoneNumber.getText().toString(),
                Calendar.getInstance().getTime().toString(),
                0L, false, false, false, 1 );

        mDatabase.child("users").child(uid).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "writeNewUser: add new user successfully");
                    showToastMessage("Sign up successfully.");
        }).addOnFailureListener(e -> {
            Log.e(TAG, "writeNewUser: ", e);
            showToastMessage("Unable sign up. Try later.");
        });
    }

    private String usernameFromEmail(final String email)
    {
        if(email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    @Override
    public void onClick(final View v)
    {
        switch (v.getId()) {
            case R.id.btn_signupEmail: {
                validateFormField();
                signUp();
            }
        }
    }
}
