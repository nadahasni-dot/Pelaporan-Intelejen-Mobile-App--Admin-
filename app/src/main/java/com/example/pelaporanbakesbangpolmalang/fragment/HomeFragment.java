package com.example.pelaporanbakesbangpolmalang.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.example.pelaporanbakesbangpolmalang.DetailLaporanActivity;
import com.example.pelaporanbakesbangpolmalang.HomeActivity;
import com.example.pelaporanbakesbangpolmalang.R;
import com.example.pelaporanbakesbangpolmalang.adapter.LaporanAdapter;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.example.pelaporanbakesbangpolmalang.model.LaporanItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    // GMAP
    private static final int REQUEST_LOCATION_PERMISSION = 99;
    // current location
    FusedLocationProviderClient fusedLocationProviderClient;
    // TODO: Rename and change types of parameters
    private ArrayList<LaporanItem> laporanList;
    // Widget
    private RecyclerView laporanRecyclerView;
    private LaporanAdapter laporanAdapter;
    private RecyclerView.LayoutManager laporanLayoutManager;
    private TextView lihatSemua;
    private RequestQueue requestQueue;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private MaterialButton cobaButton;

    // variable
    private LatLng lastKnownLocation;
    private float zoom = 10;
    private GoogleMap mMap;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param latitude  Parameter 1.
     * @param longitude Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(double latitude, double longitude) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putDouble(LATITUDE, latitude);
        args.putDouble(LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        requestQueue = VolleyHelper.getInstance(getContext()).getRequestQueue();
        laporanRecyclerView = view.findViewById(R.id.recyclerViewHomeAdmin);
        laporanRecyclerView.setHasFixedSize(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressBar = view.findViewById(R.id.progress_bar_home);
        cobaButton = view.findViewById(R.id.button_coba_home);
        lihatSemua = view.findViewById(R.id.home_lihat_semua);
        errorLayout = view.findViewById(R.id.home_error_request);
        lihatSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHomeUser = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                toHomeUser.putExtra("GOTO_FRAGMENT", "LAPORAN");
                startActivity(toHomeUser);
                getActivity().finish();
            }
        });
        cobaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecentLaporan("3");
            }
        });


//        laporanList.add(new LaporanItem(1, 1, "Laporan 1", "Deskripsi 1", "1 Januari 2020"));
//        laporanList.add(new LaporanItem(2, 2, "Laporan 2", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ips", "2 Januari 2020"));
    }

    private void getRecentLaporan(String limit) {
        progressBar.setVisibility(View.VISIBLE);
        mMap.clear();

        StringRequest getRecentLaporan = new StringRequest(Request.Method.GET, ApiHelper.ALL_LAPORAN_LIMIT + limit, new Response.Listener<String>() {
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
                    LatLng locationMarker;

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

                        locationMarker = new LatLng(objectLaporan.getDouble("lat"), objectLaporan.getDouble("lng"));

                        mMap.addMarker(new MarkerOptions().position(locationMarker)
                                .title(objectLaporan.getString("judul"))
                                .snippet(objectLaporan.getString("alamat"))
                        );
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
                            toDetail.putExtra("INTENT_FROM", "HOME");
                            toDetail.putExtra("ID_LAPORAN", String.valueOf(laporanList.get(position).getIdLaporan()));
                            startActivity(toDetail);
                            getActivity().finish();
                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    laporanRecyclerView.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
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

                progressBar.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);

//                Snackbar.make(getActivity().getSupportFragmentManager().getFragment(null, ni), message, Snackbar.LENGTH_LONG).show();
            }
        });

        requestQueue.add(getRecentLaporan);
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, zoom));
                }
            }
        });
    }

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                map.clear();
                map.addMarker(new MarkerOptions().position(latLng).title("Lokasi Dipilih").snippet(snippet));
            }
        });
    }

    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {

            @Override
            public void onPoiClick(PointOfInterest poi) {
                mMap.clear();
                Marker poiMarker = mMap.addMarker(new MarkerOptions().position(poi.latLng).title(poi.name));
            }
        });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        LatLng alun = new LatLng(-8.168875, 113.702261);

//        mMap.addMarker(new MarkerOptions().title("Perbaikan jalan di jalan mastrip").position(polije).snippet("Laporan dari POLIJE"));
//        mMap.addMarker(new MarkerOptions().title("Pelanggaran Protokol Covid").position(alun).snippet("Laporan dari pusat kota (Alun - alun Jember)"));

        getLastKnownLocation();
        getRecentLaporan("5");

//        setMapLongClick(mMap);
//        setPoiClick(mMap);
        enableMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }
}