package com.example.lucent.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lucent.R;
import com.example.lucent.adapter.DonationTableAdapter;
import com.example.lucent.adapter.PagerAdapter;
import com.example.lucent.adapter.SpendingTableAdapter;
import com.example.lucent.databinding.FragmentOrgPageBinding;
import com.example.lucent.model.Organization;
import com.example.lucent.viewmodel.OrgPageViewModel;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.sslcommerz.library.payment.model.datafield.MandatoryFieldModel;
import com.sslcommerz.library.payment.model.dataset.TransactionInfo;
import com.sslcommerz.library.payment.model.util.CurrencyType;
import com.sslcommerz.library.payment.model.util.ErrorKeys;
import com.sslcommerz.library.payment.model.util.SdkCategory;
import com.sslcommerz.library.payment.model.util.SdkType;
import com.sslcommerz.library.payment.viewmodel.listener.OnPaymentResultListener;
import com.sslcommerz.library.payment.viewmodel.management.PayUsingSSLCommerz;

import java.util.ArrayList;

public class OrgPageFragment extends Fragment {
    private FragmentOrgPageBinding binding;
    private final String TAG = "SSLC";
    private OrgPageViewModel viewModel;
    private static Organization organization;
    private SpendingTableAdapter spendingTableAdapter;
    private DonationTableAdapter donationTableAdapter;
    private RecyclerView spendingTable;
    private RecyclerView latestDonationTable;
    private Button donateBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;



    public OrgPageFragment() {
    }
    public static OrgPageFragment newInstance(Organization org) {
        OrgPageFragment fragment = new OrgPageFragment();
        organization = org;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("ORG_INFO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("org_id",organization.getId());
        editor.apply();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity activity = requireActivity();
        activity.setTitle(organization.getName());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_org_page, container, false);

        spendingTableAdapter = new SpendingTableAdapter(new ArrayList<>());
        donationTableAdapter = new DonationTableAdapter(new ArrayList<>());

        binding.idOrgpageOrgName.setText(organization.getName());
        binding.idOrgpageBalance.setText("Balance: "+ organization.getBalance() +" BDT ");
        Picasso.get().load(organization.getProfilePicURL()).into(binding.idOrgpageProfilepic);
        Picasso.get().load(organization.getCoverPicURL()).into(binding.idOrgpageCover);

        spendingTable = binding.idSpendingTable;
        latestDonationTable = binding.idLatestDonationRecycle;
        donateBtn = binding.idDonateBtn;
        swipeRefreshLayout = binding.idSwipeOrgPage;
        tabLayout = binding.idTabLayout;
        viewPager = binding.idViewPager;

        tabLayout.addTab(tabLayout.newTab().setText("Spending"));
        tabLayout.addTab(tabLayout.newTab().setText("Latest Donations"));
        tabLayout.addTab(tabLayout.newTab().setText("Top Donations"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(new PagerAdapter(requireActivity()));

        viewModel = new ViewModelProvider(this).get(OrgPageViewModel.class);
        spendingTable.setLayoutManager(new LinearLayoutManager(requireContext()));
        spendingTable.setAdapter(spendingTableAdapter);
        latestDonationTable.setLayoutManager(new LinearLayoutManager(requireContext()));
        latestDonationTable.setAdapter(donationTableAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.refresh("spending/latest/" + organization.getId());
        viewModel.fetchLatestDonation("donation/latest/"+organization.getId());

        swipeRefreshLayout.setOnRefreshListener(()->{
            viewModel.refresh("spending/latest/" + organization.getId());
            viewModel.fetchLatestDonation("donation/latest/"+organization.getId());
            observeViewModel();
            swipeRefreshLayout.setRefreshing(false);
        });
        donateBtn.setOnClickListener(View->{
            pay();
        });
        observeViewModel();
    }
    private void observeViewModel(){
        viewModel.spendingLiveData.observe(getViewLifecycleOwner(),spending -> spendingTableAdapter.updateSpendingList(spending));
        viewModel.latestDonationLiveData.observe(getViewLifecycleOwner(),donation-> donationTableAdapter.updateLatestDonationList(donation));
    }

    private void pay(){
        try {
            MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel("", "", "100", System.currentTimeMillis() + "", CurrencyType.BDT, SdkType.TESTBOX, SdkCategory.BANK_LIST);
            PayUsingSSLCommerz.getInstance().setData(requireActivity(), mandatoryFieldModel, new OnPaymentResultListener() {
                @Override
                public void transactionSuccess(TransactionInfo transactionInfo) {
                    if (transactionInfo.getRiskLevel().equals("0")) {
                        Log.e("Success", transactionInfo.getValId());
                    /* After successful transaction send this val id to your server and from
                         your server you can call this api
                         https://sandbox.sslcommerz.com/validator/api/validationserverAPI.php?val_id=yourvalid&store_id=yourstoreid&store_passwd=yourpassword
                         if you call this api from your server side you will get all the details of the transaction.
                         for more details visit:   www.tashfik.me
                    */
                    } else {// Payment is success but payment is not complete yet. Card on hold now.
                        Log.e(TAG, "Transaction in risk. Risk Title : " + transactionInfo.getRiskTitle());
                    }
                }

                @Override
                public void transactionFail(String s) {
                    Log.e(TAG, "Failed");
                }

                @Override
                public void error(int errorCode) {
                    switch (errorCode) {
                        case ErrorKeys.USER_INPUT_ERROR:// Your provides information is not valid.
                            Log.e(TAG, "User Input Error");
                            break;
                        case ErrorKeys.INTERNET_CONNECTION_ERROR:// Internet is not connected.
                            Log.e(TAG, "Internet Connection Error");
                            break;
                        case ErrorKeys.DATA_PARSING_ERROR:// Server is not giving valid data.
                            Log.e(TAG, "Data Parsing Error");
                            break;
                        case ErrorKeys.CANCEL_TRANSACTION_ERROR:// User press back button or canceled the transaction.
                            Log.e(TAG, "User Cancel The Transaction");
                            break;
                        case ErrorKeys.SERVER_ERROR:// Server is not responding.
                            Log.e(TAG, "Server Error");
                            break;
                        case ErrorKeys.NETWORK_ERROR:// For some reason network is not responding
                            Log.e(TAG, "Network Error");
                            break;
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }
}
