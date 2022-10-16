package com.example.lucent.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucent.model.API;
import com.example.lucent.model.Organization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class TopOrgViewModel extends AndroidViewModel {
    String url = "http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/org/published?page=0&size=10&sortBy=id";

    public MutableLiveData<List<Organization>> orgs = new MutableLiveData<List<Organization>>();
//    ArrayList<Organization> orgList = new ArrayList<Organization>();
    public MutableLiveData<Boolean> orgLoadErr = new MutableLiveData<Boolean>(true);
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>(true);

    private final API api = new API();

    private final CompositeDisposable disposable = new CompositeDisposable();

    public TopOrgViewModel(@NonNull Application application) {
        super(application);
    }

    //Fetches card data for
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
                                orgs.setValue(value);
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