package com.example.pelaporanbakesbangpolmalang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddPemberitahuanActivity extends AppCompatActivity {
    Toolbar addPemberitahuanToolbar;

    RequestQueue requestQueue;
    SessionHelper sessionHelper;
    ProgressDialog progressDialog;

    TextInputLayout inputJudul, inputDeskripsi;
    MaterialButton sendButton;
    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pemberitahuan);

        init();
    }

    private void init() {
        requestQueue = VolleyHelper.getInstance(this).getRequestQueue();
        sessionHelper = new SessionHelper(this);
        progressDialog = new ProgressDialog(this);

        idUser = sessionHelper.getIdUser();

        addPemberitahuanToolbar = findViewById(R.id.addPemberitahuanToolbar);
        inputJudul = findViewById(R.id.addPemberitahuanJudulInput);
        inputDeskripsi = findViewById(R.id.addPemberitahuanDeskripsiInput);
        sendButton = findViewById(R.id.addPemberitahuanButtonInput);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    sendPemberitahuan();
                }
            }
        });

        // setup toolbar
        setSupportActionBar(addPemberitahuanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void sendPemberitahuan() {
        progressDialog.setMessage("Sedang Memproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest sendPemberitahuanRequest = new StringRequest(Request.Method.POST, ApiHelper.SEND_PEMBERITAHUAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("false")) {
                        Toast.makeText(AddPemberitahuanActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(AddPemberitahuanActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    Intent toHomeUser = new Intent(AddPemberitahuanActivity.this, HomeActivity.class);
                    toHomeUser.putExtra("GOTO_FRAGMENT", "PEMBERITAHUAN");
                    startActivity(toHomeUser);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(AddPemberitahuanActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

                Toast.makeText(AddPemberitahuanActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", idUser);
                params.put("judul", inputJudul.getEditText().getText().toString().trim());
                params.put("deskripsi", inputDeskripsi.getEditText().getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(sendPemberitahuanRequest);
    }

    private boolean validateInput() {
        if (inputJudul.getEditText().getText().toString().trim().equals("")) {
            inputJudul.setErrorEnabled(true);
            inputJudul.setError("Judul tidak boleh kosong");
            return false;
        } else {
            inputJudul.setErrorEnabled(false);
            inputJudul.setError("");
        }

        if (inputDeskripsi.getEditText().getText().toString().trim().equals("")) {
            inputDeskripsi.setErrorEnabled(true);
            inputDeskripsi.setError("Deskripsi tidak boleh kosong");
            return false;
        } else {
            inputDeskripsi.setErrorEnabled(false);
            inputDeskripsi.setError("");
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent toHomeUser = new Intent(AddPemberitahuanActivity.this, HomeActivity.class);
        toHomeUser.putExtra("GOTO_FRAGMENT", "PEMBERITAHUAN");
        startActivity(toHomeUser);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}