package com.example.lucent.view;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.lucent.R;
import com.example.lucent.adapter.OrgAdapter;
import com.example.lucent.databinding.FragmentHomeBinding;
import com.example.lucent.model.Organization;
import com.example.lucent.viewmodel.HomeViewModel;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements OrgAdapter.ItemClickListener{
    private FragmentActivity activity;
    private HomeViewModel viewModel;
    private RecyclerView recyclerView;
    private FragmentHomeBinding binding;
    private OrgAdapter orgListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView errTextView;
    ImageView imgView;

    public HomeFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("MissingInflatedId")
    @Override// creates View
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = requireActivity();
        activity.setTitle("Lucent");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container,false);
        View view = binding.getRoot();

//        imgView = binding.coverImg;
//        imgView.setImageResource(R.drawable.cover_img);
        swipeRefreshLayout = binding.fragmentHome;
        progressBar = binding.idLoadingProgressbar;
        errTextView = binding.idErrorMessage;
        orgListAdapter = new OrgAdapter(new ArrayList<>(), this);

        return view;
    }
    @Override//handles operations after view creation
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.refresh();

        recyclerView = binding.topOrgCards;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(orgListAdapter);

        progressBar.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setOnRefreshListener(()->{
            recyclerView.setVisibility(View.GONE);
            errTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            viewModel.refresh();
            swipeRefreshLayout.setRefreshing(false);
        });
        observeViewModel();
    }
    //Observes changes in Live Data
    private void observeViewModel(){
        viewModel.orgListLiveData.observe(getViewLifecycleOwner(), organizations -> {
            if(organizations != null){
                recyclerView.setVisibility(View.VISIBLE);
                orgListAdapter.updateOrgList(organizations);
                progressBar.setVisibility(View.GONE);
            }
        });
        viewModel.orgLoadErr.observe(getViewLifecycleOwner(), isErr->{
            if(isErr != null){
                if(isErr){
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    errTextView.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.loading.observe(getViewLifecycleOwner(), isLoading->{
            if(isLoading != null){
                progressBar.setVisibility(isLoading? View.VISIBLE:View.GONE);
                if(isLoading){
                    recyclerView.setVisibility(View.GONE);
                    errTextView.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override//Navigates to Organization details
    public void onItemClick(Organization organization) {//To go org-page from cards
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