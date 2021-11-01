package com.autotechsolutions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomEditText;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyACarActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.radioBtn_newcar)
    RadioButton radioBtnNewcar;
    @BindView(R.id.radiobtn_preowned)
    RadioButton radiobtnPreowned;
    @BindView(R.id.radioGrp_buyacar)
    RadioGroup radioGrpBuyacar;
    @BindView(R.id.edt_LocationAddress)
    CustomEditText edtLocationAddress;
    @BindView(R.id.spinner_transmission)
    Spinner spinnerTransmission;
    @BindView(R.id.spinner_typeofbody)
    Spinner spinnerTypeofbody;
    @BindView(R.id.spinner_fueltype)
    Spinner spinnerFueltype;
    @BindView(R.id.btn_getquote)
    CustomButton btnGetquote;
    ArrayList<String> arrayListTransmission = null;
    ArrayList<String> arrayListbodytype = null;
    ArrayList<String> arrayListfuel = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyacar);
        ButterKnife.bind(this);
        // toolbar initialize
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toolbar.setNavigationIcon(R.drawable.ic_backblack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setTextColor(Color.BLACK);
        titleTv.setText("BUY A CAR");
        radioGrpBuyacar.setOnCheckedChangeListener(this);
        arrayListTransmission = new ArrayList<>();
        arrayListbodytype = new ArrayList<>();
        arrayListfuel = new ArrayList<>();
        String[] stringArraytrans = getResources().getStringArray(R.array.array_Transmission);
        String[] stringArraybodytype = getResources().getStringArray(R.array.array_bodytype);
        String[] stringArrayfuel = getResources().getStringArray(R.array.array_fule);


        for (int i = 0; i < stringArraytrans.length; i++) {
            arrayListTransmission.add(stringArraytrans[i]);
        }
        for (int i = 0; i < stringArraybodytype.length; i++) {
            arrayListbodytype.add(stringArraybodytype[i]);
        }
        for (int i = 0; i < stringArrayfuel.length; i++) {
            arrayListfuel.add(stringArrayfuel[i]);
        }

        ArrayAdapter adaptertransmission = new ArrayAdapter(BuyACarActivity.this, R.layout.spinner_reg_item, arrayListTransmission);
        adaptertransmission.setDropDownViewResource(R.layout.spinner_reg_item);
        ArrayAdapter adapterbodyype = new ArrayAdapter(BuyACarActivity.this, R.layout.spinner_reg_item, arrayListbodytype);
        adapterbodyype.setDropDownViewResource(R.layout.spinner_reg_item);
        ArrayAdapter adapterfuel = new ArrayAdapter(BuyACarActivity.this, R.layout.spinner_reg_item, arrayListfuel);
        adapterfuel.setDropDownViewResource(R.layout.spinner_reg_item);

        spinnerTransmission.setAdapter(adaptertransmission);
        spinnerTypeofbody.setAdapter(adapterbodyype);
        spinnerFueltype.setAdapter(adapterfuel);

    }

    @OnClick(R.id.btn_getquote)
    public void onViewClicked() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioBtn_newcar:

                break;

            case R.id.radiobtn_preowned:

                break;
        }
    }
}
