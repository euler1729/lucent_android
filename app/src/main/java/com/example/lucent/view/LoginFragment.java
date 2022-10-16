package com.example.lucent.view;

import android.annotation.SuppressLint;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucent.R;
import com.example.lucent.databinding.FragmentLoginBinding;
import com.example.lucent.databinding.FragmentTopOrgBinding;
import com.example.lucent.model.API;
import com.example.lucent.model.LoginResponse;
import com.example.lucent.viewmodel.LoginViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginFragment extends Fragment {
    private Navigator navigator=new Navigator();
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;
    private View view;
    private String phone=null;
    private String password=null;
    private EditText loginPhone;
    private EditText loginPassword;
    private Button loginBtn, registerBtn;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences token = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
        String refreshToken = token.getString("refresh_token", null);
        if(refreshToken != null){
            Toast.makeText(requireActivity(),"You're logged in!",Toast.LENGTH_SHORT).show();
            navigator.navProfile(requireActivity());
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container,false);
        view = binding.getRoot();
        loginPhone = binding.loginPhone;
        loginPassword =binding.loginPassword;
        loginBtn = binding.loginBtn;
        registerBtn = binding.loginRegisterBtn;
        requireActivity().setTitle("Login");
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginBtn.setOnClickListener(View->{
            phone = String.valueOf(loginPhone.getText());
            password = String.valueOf(loginPassword.getText());
            if(phone.length()!=0 && password.length()!=0){
                loginBtn.setText("Logging in");
                loginViewModel.login(phone,password,requireActivity());
            }
            else{
                Toast.makeText(requireActivity(), "Phone Number or Password can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });
        registerBtn.setOnClickListener(View->{
            navigator.navDonorRegister(requireActivity());
        });
        observeViewModel();
    }
    private void observeViewModel(){
        loginViewModel.loggingIn.observe(getViewLifecycleOwner(),log->{
            loginBtn.setText(log?"Logging in":"Login");
        });
    }
}