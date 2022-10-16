package com.example.lucent.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.lucent.R;

public class Navigator {
    private Fragment fragment;
    public void navHome(FragmentActivity activity){
        fragment = new HomeFragment();
        commit(activity,fragment);
    }
    public void navMyOrg(FragmentActivity activity){
        fragment = new MyOrgFragment();
        commit(activity,fragment);
    }
    public void navProfile(FragmentActivity activity){
        fragment = new ProfileFragment();
        commit(activity,fragment);
    }
    public void navDonorRegister(FragmentActivity activity){
        fragment = new RegisterFragment();
        commit(activity,fragment);
    }
    public void navLogin(FragmentActivity activity){
        fragment = new LoginFragment();
        commit(activity,fragment);
    }
    private void commit(FragmentActivity activity, Fragment fragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.id_fragment_controller, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
