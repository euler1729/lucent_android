package com.example.lucent.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lucent.model.API;
import com.example.lucent.model.Spending;
import com.example.lucent.model.User;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class OrgPageViewModel extends AndroidViewModel {
    public MutableLiveData<List<Spending>> spendingLiveData = new MutableLiveData<>();
    public MutableLiveData<List<User>> latestDonationLiveData = new MediatorLiveData<>();

    private final API api = new API();
    private final CompositeDisposable disposable = new CompositeDisposable();
    SharedPreferences Token;
    SharedPreferences.Editor tokenEditor;
    String refresh_token;
    String access_token;

    public OrgPageViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(String urlSpending) {
        fetchSpending(urlSpending);

    }
    public void fetchLatestDonation(String request){
        disposable.add(
                api.getLatestDonatios(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<User>>() {
                            @Override
                            public void onSuccess(List<User> users) {
                                latestDonationLiveData.setValue(users);
//                                for( User s:users){
//                                    Log.i("name: ",s.getName());
//                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        })
        );
    }
    public void fetchSpending(String url) {
        disposable.add(
                api.getSpendings(url)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<Spending>>() {
                            @Override
                            public void onSuccess(List<Spending> value) {
                                spendingLiveData.setValue(value);
                                for(Spending s:value){
                                    Log.i("desc: ",s.getDescription());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
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
