package com.example.pelaporanbakesbangpolmalang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

public class AddLaporanActivity extends AppCompatActivity {
    Toolbar addLaporanToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_laporan);

        addLaporanToolbar = findViewById(R.id.addLaporanToolbar);

        // setup toolbar
        setSupportActionBar(addLaporanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent toHomeUser = new Intent(AddLaporanActivity.this, HomeActivity.class);
        toHomeUser.putExtra("GOTO_FRAGMENT", "LAPORAN");
        startActivity(toHomeUser);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}