package com.example.pelaporanbakesbangpolmalang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class AddUserActivity extends AppCompatActivity {

    // widget
    AutoCompleteTextView jenisKelaminDropdown;
    Toolbar addPenggunaToolbar;

    String[] JK = new String[]{"Wanita", "Pria"};
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, JK);

        addPenggunaToolbar = findViewById(R.id.addPenggunaToolbar);
        jenisKelaminDropdown = findViewById(R.id.addPenggunaKelaminDropdownInput);

        // setup toolbar
        setSupportActionBar(addPenggunaToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        jenisKelaminDropdown.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent toHomeUser = new Intent(AddUserActivity.this, HomeActivity.class);
        startActivity(toHomeUser);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}