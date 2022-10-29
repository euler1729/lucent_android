package com.example.lucent.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
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

public class MyOrgViewModel extends AndroidViewModel {
    public MutableLiveData<List<Organization>> orgListLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> orgLoadErr = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private final API api = new API();
    SharedPreferences token;
    private String refresh_token;
    private String access_token;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public MyOrgViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(FragmentActivity activity){
        token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        fetchData(activity);
    }

    private void fetchData(FragmentActivity activity){
        access_token = token.getString("access_token", "Token Here");
        disposable.add(
                api.getMyOrgs("Bearer "+access_token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Organization>>() {
                    @Override
                    public void onSuccess(List<Organization> organizations) {
                        orgListLiveData.setValue(organizations);
                        orgLoadErr.setValue(false);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshAccessToken(activity);
                    }
                })
        );
    }
    private void refreshAccessToken(FragmentActivity activity){
        refresh_token = token.getString("refresh_token", "Token Here");
        disposable.add(
                api.getAccessToken("Bearer "+refresh_token)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                            @Override
                            public void onSuccess(LoginResponse response) {
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor storeToken = token.edit();
                                storeToken.putString("access_token",response.getAccess_token());
                                storeToken.apply();
                                Log.i("access_token","accessToken: "+response.getAccess_token());
                                fetchData(activity);
                            }

                            @Override
                            public void onError(Throwable e) {
                                orgLoadErr.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
