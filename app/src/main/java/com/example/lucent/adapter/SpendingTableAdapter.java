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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        try{
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "dd-MMM-yyyy h:mm a";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
            StringBuilder date = new StringBuilder(spending.getCreated());
            for(int i=0; i<date.length(); ++i){
                if(date.charAt(i)=='T'){
                    date.setCharAt(i,' ');
                    break;
                }
            }
            holder.time.setText(outputFormat.format(Objects.requireNonNull((inputFormat).parse(date.toString()))));
        }catch (ParseException e){
            holder.time.setText(spending.getCreated());
            e.printStackTrace();
        }
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
