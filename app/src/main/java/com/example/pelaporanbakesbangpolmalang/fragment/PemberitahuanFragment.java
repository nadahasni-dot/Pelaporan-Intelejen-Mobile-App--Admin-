package com.example.pelaporanbakesbangpolmalang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.pelaporanbakesbangpolmalang.AddPemberitahuanActivity;
import com.example.pelaporanbakesbangpolmalang.DetailPemberitahuanActivity;
import com.example.pelaporanbakesbangpolmalang.R;
import com.example.pelaporanbakesbangpolmalang.adapter.PemberitahuanAdapter;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.SessionHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.example.pelaporanbakesbangpolmalang.model.PemberitahuanItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PemberitahuanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PemberitahuanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String level, idUser;

    // TODO: Rename and change types of parameters
    private ArrayList<PemberitahuanItem> pemberitahuanList;

    // Widget
    private FloatingActionButton pemberitahuanFAB;
    private RecyclerView pemberitahuanRecyclerView;
    private PemberitahuanAdapter pemberitahuanAdapter;
    private RecyclerView.LayoutManager pemberitahuanLayoutManager;
    private LinearLayout errorLayout, loadingLayout;
    private RequestQueue requestQueue;
    private MaterialButton cobaButton;
    private SessionHelper sessionHelper;

    private FloatingActionButton pemberitahuanFABAddpemberitahuan;

    public PemberitahuanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PemberitahuanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PemberitahuanFragment newInstance(String param1, String param2) {
        PemberitahuanFragment fragment = new PemberitahuanFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pemberitahuan, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        sessionHelper = new SessionHelper(getContext());
        requestQueue = VolleyHelper.getInstance(getContext()).getRequestQueue();

        level = sessionHelper.getLevel();
        idUser = sessionHelper.getIdUser();

        pemberitahuanRecyclerView = view.findViewById(R.id.recyclerViewPemberitahuanAdmin);
        pemberitahuanRecyclerView.setHasFixedSize(true);

        pemberitahuanFAB = view.findViewById(R.id.pemberitahuanFABAddPemberitahuan);

        if (level.equals("2")) {
            pemberitahuanFAB.setVisibility(View.GONE);
        }

        errorLayout = view.findViewById(R.id.error_pemberitahuan_layout);
        loadingLayout = view.findViewById(R.id.loading_pemberitahuan_layout);
        cobaButton = view.findViewById(R.id.button_coba_pemberitahuan);

        pemberitahuanFABAddpemberitahuan = view.findViewById(R.id.pemberitahuanFABAddPemberitahuan);

        cobaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecentPemberitahuan();
            }
        });

        pemberitahuanFABAddpemberitahuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "tombol di klik", Toast.LENGTH_SHORT).show();

                Intent toAddPemberitahuan = new Intent(getActivity().getApplicationContext(), AddPemberitahuanActivity.class);
                startActivity(toAddPemberitahuan);
                getActivity().finish();
            }
        });

        getRecentPemberitahuan();
    }

    private void getRecentPemberitahuan() {
        errorLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        StringRequest getRecentPemberitahuan = new StringRequest(Request.Method.GET, ApiHelper.PEMBERITAHUAN_BY_ID_PENERIMA + idUser, new Response.Listener<String>() {
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
                    pemberitahuanList = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject objectPemberitahuan = data.getJSONObject(i);

                        Date tanggal = new java.util.Date(objectPemberitahuan.getLong("dibuat_pada") * 1000);
                        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
                        String formatedDate = sdf.format(tanggal);

                        pemberitahuanList.add(new PemberitahuanItem(
                                objectPemberitahuan.getInt("id_pemberitahuan"),
                                objectPemberitahuan.getInt("id_user"),
                                objectPemberitahuan.getString("judul"),
                                objectPemberitahuan.getString("deskripsi"),
                                formatedDate,
                                objectPemberitahuan.getInt("dibaca")
                        ));
                    }

                    pemberitahuanLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    pemberitahuanAdapter = new PemberitahuanAdapter(pemberitahuanList);

                    pemberitahuanRecyclerView.setLayoutManager(pemberitahuanLayoutManager);
                    pemberitahuanRecyclerView.setAdapter(pemberitahuanAdapter);

                    // handle onclick card
                    pemberitahuanAdapter.setOnItemCliclListener(new PemberitahuanAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
//                            Toast.makeText(getActivity().getApplicationContext(), "ID: " + pemberitahuanList.get(position).getIdUser(), Toast.LENGTH_SHORT).show();
                            Intent toDetail = new Intent(getActivity().getApplicationContext(), DetailPemberitahuanActivity.class);
                            toDetail.putExtra("ID_PEMBERITAHUAN", String.valueOf(pemberitahuanList.get(position).getIdPemberitahuan()));
                            startActivity(toDetail);
                            getActivity().finish();
                        }
                    });

                    loadingLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    pemberitahuanRecyclerView.setVisibility(View.VISIBLE);
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

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                loadingLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        requestQueue.add(getRecentPemberitahuan);
    }
}