package com.autotechsolutions.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.autotechsolutions.BookBodyShopActivity;
import com.autotechsolutions.BookBodyShopsActivity;
import com.autotechsolutions.BookServicesActivity;
import com.autotechsolutions.BuyACarActivity;
import com.autotechsolutions.CarcareActivity;
import com.autotechsolutions.CommingSoonActivity;
import com.autotechsolutions.Config;
import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.GrabAMCActivity;
import com.autotechsolutions.InsuranceRenewalActivity;
import com.autotechsolutions.MainActivity;
import com.autotechsolutions.R;
import com.autotechsolutions.SellYourCarActivity;
import com.autotechsolutions.utils.CommonMethods;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentHome extends Fragment implements View.OnClickListener {

    @BindView(R.id.img_addcar)
    ImageView img_addcar;

    @BindView(R.id.rel_sos)
    RelativeLayout rel_sos;

    @BindView(R.id.rel_book_bodyshop)
    RelativeLayout rel_book_bodyshop;

    @BindView(R.id.rel_bookservice)
    RelativeLayout rel_bookservice;

    @BindView(R.id.rel_insurance)
    RelativeLayout rel_insurance;

    @BindView(R.id.rel_carcare)
    RelativeLayout rel_carcare;

    @BindView(R.id.rel_amc)
    RelativeLayout rel_amc;

    @BindView(R.id.rel_buy_a_car)
    RelativeLayout rel_buy_a_car;

    @BindView(R.id.rel_sell_your_car)
    RelativeLayout rel_sell_your_car;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).showToolbarTitle("Mitsubishi Motors");
        ButterKnife.bind(this, rootView);
        //((MainActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us));

        rel_sos.setOnClickListener(this);
        img_addcar.setOnClickListener(this);
        rel_book_bodyshop.setOnClickListener(this);
        rel_bookservice.setOnClickListener(this);
        rel_insurance.setOnClickListener(this);
        rel_amc.setOnClickListener(this);
        rel_carcare.setOnClickListener(this);
        rel_buy_a_car.setOnClickListener(this);
        rel_sell_your_car.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.rel_sos:

                final String number = Config.getSharedPreferences(getActivity(), "sosnumber");
                int call_permission1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                if (call_permission1 != PackageManager.PERMISSION_GRANTED) {
                    TedPermission.with(getActivity())
                            .setPermissionListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted() {

                                    Intent intent_call = new Intent(Intent.ACTION_CALL);
                                    intent_call.setData(Uri.parse("tel:" + number));
                                    startActivity(intent_call);
                                }

                                @Override
                                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                    Toast.makeText(getActivity(), "Permission denied.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPermissions(Manifest.permission.CALL_PHONE)
                            .check();

                } else {

                    Intent intent_call = new Intent(Intent.ACTION_CALL);
                    intent_call.setData(Uri.parse("tel:" + number));
                    startActivity(intent_call);
                }


                break;

            case R.id.img_addcar:
                ((MainActivity) getActivity()).displayView(R.id.nav_my_cars);
                break;

            case R.id.rel_book_bodyshop:
                CommonMethods.saveSharedPreferences(getActivity(), "current_location", "");
                intent = new Intent(getActivity(), BookBodyShopsActivity.class);
                startActivity(intent);
                break;

            case R.id.rel_bookservice:
                CommonMethods.saveSharedPreferences(getActivity(), "current_location", "");
//                intent = new Intent(getActivity(), BookServiceActivity.class);
                intent = new Intent(getActivity(), BookServicesActivity.class);
                startActivity(intent);
                break;

            case R.id.rel_insurance:


                final Dialog successDialog = new Dialog(getActivity());
                successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                successDialog.setContentView(R.layout.dialog_confirmation_layout);
                successDialog.setCancelable(false);
                CustomTextView titleTv = (CustomTextView) successDialog.findViewById(R.id.titleTv);
                CustomButton okBtn = (CustomButton) successDialog.findViewById(R.id.yesBtn);
                CustomButton noBtn = (CustomButton) successDialog.findViewById(R.id.noBtn);
                titleTv.setVisibility(View.VISIBLE);
                titleTv.setText("do you have an insurance policy?");

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(View v) {
                        successDialog.dismiss();
                        Intent intent = new Intent(getActivity(), InsuranceRenewalActivity.class);
                        intent.putExtra("from", "yes");
                        startActivity(intent);
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(View v) {
                        successDialog.dismiss();
                        Intent intent = new Intent(getActivity(), InsuranceRenewalActivity.class);
                        intent.putExtra("from", "no");
                        startActivity(intent);

                    }
                });
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                successDialog.getWindow().setLayout(width, height);

                successDialog.show();

                break;
            case R.id.rel_amc:
                intent = new Intent(getActivity(), GrabAMCActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_carcare:
//                intent = new Intent(getActivity(), CarcareActivity.class);
//                startActivity(intent);
                intent = new Intent(getActivity(), CommingSoonActivity.class);
                startActivity(intent);
                break;

            case R.id.rel_buy_a_car:
                intent = new Intent(getActivity(), BuyACarActivity.class);
                startActivity(intent);
                break;

            case R.id.rel_sell_your_car:
                intent = new Intent(getActivity(), SellYourCarActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
