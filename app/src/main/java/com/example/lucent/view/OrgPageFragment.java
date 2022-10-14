package com.example.lucent.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucent.R;
import com.example.lucent.databinding.FragmentOrgPageBindingImpl;
import com.example.lucent.model.Organization;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

public class OrgPageFragment extends Fragment {

    private FragmentOrgPageBindingImpl binding;
    private View view;

    private static final String NAME = "param1";
    private static Organization organization;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_org_page, container, false);
        view = binding.getRoot();
        binding.idOrgpageOrgName.setText(organization.getName());
        binding.idOrgpageBalance.setText("Balance: "+Integer.toString(organization.getBalance()));
        Picasso.get().load(organization.getProfilePicURL()).into(binding.idOrgpageProfilepic);
        Picasso.get().load(organization.getCoverPicURL()).into(binding.idOrgpageCover);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        try{
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment;
                    switch(item.getItemId()){
                        case R.id.homeFragment:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.myOrgFragment:
                            selectedFragment = new MyOrgFragment();
                            break;
                        case R.id.ProfileFragment:
                            selectedFragment = new ProfileFragment();
                            break;
                        default:
                            selectedFragment = null;
                            break;
                    }
                    assert selectedFragment != null;
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_controller,selectedFragment).commit();
                    return true;
                }
            });
        }catch (Exception exp){
            exp.getMessage();
            exp.getStackTrace();
        }
    }
}