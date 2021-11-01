package com.autotechsolutions.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autotechsolutions.CarcareActivity;
import com.autotechsolutions.Config;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.MobileNumber;
import com.autotechsolutions.Model.AddCarModel;
import com.autotechsolutions.Model.carsModel;
import com.autotechsolutions.R;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.RetroApiClient;
import com.autotechsolutions.RetroApiInterface;
import com.autotechsolutions.Splash;
import com.autotechsolutions.fragment.FragmentAddCar;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarAdapter extends RecyclerView.Adapter<AddCarAdapter.Holder> {

    private Context mContext;
    private ArrayList<carsModel> addCarModels = new ArrayList<>();
    private LayoutInflater inflater;
    private FragmentAddCar fragmentAddCar;

    public AddCarAdapter(ArrayList<carsModel> addCarModels, Context context, FragmentAddCar fragmentAddCar) {
        this.addCarModels = addCarModels;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.fragmentAddCar = fragmentAddCar;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.row_addcar, viewGroup, false);
        final Holder holder = new Holder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
         final carsModel model = addCarModels.get(i);

        if (i > 9) {
            holder.txt_CarId.setText(i + 1 + "");
        } else {
            holder.txt_CarId.setText(i + 1 + "");
        }
        holder.txt_CompanyName.setText("" + model.getMakeName());
        holder.txt_CarMode.setText("" + model.getModelName());
        holder.txt_RegNumber.setText("" + model.getRegistrationNumber());
        holder.rel_Deltecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.showValidationPopupConfirmation("Are you sure you want to remove your car?", mContext, new CommonMethods.upDateButtonClicklistner() {
                    @Override
                    public void updateButtonClick() {
                        deletecar(model.getCarID());
                    }
                });

            }
        });

    }

    private void deletecar(String carID) {
        if (CommonMethods.isNetwork(mContext)) {
            CommonMethods.showProgressDialog(mContext, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.delete_user_car(Config.getSharedPreferences(mContext, "userID"), Config.getSharedPreferences(mContext, "accessToken"), carID);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {

                            CommonMethods.showValidationPopup(c.getMessage(), mContext, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    fragmentAddCar.getMyCar();
                                }
                            });

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), mContext, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(mContext, MobileNumber.class);
                                    mContext.startActivity(intent);
                                    ((Activity) mContext).finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), mContext);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, mContext);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, mContext);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return addCarModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_CompanyName)
        CustomTextView txt_CompanyName;

        @BindView(R.id.txt_CarMode)
        CustomTextView txt_CarMode;
        @BindView(R.id.txt_RegNumber)
        CustomTextView txt_RegNumber;

        @BindView(R.id.txt_CarId)
        CustomTextView txt_CarId;

        @BindView(R.id.rel_Deltecar)
        RelativeLayout rel_Deltecar;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
