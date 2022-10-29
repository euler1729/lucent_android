package com.example.lucent.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lucent.R;
import com.example.lucent.model.User;

import java.util.ArrayList;
import java.util.List;

public class DonationTableAdapter extends RecyclerView.Adapter<DonationTableAdapter.DonationViewHolder>{
    ArrayList<User> list;
    public DonationTableAdapter(ArrayList<User>list){
        this.list = list;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateLatestDonationList(List<User> newList){
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_donation_layout,parent,false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        User user = list.get(position);
        holder.name.setText("absfdf");
        holder.amount.setText(String.valueOf(user.getAmount()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final TextView amount;
        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.id_name);
            amount = itemView.findViewById(R.id.id_latest_donation_amount);
        }
    }
}
