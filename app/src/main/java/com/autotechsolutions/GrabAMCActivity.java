package com.autotechsolutions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.autotechsolutions.Adapter.AdapterAMCList;
import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.Model.AMCTypeModel;
import com.autotechsolutions.Model.carsModel;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrabAMCActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TextView titleTv;

    @BindView(R.id.recyclerview_MyAMC)
    RecyclerView recyclerview_MyAMC;
    ArrayList<String> strcarList = new ArrayList<>();
    ArrayList<String> stramctypeList = new ArrayList<>();
    ArrayList<carsModel> modelList = new ArrayList<>();
    ArrayList<AMCTypeModel> amcmodelList = new ArrayList<>();
    @BindView(R.id.spinner_carCompany)
    Spinner spinnerCarCompany;
    @BindView(R.id.txt_no_data)
    CustomTextView txtNoData;
    @BindView(R.id.btn_grab)
    CustomButton btnGrab;

    Context context = this;
    @BindView(R.id.img_Addcar)
    ImageView imgAddcar;
    private String carID;
    private LinearLayoutManager mLayoutmanager;
    private AdapterAMCList adp;
    private String amctypeID;
    private String amctype;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabamc);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
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
        titleTv.setText("My AMC");
        getUserCar();
    }

    private void getUserCar() {
        if (CommonMethods.isNetwork(GrabAMCActivity.this)) {
            CommonMethods.showProgressDialog(GrabAMCActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.getUserRegisterCarList(Config.getSharedPreferences(GrabAMCActivity.this, "userID"), Config.getSharedPreferences(GrabAMCActivity.this, "accessToken"));
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {

                    if (c != null) {
                        if (c.getCode() == 100) {
                            if (c.getData().getCarDetails().size() > 0) {
                                for (int i = 0; i < c.getData().getCarDetails().size(); i++) {
                                    modelList = c.getData().getCarDetails();
                                    strcarList.add(c.getData().getCarDetails().get(i).getModelName() + "-" + c.getData().getCarDetails().get(i).getRegistrationNumber());
                                }

                                ArrayAdapter spinnerArrayAdapter =
                                        new ArrayAdapter(GrabAMCActivity.this, R.layout.spinner_reg_item, strcarList);
                                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_reg_item);
                                spinnerCarCompany.setAdapter(spinnerArrayAdapter);
                                spinnerCarCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (parent != null)
                                            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                        // ((TextView) parent.getChildAt(0)).setTextSize(12);
                                        carID = modelList.get(position).getCarID();
                                        getAmc(carID);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            } else {
                                CommonMethods.showValidationPopup(c.getMessage(), GrabAMCActivity.this);
                            }

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), GrabAMCActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(GrabAMCActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), GrabAMCActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, GrabAMCActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, GrabAMCActivity.this);
                }
            });
        }
    }

    private void getAmc(final String carID) {

        if (CommonMethods.isNetwork(GrabAMCActivity.this)) {
            CommonMethods.showProgressDialog(GrabAMCActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.get_user_amc_details_by_car(Config.getSharedPreferences(GrabAMCActivity.this, "userID"), Config.getSharedPreferences(GrabAMCActivity.this, "accessToken"), carID);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    final Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {

                    if (c != null) {
                        if (c.getCode() == 100) {
                            if (c.getData().getArrAMC().size() > 1) {


                                txtNoData.setVisibility(View.GONE);
                                recyclerview_MyAMC.setVisibility(View.VISIBLE);
                                mLayoutmanager = new LinearLayoutManager(GrabAMCActivity.this);
                                recyclerview_MyAMC.setLayoutManager(mLayoutmanager);

                                adp = new AdapterAMCList(GrabAMCActivity.this, c.getData().getArrAMC(), c, carID);
                                recyclerview_MyAMC.setAdapter(adp);

//                                spinnerAmctype.setVisibility(View.VISIBLE);
//                                txtAmctypeHeading.setVisibility(View.VISIBLE);
//                                stramctypeList.clear();
//                                for (int i = 0; i < c.getData().getArrAMC().size(); i++) {
//                                    amcmodelList = c.getData().getArrAMC();
//                                    stramctypeList.add(c.getData().getArrAMC().get(i).getAmcTitle());
//                                }
//
//                                ArrayAdapter spinnerArrayAdapter =
//                                        new ArrayAdapter(GrabAMCActivity.this, R.layout.spinner_reg_item, stramctypeList);
//                                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_reg_item);
//                                spinnerAmctype.setAdapter(spinnerArrayAdapter);
//                                spinnerAmctype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                    @Override
//                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                        if (parent != null)
//                                            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
//                                        amctype = amcmodelList.get(position).getAmcTitle();
//                                        for (int i = 0; i < c.getData().getArrAMC().size(); i++) {
//                                            if (amctype.equals(c.getData().getArrAMC().get(i).getAmcTitle())) {
//                                                if (c.getData().getArrAMC().get(i).getIsPurchased().equals("0")) {
//                                                    btnGrab.setVisibility(View.VISIBLE);
//                                                    btnGrab.setText("BUY NOW");
//                                                    final int finalI = i;
//                                                    btnGrab.setOnClickListener(new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View view) {
//                                                            CommonMethods.showValidationPopupConfirmation("Are you sure you want to buy?", GrabAMCActivity.this, new CommonMethods.upDateButtonClicklistner() {
//                                                                @Override
//                                                                public void updateButtonClick() {
//                                                                    getAMCInquiry(c.getData().getArrAMC().get(finalI).getAmcID());
//                                                                }
//                                                            });
//
//                                                        }
//                                                    });
//                                                } else {
//                                                    btnGrab.setVisibility(View.GONE);
//                                                }
//
//                                                txtNoData.setVisibility(View.GONE);
//                                                recyclerview_MyAMC.setVisibility(View.VISIBLE);
//                                                mLayoutmanager = new LinearLayoutManager(GrabAMCActivity.this);
//                                                recyclerview_MyAMC.setLayoutManager(mLayoutmanager);
//                                                adp = new AdapterAMC(GrabAMCActivity.this, c.getData().getArrAMC().get(i).getAmcDetails(), c.getData().getArrAMC().get(i).getIsPurchased(), carID, c.getData().getArrAMC().get(i).getAmcUserID());
//                                                recyclerview_MyAMC.setAdapter(adp);
//
//                                            }else {
//
//                                            }
//
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onNothingSelected(AdapterView<?> parent) {
//
//                                    }
//                                });

                            } else if (c.getData().getArrAMC().size() > 0) {

//                                spinnerAmctype.setVisibility(View.GONE);
//                                txtAmctypeHeading.setVisibility(View.GONE);
//                                if (c.getData().getArrAMC().get(0).getIsPurchased().equals("0")) {
//                                    btnGrab.setVisibility(View.VISIBLE);
//                                    btnGrab.setText("BUY NOW");
//                                    btnGrab.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            CommonMethods.showValidationPopupConfirmation("Are you sure you want to buy?", GrabAMCActivity.this, new CommonMethods.upDateButtonClicklistner() {
//                                                @Override
//                                                public void updateButtonClick() {
//                                                    getAMCInquiry(c.getData().getArrAMC().get(0).getAmcID());
//                                                }
//                                            });
//
//                                        }
//                                    });
//                                } else {
//                                    btnGrab.setVisibility(View.GONE);
//                                }
//                                txtNoData.setVisibility(View.GONE);
//                                recyclerview_MyAMC.setVisibility(View.VISIBLE);
//                                mLayoutmanager = new LinearLayoutManager(GrabAMCActivity.this);
//                                recyclerview_MyAMC.setLayoutManager(mLayoutmanager);
//                                adp = new AdapterAMC(GrabAMCActivity.this, c.getData().getArrAMC().get(0).getAmcDetails(), c.getData().getArrAMC().get(0).getIsPurchased(), carID, c.getData().getAmcUserID());
//                                recyclerview_MyAMC.setAdapter(adp);
                            } else {
//                                spinnerAmctype.setVisibility(View.GONE);
//                                txtAmctypeHeading.setVisibility(View.GONE);
                                txtNoData.setVisibility(View.VISIBLE);
                                recyclerview_MyAMC.setVisibility(View.GONE);
                            }


                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), GrabAMCActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(GrabAMCActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
//                            spinnerAmctype.setVisibility(View.GONE);
//                            txtAmctypeHeading.setVisibility(View.GONE);
                            txtNoData.setVisibility(View.VISIBLE);
                            recyclerview_MyAMC.setVisibility(View.GONE);
                            //CommonMethods.showValidationPopup(c.getMessage(), GrabAMCActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, GrabAMCActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, GrabAMCActivity.this);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_home:
                //addSomething();
                Intent intent = new Intent(GrabAMCActivity.this, MainActivity.class);
                intent.putExtra("from","");
                startActivity(intent);
                return true;
            case android.R.id.home:
//                Intent intent1 = new Intent(GrabAMCActivity.this, MainActivity.class);
//                startActivity(intent1);
                //addSomething();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getAMCInquiry(String amcID) {
        if (CommonMethods.isNetwork(GrabAMCActivity.this)) {
            CommonMethods.showProgressDialog(GrabAMCActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.add_amc_inquiry(Config.getSharedPreferences(GrabAMCActivity.this, "userID"), Config.getSharedPreferences(GrabAMCActivity.this, "accessToken"), carID, amcID);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {

                    if (c != null) {
                        if (c.getCode() == 100) {
                            CommonMethods.showValidationPopup(c.getMessage(), GrabAMCActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(GrabAMCActivity.this, ThankYou.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), GrabAMCActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(GrabAMCActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), GrabAMCActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, GrabAMCActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, GrabAMCActivity.this);
                }
            });
        }
    }

    @OnClick(R.id.img_Addcar)
    public void onViewClicked() {
        Intent i = new Intent(GrabAMCActivity.this, MainActivity.class);
        i.putExtra("from", "addCar");
        startActivity(i);
    }
}
