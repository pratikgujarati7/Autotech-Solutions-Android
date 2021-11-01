package com.autotechsolutions.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.autotechsolutions.Config;
import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomEditText;
import com.autotechsolutions.MainActivity;
import com.autotechsolutions.MobileNumber;
import com.autotechsolutions.Model.AreaModel;
import com.autotechsolutions.Model.CityModel;
import com.autotechsolutions.Model.StateModel;
import com.autotechsolutions.R;
import com.autotechsolutions.RegisterActivity;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.Response.SplashResponse;
import com.autotechsolutions.RetroApiClient;
import com.autotechsolutions.RetroApiInterface;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment {


    @BindView(R.id.spinner_satate)
    Spinner spinnerState;
    @BindView(R.id.spinner_city)
    Spinner spinnerCity;

    ArrayList<StateModel> statelist = new ArrayList<StateModel>();
    ArrayList<CityModel> citylist = new ArrayList<CityModel>();
    ArrayList<String> strstateList = new ArrayList<>();
    ArrayList<String> strcityList = new ArrayList<>();

    String[] cityList, areaList;
    SplashResponse sr;
    @BindView(R.id.edt_fname)
    CustomEditText edtFname;
    @BindView(R.id.edt_lname)
    CustomEditText edtLname;
    @BindView(R.id.edt_email)
    CustomEditText edtEmail;
    @BindView(R.id.edt_area)
    CustomEditText edtArea;
    @BindView(R.id.edt_mobilenumber)
    CustomEditText edtMobilenumber;
    @BindView(R.id.btn_bookService)
    CustomButton btnBookService;
    private String strcityID, strstateID;
    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).showToolbarTitle("PROFILE");
        unbinder = ButterKnife.bind(this, rootView);
        //((MainActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us));

        getProfile();




        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getProfile() {
        if (CommonMethods.isNetwork(getActivity())) {
            CommonMethods.showProgressDialog(getActivity(), "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.get_user_profile_details(Config.getSharedPreferences(getActivity(), "userID"), Config.getSharedPreferences(getActivity(), "accessToken"));
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    final Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    Intent intent = null;
                    if (c != null) {
                        if (c.getCode() == 100) {

                            edtFname.setText(c.getData().getUserDetails().getFirstName());
                            edtLname.setText(c.getData().getUserDetails().getLastName());
                            if (c.getData().getUserDetails().getEmail()!= null) {
                                edtEmail.setText(c.getData().getUserDetails().getEmail());
                            }
                            edtMobilenumber.setText(Config.getSharedPreferences(getActivity(), "mobilenumber"));

                            edtArea.setText(c.getData().getUserDetails().getAreaName());

                            sr = new Gson().fromJson(Config.getSharedPreferences(getActivity(), "SplashData"), SplashResponse.class);

                            if (sr.getArrStateRecord().size() > 0) {
                                for (int i = 0; i < sr.getArrStateRecord().size(); i++) {
                                    strstateList.add(sr.getArrStateRecord().get(i).getStateName());
                                }
                            }
                            ArrayAdapter<String> cityArray = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, strstateList);
                            cityArray.setDropDownViewResource(R.layout.spinner_item);
                            spinnerState.setAdapter(cityArray);
                            ArrayAdapter myAdap = (ArrayAdapter) spinnerState.getAdapter(); //cast to an ArrayAdapter

                            int spinnerPosition = myAdap.getPosition(c.getData().getUserDetails().getStateName());


                            spinnerState.setSelection(spinnerPosition);
                            spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    strcityList.clear();
                                    if (parent != null)
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                    for (int j = 0; j < sr.getArrStateRecord().get(position).getArrAreaRecord().size(); j++) {
                                        citylist = sr.getArrStateRecord().get(position).getArrAreaRecord();
                                        strcityList.add(sr.getArrStateRecord().get(position).getArrAreaRecord().get(j).getCityName());
                                    }
                                    ArrayAdapter adapterbranch =
                                            new ArrayAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, strcityList);
                                    adapterbranch.setDropDownViewResource(R.layout.spinner_item);
                                    spinnerCity.setAdapter(adapterbranch);
                                    ArrayAdapter areaAdap = (ArrayAdapter) spinnerCity.getAdapter(); //cast to an ArrayAdapter
                                    int spinnerPosition = areaAdap.getPosition(c.getData().getUserDetails().getCityName());
                                    spinnerCity.setSelection(spinnerPosition);
                                    spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                            strcityID = citylist.get(position).getCityID();
                                            strstateID = citylist.get(position).getStateID();
//                         strareaID=sr.getArrCityRecord().get(position).getArrAreaRecord().get(position).getAreaID();
//                         strareaID=sr.getArrCityRecord().get(position).getArrAreaRecord().get(position).getCityID();
                                            Log.e("strcityID:", strcityID);
                                            Log.e("strstateID:", strstateID);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });



                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), getActivity(), new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(getActivity(), MobileNumber.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });
                        } else {
                        }
                    }else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, getActivity());
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, getActivity());
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_bookService)
    public void onViewClicked() {
        validateRegister(edtFname.getText().toString(), edtLname.getText().toString(), edtEmail.getText().toString(),edtArea.getText().toString());

    }

    private void validateRegister(String fname, String lname, String email, String area) {
        email=email.trim();
        if (TextUtils.isEmpty(fname)) {
            CommonMethods.showValidationPopup("Please enter first name", getActivity());
        } else if (TextUtils.isEmpty(lname)) {
            CommonMethods.showValidationPopup("Please enter last name", getActivity());
        } else if (TextUtils.isEmpty(email)) {
            CommonMethods.showValidationPopup("Please enter email", getActivity());
        } else if (!TextUtils.isEmpty(email) && !CommonMethods.emailValidator(email)) {
            CommonMethods.showValidationPopup("Please enter valid email", getActivity());
        } else if (TextUtils.isEmpty(spinnerState.getSelectedItem().toString())) {
            CommonMethods.showValidationPopup("Please select city", getActivity());
        } else if (TextUtils.isEmpty(spinnerCity.getSelectedItem().toString())) {
            CommonMethods.showValidationPopup("Please select area", getActivity());
        }else if (TextUtils.isEmpty(area)) {
            CommonMethods.showValidationPopup("Please enter area", getActivity());
        } else {
            updateProfile(fname, lname, email,area);
        }

    }

    private void updateProfile(String fname, String lname, String email, String area) {
        if (CommonMethods.isNetwork(getActivity())) {
            CommonMethods.showProgressDialog(getActivity(), "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.Registration(Config.getSharedPreferences(getActivity(), "userID"), Config.getSharedPreferences(getActivity(), "accessToken"), fname, lname, email, area, strcityID, "");
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {

                    if (c != null) {
                        if (c.getCode() == 100) {
                            Config.saveSharedPreferences(getActivity(), "userID", c.getData().getUserDetails().getUserID());
                             Config.saveSharedPreferences(getActivity(), "accessToken", c.getData().getUserDetails().getAccessToken());
                            Config.saveSharedPreferences(getActivity(), "islogin", "yes");
                            Config.saveSharedPreferences(getActivity(), "fname", c.getData().getUserDetails().getFirstName());
                            Config.saveSharedPreferences(getActivity(), "lname", c.getData().getUserDetails().getLastName());
                            Config.saveSharedPreferences(getActivity(), "email", c.getData().getUserDetails().getEmail());
                            Config.saveSharedPreferences(getActivity(), "city", c.getData().getUserDetails().getCityName());
                            Config.saveSharedPreferences(getActivity(), "cityID", c.getData().getUserDetails().getCityID());
                            Config.saveSharedPreferences(getActivity(), "area", c.getData().getUserDetails().getAreaName());
                            Config.saveSharedPreferences(getActivity(), "areaID", c.getData().getUserDetails().getAreaID());
//                            if (c.getData().getUserDetails().getIsVerified().equals("1")){
//                                intent=new Intent(getActivity(),MainActivity.class);
//                                startActivity(intent);
//                            }else {
//                                intent=new Intent(getActivity(),OTPActivity.class);
//                                startActivity(intent);
//                            }
                            CommonMethods.showValidationPopup(c.getMessage(), getActivity());
                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), getActivity(), new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(getActivity(), MobileNumber.class);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();
                                }
                            });
                        } else {
                            CommonMethods.showValidationPopup(c.getMessage(), getActivity());
                        }
                    } else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, getActivity());
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, getActivity());
                }
            });
        }
    }


}
