package com.example.pelaporanbakesbangpolmalang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailLaporanActivity extends AppCompatActivity {
    RequestQueue requestQueue;

    Toolbar detailLaporanToolbar;
    MaterialButton getLocationButton, cobaDetailButton;
    LinearLayout errorLayout, loadingLayout;
    ScrollView contentDetail;

    TextView judulText, pelaporText, tanggalText, alamatText, deskripsiText;
    ImageView laporanImage;

    String intentFrom = "LAPORAN";
    String idLaporan, pelapor, judulLaooran, tanggalLaporan, alamatLaporan, deskripsiLaporan, lat, lng, imageLaporan;
    String urlToGMaps = "http://maps.google.com/maps?daddr=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);

        Intent intent = getIntent();

        if (intent.hasExtra("INTENT_FROM")) {
            intentFrom = "HOME";
        }

        if (intent.hasExtra("ID_LAPORAN")) {
            idLaporan = intent.getStringExtra("ID_LAPORAN");
        }

        init();
    }

    private void init() {
        requestQueue = VolleyHelper.getInstance(this).getRequestQueue();

        errorLayout = findViewById(R.id.error_detail_laporan_layout);
        loadingLayout = findViewById(R.id.loading_detail_laporan_layout);
        contentDetail = findViewById(R.id.contentDetailLaporan);
        cobaDetailButton = findViewById(R.id.button_coba_detail_laporan);
        getLocationButton = findViewById(R.id.detailLaporanButtonGetLocation);
        detailLaporanToolbar = findViewById(R.id.detailLaporanToolbar);

        judulText = findViewById(R.id.detailLaporanJudulText);
        pelaporText = findViewById(R.id.detailLaporanPelapor);
        tanggalText = findViewById(R.id.detailLaporanTanggal);
        alamatText = findViewById(R.id.detailLaporanAlamat);
        deskripsiText = findViewById(R.id.detailLaporanDeskripsi);
        laporanImage = findViewById(R.id.detailLaporanImage);

        cobaDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetailLaporan(idLaporan);
            }
        });
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(urlToGMaps));
                startActivity(intent);
            }
        });

        // setup toolbar
        setSupportActionBar(detailLaporanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getDetailLaporan(idLaporan);
    }

    private void getDetailLaporan(String id) {
        contentDetail.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        StringRequest getRecentLaporan = new StringRequest(Request.Method.GET, ApiHelper.LAPORAN_BY_ID_LAPORAN + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    if (responseObject.getString("status").equals("false")) {
                        loadingLayout.setVisibility(View.GONE);
                        contentDetail.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(DetailLaporanActivity.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject data = responseObject.getJSONObject("data");

                    judulLaooran = data.getString("judul");
                    pelapor = "Dilaporkan oleh " + data.getString("username");
                    tanggalLaporan = "pada " + unixToDateString(data.getString("dibuat_pada"));
                    alamatLaporan = data.getString("alamat");
                    deskripsiLaporan = data.getString("deskripsi");
                    lat = data.getString("lat");
                    lng = data.getString("lng");
                    imageLaporan = ApiHelper.ASSETS_URL + data.getString("foto");
                    urlToGMaps = urlToGMaps + lat + "," + lng;

                    judulText.setText(judulLaooran);
                    pelaporText.setText(pelapor);
                    tanggalText.setText(tanggalLaporan);
                    alamatText.setText(alamatLaporan);
                    deskripsiText.setText(deskripsiLaporan);

                    Glide.with(DetailLaporanActivity.this).load(imageLaporan).into(laporanImage);

                    loadingLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    contentDetail.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    loadingLayout.setVisibility(View.GONE);
                    contentDetail.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(DetailLaporanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

                Toast.makeText(DetailLaporanActivity.this, message, Toast.LENGTH_SHORT).show();

                loadingLayout.setVisibility(View.GONE);
                contentDetail.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        requestQueue.add(getRecentLaporan);
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
        //        super.onBackPressed();
        if (intentFrom.equals("LAPORAN")) {
            Intent toHomeUser = new Intent(DetailLaporanActivity.this, HomeActivity.class);
            toHomeUser.putExtra("GOTO_FRAGMENT", "LAPORAN");
            startActivity(toHomeUser);
            finish();
        } else {
            Intent toHomeUser = new Intent(DetailLaporanActivity.this, HomeActivity.class);
            toHomeUser.putExtra("GOTO_FRAGMENT", "HOME");
            startActivity(toHomeUser);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}