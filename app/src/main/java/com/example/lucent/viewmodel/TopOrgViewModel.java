package com.example.lucent.viewmodel;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lucent.model.Organization;
import com.example.lucent.util.API;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TopOrgViewModel extends AndroidViewModel {
    public MutableLiveData<List<Organization>> orgs = new MutableLiveData<List<Organization>>();
    public MutableLiveData<Boolean> orgLoadErr = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();
    private final API api = new API();
    public TopOrgViewModel(@NonNull Application application) {
        super(application);
    }
    public void refresh(){
//        ArrayList<Organization> arrayList = api.getOrgs("/org/published?page=0&size=10&sortBy=id", context);
//        Toast.makeText(context,arrayList.size(),Toast.LENGTH_SHORT).show();
        ArrayList<Organization> arrayList = new ArrayList<Organization>();

        Organization org1= new Organization(),org2=new Organization(),org3=new Organization();

        org1.setName("1 Taka Meal");
        org1.setProfilePicURL("http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/images/2022-08-15-04-22-47ektakayahar.png");
        org1.setDescription("Support our ongoing mission against food waste.");
        org1.setBalance(100000);
        org1.setCoverPicURL("http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/images/2022-08-15-04-22-47bidyanondo.jpg");
        org2.setName("Team Trees");
        org2.setProfilePicURL("http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/images/2022-08-15-04-40-03teamtrees.png");
        org2.setDescription("Help save the trees. Be a member now with just one click. Every 20/- is one tree planted. Join Now.");
        org2.setBalance(200000);
        org2.setCoverPicURL("http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/images/2022-08-15-04-40-03teamtreescover.jpeg");
        org3.setName("Iccheghuri");
        org3.setProfilePicURL("http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/images/2022-08-15-04-41-53iccheghuri.jpg");
        org3.setDescription( "We dream for a better Bangladesh.");
        org3.setBalance(300000);
        org3.setCoverPicURL("http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/images/2022-08-15-04-41-53bidyanondo2.jpg");
        arrayList.add(org1);
        arrayList.add(org2);
        arrayList.add(org3);

        orgs.setValue(arrayList);
        orgLoadErr.setValue(false);
        loading.setValue(false);
    }
}
