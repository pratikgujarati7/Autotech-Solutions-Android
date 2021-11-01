package com.autotechsolutions;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomEditText;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.Model.BranchModel;
import com.autotechsolutions.Model.carsModel;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.Response.SplashResponse;
import com.autotechsolutions.utils.CommonMethods;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookServicesActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TextView titleTv;
    @BindView(R.id.spinner_car)
    Spinner spinner_car;

    @BindView(R.id.img_DatePick)
    ImageView img_DatePick;

    @BindView(R.id.img_locationFinder)
    ImageView img_locationFinder;

    @BindView(R.id.txt_serviceDate)
    CustomTextView txt_serviceDate;

    @BindView(R.id.edt_LocationAddress)
    CustomEditText edt_LocationAddress;

    @BindView(R.id.radioGrp_reachability)
    RadioGroup radioGrp_reachability;

    @BindView(R.id.radioBtn_selfDriven)
    RadioButton radioBtn_selfDriven;

    @BindView(R.id.radiobtn_pickUp)
    RadioButton radiobtn_pickUp;

    @BindView(R.id.lin_viewPickupAddress)
    LinearLayout lin_viewPickupAddress;
    String[] carModelList;
    int currentyear, currentmonth, currentday, selectedday, selectedmonth, selextedyear;
    @BindView(R.id.spinner_branch)
    Spinner spinnerBranch;
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.view_selfdriven)
    View viewSelfdriven;
    @BindView(R.id.btn_bookService)
    CustomButton btnBookService;
    @BindView(R.id.img_address)
    ImageView imgAddress;
    @BindView(R.id.view_pickup)
    View viewPickup;
    @BindView(R.id.txt_pickuptime)
    CustomTextView txtPickuptime;
    @BindView(R.id.img_timePick)
    ImageView imgTimePick;
    @BindView(R.id.rel_timepick)
    RelativeLayout relTimepick;
    @BindView(R.id.img_Addcar)
    ImageView imgAddcar;
    private String selFromDate;
    private String sendingdate;


    List<Address> addresseslList = new ArrayList<>();
    private ArrayList<String> strcarList = new ArrayList<>();
    private ArrayList<String> strbranchList = new ArrayList<>();
    private ArrayList<carsModel> modelList = new ArrayList<>();
    private ArrayList<BranchModel> branchList = new ArrayList<>();
    private String carID = "";
    SplashResponse sr;
    private String branchID = "";
    private String rechability = "0";
    double lattitude;
    double longitude;
    private String strlattitude = "", strlongitude = "";
    private String straddress = "";
    private int hour, minutes;
    int currenthour, currentminute;
    String timeSet = "";
    private int LOCATION_UPDATE = 888;
    private String sendingDate = "";
    private String strFinalSendingDate = "";

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {

        if (Config.getSharedPreferences(BookServicesActivity.this, "current_location").equals("")) {
            int fineLoc_permission1 = ContextCompat.checkSelfPermission(BookServicesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            int CoarseLoc_permission1 = ContextCompat.checkSelfPermission(BookServicesActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (fineLoc_permission1 == PackageManager.PERMISSION_GRANTED && CoarseLoc_permission1 == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                getPermission();
            }
        }
        super.onResume();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookservice);
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
        titleTv.setText("BOOK SERVICE");
        final Calendar c = Calendar.getInstance();
        currentyear = c.get(Calendar.YEAR);
        currentmonth = c.get(Calendar.MONTH);
        currentday = c.get(Calendar.DAY_OF_MONTH);
        selectedday = currentday;
        selectedmonth = currentmonth;
        selextedyear = currentyear;
        currenthour = c.get(Calendar.HOUR_OF_DAY);
        currentminute = c.get(Calendar.MINUTE);

        //  String timeSet = "";
        if (currenthour > 12) {
            currenthour -= 12;
            timeSet = "PM";
        } else if (currenthour == 0) {
            currenthour += 12;
            timeSet = "AM";
        } else if (currenthour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min = "";
        if (currentminute < 10)
            min = "0" + currentminute;
        else
            min = String.valueOf(currentminute);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(currenthour).append(':')
                .append(min).append(" ").append(timeSet).toString();
        txtPickuptime.setText(aTime);

        try {
            selFromDate = currentday + "-" + (currentmonth + 1) + "-" + currentyear;
            selFromDate = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(selFromDate));
            sendingdate = currentyear + "-" + (currentmonth + 1) + "-" + currentday;
            txt_serviceDate.setText(selFromDate);
        } catch (ParseException pe) {
            Log.e("In RegisterActivity", "Date Parse Excp : " + pe.getMessage());
        }


        llDate.setOnClickListener(this);
        radioGrp_reachability.setOnCheckedChangeListener(this);
        img_locationFinder.setOnClickListener(this);

        getUserCar();
        sr = new Gson().fromJson(Config.getSharedPreferences(BookServicesActivity.this, "SplashData"), SplashResponse.class);
        if (sr.getArrBranchRecord().size() > 0) {
            for (int i = 0; i < sr.getArrBranchRecord().size(); i++) {
                strbranchList.add(sr.getArrBranchRecord().get(i).getBranchName());
                branchList.add(sr.getArrBranchRecord().get(i));
            }
        }

        ArrayAdapter adapterbranch =
                new ArrayAdapter(BookServicesActivity.this, R.layout.spinner_reg_item, strbranchList);
        adapterbranch.setDropDownViewResource(R.layout.spinner_reg_item);
        spinnerBranch.setAdapter(adapterbranch);
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{
                    if (parent != null)
                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                }catch (Exception e){Log.d("Tag","exception");}
                // ((TextView) parent.getChildAt(0)).setTextSize(12);
                branchID = branchList.get(position).getBranchID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getUserCar() {
        if (CommonMethods.isNetwork(BookServicesActivity.this)) {
            CommonMethods.showProgressDialog(BookServicesActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.getUserRegisterCarList(Config.getSharedPreferences(BookServicesActivity.this, "userID"), Config.getSharedPreferences(BookServicesActivity.this, "accessToken"));
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
                                ArrayAdapter adaptercity =
                                        new ArrayAdapter(BookServicesActivity.this, R.layout.spinner_reg_item, strcarList);
                                adaptercity.setDropDownViewResource(R.layout.spinner_reg_item);

                                spinner_car.setAdapter(adaptercity);
                                spinner_car.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (parent != null)
                                            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                        //        ((TextView) parent.getChildAt(0)).setTextSize(12);
                                        carID = modelList.get(position).getCarID();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            } else {
                                CommonMethods.showValidationPopup(c.getMessage(), BookServicesActivity.this);
                            }

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), BookServicesActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(BookServicesActivity.this, MobileNumber.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), BookServicesActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, BookServicesActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, BookServicesActivity.this);
                }
            });
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                new SpinnerDatePickerDialogBuilder()
                        .context(BookServicesActivity.this)
                        .callback(BookServicesActivity.this)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate(selextedyear, selectedmonth, selectedday)
//                        .maxDate(2020, 0, 1)
                        .minDate(currentyear, currentmonth, currentday)
                        .build()
                        .show();
                break;

            case R.id.img_locationFinder:
                CommonMethods.saveSharedPreferences(BookServicesActivity.this, "current_location", "");
                Intent intent = new Intent(BookServicesActivity.this, LocationTracksActivity.class);
                startActivityForResult(intent, LOCATION_UPDATE);
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        try {
            selectedday = dayOfMonth;
            selectedmonth = monthOfYear;
            selextedyear = year;
            selFromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            selFromDate = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(selFromDate));
            sendingdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            txt_serviceDate.setText(selFromDate);

        } catch (ParseException pe) {
            Log.e("In RegisterActivity", "Date Parse Excp : " + pe.getMessage());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioBtn_selfDriven:

                lin_viewPickupAddress.setVisibility(View.GONE);
                rechability = "1";
                viewSelfdriven.setVisibility(View.GONE);
                relTimepick.setVisibility(View.GONE);
                viewPickup.setVisibility(View.GONE);

                break;

            case R.id.radiobtn_pickUp:
                viewSelfdriven.setVisibility(View.VISIBLE);
                lin_viewPickupAddress.setVisibility(View.VISIBLE);
                relTimepick.setVisibility(View.VISIBLE);
                viewPickup.setVisibility(View.VISIBLE);
                rechability = "0";
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                straddress = data.getStringExtra("Address");
                straddress = straddress.replaceAll("null,", "");
                straddress = straddress.replaceAll(",null", "");
                edt_LocationAddress.setText("");
                edt_LocationAddress.setText(straddress);
            }
        }

        if (requestCode == LOCATION_UPDATE) {
            if (resultCode == RESULT_OK) {
                straddress = data.getStringExtra("current_location");
                straddress = straddress.replaceAll("null,", "");
                straddress = straddress.replaceAll(",null", "");
                edt_LocationAddress.setText("");
                edt_LocationAddress.setText(straddress);
            }
        }
    }


    private void validateBookService(String carID, String branchID, String date, String rechability) {
        if (carID.equals("")) {
            CommonMethods.showValidationPopup("Please select your car", BookServicesActivity.this);
        } else if (branchID.equals("")) {
            CommonMethods.showValidationPopup("Please select your branch", BookServicesActivity.this);
        } else if (date.equals("")) {
            CommonMethods.showValidationPopup("Please select date", BookServicesActivity.this);
        } else if (rechability.equals("")) {
            CommonMethods.showValidationPopup("Please select rechability", BookServicesActivity.this);
        } else if (edt_LocationAddress.getText().toString().equalsIgnoreCase("")) {
            CommonMethods.showValidationPopup("Please enter location (Address)", BookServicesActivity.this);
        } else {
            bookService(carID, branchID, date, rechability);
        }
    }

    private void bookService(String carID, String branchID, String date, String rechability) {
        if (CommonMethods.isNetwork(BookServicesActivity.this)) {
            CommonMethods.showProgressDialog(BookServicesActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.add_user_car_service(Config.getSharedPreferences(BookServicesActivity.this, "userID"), Config.getSharedPreferences(BookServicesActivity.this, "accessToken"), carID, branchID, date, rechability, edt_LocationAddress.getText().toString(), strlattitude, strlongitude, txtPickuptime.getText().toString());
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));

                    if (c != null) {
                        if (c.getCode() == 100) {
                            CommonMethods.showValidationPopup(c.getMessage(), BookServicesActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(BookServicesActivity.this, ThankYou.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), BookServicesActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(BookServicesActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), BookServicesActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, BookServicesActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, BookServicesActivity.this);
                }
            });
        }
    }

    @OnClick({R.id.img_address, R.id.btn_bookService, R.id.img_Addcar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_address:
                Intent i = new Intent(BookServicesActivity.this, UserAddresses.class);
                startActivityForResult(i, 1);
                break;
            case R.id.btn_bookService:
                sendingDate = txt_serviceDate.getText().toString();
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
                validateBookService(carID, branchID, strFinalSendingDate, rechability);
                break;
            case R.id.img_Addcar:
                i = new Intent(BookServicesActivity.this, MainActivity.class);
                i.putExtra("from", "addCar");
                startActivity(i);
                break;
        }
    }

    @OnClick(R.id.rel_timepick)
    public void onViewClicked() {

        int selectedhour = currenthour;
        if (timeSet.equalsIgnoreCase("PM")) {
            if (selectedhour == 12) {
                selectedhour = 12;
            } else
                selectedhour = currenthour + 12;
        } else {
            if (selectedhour == 12) {
                selectedhour = 0;
            } else
                selectedhour = currenthour;
        }


        int selectedmin = currentminute;
        TimePickerDialog timePickerDialog = new TimePickerDialog(BookServicesActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        hour = hourOfDay;
                        minutes = minute;
                        //  String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes;
                        else
                            min = String.valueOf(minutes);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min).append(" ").append(timeSet).toString();
                        currenthour = hour;
                        currentminute = minutes;
                        txtPickuptime.setText(aTime);
                    }
                }, selectedhour, selectedmin, false);
        timePickerDialog.show();
        /*
        TimePickerDialog timePickerDialog = new TimePickerDialog(BookServicesActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        hour = hourOfDay;
                        minutes = minute;
                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes;
                        else
                            min = String.valueOf(minutes);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min).append(" ").append(timeSet).toString();
                        txtPickuptime.setText(aTime);
                    }
                }, currenthour, currentminute, false);
        timePickerDialog.show();*/
    }

    //TODO NEW GPS CODE
    boolean gps_enabled = false;
    boolean network_enabled = false;

    private void getUserLocation() {
        // Get user location


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(BookServicesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(BookServicesActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }


        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }


        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(BookServicesActivity.this)
                    .setMessage("GPS network not enable.")
                    .setPositiveButton("Location Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            Location mainlocation = null;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                mainlocation = location;
            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                mainlocation = location1;

            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                mainlocation = location2;
            }

            if (mainlocation != null) {
                try {
                    final LatLng current = new LatLng(mainlocation.getLatitude(), mainlocation.getLongitude());
                    final Geocoder geocoder = new Geocoder(getApplicationContext());
                    addresseslList = geocoder.getFromLocation(mainlocation.getLatitude(), mainlocation.getLongitude(), 1);
                    String str = addresseslList.get(0).getFeatureName();
                    str += addresseslList.get(0).getLocality() + "," + addresseslList.get(0).getAdminArea() + "," + addresseslList.get(0).getCountryName() + "," + addresseslList.get(0).getPostalCode();
                    str = str.replaceAll("null,", "");
                    str = str.replaceAll(",null", "");
                    if (straddress.equals("")) {
                        edt_LocationAddress.setText("" + str);
                    }
                    lattitude = mainlocation.getLatitude();
                    longitude = mainlocation.getLongitude();
                    strlattitude = String.valueOf(lattitude);
                    strlongitude = String.valueOf(longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            }
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
                Intent intent = new Intent(BookServicesActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getPermission() {
        TedPermission.with(BookServicesActivity.this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(BookServicesActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }

}
