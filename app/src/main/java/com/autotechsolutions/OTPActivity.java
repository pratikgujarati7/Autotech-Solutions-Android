package com.autotechsolutions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.autotechsolutions.Response.Common;
import com.autotechsolutions.utils.CommonMethods;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.philio.pinentry.PinEntryView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {
    @BindView(R.id.otpPinView)
    PinEntryView otpPinView;
    @BindView(R.id.img_next)
    ImageView imgNext;
    @BindView(R.id.txt_otp_info)
    TextView txtOtpInfo;
    private String mobile_number;
    private String isAlreadyRegister;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mobile_number = intent.getStringExtra("mobile_number");
        isAlreadyRegister = intent.getStringExtra("isAlreadyRegister");
        txtOtpInfo.setText("Please type the verification code sent to " + mobile_number);

        // Get an instance of SmsRetrieverClient, used to start listening for a matching
// SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(OTPActivity.this);

// Starts SmsRetriever, which waits for ONE matching SMS message until timeout
// (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
// action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task. If in a background thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
            }
        });

    }

    @OnClick(R.id.img_next)
    public void onViewClicked() {
        validateOtp(otpPinView.getText().toString());
//        Intent intent=new Intent(OTPActivity.this,RegisterActivity.class);
//        startActivity(intent);
    }

    private void validateOtp(String otp) {
        if (TextUtils.isEmpty(otp)) {
            CommonMethods.showValidationPopup("Please enter correct verification code.", OTPActivity.this);
        } else {
            sendOtp(otp);
        }
    }

    private void sendOtp(String otp) {
        if (CommonMethods.isNetwork(OTPActivity.this)) {
            CommonMethods.showProgressDialog(OTPActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.verifyUser(Config.getSharedPreferences(OTPActivity.this, "userID"), Config.getSharedPreferences(OTPActivity.this, "accessToken"), otp);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    Intent intent = null;
                    if (c != null) {
                        if (c.getCode() == 100) {
                            if (isAlreadyRegister.equals("1")) {
                                Config.saveSharedPreferences(OTPActivity.this, "islogin", "yes");
                                Config.saveSharedPreferences(OTPActivity.this, "referralcode", c.getData().getUserDetails().getReferralCode());
                                Config.saveSharedPreferences(OTPActivity.this, "referralMessage", c.getData().getUserDetails().getReferralMessage());
                                Config.saveSharedPreferences(OTPActivity.this, "mobilenumber", mobile_number);
                                intent = new Intent(OTPActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                intent = new Intent(OTPActivity.this, RegisterActivity.class);
                                intent.putExtra("mobile_number", mobile_number);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            otpPinView.clearText();
                            CommonMethods.showValidationPopup(c.getMessage(), OTPActivity.this);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, OTPActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, OTPActivity.this);
                }
            });
        }
    }
}
