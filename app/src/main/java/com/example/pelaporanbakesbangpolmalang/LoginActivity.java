package com.example.pelaporanbakesbangpolmalang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.SessionHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String CAPTCHA_SITE_KEY = "6Le1VNoZAAAAAFrcyMnPkLowN1yiuFBNqSoG-myi";
    private static final String CAPTCHA_SECRET_KEY = "6Le1VNoZAAAAAOeFHMHeqLOjgJoi0Xj7KoCzFvly";

    TextInputLayout inputEmail;
    TextInputLayout inputPassword;

    MaterialButton loginButton;
    TextView lupaPasswordText;
    ProgressDialog progressDialog;

    SessionHelper sessionHelper;

    RequestQueue requestQueue;

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
        progressDialog = new ProgressDialog(this);
        requestQueue = VolleyHelper.getInstance(this).getRequestQueue();

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
                                    doLogin(email, password);
//                                    if (email.equals("admin") && password.equals("password")) {
//                                        sessionHelper.createSession(email, email, "1", "1");
//                                        Intent goToHome = new Intent(LoginActivity.this, HomeActivity.class);
//
//                                        startActivity(goToHome);
//                                        finish();
//                                    } else {
//                                        Snackbar.make(findViewById(R.id.drawer_layout_login), "Username atau password salah", Snackbar.LENGTH_LONG).show();
//                                    }
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

    private void doLogin(final String email, final String password) {
        progressDialog.setMessage("Sedang Memproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest loginRequest = new StringRequest(Request.Method.POST, ApiHelper.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("false")) {
                        Snackbar.make(findViewById(R.id.drawer_layout_login), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    JSONObject data = jsonObject.getJSONObject("data");

                    String id = data.getString("id_user");
                    String level = data.getString("level");
                    String email = data.getString("email");
                    String username = data.getString("username");

                    sessionHelper.createSession(email, username, id, level);

                    Intent main = new Intent(LoginActivity.this, HomeActivity.class);
                    main.putExtra("ID_USER", id);
                    main.putExtra("LEVEL", level);
                    startActivity(main);
                    finish();
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.drawer_layout_login), e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String message = "Terjadi error. Coba beberapa saat lagi.";

                if (error instanceof NetworkError) {
                    message = "Tidak dapat terhubung ke internet. Harap periksa koneksi anda.";
                } else if (error instanceof AuthFailureError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof ClientError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof NoConnectionError) {
                    message = "Tidak ada koneksi internet. Harap periksa koneksi anda.";
                } else if (error instanceof TimeoutError) {
                    message = "Connection Time Out. Harap periksa koneksi anda.";
                }

                Snackbar.make(findViewById(R.id.drawer_layout_login), message, Snackbar.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(loginRequest);
    }
}