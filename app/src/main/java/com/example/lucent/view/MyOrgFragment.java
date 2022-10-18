package com.example.lucent.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lucent.R;
import com.example.lucent.adapter.OrgAdapter;
import com.example.lucent.databinding.FragmentMyOrgBinding;
import com.example.lucent.model.Organization;
import com.example.lucent.viewmodel.MyOrgViewModel;

import java.util.ArrayList;

public class MyOrgFragment extends Fragment implements OrgAdapter.ItemClickListener{
    private FragmentActivity activity;
    private View view;
    private MyOrgViewModel viewModel;
    private FragmentMyOrgBinding binding;
    private RecyclerView recyclerView;
    private TextView err;
    private ProgressBar progressBar;
    private OrgAdapter orgAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MyOrgFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = requireActivity();
        activity.setTitle("My Organizations");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_org, container,false);
        view = binding.getRoot();
        err = binding.idErrorMessage;
        recyclerView = binding.topOrgCards;
        progressBar = binding.idLoadingProgressbar;
        swipeRefreshLayout = binding.fragmentMyOrg;
        orgAdapter = new OrgAdapter(new ArrayList<>(),this);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String refresh_token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE).getString("refresh_token",null);
        String access_token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE).getString("refresh_token",null);
        if(refresh_token==null || access_token==null){
            err.setText("Please Login to see your subscribed Organizations.");
            err.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            recyclerView = binding.topOrgCards;
            progressBar.setVisibility(View.VISIBLE);
            viewModel = new ViewModelProvider(this).get(MyOrgViewModel.class);
            viewModel.refresh(activity);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(orgAdapter);
            observeViewModel();
        }
        swipeRefreshLayout.setOnRefreshListener(()->{
            recyclerView.setVisibility(View.GONE);
            err.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            viewModel.refresh(activity);
            swipeRefreshLayout.setRefreshing(false);
        });
    }
    @SuppressLint("SetTextI18n")
    private void observeViewModel(){
        viewModel.orgListLiveData.observe(getViewLifecycleOwner(),organizations -> {
            if (organizations != null) {
                err.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                orgAdapter.updateOrgList(organizations);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        viewModel.orgLoadErr.observe(getViewLifecycleOwner(),loadErr->{
            if(loadErr!=null){
                if(loadErr){
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    err.setText("Refresh please to load.");
                    err.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.loading.observe(getViewLifecycleOwner(),isLoading->{
            if(isLoading!=null){
                if(isLoading){
                    recyclerView.setVisibility(View.GONE);
                    err.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public void onItemClick(Organization organization) {
        try {
            Fragment fragment = OrgPageFragment.newInstance(organization);
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.id_fragment_controller,fragment,"fragment_org_page");
            transaction.addToBackStack(null);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
