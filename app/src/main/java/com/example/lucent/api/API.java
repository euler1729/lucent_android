package com.example.lucent.api;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucent.MainActivity;
import com.example.lucent.model.Organization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class API {
    private RequestQueue queue;

    public ArrayList<Organization> getOrgList(String url, Context context){
        queue =  Volley.newRequestQueue(context);
        ArrayList<Organization> orgList = new ArrayList<>();
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
                        Organization org = new Organization();
                        org.setName(name);
                        org.setDescription(description);
                        org.setProfilePicURL(profilePicURL);
                        org.setCoverPicURL(coverPicURL);
                        orgList.add(org);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Couldn't Load Organizations...",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
        return orgList;
    }


    public void getJsonObject(String url, Context context){
        queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
//                    Log.i("API", response.getString("name"));
//                    String name = response.getString("name");
//                    String coverURL = response.getString("coverPicURL");
//                    String profilePicURL = response.getString("profilePicURL");
//                    String org_name = response.getString("name");
//
//                    orgName.setText(org_name);
//                    Picasso.get().load(coverURL).into(coverImageView);
//                    Picasso.get().load(profilePicURL).into(profileImageView);
                    Log.i("Try test", response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Debugger", error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }
}
