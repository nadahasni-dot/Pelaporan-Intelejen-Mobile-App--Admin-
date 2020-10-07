package com.example.pelaporanbakesbangpolmalang;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

        //  setup recycler view
        laporanList = new ArrayList<>();
        laporanList.add(new LaporanItem(1, 1, "Laporan 1", "Deskripsi 1", "1 Januari 2020"));
        laporanList.add(new LaporanItem(2, 2, "Laporan 2", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ips", "2 Januari 2020"));
        laporanList.add(new LaporanItem(3, 3, "Laporan 3", "Deskripsi 3", "3 Januari 2020"));
        laporanList.add(new LaporanItem(4, 4, "Laporan 4", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ips", "4 Januari 2020"));
        laporanList.add(new LaporanItem(5, 5, "Laporan 5", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ips", "5 Januari 2020"));
        laporanList.add(new LaporanItem(6, 6, "Laporan 6", "Deskripsi 6", "6 Januari 2020"));
        laporanList.add(new LaporanItem(7, 7, "Laporan 7", "Deskripsi 7", "7 Januari 2020"));
        laporanList.add(new LaporanItem(8, 8, "Laporan 8", "Deskripsi 8", "8 Januari 2020"));

        laporanRecyclerView = view.findViewById(R.id.recyclerViewLaporanAdmin);
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
}