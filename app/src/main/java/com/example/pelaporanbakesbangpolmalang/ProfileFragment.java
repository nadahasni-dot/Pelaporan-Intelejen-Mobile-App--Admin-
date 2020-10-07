package com.example.pelaporanbakesbangpolmalang;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // View Widget
    FloatingActionButton fabEdit;
    FloatingActionButton fabSave;
    FloatingActionButton fabCancelEdit;

    TextInputLayout inputUsername;
    TextInputLayout inputEmail;
    TextInputLayout inputNIK;
    TextInputLayout inputPhone;
    TextInputLayout inputAlamat;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        fabEdit = view.findViewById(R.id.profileFABEdit);
        fabCancelEdit = view.findViewById(R.id.profileFABCancelEdit);
        fabSave = view.findViewById(R.id.profileFABSave);

        inputUsername = view.findViewById(R.id.profileUsernameInput);
        inputEmail = view.findViewById(R.id.profileEmailInput);
        inputNIK = view.findViewById(R.id.profileNIKInput);
        inputPhone = view.findViewById(R.id.profilePhoneInput);
        inputAlamat = view.findViewById(R.id.profileInputAlamat);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabEdit.setVisibility(View.GONE);
                fabSave.setVisibility(View.VISIBLE);
                fabCancelEdit.setVisibility(View.VISIBLE);

                inputUsername.getEditText().setEnabled(true);
                inputEmail.getEditText().setEnabled(true);
                inputNIK.getEditText().setEnabled(true);
                inputPhone.getEditText().setEnabled(true);
                inputAlamat.getEditText().setEnabled(true);

                inputUsername.getEditText().requestFocus();
            }
        });

        fabCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabSave.setVisibility(View.GONE);
                fabCancelEdit.setVisibility(View.GONE);
                fabEdit.setVisibility(View.VISIBLE);

                inputUsername.getEditText().setEnabled(false);
                inputEmail.getEditText().setEnabled(false);
                inputNIK.getEditText().setEnabled(false);
                inputPhone.getEditText().setEnabled(false);
                inputAlamat.getEditText().setEnabled(false);
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabSave.setVisibility(View.GONE);
                fabCancelEdit.setVisibility(View.GONE);
                fabEdit.setVisibility(View.VISIBLE);

                inputUsername.getEditText().setEnabled(false);
                inputEmail.getEditText().setEnabled(false);
                inputNIK.getEditText().setEnabled(false);
                inputPhone.getEditText().setEnabled(false);
                inputAlamat.getEditText().setEnabled(false);

                Toast.makeText(getActivity(), "Data saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}