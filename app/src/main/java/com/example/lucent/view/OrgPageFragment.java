package com.example.lucent.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucent.R;
import com.example.lucent.adapter.SpendingTableAdapter;
import com.example.lucent.databinding.FragmentOrgPageBinding;
import com.example.lucent.databinding.FragmentOrgPageBindingImpl;
import com.example.lucent.model.Organization;
import com.example.lucent.model.Spending;
import com.example.lucent.viewmodel.OrgPageViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrgPageFragment extends Fragment {

    private FragmentOrgPageBinding binding;
    private View view;
    private OrgPageViewModel viewModel;
    private static final String NAME = "param1";
    private static Organization organization;
    private SpendingTableAdapter spendingTableAdapter;
    private RecyclerView spendingTable;


    public OrgPageFragment() {
    }

    public static OrgPageFragment newInstance(Organization org) {
        OrgPageFragment fragment = new OrgPageFragment();
        organization = org;
        Bundle args = new Bundle();
        args.putString(NAME, org.getName());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setTitle(organization.getName());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_org_page, container, false);
        view = binding.getRoot();
        binding.idOrgpageOrgName.setText(organization.getName());
        binding.idOrgpageBalance.setText("Balance: "+Integer.toString(organization.getBalance()));
        spendingTable = binding.idSpendingTable;
        spendingTableAdapter = new SpendingTableAdapter(new ArrayList<>());
        Picasso.get().load(organization.getProfilePicURL()).into(binding.idOrgpageProfilepic);
        Picasso.get().load(organization.getCoverPicURL()).into(binding.idOrgpageCover);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(OrgPageViewModel.class);
        viewModel.refresh("spending/latest/" + organization.getId());
        spendingTable.setLayoutManager(new LinearLayoutManager(requireContext()));
        spendingTable.setAdapter(spendingTableAdapter);
        observeViewModel();
    }
    private void observeViewModel(){
        viewModel.spendingLiveData.observe(getViewLifecycleOwner(),spending -> {
            spendingTableAdapter.updateSpendingList(spending);
        });
    }
}