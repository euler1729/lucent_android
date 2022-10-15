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
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucent.R;
import com.example.lucent.databinding.FragmentTopOrgBinding;
import com.example.lucent.model.API;
import com.example.lucent.model.LoginResponse;
import com.example.lucent.viewmodel.LoginViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginFragment extends Fragment {

    EditText loginPhone, loginPassword;
    Button loginBtn, registerBtn;

    private LoginViewModel loginViewModel;
    private FragmentTopOrgBinding binding;
    private View view;


    private final API api = new API();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>(new LoginResponse());
    private MutableLiveData<Boolean> loggingIn = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> loginFailed = new MutableLiveData<>(false);

    private String phone, password;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_org, container,false);
        view = binding.getRoot();

        // Inflate the layout for this fragment
        requireActivity().setTitle("Login");

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        /*
        Redirect to profile if refresh_token is found
         */
        SharedPreferences token = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
        String refreshToken = token.getString("refresh_token", null);
        if(refreshToken != null){
            navProfile();
        }


        // Variable initialization
        loginPhone = view.findViewById(R.id.login_phone);
        loginPassword = view.findViewById(R.id.login_password);
        loginBtn = view.findViewById(R.id.login_btn);
        registerBtn = view.findViewById(R.id.login_register_btn);

        //  Events
        loginBtn.setOnClickListener(View->{
            login();
        });

        registerBtn.setOnClickListener(View->{
            navToRegister();
        });
    }

    public void login(){
        phone = String.valueOf(loginPhone.getText());
        password = String.valueOf(loginPassword.getText());
        loggingIn.setValue(true);
        loginBtn.setText("Loading");

        /*
        API Call
         */
        disposable.add(
                api.login(phone, password)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                            @Override
                            public void onSuccess(LoginResponse value) {
                                loginResponse.setValue(value);
                                loggingIn.setValue(false);
                                loginFailed.setValue(false);

                                // Updating Shared Preferences
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
                                SharedPreferences.Editor storeToken = sharedPreferences.edit();
                                storeToken.putString("access_token", value.getAccess_token());
                                storeToken.putString("refresh_token", value.getRefresh_token());
                                storeToken.commit();

                                // Navigating to my org
                                navProfile();
                            }

                            @Override
                            public void onError(Throwable e) {
                                loginFailed.setValue(true);
                                loggingIn.setValue(false);
                                e.printStackTrace();
                                loginBtn.setText("Log in");
                                Toast.makeText(getActivity(), "Log in Failed", Toast.LENGTH_SHORT).show();
                            }
                        })
        );

//        To access Token
//        SharedPreferences token = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
//        String refreshToken = token.getString("refresh_token", "Token Here");
    }



    public void navToRegister(){
        Fragment fragment = new RegisterFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.id_fragment_controller, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void navProfile(){
        Fragment fragment = new ProfileFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.id_fragment_controller, fragment);
        fragmentTransaction.addToBackStack(HomeFragment.class.getName());
        fragmentTransaction.commit();
    }


}