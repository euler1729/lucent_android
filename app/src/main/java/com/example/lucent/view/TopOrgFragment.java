package com.example.lucent.view;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.lucent.model.Organization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TopOrgFragment extends Fragment {

    String url = "http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/org/published?page=0&size=10&sortBy=id";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TopOrgFragment() {
    }
    public static TopOrgFragment newInstance(String param1, String param2) {
        TopOrgFragment fragment = new TopOrgFragment();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requireActivity().setTitle("Top Organizations");
        return inflater.inflate(R.layout.fragment_top_org, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getOrgList(view, url);//Shows Top Organisations after fetching data
    }


    //Loads Top Organisations
    public void getOrgList(View view, String url){
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
                buildRecycleView(view, orgList);
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
    private void buildRecycleView(View view, ArrayList<Organization>orgList){
        RecyclerView recyclerView = view.findViewById(R.id.homeOrgCards);
        TopOrgAdapter homeOrgAdapter = new TopOrgAdapter(requireContext(), orgList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(homeOrgAdapter);
        view.findViewById(R.id.id_loading_progressbar).setVisibility(View.GONE);
    }
}