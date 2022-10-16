package com.example.lucent.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucent.R;
import com.example.lucent.databinding.FragmentTopOrgBinding;
import com.example.lucent.model.API;
import com.example.lucent.model.LoginResponse;
import com.example.lucent.model.User;
import com.example.lucent.viewmodel.LoginViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProfileFragment extends Fragment {

    TextView name, phone;
    Button logOutBtn;


    private FragmentTopOrgBinding binding;
    private View view;
    private final API api = new API();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<User> user = new MutableLiveData<>(new User());
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> loadingFailed = new MutableLiveData<>(false);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_org, container, false);
        view = binding.getRoot();
        requireActivity().setTitle("Profile");

        return inflater.inflate(R.layout.fragment_donor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.profile_name);
        phone = view.findViewById(R.id.profile_phone);
        logOutBtn = view.findViewById(R.id.logout_btn);
        getProfile();

        logOutBtn.setOnClickListener(View->{
            logOut();
        });
    }

    public void getProfile() {
        loading.setValue(true);
        SharedPreferences token = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
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
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
                                SharedPreferences.Editor storeToken = sharedPreferences.edit();
                                storeToken.putString("name", value.getName());
                                storeToken.putString("phone", value.getPhone());
                                storeToken.commit();

                                // Update menu items
                                MenuItem bedMenuItem = MainActivity.menu.findItem(R.id.id_action_login);
                                bedMenuItem.setTitle(user.getValue().getName());

                                // Updating Page
                                name.setText(user.getValue().getName());
                                phone.setText(user.getValue().getPhone());
                            }

                            @Override
                            public void onError(Throwable e) {
                                loadingFailed.setValue(true);
                                loading.setValue(false);
                                Toast.makeText(getActivity(), "Information Fetch Failed", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        })
        );
    }

    public void logOut(){
        // Updating Shared Preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor storeToken = sharedPreferences.edit();
        storeToken.putString("access_token", null);
        storeToken.putString("refresh_token", null);
        storeToken.commit();

        // Update menu items
        MenuItem bedMenuItem = MainActivity.menu.findItem(R.id.id_action_login);
        bedMenuItem.setTitle("Login");

        navHome();
    }

    public void navHome(){
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.id_fragment_controller, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}