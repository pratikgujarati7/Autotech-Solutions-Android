package com.autotechsolutions.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.autotechsolutions.Config;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.MobileNumber;
import com.autotechsolutions.Model.NotificationModel;
import com.autotechsolutions.Model.UserAddressBookListModel;
import com.autotechsolutions.QRScanActivity;
import com.autotechsolutions.R;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.RetroApiClient;
import com.autotechsolutions.RetroApiInterface;
import com.autotechsolutions.fragment.FragmentAddCar;
import com.autotechsolutions.fragment.FragmentAddressBook;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterAddressBook extends RecyclerView.Adapter<AdapterAddressBook.Holder> {


    private Context context;
    private List<UserAddressBookListModel> userAddressBookListModels;
    private FragmentAddressBook fab;

    public AdapterAddressBook(Context context, List<UserAddressBookListModel> modelList, FragmentAddressBook fab) {
        this.context = context;
        this.userAddressBookListModels = modelList;
        this.fab = fab;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.row_address, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        final UserAddressBookListModel model = userAddressBookListModels.get(i);

        holder.txt_title.setText("" + model.getTitle());
        holder.txt_description.setText("" + model.getAddress());
        holder.delete_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.showValidationPopupConfirmation("Are you sure you want to remove your address?", context, new CommonMethods.upDateButtonClicklistner() {
                    @Override
                    public void updateButtonClick() {
                        deleteaddress(model.getUserAddressBookID());
                    }
                });
            }
        });
    }

    private void deleteaddress(String userAddressBookID) {
        if (CommonMethods.isNetwork(context)) {
            CommonMethods.showProgressDialog(context, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.delete_user_address_book(Config.getSharedPreferences(context, "userID"), Config.getSharedPreferences(context, "accessToken"), userAddressBookID);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {

                            CommonMethods.showValidationPopup(c.getMessage(), context, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    fab.get_all_user_address_book();
                                }
                            });

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), context, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(context, MobileNumber.class);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), context);
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, context);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, context);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userAddressBookListModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_title)
        CustomTextView txt_title;
        @BindView(R.id.txt_description)
        CustomTextView txt_description;
        @BindView(R.id.delete_address)
        ImageView delete_address;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
