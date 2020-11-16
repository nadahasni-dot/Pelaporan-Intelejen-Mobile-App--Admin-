package com.example.pelaporanbakesbangpolmalang.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pelaporanbakesbangpolmalang.R;
import com.example.pelaporanbakesbangpolmalang.model.PemberitahuanItem;

import java.util.ArrayList;

public class PemberitahuanAdapter extends RecyclerView.Adapter<PemberitahuanAdapter.PemberitahuanViewHolder> {
    private ArrayList<PemberitahuanItem> mPemberitahuanList;
    private OnItemClickListener mListener;
    private int status;

    public PemberitahuanAdapter(ArrayList<PemberitahuanItem> laporaList) {
        mPemberitahuanList = laporaList;
    }

    public void setOnItemCliclListener(OnItemClickListener listener) {
        mListener = listener;
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

        status = currentItem.getDibaca();
        holder.textJudul.setText(currentItem.getJudul());
        holder.textDescription.setText(currentItem.getDescription());
        holder.textDate.setText(currentItem.getTanggal());
        if (status == 1) {
            holder.statusTerbaca.setVisibility(View.VISIBLE);
        } else {
            holder.statusBelumTerbaca.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mPemberitahuanList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class PemberitahuanViewHolder extends RecyclerView.ViewHolder {
        public TextView textJudul;
        public TextView textDescription;
        public TextView textDate;
        public CardView cardPemberitahuan, statusTerbaca, statusBelumTerbaca;

        public PemberitahuanViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            textJudul = itemView.findViewById(R.id.pemberitahuan_title);
            textDescription = itemView.findViewById(R.id.pemberitahuan_description);
            textDate = itemView.findViewById(R.id.pemberitahuan_date);
            cardPemberitahuan = itemView.findViewById(R.id.card_pemberitahuan);
            statusTerbaca = itemView.findViewById(R.id.statusTerbaca);
            statusBelumTerbaca = itemView.findViewById(R.id.statusBelumTerbaca);

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
