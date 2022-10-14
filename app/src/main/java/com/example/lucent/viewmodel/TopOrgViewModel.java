package com.example.lucent.viewmodel;

import static androidx.core.content.ContentProviderCompat.requireContext;

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
    ArrayList<Organization> orgList = new ArrayList<Organization>();
    public MutableLiveData<Boolean> orgLoadErr = new MutableLiveData<Boolean>(true);
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>(true);

    private API api = new API();

    private CompositeDisposable disposable = new CompositeDisposable();

    public TopOrgViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(Context context) {

        getOrgList(url, context);
        orgs.setValue(orgList);
        orgLoadErr.setValue(false);
        loading.setValue(false);
    }

    public void refresh2() {
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

    public void getOrgList(String url, Context context) {
        if (orgList != null) {
            return;
        }
//        orgList = new ArrayList<Organization>();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); ++i) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Organization org = new Organization();
                        org.setId(jsonObject.getInt("id"));
                        org.setName(jsonObject.getString("name"));
                        org.setProfilePicURL(jsonObject.getString("profilePicURL"));
                        org.setCoverPicURL(jsonObject.getString("coverPicURL"));
                        org.setDescription(jsonObject.getString("description"));
                        org.setBalance(jsonObject.getInt("balance"));
                        org.setMemberCount(jsonObject.getInt("memberCount"));
                        orgList.add(org);
//                        Log.i("api call",name+" \nbalance: "+jsonObject.getInt("balance")+" \nMemberCount: "+jsonObject.getInt("memberCount"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    orgList.sort((a, b) -> a.getName().compareTo(b.getName()));
                    Log.i("ORG LIST: ", orgList.get(0).getName());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

}
