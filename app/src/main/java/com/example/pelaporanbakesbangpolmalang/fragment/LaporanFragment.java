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
import com.example.pelaporanbakesbangpolmalang.AddLaporanActivity;
import com.example.pelaporanbakesbangpolmalang.DetailLaporanActivity;
import com.example.pelaporanbakesbangpolmalang.R;
import com.example.pelaporanbakesbangpolmalang.adapter.LaporanAdapter;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.example.pelaporanbakesbangpolmalang.model.LaporanItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LaporanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LaporanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types of parameters
    private ArrayList<LaporanItem> laporanList;

    // Widget
    private RecyclerView laporanRecyclerView;
    private LaporanAdapter laporanAdapter;
    private RecyclerView.LayoutManager laporanLayoutManager;
    private RequestQueue requestQueue;
    private LinearLayout errorLayout, loadingLayout;
    private MaterialButton cobaButtonLaporan;

    private FloatingActionButton laporanaFABAddlaporan;

    public LaporanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LaporanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LaporanFragment newInstance(String param1, String param2) {
        LaporanFragment fragment = new LaporanFragment();
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
        View view = inflater.inflate(R.layout.fragment_laporan, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        cobaButtonLaporan = view.findViewById(R.id.button_coba_laporan);
        loadingLayout = view.findViewById(R.id.loading_laporan_layout);
        errorLayout = view.findViewById(R.id.error_laporan_layout);
        laporanaFABAddlaporan = view.findViewById(R.id.laporanFABAddLaporan);
        requestQueue = VolleyHelper.getInstance(getContext()).getRequestQueue();
        //  setup recycler view
        laporanRecyclerView = view.findViewById(R.id.recyclerViewLaporanAdmin);
        laporanRecyclerView.setHasFixedSize(true);

        laporanaFABAddlaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "tombol di klik", Toast.LENGTH_SHORT).show();

                Intent toAddLaporan = new Intent(getActivity().getApplicationContext(), AddLaporanActivity.class);
                startActivity(toAddLaporan);
                getActivity().finish();
            }
        });

        cobaButtonLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecentLaporan();
            }
        });

        getRecentLaporan();
    }

    private void getRecentLaporan() {
        errorLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        StringRequest getRecentLaporan = new StringRequest(Request.Method.GET, ApiHelper.ALL_LAPORAN, new Response.Listener<String>() {
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
                    laporanList = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject objectLaporan = data.getJSONObject(i);

                        laporanList.add(new LaporanItem(
                                objectLaporan.getInt("id_laporan"),
                                objectLaporan.getInt("id_user"),
                                objectLaporan.getString("judul"),
                                objectLaporan.getString("deskripsi"),
                                objectLaporan.getString("tanggal"),
                                objectLaporan.getString("alamat"),
                                objectLaporan.getDouble("lat"),
                                objectLaporan.getDouble("lng")
                        ));
                    }

                    laporanLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    laporanAdapter = new LaporanAdapter(laporanList);

                    laporanRecyclerView.setLayoutManager(laporanLayoutManager);
                    laporanRecyclerView.setAdapter(laporanAdapter);

                    // handle onclick card
                    laporanAdapter.setOnItemCliclListener(new LaporanAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
//                            Toast.makeText(getActivity().getApplicationContext(), "ID: " + laporanList.get(position).getIdLaporan(), Toast.LENGTH_SHORT).show();
                            Intent toDetail = new Intent(getActivity().getApplicationContext(), DetailLaporanActivity.class);
                            toDetail.putExtra("ID_LAPORAN", String.valueOf(laporanList.get(position).getIdLaporan()));
                            startActivity(toDetail);
                            getActivity().finish();
                        }
                    });

                    loadingLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    laporanRecyclerView.setVisibility(View.VISIBLE);
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

        requestQueue.add(getRecentLaporan);
    }
}