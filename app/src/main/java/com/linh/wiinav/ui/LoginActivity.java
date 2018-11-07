package com.linh.wiinav.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.linh.wiinav.R;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email, pass;
    private Button signin, signup;
    private int counter;

    private void addControls(){
        email = (EditText) findViewById(R.id.edt_email);
        pass = (EditText) findViewById(R.id.edt_password);
        signin  = (Button) findViewById(R.id.btn_signin);
        signup = (Button) findViewById(R.id.btn_signupEmail);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addControls();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(email.getText().toString(), pass.getText().toString());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });



    }

    private void validate(String mail, String password ){
        if((mail.equals("admin@gmail.com"))  && (password.equals("1234"))){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(LoginActivity.this, "Please check your email or password", Toast.LENGTH_SHORT).show();
        }
    }


}
