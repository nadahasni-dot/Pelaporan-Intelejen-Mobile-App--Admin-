package com.example.pelaporanbakesbangpolmalang.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.example.pelaporanbakesbangpolmalang.helper.SessionHelper;
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

    private RequestQueue requestQueue;
    private SessionHelper sessionHelper;

    private ArrayList<LaporanItem> laporanList;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    // Widget
    private RecyclerView laporanRecyclerView;
    private LaporanAdapter laporanAdapter;
    private RecyclerView.LayoutManager laporanLayoutManager;

    private TextView lihatSemua, countLaporan, countLaporanUser, countPelapor, countPemberitahuan;
    private Spinner spinnerWilayah;
    private MaterialButton cobaButton;

    private LinearLayout progressBar;
    private LinearLayout errorLayout;
    private ScrollView contentLayout;

    // variable
    private LatLng lastKnownLocation;
    private float zoom = 10;
    private GoogleMap mMap;
    private String url = ApiHelper.HOME_LAPORAN;

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
        sessionHelper = new SessionHelper(getContext());
        url = "all/" + sessionHelper.getIdUser();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        requestQueue = VolleyHelper.getInstance(getContext()).getRequestQueue();
        laporanRecyclerView = view.findViewById(R.id.recyclerViewHomeAdmin);
        laporanRecyclerView.setHasFixedSize(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinnerWilayah = view.findViewById(R.id.spinner_wilayah);
        spinnerWilayah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Semua Wilayah")) {
                    url = ApiHelper.HOME_LAPORAN + "all/" + sessionHelper.getIdUser();
                } else {
                    url = ApiHelper.HOME_LAPORAN + "wilayah/" + sessionHelper.getIdUser() + "/" + parent.getItemAtPosition(position).toString();
                }
//                Toast.makeText(getContext(), url, Toast.LENGTH_LONG).show();
                getHomeLaporan(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.wilayah_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWilayah.setAdapter(spinnerAdapter);

        countLaporan = view.findViewById(R.id.text_count_laporan);
        countLaporanUser = view.findViewById(R.id.text_count_laporan_user);
        countPelapor = view.findViewById(R.id.text_count_pelapor);
        countPemberitahuan = view.findViewById(R.id.text_count_pemberitahuan);

        progressBar = view.findViewById(R.id.progress_bar_home);
        contentLayout = view.findViewById(R.id.contentLayoutHomeFragment);
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
                getHomeLaporan(url);
            }
        });


//        laporanList.add(new LaporanItem(1, 1, "Laporan 1", "Deskripsi 1", "1 Januari 2020"));
//        laporanList.add(new LaporanItem(2, 2, "Laporan 2", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ips", "2 Januari 2020"));
    }

    private void getHomeLaporan(String url) {
        progressBar.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        mMap.clear();

        StringRequest getRecentLaporan = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // jika status false
                    if (jsonObject.getString("status").equals("false")) {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    countLaporan.setText(jsonObject.getString("count_laporan"));
                    countLaporanUser.setText(jsonObject.getString("count_laporan_user"));
                    countPelapor.setText(jsonObject.getString("count_pelapor"));
                    countPemberitahuan.setText(jsonObject.getString("count_pemberitahuan"));

                    JSONArray laporan = jsonObject.getJSONArray("laporan");
                    laporanList = new ArrayList<>();
                    LatLng locationMarker;

                    for (int i = 0; i < laporan.length(); i++) {
                        JSONObject objectLaporan = laporan.getJSONObject(i);

                        laporanList.add(new LaporanItem(
                                objectLaporan.getInt("id_laporan"),
                                objectLaporan.getInt("id_user"),
                                objectLaporan.getString("judul"),
                                objectLaporan.getString("deskripsi"),
                                objectLaporan.getString("tanggal"),
                                objectLaporan.getString("alamat_laporan"),
                                objectLaporan.getDouble("lat"),
                                objectLaporan.getDouble("lng")
                        ));

                        locationMarker = new LatLng(objectLaporan.getDouble("lat"), objectLaporan.getDouble("lng"));

                        mMap.addMarker(new MarkerOptions().position(locationMarker)
                                .title(objectLaporan.getString("judul"))
                                .snippet(objectLaporan.getString("alamat_laporan"))
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
                            Intent toDetail = new Intent(getActivity().getApplicationContext(), DetailLaporanActivity.class);
                            toDetail.putExtra("INTENT_FROM", "HOME");
                            toDetail.putExtra("ID_LAPORAN", String.valueOf(laporanList.get(position).getIdLaporan()));
                            startActivity(toDetail);
                            getActivity().finish();
                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    contentLayout.setVisibility(View.GONE);
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
                contentLayout.setVisibility(View.GONE);

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
                    getLastKnownLocation();
                    enableMyLocation();
                    break;
                }
        }
    }
}