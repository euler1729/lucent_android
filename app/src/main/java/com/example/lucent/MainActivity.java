package com.example.lucent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucent.adapter.HomeOrgAdapter;
import com.example.lucent.api.API;
import com.example.lucent.model.Organization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @SuppressLint("SetJavaScriptEnabled")

    String url = "http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/org/published?page=0&size=10&sortBy=id";
    private ProgressBar loadingPB;
    ArrayList<Organization> orgList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        //Loads Landing page cover image
        ImageView imgView = (ImageView) findViewById(R.id.cover_img);
        imgView.setImageResource(R.drawable.cover_img);

        //Load loading modal
        loadingPB = findViewById(R.id.idLoadingPB);

//        API api = new API();
        getOrgList(url);
    }

    public void getOrgList(String url){
        RequestQueue queue =  Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); ++i) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String description = jsonObject.getString("description");
                        String profilePicURL = jsonObject.getString("profilePicURL");
                        String coverPicURL = jsonObject.getString("coverPicURL");
                        Log.i("api call",name);
                        Organization org = new Organization();
                        org.setName(name);
                        org.setDescription(description);
                        org.setProfilePicURL(profilePicURL);
                        org.setCoverPicURL(coverPicURL);
                        org.setDescription(description);
                        orgList.add(org);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("api2",String.valueOf(orgList.size()));
                    buildRecycleView(orgList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }
    private void buildRecycleView(ArrayList<Organization>orgList){
        RecyclerView recyclerView = findViewById(R.id.homeOrgCards);
        HomeOrgAdapter homeOrgAdapter = new HomeOrgAdapter(MainActivity.this, orgList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (MainActivity.this,
                        LinearLayoutManager.VERTICAL,
                        false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(homeOrgAdapter);
        loadingPB.setVisibility(View.GONE);
    }


}