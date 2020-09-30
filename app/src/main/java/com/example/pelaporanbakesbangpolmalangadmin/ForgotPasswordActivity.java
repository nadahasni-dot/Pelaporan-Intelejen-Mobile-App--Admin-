package com.example.pelaporanbakesbangpolmalangadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    ImageView forgotBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //  pemanggilan fungsi init view
        init();
    }

    private void init(){
        //  init view
        forgotBackButton = findViewById(R.id.forgotBackButton);

        forgotBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  intent on click
                Intent backToLogin = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(backToLogin);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent backToLogin = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(backToLogin);
        finish();
    }
}