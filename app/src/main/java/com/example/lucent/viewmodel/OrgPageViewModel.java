package com.example.lucent.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lucent.model.API;
import com.example.lucent.model.Spending;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class OrgPageViewModel extends AndroidViewModel {
    public MutableLiveData<List<Spending>> spendingLiveData = new MutableLiveData<List<Spending>>();
    ArrayList<Spending> spendings = new ArrayList<Spending>();

    private final API api = new API();
    private final CompositeDisposable disposable = new CompositeDisposable();

    public OrgPageViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(String urlSpending) {
        fetchSpending(urlSpending);
    }

    private void fetchSpending(String url) {
        disposable.add(
                api.getSpendings(url)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<Spending>>() {
                            @Override
                            public void onSuccess(List<Spending> value) {
                                spendingLiveData.setValue(value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        })
        );

    }
}
