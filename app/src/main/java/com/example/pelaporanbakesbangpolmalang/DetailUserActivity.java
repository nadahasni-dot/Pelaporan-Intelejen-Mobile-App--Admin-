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
import com.bumptech.glide.Glide;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailUserActivity extends AppCompatActivity {
    Toolbar detailUserToolbar;

    // View Widget
    FloatingActionButton fabEdit;
    FloatingActionButton fabSave;
    FloatingActionButton fabCancelEdit;
    FloatingActionButton fabDelete;

    TextInputLayout inputUsername;
    TextInputLayout inputEmail;
    TextInputLayout inputNIK;
    TextInputLayout inputPhone;
    TextInputLayout inputAlamat;

    CircleImageView imageProfile;

    TextView textUsername, textLevel;

    LinearLayout errorLayout, loadingLayout;
    ScrollView contentDetail;

    MaterialButton cobaButton;

    RequestQueue requestQueue;

    String idUser, username, level, email, nik, telepon, alamat, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        Intent intent = getIntent();

        if (intent.hasExtra("ID_USER")) {
            idUser = intent.getStringExtra("ID_USER");
//            Toast.makeText(this, "ini adalah id: " + idUser, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
            Intent toHomeUser = new Intent(DetailUserActivity.this, HomeActivity.class);
            toHomeUser.putExtra("GOTO_FRAGMENT", "PENGGUNA");
            startActivity(toHomeUser);
            finish();
        }

        init();

        getUserById(idUser);
    }

    private void init() {
        requestQueue = VolleyHelper.getInstance(this).getRequestQueue();

        detailUserToolbar = findViewById(R.id.detailUserToolbar);
        // setup toolbar
        setSupportActionBar(detailUserToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fabEdit = findViewById(R.id.detailUserFABEdit);
        fabCancelEdit = findViewById(R.id.detailUserFABCancelEdit);
        fabSave = findViewById(R.id.detailUserFABSave);
        fabDelete = findViewById(R.id.detailUserFABDelete);

        inputUsername = findViewById(R.id.detailUserUsernameInput);
        inputEmail = findViewById(R.id.detailUserEmailInput);
        inputNIK = findViewById(R.id.detailUserNIKInput);
        inputPhone = findViewById(R.id.detailUserPhoneInput);
        inputAlamat = findViewById(R.id.detailUserInputAlamat);

        imageProfile = findViewById(R.id.detailUserImage);

        textUsername = findViewById(R.id.detailUserUsername);
        textLevel = findViewById(R.id.detailUserLevel);

        errorLayout = findViewById(R.id.error_detail_user_layout);
        loadingLayout = findViewById(R.id.loading_detail_user_layout);
        contentDetail = findViewById(R.id.detailUserContent);

        cobaButton = findViewById(R.id.button_coba_detail_user);

        cobaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabEdit.setVisibility(View.VISIBLE);

                getUserById(idUser);
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabEdit.setVisibility(View.GONE);
                fabSave.setVisibility(View.VISIBLE);
                fabCancelEdit.setVisibility(View.VISIBLE);
                fabDelete.setVisibility(View.VISIBLE);

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
                fabDelete.setVisibility(View.GONE);
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
                updateUserById();

                fabSave.setVisibility(View.GONE);
                fabCancelEdit.setVisibility(View.GONE);
                fabDelete.setVisibility(View.GONE);
                fabEdit.setVisibility(View.VISIBLE);

                inputUsername.getEditText().setEnabled(false);
                inputEmail.getEditText().setEnabled(false);
                inputNIK.getEditText().setEnabled(false);
                inputPhone.getEditText().setEnabled(false);
                inputAlamat.getEditText().setEnabled(false);

//                Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabSave.setVisibility(View.GONE);
                fabCancelEdit.setVisibility(View.GONE);
                fabDelete.setVisibility(View.GONE);
                fabEdit.setVisibility(View.GONE);

                if (!idUser.isEmpty()) {
                    deleteUserById(idUser);
                } else {
                    Toast.makeText(DetailUserActivity.this, "id kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserById(String id) {
        errorLayout.setVisibility(View.GONE);
        contentDetail.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        StringRequest getUser = new StringRequest(Request.Method.GET, ApiHelper.USER_BY_ID_USER + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    if (responseObject.getString("status").equals("false")) {
                        loadingLayout.setVisibility(View.GONE);
                        contentDetail.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(DetailUserActivity.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject data = responseObject.getJSONObject("data");

                    username = data.getString("username");

                    if (data.getString("level").equals("1")) {
                        level = "ADMIN";
                    } else {
                        level = "USER";
                    }

                    email = data.getString("email");
                    nik = data.getString("nik");
                    telepon = data.getString("telepon");
                    alamat = data.getString("alamat");
                    image = ApiHelper.ASSETS_URL + data.getString("foto");

                    Glide.with(DetailUserActivity.this).load(image).into(imageProfile);

                    inputUsername.getEditText().setText(username);
                    inputEmail.getEditText().setText(email);
                    inputNIK.getEditText().setText(nik);
                    inputPhone.getEditText().setText(telepon);
                    inputAlamat.getEditText().setText(alamat);

                    textUsername.setText(username);
                    textLevel.setText(level);

                    loadingLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    contentDetail.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    loadingLayout.setVisibility(View.GONE);
                    contentDetail.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(DetailUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

                Toast.makeText(DetailUserActivity.this, message, Toast.LENGTH_SHORT).show();

                loadingLayout.setVisibility(View.GONE);
                contentDetail.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        requestQueue.add(getUser);
    }

    private void deleteUserById(String id) {
        errorLayout.setVisibility(View.GONE);
        contentDetail.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, ApiHelper.AUTH_DELETE + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    if (responseObject.getString("status").equals("false")) {
                        loadingLayout.setVisibility(View.GONE);
                        contentDetail.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(DetailUserActivity.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(DetailUserActivity.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();

                    Intent toHomeUser = new Intent(DetailUserActivity.this, HomeActivity.class);
                    toHomeUser.putExtra("GOTO_FRAGMENT", "PENGGUNA");
                    startActivity(toHomeUser);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(DetailUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

                Toast.makeText(DetailUserActivity.this, message, Toast.LENGTH_SHORT).show();

                loadingLayout.setVisibility(View.GONE);
                contentDetail.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        requestQueue.add(deleteRequest);
    }

    private void updateUserById() {
        errorLayout.setVisibility(View.GONE);
        contentDetail.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        StringRequest updateRequest = new StringRequest(Request.Method.PUT, ApiHelper.AUTH_UPDATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    if (responseObject.getString("status").equals("false")) {
                        loadingLayout.setVisibility(View.GONE);
                        contentDetail.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(DetailUserActivity.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject data = responseObject.getJSONObject("data");

                    Snackbar.make(findViewById(R.id.detailUserLayout), responseObject.getString("message"), Snackbar.LENGTH_LONG).show();

                    textUsername.setText(data.getString("username"));
                    inputUsername.getEditText().setText(data.getString("username"));
                    inputEmail.getEditText().setText(data.getString("email"));
                    inputNIK.getEditText().setText(data.getString("nik"));
                    inputPhone.getEditText().setText(data.getString("telepon"));
                    inputAlamat.getEditText().setText(data.getString("alamat"));

                    errorLayout.setVisibility(View.GONE);
                    contentDetail.setVisibility(View.VISIBLE);
                    loadingLayout.setVisibility(View.GONE);
                } catch (Exception e) {
                    Toast.makeText(DetailUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

                Toast.makeText(DetailUserActivity.this, message, Toast.LENGTH_SHORT).show();

                loadingLayout.setVisibility(View.GONE);
                contentDetail.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", idUser);
                params.put("username", inputUsername.getEditText().getText().toString().trim());
                params.put("email", inputEmail.getEditText().getText().toString().trim());
                params.put("nik", inputNIK.getEditText().getText().toString().trim());
                params.put("telepon", inputPhone.getEditText().getText().toString().trim());
                params.put("alamat", inputAlamat.getEditText().getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(updateRequest);
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
        Intent toHomeUser = new Intent(DetailUserActivity.this, HomeActivity.class);
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