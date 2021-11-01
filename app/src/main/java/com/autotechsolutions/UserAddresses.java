package com.autotechsolutions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.autotechsolutions.Adapter.AdapterUserAddressBook;
import com.autotechsolutions.Model.UserAddressBookListModel;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAddresses extends AppCompatActivity {
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_notification)
    RecyclerView recyclerviewNotification;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private AdapterUserAddressBook adapterNotification;
    private AutoTechApp autoTechApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_addresses);
        ButterKnife.bind(this);
        autoTechApp = new AutoTechApp();
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toolbar.setNavigationIcon(R.drawable.ic_backblack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setTextColor(Color.BLACK);
        titleTv.setText("MY ADDRESS BOOK");
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_all_user_address_book();
    }

    private void get_all_user_address_book() {
        if (CommonMethods.isNetwork(UserAddresses.this)) {
            CommonMethods.showProgressDialog(UserAddresses.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.get_all_user_address_book(Config.getSharedPreferences(UserAddresses.this, "userID"), Config.getSharedPreferences(UserAddresses.this, "accessToken"));
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {
                            if (c.getData().getUserAddressBookList().size() > 0) {
                                recyclerviewNotification.setVisibility(View.VISIBLE);
                                //adapterNotification = new AdapterUserAddressBook(UserAddresses.this, c.getData().getUserAddressBookList(),);
                                recyclerviewNotification.setLayoutManager(new LinearLayoutManager(UserAddresses.this));
                                recyclerviewNotification.setAdapter(new AdapterUserAddressBook(UserAddresses.this, c.getData().getUserAddressBookList(), new AdapterUserAddressBook.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(UserAddressBookListModel item) {
                                        Intent intent = new Intent();
                                        intent.putExtra("Address", item.getAddress());
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }));
                                recyclerviewNotification.setHasFixedSize(true);
                            }

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), UserAddresses.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(UserAddresses.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), UserAddresses.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, UserAddresses.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, UserAddresses.this);
                }
            });
        }

    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        Intent intent = new Intent(UserAddresses.this, Address.class);
        startActivity(intent);
    }
}
