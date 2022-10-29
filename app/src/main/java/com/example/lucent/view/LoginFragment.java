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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucent.R;
import com.example.lucent.databinding.FragmentLoginBinding;
import com.example.lucent.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {
    private final Navigator navigator=new Navigator();
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;
    private View view;
    private FragmentActivity activity;
    private String phone=null;
    private String password=null;
    private EditText loginPhone;
    private EditText loginPassword;
    private Button loginBtn, registerBtn;

    public LoginFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = requireActivity();
        SharedPreferences token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        String refreshToken = token.getString("refresh_token", null);
        if(refreshToken != null){
            Toast.makeText(activity,"You're logged in!",Toast.LENGTH_SHORT).show();
            navigator.navProfile(activity);
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container,false);
        view = binding.getRoot();
        loginPhone = binding.loginPhone;
        loginPassword =binding.loginPassword;
        loginBtn = binding.loginBtn;
        registerBtn = binding.loginRegisterBtn;
        activity.setTitle("Sign In");
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
                loginViewModel.login(phone,password,activity);
            }
            else{
                Toast.makeText(activity, "Phone Number or Password can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });
        registerBtn.setOnClickListener(View-> navigator.navDonorRegister(activity));
        observeViewModel();
    }
    private void observeViewModel(){
        loginViewModel.loggingIn.observe(getViewLifecycleOwner(),log-> loginBtn.setText(log?"Logging in":"Login"));
    }
}