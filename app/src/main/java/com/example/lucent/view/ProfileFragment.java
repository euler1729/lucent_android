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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucent.R;
import com.example.lucent.databinding.FragmentDonorProfileBinding;
import com.example.lucent.databinding.FragmentTopOrgBinding;
import com.example.lucent.model.API;
import com.example.lucent.model.LoginResponse;
import com.example.lucent.model.User;
import com.example.lucent.viewmodel.LoginViewModel;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProfileFragment extends Fragment {


    private Navigator navigator = new Navigator();
    private FragmentActivity activity;
    private FragmentDonorProfileBinding binding;
    private View view;
    private final API api = new API();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<User> user = new MutableLiveData<>(new User());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loadingFailed = new MutableLiveData<>(false);
    private TextView name;
    private TextView phone;
    private Button logOutBtn;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String refresh_token = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE).getString("refresh_token",null);
        activity = requireActivity();
        if(refresh_token==null){
            navigator.navLogin(activity);
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_donor_profile, container, false);
        view = binding.getRoot();
        name = binding.profileName;
        phone = binding.profilePhone;
        logOutBtn = binding.logoutBtn;
        activity.setTitle("Profile");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProfile();
        logOutBtn.setOnClickListener(View->{
            logOut();
        });
    }

    public void getProfile() {
        loading.setValue(true);
        SharedPreferences token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        String accessToken = token.getString("access_token", "Token Here");


        disposable.add(
                api.getProfile("Bearer " + accessToken)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<User>() {
                            @Override
                            public void onSuccess(User value) {
                                user.setValue(value);
                                loading.setValue(false);
                                loadingFailed.setValue(false);

                                // Updating Login Title
//                                updateMenuTitles("Profile");

                                // Updating Shared Preferences
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
                                SharedPreferences.Editor storeToken = sharedPreferences.edit();
                                storeToken.putString("name", value.getName());
                                storeToken.putString("phone", value.getPhone());
                                storeToken.apply();

                                // Update menu items
                                MenuItem bedMenuItem = MainActivity.menu.findItem(R.id.id_action_login);
                                bedMenuItem.setTitle(Objects.requireNonNull(user.getValue()).getName());

                                // Updating Page
                                name.setText(user.getValue().getName());
                                phone.setText(user.getValue().getPhone());
//                                navHome();
                            }

                            @Override
                            public void onError(Throwable e) {
                                loadingFailed.setValue(true);
                                loading.setValue(false);
//                                Toast.makeText(getActivity(), "Information Fetch Failed", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                navigator.navLogin(activity);
                            }
                        })
        );
    }

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

        navigator.navHome(activity);
    }

}