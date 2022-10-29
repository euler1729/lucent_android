package com.example.lucent.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.lucent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static Menu menu;
    Fragment selectedFragment;
    Navigator navigator = new Navigator();
    @SuppressLint({"WrongViewCast", "MissingInflatedId", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_controller,new HomeFragment()).commit();
        try{
            bottomNavigationView.setOnItemSelectedListener(item -> {
                switch(item.getItemId()){
                    case R.id.homeFragment:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.myOrgFragment:
                        selectedFragment = new MyOrgFragment();
                        break;
                    case R.id.ProfileFragment:
                        selectedFragment = new ProfileFragment();
                        break;
                    default:
                        selectedFragment = null;
                        break;
                }
                assert selectedFragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_controller,selectedFragment).commit();
                return true;
            });
        }catch (Exception exp){
            exp.getStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu,menu);
        MainActivity.menu = menu;
        updateMenuTitles();
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateMenuTitles() {
        MenuItem bedMenuItem = menu.findItem(R.id.id_action_login);
        SharedPreferences token = this.getSharedPreferences("Token", Context.MODE_PRIVATE);
        String refreshToken = token.getString("refresh_token", null);


        if(refreshToken == null){
            bedMenuItem.setTitle("Sign In");
        }
        else{
            bedMenuItem.setTitle(token.getString("name", ""));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.id_action_login) {
            navigator.navLogin(this);
        }
        return super.onOptionsItemSelected(item);
    }

}