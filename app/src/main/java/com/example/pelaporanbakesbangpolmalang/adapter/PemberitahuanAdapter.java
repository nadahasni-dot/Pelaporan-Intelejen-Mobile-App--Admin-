package com.example.pelaporanbakesbangpolmalang.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pelaporanbakesbangpolmalang.model.PemberitahuanItem;
import com.example.pelaporanbakesbangpolmalang.R;

import java.util.ArrayList;

public class PemberitahuanAdapter extends RecyclerView.Adapter<PemberitahuanAdapter.PemberitahuanViewHolder> {
    private ArrayList<PemberitahuanItem> mPemberitahuanList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemCliclListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public PemberitahuanAdapter(ArrayList<PemberitahuanItem> laporaList) {
        mPemberitahuanList = laporaList;
    }

    @NonNull
    @Override
    public PemberitahuanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pemberitahuan_item, parent, false);
        PemberitahuanViewHolder pemberitahuanViewHolder = new PemberitahuanViewHolder(view, mListener);
        return pemberitahuanViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PemberitahuanViewHolder holder, int position) {
        PemberitahuanItem currentItem = mPemberitahuanList.get(position);

        holder.textJudul.setText(currentItem.getJudul());
        holder.textDescription.setText(currentItem.getDescription());
        holder.textDate.setText(currentItem.getTanggal());
    }

    @Override
    public int getItemCount() {
        return mPemberitahuanList.size();
    }

    public static class PemberitahuanViewHolder extends RecyclerView.ViewHolder {
        public TextView textJudul;
        public TextView textDescription;
        public TextView textDate;
        public CardView cardPemberitahuan;

        public PemberitahuanViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            textJudul = itemView.findViewById(R.id.pemberitahuan_title);
            textDescription = itemView.findViewById(R.id.pemberitahuan_description);
            textDate = itemView.findViewById(R.id.pemberitahuan_date);
            cardPemberitahuan = itemView.findViewById(R.id.card_pemberitahuan);

            cardPemberitahuan.setOnClickListener(new View.OnClickListener() {
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
