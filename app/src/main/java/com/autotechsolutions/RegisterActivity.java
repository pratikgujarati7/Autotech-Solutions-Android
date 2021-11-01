package com.autotechsolutions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.autotechsolutions.Model.AreaModel;
import com.autotechsolutions.Model.CityModel;
import com.autotechsolutions.Model.StateModel;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.Response.SplashResponse;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ArrayList<StateModel> statelist = new ArrayList<StateModel>();
    ArrayList<CityModel> citylist = new ArrayList<CityModel>();
    ArrayList<String> strstateList = new ArrayList<>();
    ArrayList<String> strcityList = new ArrayList<>();

    String[] cityList, areaList;
    SplashResponse sr;
    @BindView(R.id.edt_fname)
    EditText edtFname;
    @BindView(R.id.edt_lname)
    EditText edtLname;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.spinner_state)
    Spinner spinnerState;
    @BindView(R.id.spinner_city)
    Spinner spinnerCity;
    @BindView(R.id.edt_area)
    EditText edtArea;
    @BindView(R.id.edt_referal_code)
    EditText edtReferalCode;
    @BindView(R.id.txt_done)
    TextView txtDone;
    private String strcityID, strstateID;
    private String mobile_number;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mobile_number = intent.getStringExtra("mobile_number");

        //cityList = getResources().getStringArray(R.array.city);
        sr = new Gson().fromJson(Config.getSharedPreferences(RegisterActivity.this, "SplashData"), SplashResponse.class);

        if (sr.getArrStateRecord().size() > 0) {
            for (int i = 0; i < sr.getArrStateRecord().size(); i++) {
                strstateList.add(sr.getArrStateRecord().get(i).getStateName());
            }
        }
        ArrayAdapter<String> cityArray = new ArrayAdapter<String>(RegisterActivity.this, R.layout.spinner_reg_item, strstateList);
        cityArray.setDropDownViewResource(R.layout.spinner_reg_item);

        spinnerState.setAdapter(cityArray);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strcityList.clear();
                if (parent != null)
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                for (int j = 0; j < sr.getArrStateRecord().get(position).getArrAreaRecord().size(); j++) {
                    citylist = sr.getArrStateRecord().get(position).getArrAreaRecord();
                    strcityList.add(sr.getArrStateRecord().get(position).getArrAreaRecord().get(j).getCityName());
                }
                ArrayAdapter adapterbranch =
                        new ArrayAdapter(getApplicationContext(), R.layout.spinner_reg_item, strcityList);
                adapterbranch.setDropDownViewResource(R.layout.spinner_reg_item);
                spinnerCity.setAdapter(adapterbranch);
                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        strcityID = citylist.get(position).getCityID();
                        strstateID = citylist.get(position).getStateID();
//                         strareaID=sr.getArrCityRecord().get(position).getArrAreaRecord().get(position).getAreaID();
//                         strareaID=sr.getArrCityRecord().get(position).getArrAreaRecord().get(position).getCityID();
                        Log.e("stateID:", strstateID);
                        Log.e("cityID:", strcityID);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        areaList = getResources().getStringArray(R.array.city);
//        ArrayAdapter<String> areaArray = new ArrayAdapter<String>(RegisterActivity.this, R.layout.spinner_reg_item, areaList);
//        areaArray.setDropDownViewResource(R.layout.spinner_reg_item);
//        spinnerArea.setAdapter(areaArray);
    }

    @OnClick(R.id.txt_done)
    public void onViewClicked() {
        validateRegister(edtFname.getText().toString(), edtLname.getText().toString(), edtEmail.getText().toString(), edtReferalCode.getText().toString(),edtArea.getText().toString());

    }

    private void validateRegister(String fname, String lname, String email, String referralcode,String area) {
        email=email.trim();
        if (TextUtils.isEmpty(fname)) {
            CommonMethods.showValidationPopup("Please enter first name", RegisterActivity.this);
        } else if (TextUtils.isEmpty(lname)) {
            CommonMethods.showValidationPopup("Please enter last name", RegisterActivity.this);
        } else if (TextUtils.isEmpty(email)) {
            CommonMethods.showValidationPopup("Please enter email", RegisterActivity.this);
        } else if (!TextUtils.isEmpty(email) && !CommonMethods.emailValidator(email.trim())) {
            CommonMethods.showValidationPopup("Please enter valid email", RegisterActivity.this);
        } else if (TextUtils.isEmpty(spinnerState.getSelectedItem().toString())) {
            CommonMethods.showValidationPopup("Please select state", RegisterActivity.this);
        } else if (TextUtils.isEmpty(spinnerCity.getSelectedItem().toString())) {
            CommonMethods.showValidationPopup("Please select city", RegisterActivity.this);
        }else if (TextUtils.isEmpty(area)) {
            CommonMethods.showValidationPopup("Please enter area", RegisterActivity.this);
        }
        else {
            registration(fname, lname, email,area, referralcode);
        }

    }

    private void registration(String fname, String lname, String email,String area, String referralcode) {
        if (CommonMethods.isNetwork(RegisterActivity.this)) {
            CommonMethods.showProgressDialog(RegisterActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.Registration(Config.getSharedPreferences(RegisterActivity.this, "userID"), Config.getSharedPreferences(RegisterActivity.this, "accessToken"), fname, lname, email, area, strcityID, referralcode);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {
                            Config.saveSharedPreferences(RegisterActivity.this, "userID", c.getData().getUserDetails().getUserID());
                            Config.saveSharedPreferences(RegisterActivity.this, "accessToken", c.getData().getUserDetails().getAccessToken());
                            Config.saveSharedPreferences(RegisterActivity.this, "islogin", "yes");
                            Config.saveSharedPreferences(RegisterActivity.this, "fname", c.getData().getUserDetails().getFirstName());
                            Config.saveSharedPreferences(RegisterActivity.this, "lname", c.getData().getUserDetails().getLastName());
                            Config.saveSharedPreferences(RegisterActivity.this, "email", c.getData().getUserDetails().getEmail());
                            Config.saveSharedPreferences(RegisterActivity.this, "city", c.getData().getUserDetails().getCityName());
                            Config.saveSharedPreferences(RegisterActivity.this, "cityID", c.getData().getUserDetails().getCityID());
                            Config.saveSharedPreferences(RegisterActivity.this, "area", c.getData().getUserDetails().getAreaName());
                            Config.saveSharedPreferences(RegisterActivity.this, "areaID", c.getData().getUserDetails().getAreaID());
                            Config.saveSharedPreferences(RegisterActivity.this, "mobilenumber", mobile_number);
                            Config.saveSharedPreferences(RegisterActivity.this, "referralcode", c.getData().getUserDetails().getReferralCode());
                            Config.saveSharedPreferences(RegisterActivity.this, "referralMessage", c.getData().getUserDetails().getReferralMessage());
//                            if (c.getData().getUserDetails().getIsVerified().equals("1")){
//                                intent=new Intent(RegisterActivity.this,MainActivity.class);
//                                startActivity(intent);
//                            }else {
//                                intent=new Intent(RegisterActivity.this,OTPActivity.class);
//                                startActivity(intent);
//                            }


                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), RegisterActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, RegisterActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, RegisterActivity.this);
                }
            });
        }
    }
}
