package com.example.lucent.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lucent.R;
import com.example.lucent.model.User;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class LatestDonationTableAdapter extends RecyclerView.Adapter<LatestDonationTableAdapter.DonationViewHolder>{
    ArrayList<User> list;
    public LatestDonationTableAdapter(ArrayList<User>list){
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
        holder.name.setText(user.getDonor().getName());
        holder.amount.setText(String.valueOf(user.getAmount()));
        try{
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "dd-MMM-yyyy h:mm a";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
            StringBuilder date = new StringBuilder(user.getCreated());
            for(int i=0; i<date.length(); ++i){
                if(date.charAt(i)=='T'){
                    date.setCharAt(i,' ');
                    break;
                }
            }
            holder.time.setText(outputFormat.format(Objects.requireNonNull((inputFormat).parse(date.toString()))));
        }catch (ParseException e){
            holder.time.setText(user.getCreated());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final TextView amount;
        private final TextView time;
        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.id_latest_donation_name);
            amount = itemView.findViewById(R.id.id_latest_donation_amount);
            time = itemView.findViewById(R.id.id_latest_donation_time);
        }
    }
}
