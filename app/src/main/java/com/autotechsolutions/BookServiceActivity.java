package com.autotechsolutions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.autotechsolutions.CustomView.CustomEditText;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.utils.CommonMethods;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookServiceActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener {

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
    int currentyear, currentmonth, currentday;
    private String selFromDate;
    private String sendingdate;
    Context mContext = this;
    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    List<Address> addresseslList = new ArrayList<>();

    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CommonMethods.getSharedPreferences(BookServiceActivity.this, "current_location").equals("")) {
//            if (CommonMethods.isNetwork(BookServiceActivity.this)) {
////                CommonMethods.showProgressDialog(BookServiceActivity.this, "Fetching your location");
//                checkPermissions();
////                updateLocationUI();
//            } else {
//                CommonMethods.showValidationPopup(getResources().getString(R.string.internet_connection), BookServiceActivity.this);
//            }
        } else {
            updateLocationUI();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookservice);
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
        titleTv.setTextColor(Color.BLACK);
        titleTv.setText("BOOK A SERVICE");
        final Calendar c = Calendar.getInstance();
        currentyear = c.get(Calendar.YEAR);
        currentmonth = c.get(Calendar.MONTH);
        currentday = c.get(Calendar.DAY_OF_MONTH);
        try {
            selFromDate = currentday + "-" + (currentmonth + 1) + "-" + currentyear;
            selFromDate = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(selFromDate));
            sendingdate = currentyear + "-" + (currentmonth + 1) + "-" + currentday;
            txt_serviceDate.setText(selFromDate);
        } catch (ParseException pe) {
            Log.e("In RegisterActivity", "Date Parse Excp : " + pe.getMessage());
        }

        img_DatePick.setOnClickListener(this);
        radioGrp_reachability.setOnCheckedChangeListener(this);
        img_locationFinder.setOnClickListener(this);

        carModelList = getResources().getStringArray(R.array.car_model);
        ArrayAdapter<String> carModelArray = new ArrayAdapter<String>(BookServiceActivity.this, R.layout.spinner_item, carModelList);
        carModelArray.setDropDownViewResource(R.layout.spinner_item);
        spinner_car.setAdapter(carModelArray);


        init();
        restoreValuesFromBundle(savedInstanceState);
        if (CommonMethods.getSharedPreferences(BookServiceActivity.this, "current_location").equals("")) {
            if (CommonMethods.isNetwork(BookServiceActivity.this)) {
                checkPermissions();
                updateLocationUI();
            } else {
                CommonMethods.showValidationPopup(getResources().getString(R.string.internet_connection), BookServiceActivity.this);
            }
        }
//        init();
//        restoreValuesFromBundle(savedInstanceState);
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }

    private void updateLocationUI() {

        if (CommonMethods.getSharedPreferences(BookServiceActivity.this, "current_location").equals("")) {
            if (mCurrentLocation != null) {

                try {
                    final LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    final Geocoder geocoder = new Geocoder(getApplicationContext());

                    addresseslList = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);

                    String str = addresseslList.get(0).getLocality() + ",";
                    str += addresseslList.get(0).getAdminArea() + "," + addresseslList.get(0).getCountryName() + "," + addresseslList.get(0).getCountryCode() + "," + addresseslList.get(0).getPostalCode() + "," + addresseslList.get(0).getFeatureName() + "," + addresseslList.get(0).getPhone();


                    edt_LocationAddress.setText("" + str);
//                    edt_LocationAddress.setAlpha(0);
//                    edt_LocationAddress.animate().alpha(1).setDuration(300);

                    CommonMethods.hideProgressDialog();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            edt_LocationAddress.setText("" + CommonMethods.getSharedPreferences(BookServiceActivity.this, "current_location"));
//            edt_LocationAddress.setAlpha(0);
//            edt_LocationAddress.animate().alpha(1).setDuration(200);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                        CommonMethods.showProgressDialog(BookServiceActivity.this, "Fetching your location");
                        Log.i("TAG", "All location settings are satisfied.");

//                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i("TAG", "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
//                                try {
//                                    // Show the dialog by calling startResolutionForResult(), and check the
//                                    // result in onActivityResult().
//                                    ResolvableApiException rae = (ResolvableApiException) e;
//                                    rae.startResolutionForResult(BookServiceActivity.this, REQUEST_CHECK_SETTINGS);
//                                } catch (IntentSender.SendIntentException sie) {
//                                    Log.i("TAG", "PendingIntent unable to execute request.");
//                                }

                                buildAlertMessageNoGps();
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e("TAG", errorMessage);

                                Toast.makeText(BookServiceActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_DatePick:
                new SpinnerDatePickerDialogBuilder()
                        .context(BookServiceActivity.this)
                        .callback(BookServiceActivity.this)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate(currentyear, currentmonth, currentday)
//                        .maxDate(2020, 0, 1)
                        .minDate(currentyear, currentmonth, currentday)
                        .build()
                        .show();
                break;

            case R.id.img_locationFinder:
                CommonMethods.saveSharedPreferences(BookServiceActivity.this, "current_location", "");
                Intent intent = new Intent(BookServiceActivity.this, LocationTrackActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        try {
            selFromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            selFromDate = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(selFromDate));
            sendingdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            txt_serviceDate.setText(selFromDate);

        } catch (ParseException pe) {
            Log.e("In RegisterActivity", "Date Parse Excp : " + pe.getMessage());
        }
//        txt_serviceDate.setText(""+dayOfMonth+"-"+monthOfYear+"-"+year);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioBtn_selfDriven:

                lin_viewPickupAddress.setVisibility(View.GONE);

                break;

            case R.id.radiobtn_pickUp:
                Toast.makeText(this, "Pick your Location.", Toast.LENGTH_SHORT).show();
                lin_viewPickupAddress.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.e("TAG", "User agreed to make required location settings changes.");
                    // Nothing to do. startLocationupdates() gets called in onResume again.
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e("TAG", "User chose not to make required location settings changes.");
                    mRequestingLocationUpdates = false;
                    break;
            }
        }

    }

    PermissionListener PermissionListner = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            buildAlertMessageNoGps();
        }
    };

    private void checkPermissions() {
        int coarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);

        if (coarseLocationPermission != PackageManager.PERMISSION_GRANTED || fineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            TedPermission.with(mContext)
                    .setPermissionListener(PermissionListner)
                    .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();

        } else {
            startLocationUpdates();

        }
    }

    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        mRequestingLocationUpdates = false;
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        onBackPressed();
//        return true;
//    }
}
