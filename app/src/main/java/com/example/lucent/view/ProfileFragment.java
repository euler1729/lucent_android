package com.example.lucent.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lucent.R;
import com.example.lucent.databinding.FragmentDonorProfileBinding;
import com.example.lucent.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private final Navigator navigator = new Navigator();
    private FragmentActivity activity;
    private FragmentDonorProfileBinding binding;
    private View view;
    private ProfileViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView name;
    private TextView phone;
    private Button logOutBtn;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = requireActivity();
        String refresh_token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE).getString("refresh_token",null);
        if(refresh_token==null){
            navigator.navLogin(activity);
        }
        activity.setTitle("Profile");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_donor_profile, container, false);
        view = binding.getRoot();
        swipeRefreshLayout = binding.idDonorProfile;
        name = binding.profileName;
        phone = binding.profilePhone;
        logOutBtn = binding.logoutBtn;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.refresh(activity);
        logOutBtn.setOnClickListener(View-> logOut());
        swipeRefreshLayout.setOnRefreshListener(()->{
            viewModel.refresh(activity);
            swipeRefreshLayout.setRefreshing(false);
        });
        observeViewModel();
    }
    private void observeViewModel(){
        viewModel.loading.observe(getViewLifecycleOwner(),isLoading->{
            if(isLoading!=null && !isLoading){
                viewModel.userLiveData.observe(getViewLifecycleOwner(),user->{
                    name.setText(user.getName());
                    phone.setText(user.getPhone());
                });
            }
        });
        viewModel.err.observe(getViewLifecycleOwner(),error->{
            if(error){
                logOut();
            }
        });
    }

//    public void getProfile() {
//        loading.setValue(true);
//        SharedPreferences token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
//        String accessToken = token.getString("access_token", "Token Here");
//
//
//        disposable.add(
//                api.getProfile("Bearer " + accessToken)
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableSingleObserver<User>() {
//                            @Override
//                            public void onSuccess(User value) {
//                                user.setValue(value);
//                                loading.setValue(false);
//                                loadingFailed.setValue(false);
//
//                                // Updating Shared Preferences
//                                SharedPreferences sharedPreferences = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor storeToken = sharedPreferences.edit();
//                                storeToken.putString("name", value.getName());
//                                storeToken.putString("phone", value.getPhone());
//                                storeToken.apply();
//
//                                // Update menu items
//                                MenuItem bedMenuItem = MainActivity.menu.findItem(R.id.id_action_login);
//                                bedMenuItem.setTitle(Objects.requireNonNull(user.getValue()).getName());
//
//                                // Updating Page
//                                name.setText(user.getValue().getName());
//                                phone.setText(user.getValue().getPhone());
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                refreshAccessToken(activity);
////                                loadingFailed.setValue(true);
////                                loading.setValue(false);
//////                                Toast.makeText(getActivity(), "Information Fetch Failed", Toast.LENGTH_SHORT).show();
////                                e.printStackTrace();
////                                navigator.navLogin(activity);
//                            }
//                        })
//        );
//    }
//    private void refreshAccessToken(FragmentActivity activity){
//        String refresh_token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE).getString("refresh_token", "Token Here");
//        disposable.add(
//                api.getAccessToken("Bearer "+refresh_token)
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
//                            @Override
//                            public void onSuccess(LoginResponse response) {
//                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor storeToken = activity.getSharedPreferences("Token", Context.MODE_PRIVATE).edit();
//                                storeToken.putString("access_token",response.getAccess_token());
//                                Log.i("access_token","accessToken: "+response.getAccess_token());
//                                getProfile();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
////                                orgLoadErr.setValue(true);
//                                logOut();
//                                loading.setValue(false);
//                                e.printStackTrace();
//                            }
//                        })
//        );
//    }

    public void logOut(){
        // Updating Shared Preferences
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor storeToken = sharedPreferences.edit();
        storeToken.putString("access_token", null);
        storeToken.putString("refresh_token", null);
        storeToken.apply();

        // Update menu items
        MenuItem bedMenuItem = MainActivity.menu.findItem(R.id.id_action_login);
        bedMenuItem.setTitle("Login");

        navigator.navLogin(activity);
    }

}