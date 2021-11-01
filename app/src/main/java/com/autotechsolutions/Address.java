package com.autotechsolutions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomEditText;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Address extends AppCompatActivity {

    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_Address_title)
    CustomEditText edtAddressTitle;
    @BindView(R.id.lin_viewAddresstitle)
    LinearLayout linViewAddresstitle;
    @BindView(R.id.edt_Address)
    CustomEditText edtAddress;
    @BindView(R.id.lin_viewAddress)
    LinearLayout linViewAddress;
    @BindView(R.id.btn_addaddress)
    CustomButton btnAddaddress;
    private AutoTechApp autoTechApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
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
        titleTv.setText("ADD ADDRESS");
    }

    @OnClick(R.id.btn_addaddress)
    public void onViewClicked() {
        validateAddress(edtAddressTitle.getText().toString(), edtAddress.getText().toString());

    }

    private void validateAddress(String addresstitle, String address) {
        if (TextUtils.isEmpty(addresstitle)) {
            CommonMethods.showValidationPopup("Please enter address title", Address.this);
        } else if (TextUtils.isEmpty(address)) {
            CommonMethods.showValidationPopup("Please enter address.", Address.this);
        } else {
            addAddress(addresstitle, address);
        }
    }

    private void addAddress(String addresstitle, String address) {
        if (CommonMethods.isNetwork(Address.this)) {
            CommonMethods.showProgressDialog(Address.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.add_user_address_book(Config.getSharedPreferences(Address.this, "userID"), Config.getSharedPreferences(Address.this, "accessToken"), addresstitle, "", address);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {
                            CommonMethods.showValidationPopup(c.getMessage(), Address.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    finish();
                                }
                            });
                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), Address.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(Address.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), Address.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, Address.this);
                    }
                }


                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, Address.this);
                }
            });
        }
    }
}
