package com.example.lucent.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lucent.R;
import com.example.lucent.model.Spending;

import java.util.ArrayList;
import java.util.List;

public class SpendingTableAdapter extends RecyclerView.Adapter<SpendingTableAdapter.SpendingViewHolder> {

    ArrayList<Spending> list;

    public SpendingTableAdapter(ArrayList<Spending>list){
        this.list = list;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateSpendingList(List<Spending> newList){
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SpendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spending_table_layout,parent, false);
        return new SpendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingViewHolder holder, int position) {
        Spending spending = list.get(position);
        holder.description.setText(spending.getDescription());
        holder.amount.setText(String.valueOf(spending.getAmount()));
        holder.time.setText(spending.getCreated());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SpendingViewHolder extends RecyclerView.ViewHolder{
            private final TextView description;
            private final TextView amount;
            private final TextView time;
            public SpendingViewHolder(@NonNull View itemView){
                super(itemView);
                description = itemView.findViewById(R.id.id_table_descriptionTab);
                amount = itemView.findViewById(R.id.id_table_amountTab);
                time =  itemView.findViewById(R.id.id_table_timeTab);
            }
    }
}
