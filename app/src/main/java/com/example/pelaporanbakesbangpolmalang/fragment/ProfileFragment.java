package com.example.pelaporanbakesbangpolmalang.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.example.pelaporanbakesbangpolmalang.DetailUserActivity;
import com.example.pelaporanbakesbangpolmalang.R;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.SessionHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // View Widget
    FloatingActionButton fabEdit;
    FloatingActionButton fabSave;
    FloatingActionButton fabCancelEdit;
    TextInputLayout inputUsername;
    TextInputLayout inputEmail;
    TextInputLayout inputNIK;
    TextInputLayout inputPhone;
    TextInputLayout inputAlamat;
    CircleImageView profileImage;
    SessionHelper sessionHelper;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    TextView usernameText, levelText, countUsersText, countLaporanText;
    String idUser, username, level, email, nik, telepon, alamat, countUsers, countLaporan, image;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);

        getProfile();

        return view;
    }

    private void init(View view) {
        sessionHelper = new SessionHelper(getContext());
        requestQueue = VolleyHelper.getInstance(getContext()).getRequestQueue();
        progressDialog = new ProgressDialog(getContext());

        profileImage = view.findViewById(R.id.profileImage);

        usernameText = view.findViewById(R.id.profileUsername);
        levelText = view.findViewById(R.id.profileLevel);
        countUsersText = view.findViewById(R.id.profileTotalUser);
        countLaporanText = view.findViewById(R.id.profileTotalLaporan);

        fabEdit = view.findViewById(R.id.profileFABEdit);
        fabCancelEdit = view.findViewById(R.id.profileFABCancelEdit);
        fabSave = view.findViewById(R.id.profileFABSave);

        inputUsername = view.findViewById(R.id.profileUsernameInput);
        inputEmail = view.findViewById(R.id.profileEmailInput);
        inputNIK = view.findViewById(R.id.profileNIKInput);
        inputPhone = view.findViewById(R.id.profilePhoneInput);
        inputAlamat = view.findViewById(R.id.profileInputAlamat);

        idUser = sessionHelper.getIdUser();

        if (sessionHelper.getLevel().equals("1")) {
            level = "ADMIN";
        } else {
            level = "USER";
        }

        levelText.setText(level);

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
                updateUserById();

                fabSave.setVisibility(View.GONE);
                fabCancelEdit.setVisibility(View.GONE);
                fabEdit.setVisibility(View.VISIBLE);

                inputUsername.getEditText().setEnabled(false);
                inputEmail.getEditText().setEnabled(false);
                inputNIK.getEditText().setEnabled(false);
                inputPhone.getEditText().setEnabled(false);
                inputAlamat.getEditText().setEnabled(false);

//                Toast.makeText(getActivity(), "Data saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfile() {
        progressDialog.setMessage("Sedang memproses..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest getProfile = new StringRequest(Request.Method.GET, ApiHelper.USER_PROFILE_BY_ID_USER + idUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject responseObject = new JSONObject(response);

                    if (responseObject.getString("status").equals("false")) {
                        Toast.makeText(getContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject data = responseObject.getJSONObject("data");
                    JSONObject user = data.getJSONObject("user");

                    countUsers = data.getString("count_users");
                    countLaporan = data.getString("count_laporan");

                    username = user.getString("username");
                    email = user.getString("email");
                    nik = user.getString("nik");
                    telepon = user.getString("telepon");
                    alamat = user.getString("alamat");
                    image = ApiHelper.ASSETS_URL + user.getString("foto");

                    Glide.with(getContext()).load(image).into(profileImage);

                    inputUsername.getEditText().setText(username);
                    inputEmail.getEditText().setText(email);
                    inputNIK.getEditText().setText(nik);
                    inputPhone.getEditText().setText(telepon);
                    inputAlamat.getEditText().setText(alamat);

                    usernameText.setText(username);
                    countUsersText.setText(countUsers);
                    countLaporanText.setText(countLaporan);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        });

        requestQueue.add(getProfile);
    }

    private void updateUserById() {
        progressDialog.setMessage("Sedang memproses..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest updateRequest = new StringRequest(Request.Method.PUT, ApiHelper.AUTH_UPDATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject responseObject = new JSONObject(response);

                    if (responseObject.getString("status").equals("false")) {
                        Toast.makeText(getContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject data = responseObject.getJSONObject("data");

                    Toast.makeText(getContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();

                    usernameText.setText(data.getString("username"));
                    inputUsername.getEditText().setText(data.getString("username"));
                    inputEmail.getEditText().setText(data.getString("email"));
                    inputNIK.getEditText().setText(data.getString("nik"));
                    inputPhone.getEditText().setText(data.getString("telepon"));
                    inputAlamat.getEditText().setText(data.getString("alamat"));
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
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
}