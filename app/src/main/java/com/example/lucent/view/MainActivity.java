package com.example.lucent.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.lucent.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    NavController controller;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.id_fragment_controller);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, controller);
//        try{
//            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//                @SuppressLint("NonConstantResourceId")
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    Fragment selectedFragment;
//                    switch(item.getItemId()){
//                        case R.id.homeFragment:
//                            selectedFragment = new HomeFragment();
//                            break;
//                        case R.id.myOrgFragment:
//                            selectedFragment = new MyOrgFragment();
//                            break;
//                        case R.id.ProfileFragment:
//                            selectedFragment = new ProfileFragment();
//                            break;
//                        default:
//                            selectedFragment = null;
//                            break;
//                    }
//                    assert selectedFragment != null;
//                    getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_controller,selectedFragment).commit();
//                    return true;
//                }
//            });
//        }catch (Exception exp){
//            exp.getMessage();
//            exp.getStackTrace();
//        }
//        toolbar = findViewById();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.id_action_login) {
            Toast.makeText(this,"Login Button Clicked!",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}