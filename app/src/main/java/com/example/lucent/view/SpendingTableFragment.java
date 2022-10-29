package com.example.lucent.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.lucent.R;
import com.example.lucent.adapter.SpendingTableAdapter;
import com.example.lucent.databinding.FragmentSpendingTableBinding;
import com.example.lucent.viewmodel.OrgPageViewModel;

import java.util.ArrayList;


public class SpendingTableFragment extends Fragment {
    SpendingTableAdapter spendingTableAdapter;
    ViewPager2 viewPager2;
    OrgPageViewModel viewModel;
    FragmentSpendingTableBinding binding;
    private RecyclerView spendingTable;
    private static int orgId;


    public SpendingTableFragment() {
        // Required empty public constructor
    }

    public static SpendingTableFragment newInstance() {
        SpendingTableFragment fragment = new SpendingTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("ORG_INFO", Context.MODE_PRIVATE);
        orgId = sharedPreferences.getInt("org_id",0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_spending_table, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(OrgPageViewModel.class);
        spendingTable = binding.idSpendingTable;
        spendingTableAdapter = new SpendingTableAdapter(new ArrayList<>());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.refresh("spending/latest/" + orgId);
        spendingTable.setLayoutManager(new LinearLayoutManager(requireContext()));
        spendingTable.setAdapter(spendingTableAdapter);
        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.spendingLiveData.observe(getViewLifecycleOwner(),spending -> spendingTableAdapter.updateSpendingList(spending));
    }
}