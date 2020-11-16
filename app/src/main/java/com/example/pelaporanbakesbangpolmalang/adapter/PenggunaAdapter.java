package com.example.pelaporanbakesbangpolmalang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pelaporanbakesbangpolmalang.DetailUserActivity;
import com.example.pelaporanbakesbangpolmalang.HomeActivity;
import com.example.pelaporanbakesbangpolmalang.R;
import com.example.pelaporanbakesbangpolmalang.model.PenggunaItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PenggunaAdapter extends RecyclerView.Adapter<PenggunaAdapter.PenggunaViewHolder> {
    private ArrayList<PenggunaItem> mPenggunaList;
    private PenggunaAdapter.OnItemClickListener mListener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemCliclListener(PenggunaAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public PenggunaAdapter(ArrayList<PenggunaItem> penggunaList) {
        mPenggunaList = penggunaList;
    }

    @NonNull
    @Override
    public PenggunaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pengguna_item, parent, false);
        PenggunaViewHolder penggunaViewHolder = new PenggunaViewHolder(view, mListener);
        context = parent.getContext();
        return penggunaViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull PenggunaViewHolder holder, int position) {
        PenggunaItem currentItem = mPenggunaList.get(position);

        holder.textUsername.setText(currentItem.getUsername());
        holder.textEmail.setText(currentItem.getEmail());
        Glide.with(context).load(currentItem.getUserImageUrl()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return mPenggunaList.size();
    }

    public static class PenggunaViewHolder extends RecyclerView.ViewHolder {
        public TextView textUsername;
        public TextView textEmail;
        public CircleImageView userImage;
        public CardView cardPengguna;

        public PenggunaViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            textUsername = itemView.findViewById(R.id.penggunaUsernameTextItem);
            textEmail = itemView.findViewById(R.id.penggunaEmailTextItem);
            userImage = itemView.findViewById(R.id.penggunaImageProfile);
            cardPengguna = itemView.findViewById(R.id.card_pengguna);

            cardPengguna.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
