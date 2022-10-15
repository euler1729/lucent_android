package com.example.lucent.view;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucent.R;
import com.example.lucent.adapter.TopOrgAdapter;
import com.example.lucent.databinding.FragmentHomeBinding;
import com.example.lucent.databinding.FragmentTopOrgBinding;
import com.example.lucent.model.Organization;
import com.example.lucent.viewmodel.TopOrgViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements TopOrgAdapter.ItemClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    private View view;
    private String mParam1;
    private String mParam2;
    ImageButton topOrgFragBtn;

    private TopOrgViewModel viewModel;
    private RecyclerView recyclerView;
    private FragmentHomeBinding binding;
    private TopOrgAdapter orgListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView errTextView;

    public HomeFragment() {

    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requireActivity().setTitle("Home");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView imgView = (ImageView) view.findViewById(R.id.cover_img);
        imgView.setImageResource(R.drawable.cover_img);


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container,false);
        view = binding.getRoot();
        swipeRefreshLayout = binding.fragmentHome;
        swipeRefreshLayout = view.findViewById(R.id.fragment_home);
        progressBar = view.findViewById(R.id.id_loading_progressbar);
        errTextView = view.findViewById(R.id.id_error_message);
        orgListAdapter = new TopOrgAdapter(new ArrayList<>(), this);

        return view;
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TopOrgViewModel.class);
        viewModel.refresh();//->The line is problematic due to api call
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

    private void observeViewModel(){
        viewModel.orgs.observe(getViewLifecycleOwner(), organizations -> {
            if(organizations != null){
                recyclerView.setVisibility(View.VISIBLE);
                orgListAdapter.updateOrgList(organizations);
//                progressBar.setVisibility(View.GONE);
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

    @Override
    public void onItemClick(Organization organization) {//To go orgpage from cards
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