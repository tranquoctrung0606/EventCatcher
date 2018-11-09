package com.linh.wiinav.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.linh.wiinav.R;

import static com.linh.wiinav.helpers.ValidationHelper.isEmptyField;
import static com.linh.wiinav.helpers.ValidationHelper.isValidEmail;
import static com.linh.wiinav.helpers.ValidationHelper.isValidPassword;

public class LoginActivity
        extends AppCompatActivity
        implements View.OnClickListener
{
    private static final String TAG = "LoginActivity";

    private static final int REQUEST_SIGN_UP_BY_EMAIL = 9001;

    private FirebaseAuth mAuth;

    private EditText edtEmail, edtPass;
    private Button btnSignInByEmail, btnSignUpByEmail;

    private void addControls(){
        mAuth = FirebaseAuth.getInstance();

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
            displayMainActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addControls();
        addEvents();
    }

    private void addEvents()
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
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGN_UP_BY_EMAIL);
    }

    private void signIn(final String email, final String password)
    {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((authResultTask) -> {
            Log.d(TAG, "signIn: " + authResultTask.isSuccessful());

            if (authResultTask.isSuccessful()) {
                displayMainActivity();
            } else {
                Toast.makeText(this, "Sign in failed. Please check your email and password.", Toast.LENGTH_SHORT).show();
            }
        });
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
                    signIn(edtEmail.getText().toString(), edtPass.getText().toString());
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
            edtEmail.setError(getString(R.string.error_invalid_password));
            return false;
        }

        return true;
    }
}
