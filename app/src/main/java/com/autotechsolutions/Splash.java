package com.autotechsolutions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.autotechsolutions.Response.Common;
import com.autotechsolutions.Response.SplashResponse;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 0;
    private String userid, accessToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.e("SplashTime", "call start");
        getData();

    }

    private void getData() {
        if (CommonMethods.isNetwork(Splash.this)) {
            CommonMethods.showProgressDialog(Splash.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            accessToken = Config.getSharedPreferences(this, "accessToken");
            if (accessToken == null)
                accessToken = "";

            userid = Config.getSharedPreferences(this, "userID");
            if (userid == null)
                userid = "";
            Log.e("SplashTime", "accesstoken:"+accessToken+":"+userid);
            Call<SplashResponse> call = apiInterface.getDataOnSplash(userid, accessToken);
            call.enqueue(new Callback<SplashResponse>() {
                @Override
                public void onResponse(Call<SplashResponse> call, Response<SplashResponse> response) {
                    CommonMethods.hideProgressDialog();
                    SplashResponse c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {
                           /* String abc=new Gson().toJson(c).toString();
                            Log.e("SplashTime", "call end:"+abc);*/
                            Config.saveSharedPreferences(Splash.this, "SplashData", new Gson().toJson(c));
                            Config.saveSharedPreferences(Splash.this, "referralMessage", c.getShareMessage());
                            Config.saveSharedPreferences(Splash.this, "aboutustext", c.getAbout_us_text());
                            Config.saveSharedPreferences(Splash.this, "sosnumber", c.getSos_number());
                            Log.e("SplashTime", "call end");
                         /*   new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {*/
                            //   CommonMethods.hideProgressDialog();
                            if (Config.getSharedPreferences(Splash.this, "islogin") != null) {
                                Log.e("SplashTime", "handler end");
                                if (Config.getSharedPreferences(Splash.this, "islogin").equals("")) {
                                    Intent i = new Intent(Splash.this, MobileNumber.class);
                                    startActivity(i);
                                    finish();
                                    Log.e("SplashTime", "end");
                                } else {
                                    Intent i = new Intent(Splash.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    Log.e("SplashTime", "end");
                                }

                            } else {
                                Log.e("SplashTime", "handler end");
                                Intent i = new Intent(Splash.this, MobileNumber.class);
                                startActivity(i);
                                finish();
                                Log.e("SplashTime", "end");
                            }

                            /*    }
                            }, SPLASH_TIME_OUT);*/

                        } else if (c.getCode() == 102) {
                            //        CommonMethods.hideProgressDialog();
                            CommonMethods.showValidationPopup(c.getMessage(), Splash.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(Splash.this, MobileNumber.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            //      CommonMethods.hideProgressDialog();
                            CommonMethods.showValidationPopup(c.getMessage(), Splash.this);
                        }
                    } else {
                        //  CommonMethods.hideProgressDialog();
                        CommonMethods.showValidationPopup(Config.FailureMsg, Splash.this);
                    }

                }

                @Override
                public void onFailure(Call<SplashResponse> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, Splash.this);
                }
            });
        }
    }
}
