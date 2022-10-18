package com.example.lucent.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lucent.model.API;
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
    private final CompositeDisposable disposable = new CompositeDisposable();

    public MyOrgViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(FragmentActivity activity){
        fetchData(activity);
    }

    private void fetchData(FragmentActivity activity){
        SharedPreferences token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        String accessToken = token.getString("access_token", "Token Here");
        disposable.add(
                api.getMyOrgs("Bearer "+accessToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Organization>>() {
                    @Override
                    public void onSuccess(List<Organization> organizations) {
//                        Log.i("MyOrg: ",organizations.get(0).getName());
                        orgListLiveData.setValue(organizations);
                        orgLoadErr.setValue(false);
                        loading.setValue(false);
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
