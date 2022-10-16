package com.example.lucent.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lucent.R;
import com.example.lucent.databinding.FragmentMyOrgBinding;
import com.example.lucent.viewmodel.MyOrgViewModel;

public class MyOrgFragment extends Fragment {
    private View view;
    private MyOrgViewModel viewModel;
    private FragmentMyOrgBinding binding;
    private RecyclerView recyclerView;
    private TextView err;
    private ProgressBar progressBar;


    public MyOrgFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setTitle("My Organizations");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_org, container,false);
        view = binding.getRoot();
        err = binding.idErrorMessage;
        recyclerView = binding.topOrgCards;
        progressBar = binding.idLoadingProgressbar;
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String refresh_token = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE).getString("refresh_token",null);
        String access_token = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE).getString("refresh_token",null);
        if(refresh_token==null || access_token==null){
            err.setText("Please Login to see your subscribed Organizations.");
            err.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}