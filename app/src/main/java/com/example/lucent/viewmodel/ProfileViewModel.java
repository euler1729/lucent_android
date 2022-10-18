package com.example.lucent.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lucent.R;
import com.example.lucent.model.API;
import com.example.lucent.model.LoginResponse;
import com.example.lucent.model.User;
import com.example.lucent.view.MainActivity;
import com.example.lucent.view.Navigator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends AndroidViewModel {
    public MutableLiveData<User> userLiveData = new MediatorLiveData<>();
    public MutableLiveData<Boolean> err = new MediatorLiveData<>(false);
    public MutableLiveData<Boolean> loading = new MutableLiveData<>(true);

    private final API api = new API();
    private final CompositeDisposable disposable = new CompositeDisposable();
    Navigator navigator = new Navigator();
    SharedPreferences Token;
    SharedPreferences.Editor editToken;
    String access_token;
    String refresh_token;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(FragmentActivity activity){
        Token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        editToken = Token.edit();
        access_token = Token.getString("access_token",null);
        refresh_token = Token.getString("refresh_token",null);
        if(access_token==null){
            if(refresh_token == null){
                navigator.navLogin(activity);
            }
            else{
                refreshAccessToken(activity);
            }
        }
        else{
            profileRefresh(activity);
        }
    }

    @SuppressLint("CheckResult")
    private void profileRefresh(FragmentActivity activity){
        disposable.add(
            api.getProfile("Bearer "+access_token)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<User>() {
                        @Override
                        public void onSuccess(User user) {
                            userLiveData.setValue(user);
                            editToken.putString("name",user.getName());
                            editToken.putString("phone",user.getPhone());
                            editToken.apply();
                            MainActivity.menu.findItem(R.id.id_action_login)
                                    .setTitle(user.getName());
                            err.setValue(false);
                            loading.setValue(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            refreshAccessToken(activity);
                        }
                    }
            )
        );
    }
    private void refreshAccessToken(FragmentActivity activity){
        disposable.add(
                api.getAccessToken("Bearer "+refresh_token)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                            @Override
                            public void onSuccess(LoginResponse response) {
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor storeToken = activity.getSharedPreferences("Token", Context.MODE_PRIVATE).edit();
                                storeToken.putString("access_token",response.getAccess_token());
                                access_token = response.getAccess_token();
                                storeToken.apply();
                                Log.i("access_token","accessToken: "+response.getAccess_token());
                                refresh(activity);
                            }

                            @Override
                            public void onError(Throwable e) {
//                                orgLoadErr.setValue(true);
                                loading.setValue(false);
                                err.setValue(true);
                                e.printStackTrace();
                            }
                        }
                )
        );
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
