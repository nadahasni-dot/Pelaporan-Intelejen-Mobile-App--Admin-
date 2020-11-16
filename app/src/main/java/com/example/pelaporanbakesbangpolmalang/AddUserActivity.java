package com.example.pelaporanbakesbangpolmalang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    // widget
    AutoCompleteTextView jenisKelaminDropdown;
    Toolbar addPenggunaToolbar;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    TextInputLayout inputUsername, inputEmail, inputTelepon, inputNik, inputJenisKelamin, inputAlamat, inputPassword, inputConfirmPassword;
    MaterialButton addUserButton;

    String[] JK = new String[]{"Wanita", "Pria"};
    String username, email, telepon, nik, alamat, jenisKelamin, password;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        init();
    }

    private void init() {
        adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, JK);

        requestQueue = VolleyHelper.getInstance(this).getRequestQueue();
        progressDialog = new ProgressDialog(this);

        addPenggunaToolbar = findViewById(R.id.addPenggunaToolbar);
        jenisKelaminDropdown = findViewById(R.id.addPenggunaKelaminDropdownInput);
        jenisKelaminDropdown.setAdapter(adapter);

        inputUsername = findViewById(R.id.addPenggunaUsernameInput);
        inputEmail = findViewById(R.id.addPenggunaEmailInput);
        inputTelepon = findViewById(R.id.addPenggunaTeleponInput);
        inputNik = findViewById(R.id.addPenggunaNIKInput);
        inputAlamat = findViewById(R.id.addPenggunaAlamatInput);
        inputJenisKelamin = findViewById(R.id.addPenggunaKelaminInput);
        inputPassword = findViewById(R.id.addPenggunaPasswordInput);
        inputConfirmPassword = findViewById(R.id.addPenggunaPasswordVerifyInput);

        addUserButton = findViewById(R.id.addPenggunaSimpanButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    username = inputUsername.getEditText().getText().toString().trim();
                    email = inputEmail.getEditText().getText().toString().trim();
                    telepon = inputTelepon.getEditText().getText().toString().trim();
                    nik = inputNik.getEditText().getText().toString().trim();
                    alamat = inputAlamat.getEditText().getText().toString().trim();
                    jenisKelamin = inputJenisKelamin.getEditText().getText().toString().trim();
                    password = inputPassword.getEditText().getText().toString().trim();

                    addNewUser();
                }
            }
        });

        // setup toolbar
        setSupportActionBar(addPenggunaToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void addNewUser() {
        progressDialog.setMessage("Sedang Memproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest registerUserRequest = new StringRequest(Request.Method.POST, ApiHelper.REGISTER_NEW_USER, new Response.Listener<String>() {
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

                    Toast.makeText(AddUserActivity.this, data.getString("username") + " berhasil terdaftar", Toast.LENGTH_SHORT).show();

                    Intent toHomeUser = new Intent(AddUserActivity.this, HomeActivity.class);
                    toHomeUser.putExtra("GOTO_FRAGMENT", "PENGGUNA");
                    startActivity(toHomeUser);
                    finish();
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.layoutAddUser), e.toString(), Snackbar.LENGTH_LONG).show();
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

                Snackbar.make(findViewById(R.id.layoutAddUser), error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("email", email);
                params.put("telepon", telepon);
                params.put("nik", nik);
                params.put("alamat", alamat);
                params.put("jenis_kelamin", jenisKelamin);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(registerUserRequest);
    }

    private boolean validateInput() {
        // username
        if (inputUsername.getEditText().getText().toString().trim().equals("")) {
            inputUsername.setErrorEnabled(true);
            inputUsername.setError("username tidak boleh kosong");
            return false;
        } else {
            inputUsername.setErrorEnabled(false);
            inputUsername.setError("");
        }

        // email
        if (inputEmail.getEditText().getText().toString().trim().equals("")) {
            inputEmail.setErrorEnabled(true);
            inputEmail.setError("email tidak boleh kosong");
            return false;
        } else {
            inputEmail.setErrorEnabled(false);
            inputEmail.setError("");
        }

        // telepon
        if (inputTelepon.getEditText().getText().toString().trim().equals("")) {
            inputTelepon.setErrorEnabled(true);
            inputTelepon.setError("telepon tidak boleh kosong");
            return false;
        } else {
            inputTelepon.setErrorEnabled(false);
            inputTelepon.setError("");
        }

        // nik
        if (inputNik.getEditText().getText().toString().trim().equals("")) {
            inputNik.setErrorEnabled(true);
            inputNik.setError("nik tidak boleh kosong");
            return false;
        } else {
            inputNik.setErrorEnabled(false);
            inputNik.setError("");
        }

        // alamat
        if (inputAlamat.getEditText().getText().toString().trim().equals("")) {
            inputAlamat.setErrorEnabled(true);
            inputAlamat.setError("alamat tidak boleh kosong");
            return false;
        } else {
            inputAlamat.setErrorEnabled(false);
            inputAlamat.setError("");
        }

        // jenisKelamin
        if (inputJenisKelamin.getEditText().getText().toString().trim().isEmpty()) {
            inputJenisKelamin.setErrorEnabled(true);
            inputJenisKelamin.setError("Jenis kelamin harus dipilih Pria / Wanita");
            return false;
        } else {
            inputJenisKelamin.setErrorEnabled(false);
            inputJenisKelamin.setError("");
        }

        // password
        if (inputPassword.getEditText().getText().toString().trim().equals("")) {
            inputPassword.setErrorEnabled(true);
            inputPassword.setError("password tidak boleh kosong");
            return false;
        } else {
            inputPassword.setErrorEnabled(false);
            inputPassword.setError("");
        }

        // password verify
        if (inputPassword.getEditText().getText().toString().trim().equals(inputConfirmPassword.getEditText().getText().toString().trim())) {
            inputPassword.setErrorEnabled(false);
            inputPassword.setError("");
            inputConfirmPassword.setErrorEnabled(false);
            inputConfirmPassword.setError("");
        } else {
            inputPassword.setErrorEnabled(true);
            inputPassword.setError("password dan konfirmasi password harus sama");
            inputConfirmPassword.setErrorEnabled(true);
            inputConfirmPassword.setError("password dan konfirmasi password harus sama");
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent toHomeUser = new Intent(AddUserActivity.this, HomeActivity.class);
        toHomeUser.putExtra("GOTO_FRAGMENT", "PENGGUNA");
        startActivity(toHomeUser);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}