package com.autotechsolutions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.Model.InsuranceCompanyModel;
import com.autotechsolutions.Model.carsModel;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.Response.SplashResponse;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsuranceRenewalActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TextView titleTv;
    @BindView(R.id.spinner_car)
    Spinner spinner_car;

    @BindView(R.id.spinner_carCompany)
    Spinner spinner_carCompany;

    @BindView(R.id.edt_policyNo)
    EditText edt_policyNo;

    @BindView(R.id.img_DatePick)
    ImageView img_DatePick;

    @BindView(R.id.txt_expiryDate)
    CustomTextView txt_expiryDate;

    @BindView(R.id.spinner_year)
    Spinner spinner_year;
    int currentyear, currentmonth, currentday,selectedday,selectedmonth,selextedyear;
    @BindView(R.id.btn_bookService)
    CustomButton btnBookService;
    @BindView(R.id.ll_date)
    LinearLayout llDate;

    @BindView(R.id.rel_policynumber)
    RelativeLayout rel_policynumber;

    @BindView(R.id.view_policynumber)
    View view_policynumber;

    @BindView(R.id.rel_expdate)
    RelativeLayout rel_expdate;

    @BindView(R.id.view_expirydate)
    View view_expirydate;
    @BindView(R.id.img_Addcar)
    ImageView imgAddcar;
    private String selFromDate;
    private String sendingdate;
    String[] carModelList, carCompanyList;
    private ArrayList<carsModel> modelList = new ArrayList<>();
    private ArrayList<InsuranceCompanyModel> insurancList = new ArrayList<>();
    private ArrayList<String> strcarList = new ArrayList<>();
    private String carID = "";
    private SplashResponse sr;
    private ArrayList<String> strinsuranceList = new ArrayList<>();
    private String insuranceID = "";
    private String from;
    private String haveInsurance = "";
    private String sendingDate = "", strFinalSendingDate = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurancerenewal);
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
        titleTv.setText("RENEWAL INSURANCE");
        llDate.setOnClickListener(this);

        Intent intent = getIntent();
        from = intent.getStringExtra("from");

        if (from.equals("yes")) {
            rel_policynumber.setVisibility(View.VISIBLE);
            view_policynumber.setVisibility(View.VISIBLE);
            rel_expdate.setVisibility(View.VISIBLE);
            view_expirydate.setVisibility(View.VISIBLE);
            haveInsurance = "1";
        } else {
            rel_policynumber.setVisibility(View.GONE);
            view_policynumber.setVisibility(View.GONE);
            rel_expdate.setVisibility(View.GONE);
            view_expirydate.setVisibility(View.GONE);
            haveInsurance = "0";
        }

        final Calendar c = Calendar.getInstance();
        currentyear = c.get(Calendar.YEAR);
        currentmonth = c.get(Calendar.MONTH);
        currentday = c.get(Calendar.DAY_OF_MONTH);

        selectedday=currentday;
        selectedmonth=currentmonth;
        selextedyear=currentyear;
        try {
            selFromDate = currentday + "-" + (currentmonth + 1) + "-" + currentyear;
            selFromDate = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(selFromDate));
            sendingdate = currentyear + "-" + (currentmonth + 1) + "-" + currentday;
            txt_expiryDate.setText(selFromDate);
        } catch (ParseException pe) {
            Log.e("In RegisterActivity", "Date Parse Excp : " + pe.getMessage());
        }

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1900; i--) {
            years.add(Integer.toString(i));
        }

        ArrayAdapter<String> yearArray = new ArrayAdapter<String>(InsuranceRenewalActivity.this, R.layout.spinner_item, years);
        yearArray.setDropDownViewResource(R.layout.spinner_item);
        spinner_year.setAdapter(yearArray);

        getmyCar();

        sr = new Gson().fromJson(Config.getSharedPreferences(InsuranceRenewalActivity.this, "SplashData"), SplashResponse.class);
        if (sr.getArrMakeRecord().size() > 0) {
            for (int i = 0; i < sr.getArrInsuranceCompanyRecord().size(); i++) {
                strinsuranceList.add(sr.getArrInsuranceCompanyRecord().get(i).getCompanyName());
                insurancList.add(sr.getArrInsuranceCompanyRecord().get(i));
            }
        }
        ArrayAdapter<String> carCompanyArray = new ArrayAdapter<String>(InsuranceRenewalActivity.this, R.layout.spinner_item, strinsuranceList);
        carCompanyArray.setDropDownViewResource(R.layout.spinner_item);
        spinner_carCompany.setAdapter(carCompanyArray);
        spinner_carCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null)
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                insuranceID = insurancList.get(position).getInsuranceCompanyID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        carCompanyList = getResources().getStringArray(R.array.car_company);
//        ArrayAdapter<String> carCompanyArray = new ArrayAdapter<String>(InsuranceRenewalActivity.this, R.layout.spinner_item, carCompanyList);
//        carCompanyArray.setDropDownViewResource(R.layout.spinner_item);
//        spinner_carCompany.setAdapter(carCompanyArray);


    }

    private void getmyCar() {
        if (CommonMethods.isNetwork(InsuranceRenewalActivity.this)) {
            CommonMethods.showProgressDialog(InsuranceRenewalActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.getUserRegisterCarList(Config.getSharedPreferences(InsuranceRenewalActivity.this, "userID"), Config.getSharedPreferences(InsuranceRenewalActivity.this, "accessToken"));
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
                                    new ArrayAdapter(InsuranceRenewalActivity.this, R.layout.spinner_item, strcarList);
                            adaptercity.setDropDownViewResource(R.layout.spinner_item);

                            spinner_car.setAdapter(adaptercity);
                            spinner_car.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (parent != null)
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                        //((TextView) parent.getChildAt(0)).setTextSize(14);
                                    carID = modelList.get(position).getCarID();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), InsuranceRenewalActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(InsuranceRenewalActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), InsuranceRenewalActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, InsuranceRenewalActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, InsuranceRenewalActivity.this);
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                new SpinnerDatePickerDialogBuilder()
                        .context(InsuranceRenewalActivity.this)
                        .callback(InsuranceRenewalActivity.this)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate(selextedyear, selectedmonth, selectedday)
//                        .maxDate(2020, 0, 1)
                        .minDate(currentyear, currentmonth, currentday)
                        .build()
                        .show();
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        try {

            selectedday=dayOfMonth;
            selectedmonth=monthOfYear;
            selextedyear=year;
            selFromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            selFromDate = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(selFromDate));
            sendingdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            txt_expiryDate.setText(selFromDate);

        } catch (ParseException pe) {
            Log.e("In RegisterActivity", "Date Parse Excp : " + pe.getMessage());
        }
//        txt_expiryDate.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year);
    }

//    @OnClick(R.id.btn_bookService)
//    public void onViewClicked() {
//        if (from.equals("yes")) {
//            validateInsurancepolicy(edt_policyNo.getText().toString());
//        } else {
//            validateInsurance();
//        }
//
//    }

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
                Intent intent = new Intent(InsuranceRenewalActivity.this, MainActivity.class);
                startActivity(intent);
                //addSomething();
                return true;
            case android.R.id.home:
                //addSomething();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void validateInsurancepolicy(String policyNumber) {
        sendingDate = txt_expiryDate.getText().toString();
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = originalFormat.parse(sendingDate);
            System.out.println("Old Format :   " + originalFormat.format(date));
            System.out.println("New Format :   " + targetFormat.format(date));
            strFinalSendingDate = targetFormat.format(date);

        } catch (ParseException ex) {
        }
        if (TextUtils.isEmpty(policyNumber)) {
            CommonMethods.showValidationPopup("Please enter policy number", InsuranceRenewalActivity.this);
        } else if (insuranceID.equals("")) {
            CommonMethods.showValidationPopup("Please choose Insurance company", InsuranceRenewalActivity.this);
        } else if (carID.equals("")) {
            CommonMethods.showValidationPopup("Please choose your car", InsuranceRenewalActivity.this);
        } else if (TextUtils.isEmpty(strFinalSendingDate)) {
            CommonMethods.showValidationPopup("Please select date", InsuranceRenewalActivity.this);
        } else {
            insuranceQuote(policyNumber, insuranceID, carID, strFinalSendingDate);
        }
    }

    private void validateInsurance() {
        //String date = txt_expiryDate.getText().toString();
        if (insuranceID.equals("")) {
            CommonMethods.showValidationPopup("Please choose Insurance company", InsuranceRenewalActivity.this);
        } else if (carID.equals("")) {
            CommonMethods.showValidationPopup("Please choose your car", InsuranceRenewalActivity.this);
        } else {
            insuranceQuote("", insuranceID, carID, "");
        }
    }

    private void insuranceQuote(String policyNumber, String insuranceID, String carID, String date) {

        if (CommonMethods.isNetwork(InsuranceRenewalActivity.this)) {
            CommonMethods.showProgressDialog(InsuranceRenewalActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.car_insurance_quote(Config.getSharedPreferences(InsuranceRenewalActivity.this, "userID"), Config.getSharedPreferences(InsuranceRenewalActivity.this, "accessToken"), carID, insuranceID, policyNumber, date, spinner_year.getSelectedItem().toString(), haveInsurance);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {

                    if (c != null) {
                        if (c.getCode() == 100) {
                            CommonMethods.showValidationPopup(c.getMessage(), InsuranceRenewalActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(InsuranceRenewalActivity.this, ThankYou.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), InsuranceRenewalActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(InsuranceRenewalActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), InsuranceRenewalActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, InsuranceRenewalActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, InsuranceRenewalActivity.this);
                }
            });
        }

    }

    @OnClick({R.id.img_Addcar, R.id.btn_bookService})
    public void onViewClicked(View view) {
        Intent i=null;
        switch (view.getId()) {
            case R.id.img_Addcar:
                i = new Intent(InsuranceRenewalActivity.this, MainActivity.class);
                i.putExtra("from", "addCar");
                startActivity(i);
                break;
            case R.id.btn_bookService:
                if (from.equals("yes")) {
                    validateInsurancepolicy(edt_policyNo.getText().toString());
                } else {
                    validateInsurance();
                }
                break;
        }
    }

//    @OnClick(R.id.img_Addcar)
//    public void onViewClicked() {
//    }
}
