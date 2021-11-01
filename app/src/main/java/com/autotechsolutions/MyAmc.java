package com.autotechsolutions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.autotechsolutions.Adapter.AdapterAMC;
import com.autotechsolutions.Adapter.AdapterAMCList;
import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAmc extends AppCompatActivity {
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_MyAMC)
    RecyclerView recyclerviewMyAMC;
    @BindView(R.id.txt_no_data)
    CustomTextView txtNoData;
    @BindView(R.id.btn_grab)
    CustomButton btnGrab;
    private String data,amcType;
    private AdapterAMC adp;
    private LinearLayoutManager mLayoutmanager;
    private String position,carID,title,amcPrice,ispurchased;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_amc);
        ButterKnife.bind(this);
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

        Intent intent = getIntent();
        if (intent.hasExtra("data")) {
            data = intent.getStringExtra("data");
            position=intent.getStringExtra("position");
            carID=intent.getStringExtra("carID");
            title=intent.getStringExtra("title");
            amcType=intent.getStringExtra("amcType");
            amcPrice=intent.getStringExtra("amcPrice");
            ispurchased=intent.getStringExtra("ispurchased");
        }
        titleTv.setText(title);
        mLayoutmanager = new LinearLayoutManager(MyAmc.this);
        recyclerviewMyAMC.setLayoutManager(mLayoutmanager);
        final Common c = new Gson().fromJson(data, Common.class);
         if (c.getData().getArrAMC().get(Integer.parseInt(position)).getAmcDetails().size() > 0) {

            if (ispurchased.equals("0")) {
                btnGrab.setVisibility(View.VISIBLE);
                btnGrab.setText("BUY NOW");
                btnGrab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonMethods.showValidationPopupConfirmation("Are you sure you want to buy?", MyAmc.this, new CommonMethods.upDateButtonClicklistner() {
                            @Override
                            public void updateButtonClick() {
                                getAMCInquiry(c.getData().getArrAMC().get(Integer.parseInt(position)).getAmcID());
                            }
                        });

                    }
                });
            } else {
                btnGrab.setVisibility(View.GONE);
            }
            txtNoData.setVisibility(View.GONE);
            recyclerviewMyAMC.setVisibility(View.VISIBLE);
            adp = new AdapterAMC(MyAmc.this, c.getData().getArrAMC().get(Integer.parseInt(position)).getAmcDetails(), c.getData().getArrAMC().get(Integer.parseInt(position)).getIsPurchased(), carID, c.getData().getArrAMC().get(Integer.parseInt(position)).getAmcUserID(),amcType);
            recyclerviewMyAMC.setAdapter(adp);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            recyclerviewMyAMC.setVisibility(View.GONE);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        onBackPressed();
        return true;
    }

    private void getAMCInquiry(String amcID) {
        if (CommonMethods.isNetwork(MyAmc.this)) {
            CommonMethods.showProgressDialog(MyAmc.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.add_amc_inquiry(Config.getSharedPreferences(MyAmc.this, "userID"), Config.getSharedPreferences(MyAmc.this, "accessToken"), carID, amcID);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {

                    if (c != null) {
                        if (c.getCode() == 100) {
                            CommonMethods.showValidationPopup(c.getMessage(), MyAmc.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(MyAmc.this, ThankYou.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), MyAmc.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(MyAmc.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), MyAmc.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, MyAmc.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, MyAmc.this);
                }
            });
        }
    }

}
