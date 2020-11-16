package com.example.pelaporanbakesbangpolmalang.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.example.pelaporanbakesbangpolmalang.AddUserActivity;
import com.example.pelaporanbakesbangpolmalang.DetailLaporanActivity;
import com.example.pelaporanbakesbangpolmalang.DetailUserActivity;
import com.example.pelaporanbakesbangpolmalang.R;
import com.example.pelaporanbakesbangpolmalang.adapter.LaporanAdapter;
import com.example.pelaporanbakesbangpolmalang.adapter.PenggunaAdapter;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.example.pelaporanbakesbangpolmalang.model.LaporanItem;
import com.example.pelaporanbakesbangpolmalang.model.PenggunaItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // array list item pengguna
    private ArrayList<PenggunaItem> penggunaList;

    private RequestQueue requestQueue;

    // Widget
    private RecyclerView penggunaRecyclerView;
    private PenggunaAdapter penggunaAdapter;
    private RecyclerView.LayoutManager penggunaLayoutManager;
    private LinearLayout errorLayout, loadingLayout;
    private MaterialButton cobaButton;

    private FloatingActionButton penggunaFABAddpengguna;

    public UsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
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
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        requestQueue = VolleyHelper.getInstance(getContext()).getRequestQueue();

        penggunaRecyclerView = view.findViewById(R.id.recyclerViewPenggunaAdmin);
        penggunaRecyclerView.setHasFixedSize(true);
        penggunaFABAddpengguna = view.findViewById(R.id.penggunaFABAddUser);

        loadingLayout = view.findViewById(R.id.loading_pengguna_layout);
        errorLayout = view.findViewById(R.id.error_pengguna_layout);
        cobaButton = view.findViewById(R.id.button_coba_pengguna);

        cobaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllUser();
            }
        });

        penggunaFABAddpengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "tombol di klik", Toast.LENGTH_SHORT).show();

                Intent toAddUser = new Intent(getActivity().getApplicationContext(), AddUserActivity.class);
                startActivity(toAddUser);
                getActivity().finish();
            }
        });

        getAllUser();
    }

    private void getAllUser() {
        errorLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        StringRequest getAllUser = new StringRequest(Request.Method.GET, ApiHelper.ALL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // jika status false
                    if (jsonObject.getString("status").equals("false")) {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray data = jsonObject.getJSONArray("data");
                    penggunaList = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject objectPengguna = data.getJSONObject(i);

                        penggunaList.add(new PenggunaItem(
                                objectPengguna.getInt("id_user"),
                                objectPengguna.getString("username"),
                                objectPengguna.getString("email"),
                                ApiHelper.ASSETS_URL + objectPengguna.getString("foto")
                        ));
                    }

                    penggunaLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    penggunaAdapter = new PenggunaAdapter(penggunaList);

                    penggunaRecyclerView.setLayoutManager(penggunaLayoutManager);
                    penggunaRecyclerView.setAdapter(penggunaAdapter);

                    // handle onclick card
                    penggunaAdapter.setOnItemCliclListener(new PenggunaAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
//                            Toast.makeText(getActivity().getApplicationContext(), "ID: " + penggunaList.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                            Intent toDetail = new Intent(getActivity().getApplicationContext(), DetailUserActivity.class);
                            toDetail.putExtra("ID_USER", String.valueOf(penggunaList.get(position).getIdUser()));
                            startActivity(toDetail);
                            getActivity().finish();
                        }
                    });

                    loadingLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    penggunaRecyclerView.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    loadingLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
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

//                Fragment errorFragment = new ErrorFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.container_user, errorFragment).commit();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                loadingLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);

//                Snackbar.make(getActivity().getSupportFragmentManager().getFragment(null, ni), message, Snackbar.LENGTH_LONG).show();
            }
        });

        requestQueue.add(getAllUser);
    }
}