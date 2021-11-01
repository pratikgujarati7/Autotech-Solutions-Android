package com.autotechsolutions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomEditText;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRScanActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TextView titleTv;

    @BindView(R.id.edt_QRCode)
    CustomEditText edt_QRCode;
    @BindView(R.id.btn_bookService)
    CustomButton btnBookService;
    private Context mContext = this;
    @BindView(R.id.qrdecoderview)
    DecoratedBarcodeView barcodeScanner;
    private BeepManager beepManager;
    private String code;
    private BarcodeFormat codeformate;
    private int codeformatnumber;
    private String amc_couponID, amcUserID,amcType;
    String scannedText = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
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
        titleTv.setText("MY AMC");
        checkPermissions();
        barcodeScanner.decodeSingle(callback);
        barcodeScanner.setStatusText("");
        beepManager = new BeepManager(QRScanActivity.this);

        Intent intent = getIntent();
        amc_couponID = intent.getStringExtra("amc_couponID");
        amcUserID = intent.getStringExtra("amcUserID");
        amcType = intent.getStringExtra("amcType");
    }


    private void checkPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            TedPermission.with(mContext)
                    .setPermissionListener(PermissionListner)
                    .setPermissions(Manifest.permission.CAMERA)
                    .check();

        } else {
            setupBarcode();
        }
    }

    private void setupBarcode() {
        barcodeScanner.decodeSingle(callback);
        beepManager = new BeepManager(QRScanActivity.this);
    }

    PermissionListener PermissionListner = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            setupBarcode();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(mContext, "Accces Denied.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        barcodeScanner.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScanner.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result != null) {
                //if qrcode has nothing in it
                if (result.getText() == null) {
                    Toast.makeText(QRScanActivity.this, "Result Not Found", Toast.LENGTH_LONG).show();
                } else {
                    barcodeScanner.pause();
                    code = result.getText();
                    codeformate = result.getBarcodeFormat();
                    edt_QRCode.setText(code);
                    Log.e("format:", codeformate + "");
                }
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };



    @OnClick(R.id.btn_bookService)
    public void onViewClicked() {
        validateQr(edt_QRCode.getText().toString());
    }

    private void validateQr(String qrcode) {
        if (TextUtils.isEmpty(qrcode)) {
            CommonMethods.showValidationPopup("Please scan your coupon", QRScanActivity.this);
        } else {
            sendQr(qrcode);
        }
    }

    private void sendQr(String qrcode) {
        if (CommonMethods.isNetwork(QRScanActivity.this)) {
            CommonMethods.showProgressDialog(QRScanActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.redeem_amc_coupan(Config.getSharedPreferences(QRScanActivity.this, "userID"), Config.getSharedPreferences(QRScanActivity.this, "accessToken"), amc_couponID, qrcode, amcUserID,amcType);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {
                            CommonMethods.showValidationPopup(c.getMessage(), QRScanActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(QRScanActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), QRScanActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(QRScanActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), QRScanActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, QRScanActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, QRScanActivity.this);
                }
            });
        }
    }
}
