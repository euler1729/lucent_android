package com.example.lucent.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lucent.R;
import com.example.lucent.model.API;
import com.example.lucent.model.LoginResponse;
import com.example.lucent.model.Membership;
import com.example.lucent.model.MembershipRequest;
import com.example.lucent.model.OTPResponse;
import com.example.lucent.model.OTPresendResponse;
import com.example.lucent.model.Organization;
import com.example.lucent.model.PayRequest;
import com.example.lucent.model.Payment;
import com.example.lucent.model.Spending;
import com.example.lucent.model.User;
import com.example.lucent.model.OTPRequest;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class OrgPageViewModel extends AndroidViewModel {
    public MutableLiveData<List<Spending>> spendingLiveData = new MutableLiveData<>();
    public MutableLiveData<List<User>> latestDonationLiveData = new MediatorLiveData<>();
    public MutableLiveData<Integer> check = new MediatorLiveData<>(-1);
    public MutableLiveData<Integer> paychek = new MediatorLiveData<>(0);
    public MutableLiveData<Integer> membershipReqCheck = new MediatorLiveData<>(0);
    private final API api = new API();
    private final CompositeDisposable disposable = new CompositeDisposable();
    SharedPreferences Token;
    SharedPreferences.Editor tokenEditor;
    String refresh_token;
    String access_token;
    Boolean verified;
    Dialog dialog;
    boolean loggedin = false;
    public OrgPageViewModel(@NonNull Application application) {
        super(application);
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
    public void checkMembership(FragmentActivity activity, int orgId){

        Token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        access_token = Token.getString("access_token", null);
        refresh_token = Token.getString("refresh_token",null);
        verified = Token.getBoolean("verified", false);
        System.out.println("verified here = " + verified);
        if(refresh_token==null){
            check.setValue(0);//not logged in
            return;
        }
        else if(verified == false) {
            check.setValue(4); // 4 means not verified
            return;
        }
        disposable.add(
                api.checkMembersip("Bearer "+access_token, "membership/check/"+orgId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Membership>(){
                            @Override
                            public void onSuccess(Membership membership) {
                                loggedin = true;
                                if(membership.isApproved()) check.setValue(2);//2 means already member
                                else check.setValue(1);//value 1 means membership pending
                                Log.e("Checkmembership", membership.isApproved()?"YEs":"No");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("Checkmembership", "error");
                                if(loggedin){
                                    check.setValue(3);//3 means not a member
                                    return;
                                }
                                refreshAccessToken(activity,orgId);
                            }
                        }
                )
        );
    }
    private void refreshAccessToken(FragmentActivity activity,int orgId){
        refresh_token = Token.getString("refresh_token", "Token Here");
        disposable.add(
                api.getAccessToken("Bearer "+refresh_token)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                            @Override
                            public void onSuccess(LoginResponse response) {
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor storeToken = Token.edit();
                                storeToken.putString("access_token",response.getAccess_token());
                                storeToken.apply();
                                Log.i("access_token","accessToken: "+response.getAccess_token());
                                loggedin = true;
                                checkMembership(activity,orgId);
                            }

                            @Override
                            public void onError(Throwable e) {
                                check.setValue(0);//0 means not logged in
                                loggedin = false;
                                e.printStackTrace();
                            }
                        })
        );
    }
    public void requestPayment(FragmentActivity activity, PayRequest payRequest){
        Token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        access_token = Token.getString("access_token", null);
        Log.i("Payment","request payment");
        disposable.add(
                api.donate("Bearer "+access_token,payRequest)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Payment>(){

                            @Override
                            public void onSuccess(Payment payment) {
                                Log.i("Payment",String.valueOf(payment.getAmount()));
                                paychek.setValue(1);
                            }

                            @Override
                            public void onError(Throwable e) {
                                paychek.setValue(2);
                                Log.i("Payment","Failed!");
                            }
                        })

        );
    }
    public void verifyAccount(FragmentActivity activity, OTPRequest otpRequest){
        Token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor storeToken = activity.getSharedPreferences("Token", Context.MODE_PRIVATE).edit();
        access_token = Token.getString("access_token", null);

        disposable.add(
                api.verifyOTP("Bearer "+access_token, otpRequest.getPhone(), otpRequest.getCode())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<OTPResponse>(){
                                           @Override
                                           public void onSuccess(OTPResponse otpResponse) {
                                               Toast.makeText(activity,"Verification Success",Toast.LENGTH_SHORT).show();

                                               Token.edit().putBoolean("verified", true).apply();
                                               System.out.println("Token verified = " + Token.getBoolean("verified", false));

                                               check.setValue(-1);
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
//                                               membershipReqCheck.setValue(2);
                                           }
                                       }
                        )
        );
    }

    public void resendOTP(FragmentActivity activity){
        Token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        access_token = Token.getString("access_token", null);

        disposable.add(
                api.resendOTP("Bearer "+access_token)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<OTPresendResponse>(){
                                           @Override
                                           public void onSuccess(OTPresendResponse response) {
                                               Toast.makeText(activity,"OTP sent",Toast.LENGTH_SHORT).show();
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
                                           }
                                       }
                        )
        );
    }


    public void requestMembership(FragmentActivity activity, MembershipRequest membershipRequest){
        Token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        access_token = Token.getString("access_token", null);
        disposable.add(
                api.requestMembership("Bearer "+access_token,membershipRequest)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Membership>(){

                            @Override
                            public void onSuccess(Membership membership) {
                                membershipReqCheck.setValue(1);
                            }

                            @Override
                            public void onError(Throwable e) {
                                membershipReqCheck.setValue(2);
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
