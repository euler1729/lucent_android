package com.example.lucent.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lucent.R;
import com.example.lucent.adapter.LatestDonationTableAdapter;
import com.example.lucent.databinding.FragmentLatestDonationBinding;
import com.example.lucent.viewmodel.OrgPageViewModel;

import java.util.ArrayList;


public class LatestDonationFragment extends Fragment {
    private LatestDonationTableAdapter adapter;
    private OrgPageViewModel viewModel;
    private FragmentLatestDonationBinding binding;
    private RecyclerView recyclerView;
    private static int orgId;

    public LatestDonationFragment() {
    }
    public static LatestDonationFragment newInstance() {
        LatestDonationFragment fragment = new LatestDonationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orgId = requireActivity().getSharedPreferences("ORG_INFO", Context.MODE_PRIVATE).getInt("org_id",0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_latest_donation, container, false);
        viewModel = new ViewModelProvider(this).get(OrgPageViewModel.class);
        recyclerView = binding.idLatestDonationRecycle;
        adapter = new LatestDonationTableAdapter(new ArrayList<>());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.fetchLatestDonation("donation/latest/"+orgId);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        observeViewModel();
    }
    private void observeViewModel(){
        viewModel.latestDonationLiveData.observe(getViewLifecycleOwner(),donation-> adapter.updateLatestDonationList(donation));
    }
}