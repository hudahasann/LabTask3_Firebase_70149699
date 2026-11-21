package com.uol.lostfound;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LostItemAdapter extends RecyclerView.Adapter<LostItemAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(LostItem item);
    }

    private final List<LostItem> itemList;
    private final OnItemClickListener listener;

    public LostItemAdapter(List<LostItem> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lost, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LostItem item = itemList.get(position);

        holder.tvItemName.setText(item.getItemName());
        holder.tvType.setText(item.getType().toUpperCase());

        if ("Lost".equalsIgnoreCase(item.getType())) {
            holder.tvType.setTextColor(Color.parseColor("#DC2626"));
            holder.tvType.setBackgroundColor(Color.parseColor("#FEE2E2"));
        } else {
            holder.tvType.setTextColor(Color.parseColor("#059669"));
            holder.tvType.setBackgroundColor(Color.parseColor("#D1FAE5"));
        }

        holder.tvLocation.setText(item.getLocation());
        holder.tvContact.setText(item.getContact());

        if ("Returned".equalsIgnoreCase(item.getStatus())) {
            holder.tvStatus.setText("Returned");
            holder.tvStatus.setTextColor(Color.parseColor("#15803D"));
            holder.icStatus.setImageResource(R.drawable.ic_check);
            holder.icStatus.setColorFilter(Color.parseColor("#15803D"));
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E2E8F0"));
            holder.tvItemName.setTextColor(Color.parseColor("#64748B"));
        } else {
            holder.tvStatus.setText("Open");
            holder.tvStatus.setTextColor(Color.parseColor("#D97706"));
            holder.icStatus.setImageResource(android.R.drawable.presence_online);
            holder.icStatus.setColorFilter(Color.parseColor("#D97706"));
            holder.cardView.setCardBackgroundColor(Color.WHITE);
            holder.tvItemName.setTextColor(Color.parseColor("#0F172A"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvItemName, tvType, tvLocation, tvContact, tvStatus;
        ImageView icStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvType = itemView.findViewById(R.id.tvType);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            icStatus = itemView.findViewById(R.id.icStatus);
        }
    }
}
