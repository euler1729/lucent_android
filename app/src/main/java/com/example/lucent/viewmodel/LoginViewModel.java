package com.example.lucent.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lucent.R;
import com.example.lucent.model.API;
import com.example.lucent.model.LoginResponse;
import com.example.lucent.model.User;
import com.example.lucent.view.MainActivity;
import com.example.lucent.view.Navigator;

import java.util.Objects;

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
    Navigator navigator = new Navigator();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void login(String phone, String password, FragmentActivity activity) {
        userLogin(phone, password, activity);
    }

    private void userLogin(String phone, String password, FragmentActivity activity) {
        loggingIn.setValue(true);
        loginFailed.setValue(false);
        disposable.add(
                api.login(phone, password)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                            @Override
                            public void onSuccess(LoginResponse value) {
                                loginResponse.setValue(value);
                                Log.i("Logging in: ", value.getAccess_token() + " " + value.getRefresh_token());
                                // Updating Shared Preferences
                                SharedPreferences.Editor storeToken = activity.getSharedPreferences("Token", Context.MODE_PRIVATE).edit();
                                storeToken.putString("access_token", value.getAccess_token());
                                storeToken.putString("refresh_token", value.getRefresh_token());
                                storeToken.apply();
                                getProfile(activity);
                            }

                            @Override
                            public void onError(Throwable e) {
                                loginFailed.setValue(true);
                                loggingIn.setValue(false);
                                e.printStackTrace();
//                                loginBtn.setText("Log in");
                                Toast.makeText(activity, "Log in Failed", Toast.LENGTH_SHORT).show();
                            }
                        })
        );

//        To access Token
//        SharedPreferences token = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
//        String refreshToken = token.getString("refresh_token", "Token Here");

    }

    private void getProfile(FragmentActivity activity) {
        SharedPreferences token = activity.getSharedPreferences("Token", Context.MODE_PRIVATE);
        String accessToken = token.getString("access_token", "Token Here");
        disposable.add(
                api.getProfile("Bearer " + accessToken)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<User>() {
                            @Override
                            public void onSuccess(User value) {
                                SharedPreferences.Editor storeToken = token.edit();
                                storeToken.putString("name", value.getName());
                                storeToken.putString("phone", value.getPhone());
                                storeToken.apply();
                                // Update menu items
                                MenuItem bedMenuItem = MainActivity.menu.findItem(R.id.id_action_login);
                                bedMenuItem.setTitle(Objects.requireNonNull(value.getName()));
                                loggingIn.setValue(false);
                                loginFailed.setValue(false);
                                navigator.navHome(activity);
                            }

                            @Override
                            public void onError(Throwable e) {
                                loginFailed.setValue(true);
                                Toast.makeText(activity, "Information Fetch Failed", Toast.LENGTH_SHORT).show();
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
