package com.autotechsolutions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.utils.CommonMethods;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquireActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TextView titleTv;

    @BindView(R.id.img_TitlePhoto)
    ImageView img_TitlePhoto;

    @BindView(R.id.txt_title)
    CustomTextView txt_title;

    @BindView(R.id.txt_description)
    CustomTextView txt_description;

    @BindView(R.id.btn_inquire)
    CustomButton btn_inquire;
    private String image;
    private String name, description, carID, productmodelID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire);
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
        titleTv.setText("CAR CARE");
        Intent intent = getIntent();
        image = intent.getStringExtra("image");
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        carID = intent.getStringExtra("carID");
        productmodelID = intent.getStringExtra("productmodelID");

        txt_title.setText(name);
        txt_description.setText(description);
        Glide.with(this).load(image).into(img_TitlePhoto);

        btn_inquire.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_inquire:
                //Toast.makeText(this, "Inquire.", Toast.LENGTH_SHORT).show();
                addInquiry();
                break;
            default:
                break;
        }
    }

    private void addInquiry() {
        if (CommonMethods.isNetwork(InquireActivity.this)) {
            CommonMethods.showProgressDialog(InquireActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.add_product_inquiry(Config.getSharedPreferences(InquireActivity.this, "userID"), Config.getSharedPreferences(InquireActivity.this, "accessToken"), carID, productmodelID);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {

                    if (c != null) {
                        if (c.getCode() == 100) {
                            CommonMethods.showValidationPopup(c.getMessage(), InquireActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
//                                Intent intent=new Intent(InquireActivity.this,MainActivity.class);
//                                startActivity(intent);
                                    finish();
                                }
                            });
                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), InquireActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(InquireActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), InquireActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, InquireActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, InquireActivity.this);
                }
            });
        }
    }
}
