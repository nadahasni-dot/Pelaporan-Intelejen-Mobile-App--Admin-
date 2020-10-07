package com.example.pelaporanbakesbangpolmalang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

public class DetailUserActivity extends AppCompatActivity {
    Toolbar detailUserToolbar;

    // View Widget
    FloatingActionButton fabEdit;
    FloatingActionButton fabSave;
    FloatingActionButton fabCancelEdit;

    TextInputLayout inputUsername;
    TextInputLayout inputEmail;
    TextInputLayout inputNIK;
    TextInputLayout inputPhone;
    TextInputLayout inputAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        init();
    }

    private void init() {
        detailUserToolbar = findViewById(R.id.detailUserToolbar);
        // setup toolbar
        setSupportActionBar(detailUserToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fabEdit = findViewById(R.id.detailUserFABEdit);
        fabCancelEdit = findViewById(R.id.detailUserFABCancelEdit);
        fabSave = findViewById(R.id.detailUserFABSave);

        inputUsername = findViewById(R.id.detailUserUsernameInput);
        inputEmail = findViewById(R.id.detailUserEmailInput);
        inputNIK = findViewById(R.id.detailUserNIKInput);
        inputPhone = findViewById(R.id.detailUserPhoneInput);
        inputAlamat = findViewById(R.id.detailUserInputAlamat);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabEdit.setVisibility(View.GONE);
                fabSave.setVisibility(View.VISIBLE);
                fabCancelEdit.setVisibility(View.VISIBLE);

                inputUsername.getEditText().setEnabled(true);
                inputEmail.getEditText().setEnabled(true);
                inputNIK.getEditText().setEnabled(true);
                inputPhone.getEditText().setEnabled(true);
                inputAlamat.getEditText().setEnabled(true);

                inputUsername.getEditText().requestFocus();
            }
        });

        fabCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabSave.setVisibility(View.GONE);
                fabCancelEdit.setVisibility(View.GONE);
                fabEdit.setVisibility(View.VISIBLE);

                inputUsername.getEditText().setEnabled(false);
                inputEmail.getEditText().setEnabled(false);
                inputNIK.getEditText().setEnabled(false);
                inputPhone.getEditText().setEnabled(false);
                inputAlamat.getEditText().setEnabled(false);
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabSave.setVisibility(View.GONE);
                fabCancelEdit.setVisibility(View.GONE);
                fabEdit.setVisibility(View.VISIBLE);

                inputUsername.getEditText().setEnabled(false);
                inputEmail.getEditText().setEnabled(false);
                inputNIK.getEditText().setEnabled(false);
                inputPhone.getEditText().setEnabled(false);
                inputAlamat.getEditText().setEnabled(false);

                Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent toHomeUser = new Intent(DetailUserActivity.this, HomeActivity.class);
        startActivity(toHomeUser);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}