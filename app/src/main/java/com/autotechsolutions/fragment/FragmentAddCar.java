package com.autotechsolutions.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.autotechsolutions.Adapter.AddCarAdapter;
import com.autotechsolutions.Config;
import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.MainActivity;
import com.autotechsolutions.MobileNumber;
import com.autotechsolutions.Model.AddCarModel;
import com.autotechsolutions.Model.ModelMakeModel;
import com.autotechsolutions.R;
import com.autotechsolutions.RegisterActivity;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.Response.SplashResponse;
import com.autotechsolutions.RetroApiClient;
import com.autotechsolutions.RetroApiInterface;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.philio.pinentry.PinEntryView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAddCar extends Fragment implements View.OnClickListener {

    //    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    TextView titleTv;
    @BindView(R.id.recyclerview_carlist)
    RecyclerView mRecyclerView;

    @BindView(R.id.lin_carList)
    LinearLayout lin_carList;
    @BindView(R.id.lin_addNewCar)
    LinearLayout lin_addNewCar;

    @BindView(R.id.lin_addNewCarView)
    LinearLayout lin_addNewCarView;

    @BindView(R.id.lin_myCarListView)
    LinearLayout lin_myCarListView;

    @BindView(R.id.view_carList)
    View view_carList;
    @BindView(R.id.view_addNewcar)
    View view_addNewcar;

    @BindView(R.id.spinner_carCompany)
    Spinner spinner_carCompany;

    @BindView(R.id.spinner_carModel)
    Spinner spinner_carModel;

    @BindView(R.id.spinner_fuletype)
    Spinner spinner_fuletype;

    @BindView(R.id.btn_saveCar)
    CustomButton btn_saveCar;

    AddCarAdapter addCarAdapter;
    RecyclerView.LayoutManager mLayoutmanager;

    String[] carCompanyList, carModelList;

    @BindView(R.id.entry_registerNumberone)
    PinEntryView entry_registerNumberOne;
    @BindView(R.id.entry_registerNumbertwo)
    PinEntryView entry_registerNumberTwo;
    @BindView(R.id.entry_registerNumberthree)
    PinEntryView entry_registerNumberThree;
    @BindView(R.id.entry_registerNumberfour)
    PinEntryView entry_registerNumberFour;

    ArrayList<AddCarModel> mDataset = new ArrayList<AddCarModel>();
    private SplashResponse sr;
    ArrayList<ModelMakeModel> modellist = new ArrayList<ModelMakeModel>();
    ArrayList<String> strcompanyList = new ArrayList<>();
    ArrayList<String> strmodelList = new ArrayList<>();
    private String companyID = "", modelID = "";
    String fuletype = "";
    private String registerNumber="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_addcar, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).showToolbarTitle("ADD YOUR CARS");
        ButterKnife.bind(this, rootView);
        //((MainActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us));
        init();
//        et_city.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        entry_registerNumberOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==2){
                    entry_registerNumberTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        entry_registerNumberTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==2 ){
                    entry_registerNumberThree.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        entry_registerNumberThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==2 ){
                    entry_registerNumberFour.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        entry_registerNumberOne.setOnKeyListener(new View.OnKeyListener() {
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (entry_registerNumberOne.getText().length()==2){
//                    entry_registerNumberTwo.requestFocus();
//                }
//                return false;
//            }
//        });
//        entry_registerNumberTwo.setOnKeyListener(new View.OnKeyListener() {
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (entry_registerNumberTwo.getText().length()==2){
//                    entry_registerNumberThree.requestFocus();
//                }
//                return false;
//            }
//        });
//        entry_registerNumberThree.setOnKeyListener(new View.OnKeyListener() {
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (entry_registerNumberThree.getText().length()==2){
//                    entry_registerNumberFour.requestFocus();
//                }
//                return false;
//            }
//        });


        sr = new Gson().fromJson(Config.getSharedPreferences(getActivity(), "SplashData"), SplashResponse.class);
        if (sr.getArrMakeRecord().size() > 0) {
            for (int i = 0; i < sr.getArrMakeRecord().size(); i++) {
                strcompanyList.add(sr.getArrMakeRecord().get(i).getMakeName());
            }
        }
        spinner_fuletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                fuletype = String.valueOf(spinner_fuletype.getSelectedItemId() + 1);
                Log.e("fuleType:", fuletype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> carCompanyArray = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, strcompanyList);
        carCompanyArray.setDropDownViewResource(R.layout.spinner_item);
        spinner_carCompany.setAdapter(carCompanyArray);
        spinner_carCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strmodelList.clear();
                if (parent != null)
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                for (int j = 0; j < sr.getArrMakeRecord().get(position).getArrModelRecord().size(); j++) {
                    modellist = sr.getArrMakeRecord().get(position).getArrModelRecord();
                    strmodelList.add(sr.getArrMakeRecord().get(position).getArrModelRecord().get(j).getModelName());
                }
                ArrayAdapter adapterbranch =
                        new ArrayAdapter(getActivity(), R.layout.spinner_reg_item, strmodelList);
                adapterbranch.setDropDownViewResource(R.layout.spinner_reg_item);
                spinner_carModel.setAdapter(adapterbranch);
                spinner_carModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                        companyID = modellist.get(position).getMakeID();
                        modelID = modellist.get(position).getModelID();
//                         strareaID=sr.getArrCityRecord().get(position).getArrAreaRecord().get(position).getAreaID();
//                         strareaID=sr.getArrCityRecord().get(position).getArrAreaRecord().get(position).getCityID();
                        Log.e("AreaID:", companyID);
                        Log.e("cityID:", modelID);
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
        //getMyCar();

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void getMyCar() {
        if (CommonMethods.isNetwork(getActivity())) {
            CommonMethods.showProgressDialog(getActivity(), "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.getUserRegisterCarList(Config.getSharedPreferences(getActivity(), "userID"), Config.getSharedPreferences(getActivity(), "accessToken"));
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {
                            if (c.getData().getCarDetails().size() > 0) {
                                mLayoutmanager = new GridLayoutManager(getActivity(), 1);
                                mRecyclerView.setLayoutManager(mLayoutmanager);
                                addCarAdapter = new AddCarAdapter(c.getData().getCarDetails(), getActivity(), FragmentAddCar.this);
                                mRecyclerView.setAdapter(addCarAdapter);
                            }

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

    private void init() {
        lin_carList.setOnClickListener(this);
        lin_addNewCar.setOnClickListener(this);
        btn_saveCar.setOnClickListener(this);
        carListVisible();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lin_carList:
                carListVisible();
                break;

            case R.id.lin_addNewCar:
                addNewcarVisible();
                break;

            case R.id.btn_saveCar:
                registerNumber=entry_registerNumberOne.getText().toString()+"-"+entry_registerNumberTwo.getText().toString()+"-"+entry_registerNumberThree.getText().toString()+"-"+entry_registerNumberFour.getText().toString();
                //registerNumber=entry_registerNumberOne.getText().toString()+"-"+entry_registerNumberTwo.getText().toString()+"-"+entry_registerNumberThree.getText().toString()+"-"+entry_registerNumberFour.getText().toString();
                if (entry_registerNumberFour.getText().toString().length()==3 && CommonMethods.findnumeric(entry_registerNumberThree.getText().toString())){
                    registerNumber=entry_registerNumberOne.getText().toString()+"-"+entry_registerNumberTwo.getText().toString()+"-"+entry_registerNumberThree.getText().toString().substring(0,1)+"-"+entry_registerNumberThree.getText().toString().substring(1,2)+entry_registerNumberFour.getText().toString();
                }
                validatefields(registerNumber);
                break;
        }
    }

    private void validatefields(String regnumone) {
        if (TextUtils.isEmpty(regnumone)) {
            CommonMethods.showValidationPopup("Please enter state code", getActivity());
        }else if (!CommonMethods.regnumonevalidator(regnumone)){
            CommonMethods.showValidationPopup("Please enter valid car registration number. it should be XX-00-XX-0000.", getActivity());
        }
        else if (companyID.equals("")) {
            CommonMethods.showValidationPopup("Please choose car company", getActivity());
        } else if (modelID.equals("")) {
            CommonMethods.showValidationPopup("Please choose car model", getActivity());
        } else {
            regnumone=regnumone.toUpperCase();
            addCar(regnumone);
        }
    }

    private void addCar(String regnum) {

        if (CommonMethods.isNetwork(getActivity())) {
            CommonMethods.showProgressDialog(getActivity(), "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.addUserCar(Config.getSharedPreferences(getActivity(), "userID"), Config.getSharedPreferences(getActivity(), "accessToken"), companyID, modelID, regnum, fuletype);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    if (c != null) {
                        if (c.getCode() == 100) {
                            CommonMethods.showValidationPopup(c.getMessage(), getActivity(), new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    entry_registerNumberOne.clearText();
                                    entry_registerNumberTwo.clearText();
                                    entry_registerNumberThree.clearText();
                                    entry_registerNumberFour.clearText();
                                    carListVisible();
                                }
                            });
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

    private void carListVisible() {
        view_carList.setVisibility(View.VISIBLE);
        view_addNewcar.setVisibility(View.GONE);
        lin_myCarListView.setVisibility(View.VISIBLE);
        lin_addNewCarView.setVisibility(View.GONE);
        getMyCar();

    }

    private void addNewcarVisible() {
        view_carList.setVisibility(View.GONE);
        view_addNewcar.setVisibility(View.VISIBLE);
        lin_myCarListView.setVisibility(View.GONE);
        lin_addNewCarView.setVisibility(View.VISIBLE);
    }
}
