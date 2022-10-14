package com.example.lucent.view;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavAction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucent.R;
import com.example.lucent.adapter.TopOrgAdapter;
import com.example.lucent.databinding.FragmentTopOrgBinding;
import com.example.lucent.model.Organization;
import com.example.lucent.viewmodel.TopOrgViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TopOrgFragment extends Fragment implements TopOrgAdapter.ItemClickListener{
    String url = "http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/org/published?page=0&size=10&sortBy=id";
    private FragmentTopOrgBinding binding;
    private View view;
    private TopOrgAdapter orgListAdapter;

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
        progressBar = binding.idLoadingProgressbar;
        orgListAdapter = new TopOrgAdapter(new ArrayList<>(), this);
        requireActivity().setTitle("Top Organizations");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TopOrgViewModel.class);
        viewModel.refresh();//->The line is problematic due to api call
        recyclerView = binding.topOrgCards;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(orgListAdapter);
        observeViewModel();
//        getOrgList(url);
    }
    private void observeViewModel(){
        viewModel.orgs.observe(getViewLifecycleOwner(), organizations -> {
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
            transaction.replace(R.id.id_fragment_controller,fragment,"fragment_org_page");
            transaction.addToBackStack(null);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }

    }

    //Loads Top Organisations
    public void getOrgList(String url){
        ArrayList<Organization> orgList = new ArrayList<>();
        RequestQueue queue =  Volley.newRequestQueue(requireContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); ++i) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String description = jsonObject.getString("description");
                        String profilePicURL = jsonObject.getString("profilePicURL");
                        String coverPicURL = jsonObject.getString("coverPicURL");
                        Organization org = new Organization();
                        org.setName(name);
                        org.setProfilePicURL(profilePicURL);
                        org.setCoverPicURL(coverPicURL);
                        org.setDescription(description);
                        orgList.add(org);
//                        Log.i("api call",name+" \nbalance: "+jsonObject.getInt("balance")+" \nMemberCount: "+jsonObject.getInt("memberCount"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    orgList.sort((a,b)->a.getName().compareTo(b.getName()));
                }
                buildRecycleView(orgList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    //Inserts Organizations list into Card adapter
    private void buildRecycleView(ArrayList<Organization>orgList){
        orgListAdapter = new TopOrgAdapter(orgList,this);
        recyclerView.setAdapter(orgListAdapter);
        progressBar.setVisibility(View.GONE);
    }



}