package com.example.pelaporanbakesbangpolmalang.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pelaporanbakesbangpolmalang.AddPemberitahuanActivity;
import com.example.pelaporanbakesbangpolmalang.DetailPemberitahuanActivity;
import com.example.pelaporanbakesbangpolmalang.R;
import com.example.pelaporanbakesbangpolmalang.adapter.PemberitahuanAdapter;
import com.example.pelaporanbakesbangpolmalang.model.PemberitahuanItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

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

    // TODO: Rename and change types of parameters
    private ArrayList<PemberitahuanItem> pemberitahuanList;

    // Widget
    private RecyclerView pemberitahuanRecyclerView;
    private PemberitahuanAdapter pemberitahuanAdapter;
    private RecyclerView.LayoutManager pemberitahuanLayoutManager;

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

        pemberitahuanFABAddpemberitahuan = view.findViewById(R.id.pemberitahuanFABAddPemberitahuan);
        pemberitahuanFABAddpemberitahuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "tombol di klik", Toast.LENGTH_SHORT).show();

                Intent toAddPemberitahuan = new Intent(getActivity().getApplicationContext(), AddPemberitahuanActivity.class);
                startActivity(toAddPemberitahuan);
                getActivity().finish();
            }
        });

        //  setup recycler view
        pemberitahuanList = new ArrayList<>();
        pemberitahuanList.add(new PemberitahuanItem(1, 1, "Pemberitahuan 1", "Deskripsi 1", "1 Januari 2020"));
        pemberitahuanList.add(new PemberitahuanItem(2, 2, "Pemberitahuan 2", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ips", "2 Januari 2020"));
        pemberitahuanList.add(new PemberitahuanItem(3, 3, "Pemberitahuan 3", "Deskripsi 3", "3 Januari 2020"));
        pemberitahuanList.add(new PemberitahuanItem(4, 4, "Pemberitahuan 4", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ips", "4 Januari 2020"));
        pemberitahuanList.add(new PemberitahuanItem(5, 5, "Pemberitahuan 5", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ips", "5 Januari 2020"));
        pemberitahuanList.add(new PemberitahuanItem(6, 6, "Pemberitahuan 6", "Deskripsi 6", "6 Januari 2020"));
        pemberitahuanList.add(new PemberitahuanItem(7, 7, "Pemberitahuan 7", "Deskripsi 7", "7 Januari 2020"));
        pemberitahuanList.add(new PemberitahuanItem(8, 8, "Pemberitahuan 8", "Deskripsi 8", "8 Januari 2020"));

        pemberitahuanRecyclerView = view.findViewById(R.id.recyclerViewPemberitahuanAdmin);
        pemberitahuanRecyclerView.setHasFixedSize(true);
        pemberitahuanLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        pemberitahuanAdapter = new PemberitahuanAdapter(pemberitahuanList);

        pemberitahuanRecyclerView.setLayoutManager(pemberitahuanLayoutManager);
        pemberitahuanRecyclerView.setAdapter(pemberitahuanAdapter);

        // handle onclick card
        pemberitahuanAdapter.setOnItemCliclListener(new PemberitahuanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity().getApplicationContext(), "ID: " + pemberitahuanList.get(position).getJudul(), Toast.LENGTH_SHORT).show();
                Intent toDetail = new Intent(getActivity().getApplicationContext(), DetailPemberitahuanActivity.class);
                startActivity(toDetail);
                getActivity().finish();
            }
        });

        return view;
    }
}