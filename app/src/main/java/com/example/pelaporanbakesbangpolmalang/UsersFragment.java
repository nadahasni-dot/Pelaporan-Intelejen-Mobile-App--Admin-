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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    // Widget
    private RecyclerView penggunaRecyclerView;
    private PenggunaAdapter penggunaAdapter;
    private RecyclerView.LayoutManager penggunaLayoutManager;

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

        penggunaFABAddpengguna = view.findViewById(R.id.penggunaFABAddUser);
        penggunaFABAddpengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "tombol di klik", Toast.LENGTH_SHORT).show();

                Intent toAddUser = new Intent(getActivity().getApplicationContext(), AddUserActivity.class);
                startActivity(toAddUser);
                getActivity().finish();
            }
        });

        //  setup recycler view
        penggunaList = new ArrayList<>();
        penggunaList.add(new PenggunaItem(1, "User 1", "email1@email.com", "http://google.com"));
        penggunaList.add(new PenggunaItem(2, "User 2", "email2@email.com", "http://google.com"));
        penggunaList.add(new PenggunaItem(3, "User 3", "email3@email.com", "http://google.com"));
        penggunaList.add(new PenggunaItem(4, "User 4", "email4@email.com", "http://google.com"));
        penggunaList.add(new PenggunaItem(5, "User 5", "email5 @email.com", "http://google.com"));

        penggunaRecyclerView = view.findViewById(R.id.recyclerViewPenggunaAdmin);
        penggunaRecyclerView.setHasFixedSize(true);
        penggunaLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        penggunaAdapter = new PenggunaAdapter(penggunaList);

        penggunaRecyclerView.setLayoutManager(penggunaLayoutManager);
        penggunaRecyclerView.setAdapter(penggunaAdapter);

        // handle onclick card
        penggunaAdapter.setOnItemCliclListener(new PenggunaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity().getApplicationContext(), "ID: " + penggunaList.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                Intent toDetail = new Intent(getActivity().getApplicationContext(), DetailUserActivity.class);
                startActivity(toDetail);
                getActivity().finish();
            }
        });

        return view;
    }
}