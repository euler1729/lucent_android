package com.example.lucent.adapter;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lucent.R;
import com.example.lucent.model.Organization;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Locale;


public class TopOrgAdapter extends RecyclerView.Adapter<TopOrgAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Organization> orgModelArrayList;

    //Constructor
    public TopOrgAdapter(Context context, ArrayList<Organization> orgModelArrayList) {
        this.context = context;
        this.orgModelArrayList = orgModelArrayList;
    }

    @NonNull
    @Override
    public TopOrgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orgcard_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TopOrgAdapter.ViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        Organization org = orgModelArrayList.get(position);
        holder.orgName.setText(org.getName().toUpperCase(Locale.ROOT));
        holder.orgDesc.setText(org.getDescription());
        holder.rank.setText("RANK:  "+(++position));
        Picasso.get().load(org.getProfilePicURL()).into(holder.profileImg);
    }

    @Override
    public int getItemCount() {
        return orgModelArrayList.size();
    }

    // View holder class for initializing views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profileImg;
        private final TextView orgName;
        private final TextView orgDesc;
        private final TextView rank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.idCardProfileImg);
            orgName = itemView.findViewById(R.id.idCardText);
            orgDesc = itemView.findViewById(R.id.idOrgCardDesc);
            rank = itemView.findViewById(R.id.id_rank);
        }
    }
}
