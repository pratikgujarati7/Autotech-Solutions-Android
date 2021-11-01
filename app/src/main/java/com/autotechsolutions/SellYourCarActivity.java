package com.autotechsolutions;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.autotechsolutions.Adapter.AdapterBodyPhotos;
import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomEditText;
import com.autotechsolutions.CustomView.EmptyRecyclerView;
import com.autotechsolutions.Model.carsModel;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellYourCarActivity extends AppCompatActivity {


    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_selectcar)
    Spinner spinnerSelectcar;
    @BindView(R.id.edt_yearofmenufacturing)
    CustomEditText edtYearofmenufacturing;
    @BindView(R.id.edt_expectedprice)
    CustomEditText edtExpectedprice;
    @BindView(R.id.img_AddPhoto)
    ImageView imgAddPhoto;
    @BindView(R.id.recyclerview_carPhoto)
    EmptyRecyclerView recyclerviewCarPhoto;
    @BindView(R.id.img_addPhotoView)
    ImageView imgAddPhotoView;
    @BindView(R.id.btn_getquote)
    CustomButton btnGetquote;
    private ArrayList<AlbumFile> mAlbumFiles;
    private ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellyourcar);
        ButterKnife.bind(this);
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
        titleTv.setText("SELL YOUR CAR");
        getUserCar();

    }

    @OnClick({R.id.img_AddPhoto, R.id.img_addPhotoView, R.id.btn_getquote})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_AddPhoto:
                selectImage();
                break;
            case R.id.img_addPhotoView:
                selectImage();
                break;
            case R.id.btn_getquote:
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
                        GridLayoutManager mLayoutmanager = new GridLayoutManager(SellYourCarActivity.this, 1);
                        recyclerviewCarPhoto.setLayoutManager(new LinearLayoutManager(SellYourCarActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        for (int i = 0; i < mAlbumFiles.size(); i++) {
                            File imgFile = new File(mAlbumFiles.get(i).getPath());
                            mArrayUri.add(Uri.fromFile(imgFile));
                        }


                        if (mArrayUri.size() == 0) {
                            recyclerviewCarPhoto.setVisibility(View.GONE);
                            imgAddPhotoView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerviewCarPhoto.setVisibility(View.VISIBLE);
                            imgAddPhotoView.setVisibility(View.GONE);
                            AdapterBodyPhotos adapterBodyPhotos = new AdapterBodyPhotos(SellYourCarActivity.this, mAlbumFiles);
                            recyclerviewCarPhoto.setAdapter(adapterBodyPhotos);
                        }


                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                })
                .start();
    }

    private ArrayList<carsModel> modelList = new ArrayList<>();
    private ArrayList<String> strcarList = new ArrayList<>();
    private String carID;

    private void getUserCar() {
        if (CommonMethods.isNetwork(SellYourCarActivity.this)) {
            CommonMethods.showProgressDialog(SellYourCarActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.getUserRegisterCarList(Config.getSharedPreferences(SellYourCarActivity.this, "userID"), Config.getSharedPreferences(SellYourCarActivity.this, "accessToken"));
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
                                    new ArrayAdapter(SellYourCarActivity.this, R.layout.spinner_reg_item, strcarList);
                            adaptercity.setDropDownViewResource(R.layout.spinner_reg_item);

                            spinnerSelectcar.setAdapter(adaptercity);
                            spinnerSelectcar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            CommonMethods.showValidationPopup(c.getMessage(), SellYourCarActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(SellYourCarActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), SellYourCarActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, SellYourCarActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, SellYourCarActivity.this);
                }
            });
        }
    }
}
