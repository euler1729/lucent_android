package com.example.lucent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lucent.model.API;
import com.example.lucent.model.LoginResponse;
import com.example.lucent.model.Organization;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {

    public MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>(new LoginResponse());
    public MutableLiveData<Boolean> loggingIn = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> loginFailed = new MutableLiveData<>(false);

    private final API api = new API();
    private final CompositeDisposable disposable = new CompositeDisposable();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

//    public void loginUser(){
//        loggingIn.setValue(true);
//
//        disposable.add(
//                api.login()
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
//                            @Override
//                            public void onSuccess(LoginResponse value) {
//                                loginResponse.setValue(value);
//                                loggingIn.setValue(false);
//                                loginFailed.setValue(false);
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                loginFailed.setValue(true);
//                                loggingIn.setValue(false);
//                                e.printStackTrace();
//                            }
//                        })
//        );
//    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}
