package com.autotechsolutions;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AMCDetail extends AppCompatActivity {
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_listtitle)
    CustomTextView txtListtitle;
    @BindView(R.id.txt_subtitle)
    CustomTextView txtSubtitle;
    @BindView(R.id.lin_expand)
    RelativeLayout linExpand;
    @BindView(R.id.txt_labour)
    CustomTextView txtLabour;
    @BindView(R.id.ll_labour)
    LinearLayout llLabour;
    @BindView(R.id.txt_parts)
    CustomTextView txtParts;
    @BindView(R.id.ll_parts)
    LinearLayout llParts;
    @BindView(R.id.sub_item)
    LinearLayout subItem;
    @BindView(R.id.txt_terms)
    CustomTextView txtTerms;
    @BindView(R.id.txt_termsandcondition)
    CustomTextView txtTermsandcondition;
    @BindView(R.id.btn_grab)
    CustomButton btnGrab;
    private String title, subtitle, labour, parts;
    private String tnc;
    String isPurchased;
    private String carID, amcID,amcType;
    private String price, amcUserID, amc_couponID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amc_details);
        ButterKnife.bind(this);
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toolbar.setNavigationIcon(R.drawable.ic_backblack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleTv.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        subtitle = intent.getStringExtra("subtitle");
        labour = intent.getStringExtra("labour");
        parts = intent.getStringExtra("parts");
        tnc = intent.getStringExtra("tnc");
        isPurchased = intent.getStringExtra("isPurchased");
        carID = intent.getStringExtra("carID");
        amcID = intent.getStringExtra("amcID");
        price = intent.getStringExtra("price");
        amcUserID = intent.getStringExtra("amcUserID");
        amcType = intent.getStringExtra("amcType");
        amc_couponID = intent.getStringExtra("amc_couponID");
        if (title.length()>20){
            titleTv.setText("AMC DETAILS");
        }else {
            titleTv.setText(title);
        }


        txtListtitle.setText(title);
        txtSubtitle.setText(subtitle);

        txtLabour.setText(Html.fromHtml(labour, null, new UlTagHandler()));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            txtLabour.setText(Html.fromHtml(labour, Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            txtLabour.setText(Html.fromHtml(labour));
//        }
        txtParts.setText(Html.fromHtml(parts, null, new UlTagHandler()));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            txtParts.setText(Html.fromHtml(parts, Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            txtParts.setText(Html.fromHtml(parts));
//        }
        txtTermsandcondition.setText(Html.fromHtml(tnc, null, new UlTagHandler()));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            txtTermsandcondition.setText(Html.fromHtml(tnc, Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            txtTermsandcondition.setText(Html.fromHtml(tnc));
//        }
        //txtLabour.setText(Html.fromHtml(labour));
        //txtParts.setText(Html.fromHtml(parts));

        //txtTermsandcondition.setText(Html.fromHtml(tnc));

        if (parts.equals("")){
            llParts.setVisibility(View.GONE);
        }else {
            llParts.setVisibility(View.VISIBLE);
        }
        if (labour.equals("")){
            llLabour.setVisibility(View.GONE);
        }else {
            llLabour.setVisibility(View.VISIBLE);
        }

        if (isPurchased.equals("0")) {
            btnGrab.setText("Buy now");
        } else {
            btnGrab.setText("Use");
        }

    }


    @OnClick(R.id.btn_grab)
    public void onViewClicked() {
        if (btnGrab.getText().toString().equals("Use")) {
            Intent intent = new Intent(AMCDetail.this, QRScanActivity.class);
            intent.putExtra("amcUserID", amcUserID);
            intent.putExtra("amc_couponID", amc_couponID);
            intent.putExtra("amcType", amcType);
            startActivity(intent);
        } else if (btnGrab.getText().toString().equals("Buy now")) {
            CommonMethods.showValidationPopupConfirmation("Are you sure you want to buy ?", AMCDetail.this, new CommonMethods.upDateButtonClicklistner() {
                @Override
                public void updateButtonClick() {
                    getAMCInquiry();
                }
            });
        }

    }

    private void getAMCInquiry() {
        if (CommonMethods.isNetwork(AMCDetail.this)) {
            CommonMethods.showProgressDialog(AMCDetail.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.add_amc_inquiry(Config.getSharedPreferences(AMCDetail.this, "userID"), Config.getSharedPreferences(AMCDetail.this, "accessToken"), carID, amcID);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {
                            CommonMethods.showValidationPopup(c.getMessage(), AMCDetail.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(AMCDetail.this, ThankYou.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), AMCDetail.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(AMCDetail.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), AMCDetail.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, AMCDetail.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, AMCDetail.this);
                }
            });
        }
    }
}
