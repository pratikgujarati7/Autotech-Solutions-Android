package com.autotechsolutions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import com.autotechsolutions.Adapter.AdapterBodyPhotos;
import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomEditText;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.CustomView.EmptyRecyclerView;
import com.autotechsolutions.Model.BranchModel;
import com.autotechsolutions.Model.carsModel;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.Response.SplashResponse;
import com.autotechsolutions.utils.CommonMethods;
import com.google.android.gms.common.api.ApiException;
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
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;
import com.yanzhenjie.album.widget.divider.Divider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookBodyShopActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener {


    private static final int THUMBNAIL_SIZE = 100;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TextView titleTv;
    @BindView(R.id.spinner_car)
    Spinner spinner_car;

    @BindView(R.id.img_DatePick)
    ImageView img_DatePick;

    @BindView(R.id.txt_serviceDate)
    CustomTextView txt_serviceDate;

    @BindView(R.id.radioGrp_reachability)
    RadioGroup radioGrp_reachability;

    @BindView(R.id.radioBtn_selfDriven)
    RadioButton radioBtn_selfDriven;

    @BindView(R.id.radiobtn_pickUp)
    RadioButton radiobtn_pickUp;

    @BindView(R.id.lin_viewPickupAddress)
    LinearLayout lin_viewPickupAddress;

    @BindView(R.id.recyclerview_carPhoto)
    EmptyRecyclerView recyclerview_carPhoto;
    @BindView(R.id.img_AddPhoto)
    ImageView img_AddPhoto;

    @BindView(R.id.img_addPhotoView)
    ImageView img_addPhotoView;

    @BindView(R.id.img_locationFinder)
    ImageView img_locationFinder;

    @BindView(R.id.img_address)
    ImageView img_address;

    @BindView(R.id.edt_LocationAddress)
    CustomEditText edt_LocationAddress;

    AutoTechApp autoTechApp;
    String[] carModelList;
    int currentyear, currentmonth, currentday;
    @BindView(R.id.spinner_branch)
    Spinner spinnerBranch;
    @BindView(R.id.btn_bookService)
    CustomButton btnBookService;
    @BindView(R.id.view_pickup)
    View viewPickup;
    @BindView(R.id.txt_pickuptime)
    CustomTextView txtPickuptime;
    @BindView(R.id.img_timePick)
    ImageView imgTimePick;
    @BindView(R.id.rel_timepick)
    RelativeLayout relTimepick;
    @BindView(R.id.view_selfdriven)
    View viewSelfdriven;
    private String selFromDate;
    private String sendingdate;
    Context mContext = this;
    RecyclerView.LayoutManager mLayoutmanager;
    public ArrayList<Uri> mArrayUri = new ArrayList<Uri>();


    // location last updated time
    private String mLastUpdateTime;
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
    private String rechability = "0";
    double lattitude;
    double longitude;
    private String strlattitude = "", strlongitude = "";
    private ArrayList<String> strcarList = new ArrayList<>();
    private ArrayList<String> strbranchList = new ArrayList<>();
    private ArrayList<carsModel> modelList = new ArrayList<>();
    private ArrayList<BranchModel> branchList = new ArrayList<>();
    ArrayList<Bitmap> imagebitmap = new ArrayList<Bitmap>();
    ArrayList<File> imageFile = new ArrayList<File>();
    private String carID;
    private SplashResponse sr;
    private String branchID;
    String imageFilePath;
    File file = null;
    private String imagekey = "";
    private int statusCode;
    private String date;
    private String straddress = "";
    private int hour, minutes;
    private ArrayList<AlbumFile> mAlbumFiles;
    AdapterBodyPhotos adapterBodyPhotos;
    private int currenthour, currentminute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookbodyshop);
        ButterKnife.bind(this);
        autoTechApp = new AutoTechApp();

        // toolbar initialize
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
        titleTv.setText("BOOK A BODYSHOP");

        //select default date using calender
        final Calendar c = Calendar.getInstance();
        currentyear = c.get(Calendar.YEAR);
        currentmonth = c.get(Calendar.MONTH);
        currentday = c.get(Calendar.DAY_OF_MONTH);
        currenthour = c.get(Calendar.HOUR_OF_DAY);
        currentminute = c.get(Calendar.MINUTE);

        String timeSet = "";
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

        //bind clicklistners
        img_locationFinder.setOnClickListener(this);
        img_DatePick.setOnClickListener(this);
        radioGrp_reachability.setOnCheckedChangeListener(this);
        img_AddPhoto.setOnClickListener(this);

        //get usercars api
        getUserCar();

        init();
        restoreValuesFromBundle(savedInstanceState);
        if (CommonMethods.getSharedPreferences(BookBodyShopActivity.this, "current_location").equals("")) {
            if (CommonMethods.isNetwork(BookBodyShopActivity.this)) {
                checkPermissions();
                updateLocationUI();
            } else {
                CommonMethods.showValidationPopup(getResources().getString(R.string.internet_connection), BookBodyShopActivity.this);
            }
        }

        //get splashdata
        sr = new Gson().fromJson(Config.getSharedPreferences(BookBodyShopActivity.this, "SplashData"), SplashResponse.class);
        if (sr.getArrBranchRecord().size() > 0) {
            for (int i = 0; i < sr.getArrBranchRecord().size(); i++) {
                strbranchList.add(sr.getArrBranchRecord().get(i).getBranchName());
                branchList.add(sr.getArrBranchRecord().get(i));
            }
        }

        //select branch using adapter
        ArrayAdapter adapterbranch =
                new ArrayAdapter(BookBodyShopActivity.this, R.layout.spinner_reg_item, strbranchList);
        adapterbranch.setDropDownViewResource(R.layout.spinner_reg_item);
        spinnerBranch.setAdapter(adapterbranch);
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null)
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                branchID = branchList.get(position).getBranchID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //checkPermission


        // add image
        if (mArrayUri.size() == 0) {
            recyclerview_carPhoto.setVisibility(View.GONE);
            img_addPhotoView.setVisibility(View.VISIBLE);
            img_addPhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
                }
            });
        } else {
            recyclerview_carPhoto.setVisibility(View.VISIBLE);
            img_addPhotoView.setVisibility(View.GONE);
        }
    }

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
        if (CommonMethods.getSharedPreferences(BookBodyShopActivity.this, "current_location").equals("")) {
//            if (CommonMethods.isNetwork(BookServiceActivity.this)) {
////                CommonMethods.showProgressDialog(BookServiceActivity.this, "Fetching your location");
//                checkPermissions();
////                updateLocationUI();
//            } else {
//                CommonMethods.showValidationPopup(getResources().getString(R.string.internet_connection), BookServiceActivity.this);
            startLocationUpdates();
//            }
        } else {
            updateLocationUI();
        }

    }

    private void getUserCar() {
        if (CommonMethods.isNetwork(BookBodyShopActivity.this)) {
            CommonMethods.showProgressDialog(BookBodyShopActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.getUserRegisterCarList(Config.getSharedPreferences(BookBodyShopActivity.this, "userID"), Config.getSharedPreferences(BookBodyShopActivity.this, "accessToken"));
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
                                    new ArrayAdapter(BookBodyShopActivity.this, R.layout.spinner_reg_item, strcarList);
                            adaptercity.setDropDownViewResource(R.layout.spinner_reg_item);

                            spinner_car.setAdapter(adaptercity);
                            spinner_car.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (parent != null)
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                    carID = modelList.get(position).getCarID();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), BookBodyShopActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(BookBodyShopActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), BookBodyShopActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, BookBodyShopActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, BookBodyShopActivity.this);
                }
            });
        }
    }


    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
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

        if (CommonMethods.getSharedPreferences(BookBodyShopActivity.this, "current_location").equals("")) {
            if (mCurrentLocation != null) {

                try {
                    final LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    final Geocoder geocoder = new Geocoder(getApplicationContext());

                    addresseslList = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);

                    String str = addresseslList.get(0).getLocality() + ",";
                    str += addresseslList.get(0).getAdminArea() + "," + addresseslList.get(0).getCountryName() + "," + addresseslList.get(0).getCountryCode() + "," + addresseslList.get(0).getPostalCode() + "," + addresseslList.get(0).getFeatureName() + "," + addresseslList.get(0).getPhone();

                    edt_LocationAddress.setText("" + str);
//                    edt_LocationAddress.setAlpha(0);
//                    edt_LocationAddress.animate().alpha(1).setDuration(200);

//                    CommonMethods.hideProgressDialog();
                    lattitude = mCurrentLocation.getLatitude();
                    longitude = mCurrentLocation.getLongitude();
                    strlattitude = String.valueOf(lattitude);
                    strlongitude = String.valueOf(longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            edt_LocationAddress.setText("" + CommonMethods.getSharedPreferences(BookBodyShopActivity.this, "current_location"));
//            edt_LocationAddress.setAlpha(0);
//            edt_LocationAddress.animate().alpha(1).setDuration(200);
        }

    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_DatePick:
                new SpinnerDatePickerDialogBuilder()
                        .context(BookBodyShopActivity.this)
                        .callback(BookBodyShopActivity.this)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate(currentyear, currentmonth, currentday)
//                        .maxDate(2020, 0, 1)
                        .minDate(currentyear, currentmonth, currentday)
                        .build()
                        .show();
                break;

            case R.id.img_AddPhoto:
                //showPickupDialog(BookBodyShopActivity.this);
                //startActivity(new Intent(this, ImageActivity.class));
                selectImage();
                break;

            case R.id.img_locationFinder:
                //CommonMethods.saveSharedPreferences(BookBodyShopActivity.this, "current_location", "");
                Intent intent = new Intent(BookBodyShopActivity.this, LocationTrackActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void selectImage() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                .columnCount(2)
                .selectCount(6)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newDarkBuilder(this)
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        mLayoutmanager = new GridLayoutManager(BookBodyShopActivity.this, 1);
                        recyclerview_carPhoto.setLayoutManager(new LinearLayoutManager(BookBodyShopActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        for (int i = 0; i < mAlbumFiles.size(); i++) {
                            File imgFile = new File(mAlbumFiles.get(i).getPath());
                            imageFile.add(imgFile);
                            mArrayUri.add(Uri.fromFile(imgFile));
                        }


                        if (mArrayUri.size() == 0) {
                            recyclerview_carPhoto.setVisibility(View.GONE);
                            img_addPhotoView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerview_carPhoto.setVisibility(View.VISIBLE);
                            img_addPhotoView.setVisibility(View.GONE);
                            adapterBodyPhotos = new AdapterBodyPhotos(BookBodyShopActivity.this, mAlbumFiles);
                            recyclerview_carPhoto.setAdapter(adapterBodyPhotos);
                        }


//                        adapterBodyPhotos.notifyDataSetChanged(mArrayUri);

                        // adapterBodyPhotos.notifyDataSetChanged(mAlbumFiles);

                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                })
                .start();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        txt_serviceDate.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year);

        try {
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
                relTimepick.setVisibility(View.GONE);
                viewPickup.setVisibility(View.GONE);
                viewSelfdriven.setVisibility(View.GONE);
                rechability = "1";
                break;

            case R.id.radiobtn_pickUp:
                //Toast.makeText(this, "Pick your Location.", Toast.LENGTH_SHORT).show();
                rechability = "0";
                lin_viewPickupAddress.setVisibility(View.VISIBLE);
                relTimepick.setVisibility(View.VISIBLE);
                viewPickup.setVisibility(View.VISIBLE);
                viewSelfdriven.setVisibility(View.VISIBLE);
                break;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                straddress = data.getStringExtra("Address");
                edt_LocationAddress.setText(straddress);
            }
        }


        if (requestCode == REQUEST_CHECK_SETTINGS) {
            // Check for the integer request code originally supplied to startResolutionForResult().
//            case REQUEST_CHECK_SETTINGS:
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
//                break;
        }

    }

    private void checkPermissions() {
        int coarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        int readstoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writestoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (coarseLocationPermission != PackageManager.PERMISSION_GRANTED || fineLocationPermission != PackageManager.PERMISSION_GRANTED
                || cameraPermission != PackageManager.PERMISSION_GRANTED || readstoragePermission != PackageManager.PERMISSION_GRANTED || writestoragePermission != PackageManager.PERMISSION_GRANTED) {
            TedPermission.with(mContext)
                    .setPermissionListener(PermissionListner)
                    .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();

        } else {
            startLocationUpdates();
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

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

//                        CommonMethods.showProgressDialog(BookBodyShopActivity.this, "Fetching your location");
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

                                //Toast.makeText(BookServiceActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
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

    private void validateBookService(String carID, String branchID, String date, String rechability) {
        if (carID.equals("")) {
            CommonMethods.showValidationPopup("Please select your car", BookBodyShopActivity.this);
        } else if (branchID.equals("")) {
            CommonMethods.showValidationPopup("Please select your branch", BookBodyShopActivity.this);
        } else if (date.equals("")) {
            CommonMethods.showValidationPopup("Please select date", BookBodyShopActivity.this);
        } else if (rechability.equals("")) {
            CommonMethods.showValidationPopup("Please select rechability", BookBodyShopActivity.this);
        } else {
            //bookService(carID,branchID,date,rechability);
            new SyncTask().execute();
        }
    }

    @OnClick({R.id.img_address, R.id.btn_bookService})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_address:
                Intent i = new Intent(BookBodyShopActivity.this, UserAddresses.class);
                startActivityForResult(i, 1);
                break;
            case R.id.btn_bookService:
                date = txt_serviceDate.getText().toString();
                validateBookService(carID, branchID, date, rechability);
                break;
        }
    }

    @OnClick(R.id.rel_timepick)
    public void onViewClicked() {
//        Calendar mcurrentTime = Calendar.getInstance();
//        int currenthour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int currentminute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(BookBodyShopActivity.this,
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
        timePickerDialog.show();
    }

    public class SyncTask extends AsyncTask<String, Integer, String> {
        private int code;
        private String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            return uploadFile();
        }


        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.URL + Config.add_user_car_bodyshop);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                //publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                Log.e("Asynctask", "Running");
                if (imageFile != null) {
                    for (int i = 0; i < imageFile.size(); i++) {
                        imagekey = "image_data" + i;
//                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                        imagebitmap.get(i).compress(Bitmap.CompressFormat.PNG, 100, bos);
//                        byte[] data = bos.toByteArray();
                        entity.addPart(imagekey, new FileBody(imageFile.get(i)));
                        //entity.addPart(imagekey, new ByteArrayBody(data, "image/png", "image"));

                    }
                }
                int imagecount = imageFile != null ? imageFile.size() : 0;
                String strimageCount = String.valueOf(imagecount);
                // Extra parameters if you want to pass to server

                entity.addPart("userID", new StringBody(Config.getSharedPreferences(BookBodyShopActivity.this, "userID")));
                entity.addPart("accessToken", new StringBody(Config.getSharedPreferences(BookBodyShopActivity.this, "accessToken")));
                entity.addPart("carID", new StringBody(carID));
                entity.addPart("branchID", new StringBody(branchID));
                entity.addPart("serviceDate", new StringBody(date));
                entity.addPart("reachability", new StringBody(rechability));
                entity.addPart("pickupAddress", new StringBody(edt_LocationAddress.getText().toString()));
                entity.addPart("latitude", new StringBody(strlattitude));
                entity.addPart("longitude", new StringBody(strlongitude));
                entity.addPart("serviceTime", new StringBody(txtPickuptime.getText().toString()));
                entity.addPart("image_data_count", new StringBody(strimageCount));


                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                entity.writeTo(bytes);
                String entityContentAsString = new String(bytes.toByteArray());
                Log.e("Request:", entityContentAsString);
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {

                    responseString = EntityUtils.toString(r_entity);
                    JSONObject object = new JSONObject(responseString);
                    Log.e("respnseObject:", object + "");
                    code = object.getInt("code");
                    message = object.getString("message");
                } else {
                    Log.e("Asynctask", "stop");
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                    Log.e("responseString", responseString);
                }
            } catch (Exception e) {
            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (statusCode == 200) {
                if (code == 100) {
                    CommonMethods.showValidationPopup(message, BookBodyShopActivity.this, new CommonMethods.OkButtonClicklistner() {
                        @Override
                        public void OkButtonClick() {
                            Intent intent = new Intent(BookBodyShopActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }

        }
    }

}
