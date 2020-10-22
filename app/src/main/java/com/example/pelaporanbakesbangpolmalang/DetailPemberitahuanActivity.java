package com.example.pelaporanbakesbangpolmalang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

public class DetailPemberitahuanActivity extends AppCompatActivity {
    Toolbar detailPemberitahuanToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemberitahuan);

        detailPemberitahuanToolbar = findViewById(R.id.detailPemberitahuanToolbar);

        // setup toolbar
        setSupportActionBar(detailPemberitahuanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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