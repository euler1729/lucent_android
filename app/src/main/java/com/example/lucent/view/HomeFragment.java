package com.example.lucent.view;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucent.R;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ImageButton topOrgFragBtn;

    public HomeFragment() {

    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requireActivity().setTitle("Home");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView imgView = (ImageView) view.findViewById(R.id.cover_img);
        imgView.setImageResource(R.drawable.cover_img);
        return view;
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        topOrgFragBtn = (ImageButton) view.findViewById(R.id.id_top_org);
//        topOrgFragBtn.setOnClickListener(v->{
//            goToTopOrgPage();
//        });
    }
//    void goToTopOrgPage(){
//        HomeFragmentDirections.ActionHomeFragmentToTopOrgFragment action = HomeFragmentDirections.actionHomeFragmentToTopOrgFragment("From Home");
//        Navigation.findNavController(topOrgFragBtn).navigate(action);
//    }
}