package com.example.lucent.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucent.R;
import com.example.lucent.adapter.PagerAdapter;
import com.example.lucent.databinding.FragmentOrgPageBinding;
import com.example.lucent.model.API;
import com.example.lucent.model.MembershipRequest;
import com.example.lucent.model.Organization;
import com.example.lucent.model.PayRequest;
import com.example.lucent.util.PopupDialog;
import com.example.lucent.viewmodel.OrgPageViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;
import com.sslcommerz.library.payment.model.datafield.MandatoryFieldModel;
import com.sslcommerz.library.payment.model.dataset.TransactionInfo;
import com.sslcommerz.library.payment.model.util.CurrencyType;
import com.sslcommerz.library.payment.model.util.ErrorKeys;
import com.sslcommerz.library.payment.model.util.SdkCategory;
import com.sslcommerz.library.payment.model.util.SdkType;
import com.sslcommerz.library.payment.viewmodel.listener.OnPaymentResultListener;
import com.sslcommerz.library.payment.viewmodel.management.PayUsingSSLCommerz;

import io.reactivex.disposables.CompositeDisposable;

public class OrgPageFragment extends Fragment{
    private Navigator navigator = new Navigator();
    private  OrgPageViewModel viewModel;
    private FragmentOrgPageBinding binding;
    private final String TAG = "SSLC";
    private static Organization organization;
    private Button donateBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Dialog dialog;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog pay_window;
    private Button paynow;
    private EditText amount;
    private AlertDialog failed_window;
    private AlertDialog success_window;
    private AlertDialog logDialog;
    private Button logPopBtn;

    private AlertDialog membership_req_window;
    private Button send_req_btn;
    private EditText nid;
    private EditText membershipCode;

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
        viewModel = new ViewModelProvider(this).get(OrgPageViewModel.class);
        binding.idOrgpageOrgName.setText(organization.getName());
        binding.idOrgpageBalance.setText("Balance: "+ organization.getBalance() +" BDT ");
        Picasso.get().load(organization.getProfilePicURL()).into(binding.idOrgpageProfilepic);
        Picasso.get().load(organization.getCoverPicURL()).into(binding.idOrgpageCover);

        donateBtn = binding.idDonateBtn;
        swipeRefreshLayout = binding.idSwipeOrgPage;
        tabLayout = binding.idTabLayout;
        viewPager = binding.idViewPager;

        tabLayout.addTab(tabLayout.newTab().setText("Spending"));
        tabLayout.addTab(tabLayout.newTab().setText("Latest Donations"));
        tabLayout.addTab(tabLayout.newTab().setText("Top Donations"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(new PagerAdapter(requireActivity()));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            String[] titles = new String[]{"Spending", "Latest Donations", "Top Donations"};
            tab.setText(titles[position]);
            viewPager.setCurrentItem(position);
        }).attach();
        viewPager.setCurrentItem(0);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donateBtn.setOnClickListener(View->{
            viewModel.checkMembership(requireActivity(),organization.getId());
            observeViewModel();
        });

    }
    private void observeViewModel(){
        viewModel.check.observe(getViewLifecycleOwner(),ck->{
            if(ck!=-1){
                if(ck==0){
                    dialogBuilder = new AlertDialog.Builder(requireContext());
                    final View log_window_view = getLayoutInflater().inflate(R.layout.login_popup,null);
                    dialogBuilder.setView(log_window_view);
                    logDialog = dialogBuilder.create();
                    logPopBtn = (Button) log_window_view.findViewById(R.id.id_login_popup_btn);
                    logDialog.show();
                    logPopBtn.setOnClickListener(v->{
                        logDialog.dismiss();
                        navigator.navLogin(requireActivity());
                    });
                }
                else if(ck==1){//Pending Request
                    dialog = new Dialog(requireContext());
                    dialog.setContentView(R.layout.pending_popup);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dialog.show();
                }
                else if(ck==2){//Already Member
                    createPayDialog();
                }
                else if(ck==3){//Send membership request
                    requestMembership();
                }
            }
        });
        viewModel.paychek.observe(getViewLifecycleOwner(),ck->{
            if(ck!=0){
                if(ck==1){
                    Log.e("Payment","Done");
                    dialogBuilder = new AlertDialog.Builder(requireContext());
                    final View success_window_view = getLayoutInflater().inflate(R.layout.success_payment_popup,null);
                    dialogBuilder.setView(success_window_view);
                    success_window = dialogBuilder.create();
                    success_window.show();
                }
                if(ck==2){
                    Log.e("Payment","Failed");
                    dialogBuilder = new AlertDialog.Builder(requireContext());
                    final View success_window_view = getLayoutInflater().inflate(R.layout.payment_failed_popup,null);
                    dialogBuilder.setView(success_window_view);
                    failed_window = dialogBuilder.create();
                    failed_window.show();
                }
            }
        });
        viewModel.membershipReqCheck.observe(getViewLifecycleOwner(),ck->{
            if(ck==1){
                Toast.makeText(requireContext(),"Membership Request Sent Successfully!",Toast.LENGTH_SHORT).show();
            }
            else if(ck==2){
                Toast.makeText(requireContext(),"Failed to Send Request!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void requestMembership(){
        dialogBuilder = new AlertDialog.Builder(requireContext());
        final View membership_req_view = getLayoutInflater().inflate(R.layout.membership_request_popup,null);
        nid = (EditText) membership_req_view.findViewById(R.id.id_nid);
        membershipCode = (EditText) membership_req_view.findViewById(R.id.id_membership_code);
        send_req_btn = (Button) membership_req_view.findViewById(R.id.id_req_popup_btn);
        dialogBuilder.setView(membership_req_view);
        membership_req_window = dialogBuilder.create();
        membership_req_window.show();

        send_req_btn.setOnClickListener(v->{
            String nnid = String.valueOf(nid.getText());
            viewModel.requestMembership(requireActivity(),new MembershipRequest(organization.getId(),nnid,
                    String.valueOf(membershipCode.getText())));
            membership_req_window.dismiss();
            Toast.makeText(requireContext(),"Sending Request...",Toast.LENGTH_SHORT).show();
        });
    }
    private void createPayDialog(){
        dialogBuilder = new AlertDialog.Builder(requireContext());
        final View pay_window_view = getLayoutInflater().inflate(R.layout.pay_popup,null);
        amount = (EditText) pay_window_view.findViewById(R.id.id_donate_amount) ;
        paynow = (Button) pay_window_view.findViewById(R.id.id_pay_now);
        dialogBuilder.setView(pay_window_view);
        pay_window = dialogBuilder.create();
        pay_window.show();
        paynow.setOnClickListener(v->{
            pay(String.valueOf(amount.getText()));
        });


    }

    private void pay(String amount){
        pay_window.dismiss();
        try {
            MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel("lucen635b58ebd7974", "lucen635b58ebd7974@ssl", amount, System.currentTimeMillis() + "", CurrencyType.BDT, SdkType.TESTBOX, SdkCategory.BANK_LIST);
            PayUsingSSLCommerz.getInstance().setData(requireActivity(), mandatoryFieldModel, new OnPaymentResultListener() {
                @Override
                public void transactionSuccess(TransactionInfo transactionInfo) {
                    if (transactionInfo.getRiskLevel().equals("0")) {
                        Log.e("Success", transactionInfo.getValId());
                        Log.e("Pay",transactionInfo.getStoreAmount());
                        viewModel.requestPayment(requireActivity(),new PayRequest(organization.getId(),Integer.parseInt(amount),"SSLCOMMERZ"));
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
