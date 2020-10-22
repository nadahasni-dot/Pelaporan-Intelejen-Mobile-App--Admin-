package com.example.pelaporanbakesbangpolmalang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pelaporanbakesbangpolmalang.helper.SessionHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private static final String CAPTCHA_SITE_KEY = "6Le1VNoZAAAAAFrcyMnPkLowN1yiuFBNqSoG-myi";
    private static final String CAPTCHA_SECRET_KEY = "6Le1VNoZAAAAAOeFHMHeqLOjgJoi0Xj7KoCzFvly";
    
    TextInputLayout inputEmail;
    TextInputLayout inputPassword;
    
    MaterialButton loginButton;
    TextView lupaPasswordText;

    SessionHelper sessionHelper;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  init view call
        init();
    }

    private void init() {
        // instance helper
        sessionHelper = new SessionHelper(this);

        // init view
        loginButton = findViewById(R.id.loginButtonLogin);
        lupaPasswordText = findViewById(R.id.loginTextLupaPassword);
        inputEmail = findViewById(R.id.loginEmailInput);
        inputPassword = findViewById(R.id.loginPasswordInput);

        // button on click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = inputEmail.getEditText().getText().toString().trim();
                password = inputPassword.getEditText().getText().toString().trim();

                SafetyNet.getClient(getApplicationContext()).verifyWithRecaptcha(CAPTCHA_SITE_KEY)
                        .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                                // Indicates communication with reCAPTCHA service was
                                // successful.
                                String userResponseToken = recaptchaTokenResponse.getTokenResult();
                                if (!userResponseToken.isEmpty()) {
                                    // Validate the user response token using the
                                    // reCAPTCHA siteverify API.
                                    Log.e("ERROR", "VALIDATION STEP NEEDED");
                                    
                                    if (email.equals("admin") && password.equals("password")) {
                                        sessionHelper.createSession(email,email,"1","1");
                                        Intent goToHome = new Intent(LoginActivity.this, HomeActivity.class);

                                        startActivity(goToHome);
                                        finish();   
                                    } else {
                                        Snackbar.make(findViewById(R.id.drawer_layout_login), "Username atau password salah", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e instanceof ApiException) {
                                    // An error occurred when communicating with the
                                    // reCAPTCHA service. Refer to the status code to
                                    // handle the error appropriately.
                                    ApiException apiException = (ApiException) e;
                                    int statusCode = apiException.getStatusCode();
                                    Log.e("ERROR", "Error: " + CommonStatusCodes
                                            .getStatusCodeString(statusCode));
                                } else {
                                    // A different, unknown type of error occurred.
                                    Log.e("ERROR", "Error: " + e.getMessage());
                                }
                            }
                        });
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