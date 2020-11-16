package com.example.pelaporanbakesbangpolmalang.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.pelaporanbakesbangpolmalang.DetailUserActivity;
import com.example.pelaporanbakesbangpolmalang.HomeActivity;
import com.example.pelaporanbakesbangpolmalang.LoginActivity;
import com.example.pelaporanbakesbangpolmalang.R;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.SessionHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String idUser;

    RequestQueue requestQueue;
    SessionHelper sessionHelper;
    ProgressDialog progressDialog;

    TextInputLayout inputPasswordLama, inputPasswordBaru, inputPasswordConfirm;
    MaterialButton rubahButton;

    public PasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PasswordFragment newInstance(String param1, String param2) {
        PasswordFragment fragment = new PasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        // Inflate the layout for this fragment

        init(view);

        return view;
    }

    private void init(View view) {
        sessionHelper = new SessionHelper(getContext());
        requestQueue = VolleyHelper.getInstance(getContext()).getRequestQueue();
        progressDialog = new ProgressDialog(getContext());

        idUser = sessionHelper.getIdUser();

        rubahButton = view.findViewById(R.id.rubahPasswordButton);
        inputPasswordLama = view.findViewById(R.id.rubahPasswordLamaInput);
        inputPasswordBaru = view.findViewById(R.id.rubahPasswordBaruInput);
        inputPasswordConfirm = view.findViewById(R.id.rubahPasswordKonfirmasiInput);

        rubahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    updatePassword();
                }
            }
        });
    }

    private void updatePassword() {
        progressDialog.setMessage("Sedang Memproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest loginRequest = new StringRequest(Request.Method.PUT, ApiHelper.AUTH_UPDATE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    inputPasswordLama.getEditText().setText("");
                    inputPasswordBaru.getEditText().setText("");
                    inputPasswordConfirm.getEditText().setText("");
                    
                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
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

                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", idUser);
                params.put("password_lama", inputPasswordLama.getEditText().getText().toString().trim());
                params.put("password_baru", inputPasswordBaru.getEditText().getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(loginRequest);
    }

    private boolean validateInput() {
        if (inputPasswordLama.getEditText().getText().toString().trim().equals("")) {
            inputPasswordLama.setErrorEnabled(true);
            inputPasswordLama.setError("Password lama harus diisi");
            return false;
        } else {
            inputPasswordLama.setErrorEnabled(false);
            inputPasswordLama.setError("");
        }

        if (inputPasswordBaru.getEditText().getText().toString().trim().equals("")) {
            inputPasswordBaru.setErrorEnabled(true);
            inputPasswordBaru.setError("Password baru harus diisi");
            return false;
        } else {
            inputPasswordBaru.setErrorEnabled(false);
            inputPasswordBaru.setError("");
        }

        if (!inputPasswordBaru.getEditText().getText().toString().trim().equals(inputPasswordConfirm.getEditText().getText().toString().trim())) {
            inputPasswordConfirm.setErrorEnabled(true);
            inputPasswordConfirm.setError("Konfirmasi password harus sama");
            return false;
        } else {
            inputPasswordConfirm.setErrorEnabled(false);
            inputPasswordConfirm.setError("");
        }

        return  true;
    }
}