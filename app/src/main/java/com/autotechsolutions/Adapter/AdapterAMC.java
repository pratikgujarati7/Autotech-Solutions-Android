package com.autotechsolutions.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.autotechsolutions.AMCDetail;
import com.autotechsolutions.Config;
import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.InsuranceRenewalActivity;
import com.autotechsolutions.MainActivity;
import com.autotechsolutions.Model.AMCModel;
import com.autotechsolutions.QRScanActivity;
import com.autotechsolutions.R;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.RetroApiClient;
import com.autotechsolutions.RetroApiInterface;
import com.autotechsolutions.UlTagHandler;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterAMC extends RecyclerView.Adapter<AdapterAMC.Holder> {

    private ArrayList<AMCModel> amcModels;
    private Context mContext;
    String isPurchased;
    String carID;
    String amcUserID;
    String amcType;
    AMCModel amcModel;

    public AdapterAMC(Context context, ArrayList<AMCModel> amcModelList,String isPurchased,String carID,String amcUserID,String amcType)
    {

        this.mContext = context;
        this.amcModels = amcModelList;
        this.isPurchased = isPurchased;
        this.carID = carID;
        this.amcUserID = amcUserID;
        this.amcType = amcType;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.row_amc_layout, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {


       final AMCModel amcModel = amcModels.get(i);
        holder.txt_listtitle.setText(""+amcModel.getTitle());
        holder.txt_subtitle.setText(""+amcModel.getSubTitle());
        holder.txt_labour.setText(Html.fromHtml(amcModel.getLabourDetails(), null, new UlTagHandler()));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.txt_labour.setText(Html.fromHtml(amcModel.getLabourDetails(), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            holder.txt_labour.setText(Html.fromHtml(amcModel.getLabourDetails(), null, new UlTagHandler()));
//        }
        holder.txt_parts.setText(Html.fromHtml(amcModel.getPartsDetails(), null, new UlTagHandler()));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.txt_parts.setText(Html.fromHtml(amcModel.getPartsDetails(), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            holder.txt_parts.setText(Html.fromHtml(amcModel.getPartsDetails(), null, new UlTagHandler()));
//        }
        //holder.txt_labour.setText(""+amcModel.getLabourDetails());
        //holder.txt_parts.setText(""+amcModel.getPartsDetails());
        if (amcModel.getPartsDetails().equals("")){
            holder.ll_parts.setVisibility(View.GONE);
            holder.linermain.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//            holder.linermain.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        }else {
            holder.ll_parts.setVisibility(View.VISIBLE);
        }
        if (amcModel.getLabourDetails().equals("")){
            holder.ll_labour.setVisibility(View.GONE);
            holder.linermain.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//            holder.linermain.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        }else {
            holder.ll_labour.setVisibility(View.VISIBLE);
        }
//        if (isPurchased.equals("0")){
//            holder.btn_grab.setVisibility(View.VISIBLE);
//            holder.btn_grab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getAMCInquiry();
//
//                }
//            });
//
//        }else {
//            holder.btn_grab.setVisibility(View.GONE);
//        }

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, AMCDetail.class);
                intent.putExtra("title",amcModel.getTitle());
                intent.putExtra("isPurchased",isPurchased);
                intent.putExtra("subtitle",amcModel.getSubTitle());
                intent.putExtra("labour",amcModel.getLabourDetails());
                intent.putExtra("parts",amcModel.getPartsDetails());
                intent.putExtra("tnc",amcModel.getTnc());
                intent.putExtra("carID",carID);
                intent.putExtra("amcID",amcModel.getAmcID());
                intent.putExtra("amcUserID",amcUserID);
                intent.putExtra("amcType",amcType);
                intent.putExtra("amc_couponID",amcModel.getAmcCoupanID());
                mContext.startActivity(intent);
            }
        });



    }



    @Override
    public int getItemCount() {
        return this.amcModels==null ? 0 :this.amcModels.size();
    }


    public class Holder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_listtitle)
        CustomTextView txt_listtitle;

        @BindView(R.id.txt_subtitle)
        CustomTextView txt_subtitle;

        @BindView(R.id.txt_labour)
        CustomTextView txt_labour;

        @BindView(R.id.txt_parts)
        CustomTextView txt_parts;

//        @BindView(R.id.btn_grab)
//        CustomButton btn_grab;

        @BindView(R.id.sub_item)
        LinearLayout sub_item;
        @BindView(R.id.ll_main)
        LinearLayout ll_main;
        @BindView(R.id.ll_labour)
        LinearLayout ll_labour;
        @BindView(R.id.ll_parts)
        LinearLayout ll_parts;

        @BindView(R.id.linermain)
        LinearLayout linermain;


        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
