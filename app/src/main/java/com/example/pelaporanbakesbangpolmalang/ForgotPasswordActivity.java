package com.example.pelaporanbakesbangpolmalang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String CAPTCHA_SITE_KEY = "6Le1VNoZAAAAAFrcyMnPkLowN1yiuFBNqSoG-myi";

    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    ImageView forgotBackButton;
    MaterialButton forgotButton;
    TextInputLayout inputEmail;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //  pemanggilan fungsi init view
        init();
    }

    private void init(){
        requestQueue = VolleyHelper.getInstance(this).getRequestQueue();
        progressDialog = new ProgressDialog(this);
        //  init view
        forgotBackButton = findViewById(R.id.forgotBackButton);
        inputEmail = findViewById(R.id.forgotEmailInput);
        forgotButton = findViewById(R.id.forgotSendRequestButton);

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = inputEmail.getEditText().getText().toString().trim();

                if (email.isEmpty()) {
                    inputEmail.setErrorEnabled(true);
                    inputEmail.setError("Email harus diisi");
                    return;
                } else {
                    inputEmail.setErrorEnabled(false);
                    inputEmail.setError("");
                }

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
                                    sendRequestEmail();
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

    private void sendRequestEmail() {
        progressDialog.setMessage("Sedang memproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest sendEmailRequest = new StringRequest(Request.Method.POST, ApiHelper.FORGOT_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject responseObject = new JSONObject(response);

                    if (responseObject.getString("status").equals("false")) {
                        inputEmail.getEditText().setText("");
                        inputEmail.requestFocus();
                        Toast.makeText(ForgotPasswordActivity.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        requestQueue.add(sendEmailRequest);
    }

    @Override
    public void onBackPressed() {
        Intent backToLogin = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(backToLogin);
        finish();
    }
}