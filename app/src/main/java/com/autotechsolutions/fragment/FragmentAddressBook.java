package com.autotechsolutions.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autotechsolutions.Adapter.AdapterAddressBook;
import com.autotechsolutions.Adapter.AdapterNotification;
import com.autotechsolutions.Address;
import com.autotechsolutions.Config;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.MainActivity;
import com.autotechsolutions.MobileNumber;
import com.autotechsolutions.Model.NotificationModel;
import com.autotechsolutions.QRScanActivity;
import com.autotechsolutions.R;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.RetroApiClient;
import com.autotechsolutions.RetroApiInterface;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAddressBook extends Fragment {

    @BindView(R.id.recyclerview_notification)
    RecyclerView recyclerview_notification;

    AdapterAddressBook adapterNotification;
    List<NotificationModel> notificationModels = new ArrayList<>();
    @BindView(R.id.txt_no_data)
    CustomTextView txtNoData;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_addressbook, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).showToolbarTitle("MY ADDRESS BOOK");
        unbinder = ButterKnife.bind(this, rootView);
        //((MainActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us));


//        notificationModels.add(new NotificationModel("Service","Congratulations! your service is book successfully."));
//        notificationModels.add(new NotificationModel("Bodyshop","Congratulations! your service for body shop book successfully."));
//        notificationModels.add(new NotificationModel("Insurance Renewal","Congratulations! your insurance detail updated. Congratulations! your insurance detail updated."));


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void get_all_user_address_book() {


        if (CommonMethods.isNetwork(getActivity())) {
            CommonMethods.showProgressDialog(getActivity(), "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.get_all_user_address_book(Config.getSharedPreferences(getActivity(), "userID"), Config.getSharedPreferences(getActivity(), "accessToken"));
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c!=null){
                    if (c.getCode() == 100) {
                        if (c.getData().getUserAddressBookList().size() > 0) {
                            txtNoData.setVisibility(View.GONE);
                            recyclerview_notification.setVisibility(View.VISIBLE);
                            adapterNotification = new AdapterAddressBook(getActivity(), c.getData().getUserAddressBookList(), FragmentAddressBook.this);
                            recyclerview_notification.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerview_notification.setAdapter(adapterNotification);
                            recyclerview_notification.setHasFixedSize(true);
                        } else {
                            txtNoData.setVisibility(View.VISIBLE);
                            recyclerview_notification.setVisibility(View.GONE);
                        }

                    } else if (c.getCode() == 102) {
                        CommonMethods.showValidationPopup(c.getMessage(), getActivity(), new CommonMethods.OkButtonClicklistner() {
                            @Override
                            public void OkButtonClick() {
                                Intent intent = new Intent(getActivity(), MobileNumber.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }
                        });
                    } else {
                        CommonMethods.showValidationPopup(c.getMessage(), getActivity());
                    }}else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, getActivity());
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, getActivity());
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        get_all_user_address_book();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), Address.class);
        startActivity(intent);
    }
}
