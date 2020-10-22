package com.example.pelaporanbakesbangpolmalang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

public class AddPemberitahuanActivity extends AppCompatActivity {
    Toolbar addPemberitahuanToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pemberitahuan);

        addPemberitahuanToolbar = findViewById(R.id.addPemberitahuanToolbar);

        // setup toolbar
        setSupportActionBar(addPemberitahuanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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