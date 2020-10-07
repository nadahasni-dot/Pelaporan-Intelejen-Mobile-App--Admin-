package com.example.pelaporanbakesbangpolmalang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder> {
    private ArrayList<LaporanItem> mLaporanList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemCliclListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public LaporanAdapter(ArrayList<LaporanItem> laporaList) {
        mLaporanList = laporaList;
    }

    @NonNull
    @Override
    public LaporanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.laporan_item, parent, false);
        LaporanViewHolder laporanViewHolder = new LaporanViewHolder(view, mListener);
        return laporanViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanViewHolder holder, int position) {
        LaporanItem currentItem = mLaporanList.get(position);

        holder.textJudul.setText(currentItem.getJudul());
        holder.textDescription.setText(currentItem.getDescription());
        holder.textDate.setText(currentItem.getTanggal());
    }

    @Override
    public int getItemCount() {
        return mLaporanList.size();
    }

    public static class LaporanViewHolder extends RecyclerView.ViewHolder {
        public TextView textJudul;
        public TextView textDescription;
        public TextView textDate;
        public CardView cardLaporan;

        public LaporanViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            textJudul = itemView.findViewById(R.id.laporan_title);
            textDescription = itemView.findViewById(R.id.laporan_description);
            textDate = itemView.findViewById(R.id.laporan_date);
            cardLaporan = itemView.findViewById(R.id.card_laporan);

            cardLaporan.setOnClickListener(new View.OnClickListener() {
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
