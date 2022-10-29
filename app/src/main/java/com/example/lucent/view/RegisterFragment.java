package com.example.lucent.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucent.R;
import com.example.lucent.databinding.FragmentRegisterBinding;
import com.example.lucent.model.API;
import com.example.lucent.model.RegisterRequest;
import com.example.lucent.model.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class RegisterFragment extends Fragment {

    TextView registerName, registerPhone, registerPassword;
    Button registerBtn, loginBtn;

    private FragmentActivity activity;
    private FragmentRegisterBinding binding;
    private View view;
    Navigator navigator = new Navigator();
    private final API api = new API();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<User> user = new MutableLiveData<>(new User());
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> loadingFailed = new MutableLiveData<>(false);


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = requireActivity();
        activity.setTitle("Register");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        view = binding.getRoot();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Variable Initialization
        registerName = view.findViewById(R.id.registration_name);
        registerPhone = view.findViewById(R.id.registration_phone);
        registerPassword = view.findViewById(R.id.registration_password);
        registerBtn = view.findViewById(R.id.registration_btn);
        loginBtn = view.findViewById(R.id.registration_login_btn);

        // On login button click navigate to login page
        loginBtn.setOnClickListener(View-> navigator.navLogin(activity));

        // On register button click register user
        registerBtn.setOnClickListener(View-> register());
    }

    public void register(){
        System.out.println("NNNNNNNAAAAAAAAAMMMMMMMMMEEEEEEEEEE: " + registerName.getText());
        loading.setValue(true);
        registerBtn.setText("Creating");

        RegisterRequest registerRequest = new RegisterRequest(registerName.getText().toString(), registerPhone.getText().toString(), registerPassword.getText().toString());
        /*
        API Call
         */
        disposable.add(
                api.register(registerRequest)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<User>() {
                            @Override
                            public void onSuccess(User value) {
                                Toast.makeText(getActivity(), "Registration Success", Toast.LENGTH_SHORT).show();
                                navLogin();
                            }

                            @Override
                            public void onError(Throwable e) {
                                loadingFailed.setValue(true);
                                loading.setValue(false);
                                Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                registerBtn.setText("Register");
                                e.printStackTrace();
                            }
                        })
        );
    }

    public void navLogin(){
        Fragment fragment = new LoginFragment();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.id_fragment_controller, fragment);
        fragmentTransaction.addToBackStack(HomeFragment.class.getName());
        fragmentTransaction.commit();
    }
}