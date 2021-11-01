package com.autotechsolutions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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

import com.autotechsolutions.Adapter.AdapterCarCare;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.Model.CarCareModel;
import com.autotechsolutions.Model.carsModel;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarcareActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TextView titleTv;

    @BindView(R.id.spinner_car)
    Spinner spinner_car;

    @BindView(R.id.recyclerview_listCareCare)
    RecyclerView recyclerview_listCareCare;

    List<CarCareModel> list = new ArrayList<>();
    String[] carModelList;
    @BindView(R.id.txt_no_data)
    CustomTextView txtNoData;
    @BindView(R.id.img_Addcar)
    ImageView imgAddcar;
    private ArrayList<carsModel> modelList = new ArrayList<>();
    private ArrayList<String> strcarList = new ArrayList<>();
    private String carID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carcare);
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
        titleTv.setTextColor(Color.BLACK);
        titleTv.setText("CAR CARE");

        getmyCar();


//        carModelList = getResources().getStringArray(R.array.car_model);
//        ArrayAdapter<String> carModelArray = new ArrayAdapter<String>(CarcareActivity.this, R.layout.spinner_item, carModelList);
//        carModelArray.setDropDownViewResource(R.layout.spinner_item);
//        spinner_car.setAdapter(carModelArray);

//        list.add(new CarCareModel("http://goo.gl/gEgYUd","Tyre"));
//        list.add(new CarCareModel("http://goo.gl/gEgYUd","Side Glass"));
//        list.add(new CarCareModel("http://goo.gl/gEgYUd","Lock"));
//        list.add(new CarCareModel("http://goo.gl/gEgYUd","Mirror"));
//        list.add(new CarCareModel("http://goo.gl/gEgYUd","Light"));

//        recyclerview_listCareCare.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getmyCar() {
        if (CommonMethods.isNetwork(CarcareActivity.this)) {
            CommonMethods.showProgressDialog(CarcareActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.getUserRegisterCarList(Config.getSharedPreferences(CarcareActivity.this, "userID"), Config.getSharedPreferences(CarcareActivity.this, "accessToken"));
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

                            }

                            ArrayAdapter adaptercity =
                                    new ArrayAdapter(CarcareActivity.this, R.layout.spinner_reg_item, strcarList);
                            adaptercity.setDropDownViewResource(R.layout.spinner_reg_item);

                            spinner_car.setAdapter(adaptercity);
                            spinner_car.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (parent != null)
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                        ((TextView) parent.getChildAt(0)).setTextSize(12);
                                    carID = modelList.get(position).getCarID();
                                    getCarcare();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), CarcareActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(CarcareActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), CarcareActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, CarcareActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, CarcareActivity.this);
                }
            });
        }
    }

    private void getCarcare() {
        if (CommonMethods.isNetwork(CarcareActivity.this)) {
            CommonMethods.showProgressDialog(CarcareActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.get_all_products_by_carID(Config.getSharedPreferences(CarcareActivity.this, "userID"), Config.getSharedPreferences(CarcareActivity.this, "accessToken"), carID);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {
                            if (c.getData().getProductsList().size() > 0) {
                                recyclerview_listCareCare.setVisibility(View.VISIBLE);
                                txtNoData.setVisibility(View.GONE);
                                recyclerview_listCareCare.setLayoutManager(new GridLayoutManager(CarcareActivity.this, 2));
                                recyclerview_listCareCare.setAdapter(new AdapterCarCare(CarcareActivity.this, c.getData().getProductsList(), carID));
                                recyclerview_listCareCare.setHasFixedSize(true);
                            } else {
                                recyclerview_listCareCare.setVisibility(View.GONE);
                                txtNoData.setVisibility(View.VISIBLE);
                            }
                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), CarcareActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(CarcareActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), CarcareActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, CarcareActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, CarcareActivity.this);
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
                Intent intent = new Intent(CarcareActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.img_Addcar)
    public void onViewClicked() {
        Intent i = new Intent(CarcareActivity.this, MainActivity.class);
        i.putExtra("from", "addCar");
        startActivity(i);
    }
}
