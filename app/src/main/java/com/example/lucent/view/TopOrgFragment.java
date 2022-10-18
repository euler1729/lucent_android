package com.example.lucent.view;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.lucent.R;
import com.example.lucent.adapter.OrgAdapter;
import com.example.lucent.databinding.FragmentTopOrgBinding;
import com.example.lucent.model.Organization;
import com.example.lucent.viewmodel.TopOrgViewModel;

import java.util.ArrayList;
import java.util.List;

public class TopOrgFragment extends Fragment implements OrgAdapter.ItemClickListener{
    String url = "http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/org/published?page=0&size=10&sortBy=id";
    private FragmentTopOrgBinding binding;
    private View view;
    private OrgAdapter orgListAdapter;

    private TopOrgViewModel viewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    public TopOrgFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_org, container,false);
        view = binding.getRoot();
        swipeRefreshLayout = view.findViewById(R.id.id_top_org_swipe);
        progressBar = binding.idLoadingProgressbar2;
        orgListAdapter = new OrgAdapter(new ArrayList<>(), this);
        requireActivity().setTitle("Top Organizations");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TopOrgViewModel.class);
//        viewModel.refresh();//->The line is problematic due to api call
        recyclerView = binding.topOrgCards;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(orgListAdapter);
        observeViewModel();
//        getOrgList(url);
    }
    private void observeViewModel(){
        viewModel.orgListLiveData.observe(getViewLifecycleOwner(), organizations -> {
            if(organizations != null && organizations instanceof List){
                recyclerView.setVisibility(View.VISIBLE);
                orgListAdapter.updateOrgList(organizations);
            }
        });
//        viewModel.orgLoadErr.observe(this, isErr->{
//            if(isErr!=null && isErr instanceof Boolean){
//
//            }
//        });
        viewModel.loading.observe(getViewLifecycleOwner(), isLoading->{
            if(isLoading!=null && isLoading instanceof Boolean){
                progressBar.setVisibility(isLoading? View.VISIBLE:View.GONE);
                if(isLoading){
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Organization organization) {
//        Toast toast = Toast.makeText(getActivity(),organization.getName(),Toast.LENGTH_SHORT);
//        toast.show();
        try {
            Fragment fragment = OrgPageFragment.newInstance(organization);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//            transaction.hide(this);
            transaction.replace(R.id.id_top_org,fragment,"fragment_org_page").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack(null);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }

    }

    //Loads Top Organisations

    //Inserts Organizations list into Card adapter
    private void buildRecycleView(ArrayList<Organization>orgList){
        orgListAdapter = new OrgAdapter(orgList,this);
        recyclerView.setAdapter(orgListAdapter);
        progressBar.setVisibility(View.GONE);
    }



}