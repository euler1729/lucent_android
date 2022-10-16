package com.example.lucent.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.lucent.model.API;
import com.example.lucent.model.Organization;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class TopOrgViewModel extends AndroidViewModel {
    public MutableLiveData<List<Organization>> orgListLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> orgLoadErr = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> loading = new MutableLiveData<>(true);

    private final API api = new API();
    private final CompositeDisposable disposable = new CompositeDisposable();
    public TopOrgViewModel(@NonNull Application application) {
        super(application);
    }

    //Fetches card data from api of top organizations
    public void refresh() {
        fetchFromRemote();
    }

    @SuppressLint("CheckResult")
    private void fetchFromRemote() {
        loading.setValue(true);
        disposable.add(
                api.getOrgs()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<Organization>>() {
                            @Override
                            public void onSuccess(List<Organization> value) {
                                orgListLiveData.setValue(value);
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