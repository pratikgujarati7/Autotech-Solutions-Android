package com.autotechsolutions;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.LatLng;
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
import java.io.IOException;
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
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookBodyShopsActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener {


    private static final int THUMBNAIL_SIZE = 100;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TextView titleTv;
    @BindView(R.id.spinner_car)
    Spinner spinner_car;

    @BindView(R.id.img_DatePick)
    ImageView img_DatePick;

    @BindView(R.id.ll_date)
    LinearLayout llDate;

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
    int currentyear, currentmonth, currentday,selectedday,selectedmonth,selextedyear;
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
    @BindView(R.id.edt_claimNo)
    EditText edtClaimNo;
    @BindView(R.id.rel_claimnumber)
    RelativeLayout relClaimnumber;
    @BindView(R.id.view_claimnumber)
    View viewClaimnumber;
    @BindView(R.id.img_Addcar)
    ImageView imgAddcar;
    private String selFromDate;
    private String sendingdate;
    Context mContext = this;
    RecyclerView.LayoutManager mLayoutmanager;
    public ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    MainActivity mainActivity;

    // location last updated time
    private String mLastUpdateTime;
    private static final int REQUEST_CHECK_SETTINGS = 100;

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
    String timeSet = "";
    private int LOCATION_UPDATE = 888;
    private File compressedImageFile;
    private String sendingDate = "", strFinalSendingDate = "";
    private String claim_number = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookbodyshop);
        ButterKnife.bind(this);
        autoTechApp = new AutoTechApp();
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

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
        titleTv.setText("BOOK BODYSHOP");

        //select default date using calender
        final Calendar c = Calendar.getInstance();
        currentyear = c.get(Calendar.YEAR);
        currentmonth = c.get(Calendar.MONTH);
        currentday = c.get(Calendar.DAY_OF_MONTH);
        selectedday=currentday;
        selectedmonth=currentmonth;
        selextedyear=currentyear;
        currenthour = c.get(Calendar.HOUR_OF_DAY);
        currentminute = c.get(Calendar.MINUTE);

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
        llDate.setOnClickListener(this);
        //get usercars api
        getUserCar();


        //get splashdata
        sr = new Gson().fromJson(Config.getSharedPreferences(BookBodyShopsActivity.this, "SplashData"), SplashResponse.class);
        if (sr.getArrBranchRecord().size() > 0) {
            for (int i = 0; i < sr.getArrBranchRecord().size(); i++) {
                strbranchList.add(sr.getArrBranchRecord().get(i).getBranchName());
                branchList.add(sr.getArrBranchRecord().get(i));
            }
        }

        //select branch using adapter
        ArrayAdapter adapterbranch =
                new ArrayAdapter(BookBodyShopsActivity.this, R.layout.spinner_reg_item, strbranchList);
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.getSharedPreferences(BookBodyShopsActivity.this, "current_location").equals("")) {
            int fineLoc_permission1 = ContextCompat.checkSelfPermission(BookBodyShopsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            int CoarseLoc_permission1 = ContextCompat.checkSelfPermission(BookBodyShopsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (fineLoc_permission1 == PackageManager.PERMISSION_GRANTED && CoarseLoc_permission1 == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                getPermission();
            }
        }

    }

    private void getUserCar() {
        if (CommonMethods.isNetwork(BookBodyShopsActivity.this)) {
            CommonMethods.showProgressDialog(BookBodyShopsActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.getUserRegisterCarList(Config.getSharedPreferences(BookBodyShopsActivity.this, "userID"), Config.getSharedPreferences(BookBodyShopsActivity.this, "accessToken"));
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
                                    new ArrayAdapter(BookBodyShopsActivity.this, R.layout.spinner_reg_item, strcarList);
                            adaptercity.setDropDownViewResource(R.layout.spinner_reg_item);

                            spinner_car.setAdapter(adaptercity);
                            spinner_car.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (parent != null)
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                    //((TextView) parent.getChildAt(0)).setTextSize(12);
                                    carID = modelList.get(position).getCarID();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), BookBodyShopsActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(BookBodyShopsActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), BookBodyShopsActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, BookBodyShopsActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, BookBodyShopsActivity.this);
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
                        .context(BookBodyShopsActivity.this)
                        .callback(BookBodyShopsActivity.this)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate(selextedyear, selectedmonth, selectedday)
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
                CommonMethods.saveSharedPreferences(BookBodyShopsActivity.this, "current_location", "");
                Intent intent = new Intent(BookBodyShopsActivity.this, LocationTracksActivity.class);
                startActivityForResult(intent, LOCATION_UPDATE);
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
                        mLayoutmanager = new GridLayoutManager(BookBodyShopsActivity.this, 1);
                        recyclerview_carPhoto.setLayoutManager(new LinearLayoutManager(BookBodyShopsActivity.this, LinearLayoutManager.HORIZONTAL, false));
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
                            adapterBodyPhotos = new AdapterBodyPhotos(BookBodyShopsActivity.this, mAlbumFiles);
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
            selectedday=dayOfMonth;
            selectedmonth=monthOfYear;
            selextedyear=year;
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

        if (requestCode == LOCATION_UPDATE) {
            if (resultCode == RESULT_OK) {
                straddress = data.getStringExtra("current_location");
                straddress = straddress.replaceAll("null,", "");
                straddress = straddress.replaceAll(",null", "");
                edt_LocationAddress.setText("");
                edt_LocationAddress.setText(straddress);
            }
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                straddress = data.getStringExtra("Address");
                straddress.replaceAll("null,", "");
                straddress.replaceAll(",null", "");
                edt_LocationAddress.setText(straddress);
            }
        }
    }

    private void validateBookService(String carID, String branchID, String date, String rechability) {
        if (carID.equals("")) {
            CommonMethods.showValidationPopup("Please select your car", BookBodyShopsActivity.this);
        } else if (branchID.equals("")) {
            CommonMethods.showValidationPopup("Please select your branch", BookBodyShopsActivity.this);
        } else if (date.equals("")) {
            CommonMethods.showValidationPopup("Please select date", BookBodyShopsActivity.this);
        } else if (rechability.equals("")) {
            CommonMethods.showValidationPopup("Please select rechability", BookBodyShopsActivity.this);
        } else if (edt_LocationAddress.getText().toString().equalsIgnoreCase("")) {
            CommonMethods.showValidationPopup("Please enter location (Address)", BookBodyShopsActivity.this);
        } else {
            //bookService(carID,branchID,date,rechability);
            new SyncTask().execute();
        }
    }

    @OnClick({R.id.img_address, R.id.btn_bookService, R.id.img_Addcar})
    public void onViewClicked(View view) {
        Intent i = null;
        switch (view.getId()) {
            case R.id.img_address:
                i = new Intent(BookBodyShopsActivity.this, UserAddresses.class);
                startActivityForResult(i, 1);
                break;
            case R.id.btn_bookService:
                //date = txt_serviceDate.getText().toString();
                sendingDate = txt_serviceDate.getText().toString();
                claim_number = edtClaimNo.getText().toString();
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
                i = new Intent(BookBodyShopsActivity.this, MainActivity.class);
                i.putExtra("from", "addCar");
                startActivity(i);
                break;
        }
    }

    @OnClick(R.id.rel_timepick)
    public void onViewClicked() {
//        Calendar mcurrentTime = Calendar.getInstance();
//        int currenthour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int currentminute = mcurrentTime.get(Calendar.MINUTE);

        int selectedhour = currenthour;
        if (timeSet.equalsIgnoreCase("PM")) {
            if (selectedhour == 12)
            {
                selectedhour=12;
            }
            else
                selectedhour = currenthour + 12;
        } else {
            if (selectedhour == 12)
            {
                selectedhour=0;
            }
            else
                selectedhour=currenthour;
        }


        int selectedmin = currentminute;
        TimePickerDialog timePickerDialog = new TimePickerDialog(BookBodyShopsActivity.this,
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
    }

    public class SyncTask extends AsyncTask<String, Integer, String> {
        private int code;
        private String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonMethods.showProgressDialog(BookBodyShopsActivity.this, "Loading");
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
                        compressedImageFile = new Compressor(BookBodyShopsActivity.this).compressToFile(imageFile.get(i));
                        entity.addPart(imagekey, new FileBody(compressedImageFile));
                    }
                }
                int imagecount = imageFile != null ? imageFile.size() : 0;
                String strimageCount = String.valueOf(imagecount);
                // Extra parameters if you want to pass to server

                entity.addPart("userID", new StringBody(Config.getSharedPreferences(BookBodyShopsActivity.this, "userID")));
                entity.addPart("accessToken", new StringBody(Config.getSharedPreferences(BookBodyShopsActivity.this, "accessToken")));
                entity.addPart("carID", new StringBody(carID));
                entity.addPart("branchID", new StringBody(branchID));
                entity.addPart("serviceDate", new StringBody(strFinalSendingDate));
                entity.addPart("reachability", new StringBody(rechability));
                entity.addPart("pickupAddress", new StringBody(edt_LocationAddress.getText().toString()));
                entity.addPart("latitude", new StringBody(strlattitude));
                entity.addPart("longitude", new StringBody(strlongitude));
                entity.addPart("serviceTime", new StringBody(txtPickuptime.getText().toString()));
                entity.addPart("image_data_count", new StringBody(strimageCount));
                entity.addPart("claim_number", new StringBody(claim_number));

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
            CommonMethods.hideProgressDialog();
            if (statusCode == 200) {
                if (code == 100) {
                    CommonMethods.showValidationPopup(message, BookBodyShopsActivity.this, new CommonMethods.OkButtonClicklistner() {
                        @Override
                        public void OkButtonClick() {
                            Intent intent = new Intent(BookBodyShopsActivity.this, ThankYou.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }
            }

        }
    }

    //TODO NEW GPS CODE
    boolean gps_enabled = false;
    boolean network_enabled = false;

    private void getUserLocation() {
        // Get user location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(BookBodyShopsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(BookBodyShopsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            new AlertDialog.Builder(BookBodyShopsActivity.this)
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
                    str = str.replaceAll(",null", "");
                    str = str.replaceAll("null,", "");
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
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_home:
                //addSomething();
                Intent intent = new Intent(BookBodyShopsActivity.this, MainActivity.class);
                startActivity(intent);
                // mainActivity.callhome();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getPermission() {
        TedPermission.with(BookBodyShopsActivity.this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(BookBodyShopsActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }
}
