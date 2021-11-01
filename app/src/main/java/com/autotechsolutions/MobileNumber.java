package com.autotechsolutions;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.autotechsolutions.Response.Common;
import com.autotechsolutions.Response.SplashResponse;
import com.autotechsolutions.utils.CommonMethods;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileNumber extends AppCompatActivity {
    @BindView(R.id.edt_mobilenumber)
    EditText edtMobilenumber;
    @BindView(R.id.img_next)
    ImageView imgNext;
    private String deviceToken = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobilenumber);
        ButterKnife.bind(this);
        if (Config.getSharedPreferences(MobileNumber.this, "token") == null) {
            deviceToken = FirebaseInstanceId.getInstance().getToken();
        } else {
            deviceToken = Config.getSharedPreferences(MobileNumber.this, "token");
        }
    }


    @OnClick(R.id.img_next)
    public void onViewClicked() {
        validateMObileNumber(edtMobilenumber.getText().toString());
    }

    private void validateMObileNumber(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            CommonMethods.showValidationPopup("Please enter your mobile number", MobileNumber.this);
        } else if (mobile.length() < 10 || mobile.length() > 10) {
            CommonMethods.showValidationPopup("Please enter valid mobile number", MobileNumber.this);
        } else {
            sendMobilenumber(mobile);
        }
    }

    private void sendMobilenumber(final String mobile) {
        if (CommonMethods.isNetwork(MobileNumber.this)) {
            CommonMethods.showProgressDialog(MobileNumber.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.sendMobile(mobile, "1", deviceToken);
            call.enqueue(new Callback<Common>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    Log.e("On Response Code","" + response.code());
//                    Log.e("On Response String","" + response.body().toString());
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                }

                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {
                            Config.saveSharedPreferences(MobileNumber.this, "userID", c.getData().getUserDetails().getUserID());
                            Config.saveSharedPreferences(MobileNumber.this, "accessToken", c.getData().getUserDetails().getAccessToken());
                            //TODO add isAlreadyRegister flag
//                            if (c.getData().getUserDetails().getIsVerified().equals("1")){
//                                intent=new Intent(MobileNumber.this,MainActivity.class);
//                                startActivity(intent);
//                            }else {
//                                intent=new Intent(MobileNumber.this,OTPActivity.class);
//                                startActivity(intent);
//                            }
                            Intent intent = new Intent(MobileNumber.this, OTPActivity.class);
                            intent.putExtra("mobile_number", mobile);
                            intent.putExtra("isAlreadyRegister", String.valueOf(c.getData().getUserDetails().getIsUserAlreadyRegistered()));
                            startActivity(intent);
                            finish();
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), MobileNumber.this);
                        }
                    }else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, MobileNumber.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, MobileNumber.this);
                }
            });
        }
    }
}
