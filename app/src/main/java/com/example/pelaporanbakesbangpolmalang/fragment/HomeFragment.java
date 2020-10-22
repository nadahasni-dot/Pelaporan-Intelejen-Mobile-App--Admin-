package com.example.pelaporanbakesbangpolmalang.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pelaporanbakesbangpolmalang.DetailLaporanActivity;
import com.example.pelaporanbakesbangpolmalang.R;
import com.example.pelaporanbakesbangpolmalang.adapter.LaporanAdapter;
import com.example.pelaporanbakesbangpolmalang.model.LaporanItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<LaporanItem> laporanList;

    // Widget
    private RecyclerView laporanRecyclerView;
    private LaporanAdapter laporanAdapter;
    private RecyclerView.LayoutManager laporanLayoutManager;
    private TextView lihatSemua;

    // GMAP
    private static final int REQUEST_LOCATION_PERMISSION = 99;
    private GoogleMap mMap;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lihatSemua = view.findViewById(R.id.home_lihat_semua);
        lihatSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaporanFragment laporanFragment = new LaporanFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_admin, laporanFragment, "LAPORAN_FRAGMENT")
                        .addToBackStack(null)
                        .commit();
            }
        });

        laporanList = new ArrayList<>();
        laporanList.add(new LaporanItem(1, 1, "Laporan 1", "Deskripsi 1", "1 Januari 2020"));
        laporanList.add(new LaporanItem(2, 2, "Laporan 2", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ips", "2 Januari 2020"));
        laporanList.add(new LaporanItem(3, 3, "Laporan 3", "Deskripsi 3", "3 Januari 2020"));

        laporanRecyclerView = view.findViewById(R.id.recyclerViewHomeAdmin);
        laporanRecyclerView.setHasFixedSize(true);
        laporanLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        laporanAdapter = new LaporanAdapter(laporanList);

        laporanRecyclerView.setLayoutManager(laporanLayoutManager);
        laporanRecyclerView.setAdapter(laporanAdapter);

        // handle onclick card
        laporanAdapter.setOnItemCliclListener(new LaporanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity().getApplicationContext(), "ID: " + laporanList.get(position).getJudul(), Toast.LENGTH_SHORT).show();
                Intent toDetail = new Intent(getActivity().getApplicationContext(), DetailLaporanActivity.class);
                startActivity(toDetail);
                getActivity().finish();
            }
        });
        return view;
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
        float zoom = 10;

        LatLng jember = new LatLng(-8.17211, 113.69953);
        LatLng polije = new LatLng(-8.159391, 113.722460);
        LatLng alun = new LatLng(-8.168875, 113.702261);

        mMap.addMarker(new MarkerOptions().title("Demo masa pada pusat kota").position(jember).snippet("Laporan dari pusat kota"));
        mMap.addMarker(new MarkerOptions().title("Perbaikan jalan di jalan mastrip").position(polije).snippet("Laporan dari POLIJE"));
        mMap.addMarker(new MarkerOptions().title("Pelanggaran Protokol Covid").position(alun).snippet("Laporan dari pusat kota (Alun - alun Jember)"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jember, zoom));

        setMapLongClick(mMap);
        setPoiClick(mMap);
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