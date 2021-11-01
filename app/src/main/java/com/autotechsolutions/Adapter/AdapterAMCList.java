package com.autotechsolutions.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.autotechsolutions.AMCDetail;
import com.autotechsolutions.Config;
import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.GrabAMCActivity;
import com.autotechsolutions.MobileNumber;
import com.autotechsolutions.Model.AMCModel;
import com.autotechsolutions.Model.AMCTypeModel;
import com.autotechsolutions.MyAmc;
import com.autotechsolutions.R;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.RetroApiClient;
import com.autotechsolutions.RetroApiInterface;
import com.autotechsolutions.ThankYou;
import com.autotechsolutions.UlTagHandler;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterAMCList extends RecyclerView.Adapter<AdapterAMCList.Holder> {

    private ArrayList<AMCTypeModel> amcModels;
    private Context mContext;
    AMCModel amcModel;
    Common common;
    String carID;

    public AdapterAMCList(Context context, ArrayList<AMCTypeModel> amcModelList, Common common,String carID) {

        this.mContext = context;
        this.amcModels = amcModelList;
        this.common = common;
        this.carID = carID;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.row_amc, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {


        final AMCTypeModel amcModel = amcModels.get(i);
        holder.txt_listtitle.setText("" + amcModel.getAmcTitle());
        holder.txt_subtitle.setText("" + amcModel.getAmcDescription());
        if (amcModel.getPrice().equals("0")){
            holder.txt_price.setVisibility(View.GONE);
        }else {
            holder.txt_price.setVisibility(View.VISIBLE);
            holder.txt_price.setText("₹ "+amcModel.getPrice());
        }

        if (common.getData().getArrAMC().get(i).getIsPurchased().equals("0")) {
            holder.btn_grab.setVisibility(View.VISIBLE);
            holder.btn_grab.setText("BUY NOW");
            holder.btn_grab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonMethods.showValidationPopupConfirmation("Are you sure you want to buy?", mContext, new CommonMethods.upDateButtonClicklistner() {
                        @Override
                        public void updateButtonClick() {
                            getAMCInquiry(common.getData().getArrAMC().get(i).getAmcID());
                        }
                    });

                }
            });
        } else {

        }

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, MyAmc.class);
                intent.putExtra("data",new Gson().toJson(common));
                intent.putExtra("position",i+"");
                intent.putExtra("title",amcModel.getAmcTitle());
                intent.putExtra("amcType",amcModel.getAmcType());
                intent.putExtra("amcPrice",amcModel.getPrice());
                intent.putExtra("amcPrice",amcModel.getPrice());
                intent.putExtra("ispurchased",amcModel.getIsPurchased());
                intent.putExtra("carID",carID);
                mContext.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return this.amcModels == null ? 0 : this.amcModels.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_listtitle)
        CustomTextView txt_listtitle;

        @BindView(R.id.txt_subtitle)
        CustomTextView txt_subtitle;

        @BindView(R.id.txt_price)
        CustomTextView txt_price;

        @BindView(R.id.btn_grab)
        CustomButton btn_grab;

        @BindView(R.id.ll_main)
        LinearLayout ll_main;


        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    private void getAMCInquiry(String amcID) {
        if (CommonMethods.isNetwork(mContext)) {
            CommonMethods.showProgressDialog(mContext, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.add_amc_inquiry(Config.getSharedPreferences(mContext, "userID"), Config.getSharedPreferences(mContext, "accessToken"), carID, amcID);
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
                                    Intent intent = new Intent(mContext, ThankYou.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intent);
                                    ((Activity) mContext).finish();
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

}
