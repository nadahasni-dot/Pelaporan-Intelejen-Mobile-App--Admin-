package com.example.pelaporanbakesbangpolmalangadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    @Override
    public void onBackPressed() {
        Intent backToLogin = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(backToLogin);
        finish();
    }
}