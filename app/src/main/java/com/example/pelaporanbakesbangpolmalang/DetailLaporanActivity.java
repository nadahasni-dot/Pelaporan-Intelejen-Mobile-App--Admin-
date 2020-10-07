package com.example.pelaporanbakesbangpolmalang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

public class DetailLaporanActivity extends AppCompatActivity {
    Toolbar detailLaporanToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);

        detailLaporanToolbar = findViewById(R.id.detailLaporanToolbar);
        // setup toolbar
        setSupportActionBar(detailLaporanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();

        Intent toHomeUser = new Intent(DetailLaporanActivity.this, HomeActivity.class);
        startActivity(toHomeUser);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}