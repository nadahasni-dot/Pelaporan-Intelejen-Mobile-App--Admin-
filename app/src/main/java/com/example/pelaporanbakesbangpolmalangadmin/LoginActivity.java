package com.example.pelaporanbakesbangpolmalangadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    MaterialButton loginButton;
    TextView lupaPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  init view call
        init();
    }

    private void init() {
        // init view
        loginButton = findViewById(R.id.loginButtonLogin);
        lupaPasswordText = findViewById(R.id.loginTextLupaPassword);

        // button on click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToForgotPass = new Intent(LoginActivity.this, HomeActivity.class);

                startActivity(goToForgotPass);
                finish();
            }
        });

        //  lupa password on click
        lupaPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToForgotPass = new Intent(LoginActivity.this, ForgotPasswordActivity.class);

                startActivity(goToForgotPass);
                finish();
            }
        });
    }
}