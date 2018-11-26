package com.linh.wiinav.ui;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linh.wiinav.R;
import com.linh.wiinav.models.User;

import java.util.Calendar;

import static com.linh.wiinav.helpers.ValidationHelper.isEmptyField;
import static com.linh.wiinav.helpers.ValidationHelper.isValidEmail;
import static com.linh.wiinav.helpers.ValidationHelper.isValidPassword;

public class SignUpActivity
        extends BaseActivity
        implements View.OnClickListener
{
    private static final String TAG = "SignUpActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Button btnSignup;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;

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
        btnSignup.setOnClickListener(this);
    }

    @Override
    protected void addControls()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        btnSignup = findViewById(R.id.btn_signupEmail);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirmpassword);
    }

    private void displayNextScreen(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean validateFormField(){
        if(edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString()))
            return true;
        return false;
    }

    private boolean validation()
    {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmpassword = edtConfirmPassword.getText().toString();

        if (isEmptyField(email)) {
            edtEmail.setError(getString(R.string.error_field_required));
            return false;
        }

        if (isEmptyField(password)) {
            edtPassword.setError(getString(R.string.error_field_required));
            return false;
        }

        if (!isValidEmail(email)) {
            edtEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }

        if (!isValidPassword(password)) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            return false;
        }

        if(isEmptyField(confirmpassword)){
            edtConfirmPassword.setError(getString(R.string.error_field_required));
            return false;
        }



        return true;
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

        displayNextScreen();
    }

    private void writeNewUser(final String uid, final String username, final String email)
    {
        Log.d(TAG, "writeNewUser: " + uid);

        User user = new User(email, username, "0",
                Calendar.getInstance().getTime().toString(),
                0L, false, false, 1 );

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
               // validateFormField();
                if(validation() && validateFormField()) {
                    signUp();
                }
                else{
                    Toast.makeText(this, "Please check your Confirm Password", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
