package com.example.pelaporanbakesbangpolmalang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailPemberitahuanActivity extends AppCompatActivity {
    Toolbar detailPemberitahuanToolbar;

    LinearLayout loadingLayout, errorLayout;
    ScrollView contentPemberitahuan;
    MaterialButton cobaButton;
    RequestQueue requestQueue;

    TextView judulPemberitahuanText, deskripsiPemberitahuanText, tanggalPemberitahuanText, pelaporPemberitahuanText;

    String idPemberitahuan, judul, pelapor, tanggal, deskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemberitahuan);

        Intent intent = getIntent();

        if (intent.hasExtra("ID_PEMBERITAHUAN")) {
            idPemberitahuan = intent.getStringExtra("ID_PEMBERITAHUAN");
        } else {
            Toast.makeText(this, "Pemberitahuan tidak ditemukan", Toast.LENGTH_SHORT).show();
            Intent toHomeUser = new Intent(DetailPemberitahuanActivity.this, HomeActivity.class);
            toHomeUser.putExtra("GOTO_FRAGMENT", "PEMBERITAHUAN");
            startActivity(toHomeUser);
            finish();
            return;
        }

        init();
        getPemberitahuanById(idPemberitahuan);
    }

    private void init() {
        requestQueue = VolleyHelper.getInstance(this).getRequestQueue();

        loadingLayout = findViewById(R.id.loading_detail_pemberitahuan_layout);
        errorLayout = findViewById(R.id.error_detail_pemberitahuan_layout);
        contentPemberitahuan = findViewById(R.id.contentDetailPemberitahuan);
        detailPemberitahuanToolbar = findViewById(R.id.detailPemberitahuanToolbar);

        judulPemberitahuanText = findViewById(R.id.detailPemberitahuanJudulText);
        pelaporPemberitahuanText = findViewById(R.id.detailPemberitahuanPelapor);
        tanggalPemberitahuanText = findViewById(R.id.detailPemberitahuanTanggal);
        deskripsiPemberitahuanText = findViewById(R.id.detailPemberitahuanDeskripsi);

        cobaButton = findViewById(R.id.button_coba_detail_pemberitahuan);
        cobaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPemberitahuanById(idPemberitahuan);
            }
        });

        // setup toolbar
        setSupportActionBar(detailPemberitahuanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getPemberitahuanById(String id) {
        errorLayout.setVisibility(View.GONE);
        contentPemberitahuan.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        StringRequest getPemberitahuan = new StringRequest(Request.Method.GET, ApiHelper.PEMBERITAHUAN_BY_ID_PEMBERITAHUAN + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    if (responseObject.getString("status").equals("false")) {
                        loadingLayout.setVisibility(View.GONE);
                        contentPemberitahuan.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(DetailPemberitahuanActivity.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject data = responseObject.getJSONObject("data");

                    judul = data.getString("judul");
                    pelapor = "Dilaporkan oleh " + data.getString("username");
                    tanggal = "pada " + unixToDateString(data.getString("dibuat_pada"));
                    deskripsi = data.getString("deskripsi");

                    judulPemberitahuanText.setText(judul);
                    pelaporPemberitahuanText.setText(pelapor);
                    tanggalPemberitahuanText.setText(tanggal);
                    deskripsiPemberitahuanText.setText(deskripsi);

                    loadingLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    contentPemberitahuan.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    loadingLayout.setVisibility(View.GONE);
                    contentPemberitahuan.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(DetailPemberitahuanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

                Toast.makeText(DetailPemberitahuanActivity.this, message, Toast.LENGTH_SHORT).show();

                loadingLayout.setVisibility(View.GONE);
                contentPemberitahuan.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        requestQueue.add(getPemberitahuan);
    }

    private String unixToDateString(String unix) {
        Date tanggal = new java.util.Date(Long.parseLong(unix) * 1000);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
        String formatedDate = sdf.format(tanggal);
        return formatedDate;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent toHomeUser = new Intent(DetailPemberitahuanActivity.this, HomeActivity.class);
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