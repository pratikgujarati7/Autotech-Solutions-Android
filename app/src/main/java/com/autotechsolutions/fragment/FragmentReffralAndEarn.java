package com.autotechsolutions.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.autotechsolutions.Config;
import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.InsuranceRenewalActivity;
import com.autotechsolutions.MainActivity;
import com.autotechsolutions.R;
import com.autotechsolutions.Response.SplashResponse;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentReffralAndEarn extends Fragment implements View.OnClickListener {


    @BindView(R.id.btn_reffernow)
    CustomButton btn_reffernow;

    @BindView(R.id.txt_refferalCode)
    CustomTextView txt_refferalCode;
    private SplashResponse sr;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_referearn_new, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).showToolbarTitle("REFER AND EARN");
        ButterKnife.bind(this, rootView);
        //((MainActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us));


        sr = new Gson().fromJson(Config.getSharedPreferences(getActivity(), "SplashData"), SplashResponse.class);
        //String str=sr.getShareMessage();
        txt_refferalCode.setText(Config.getSharedPreferences(getActivity(),"referralcode"));
        btn_reffernow.setOnClickListener(this);
        txt_refferalCode.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reffernow:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
//                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
//                    String shareMessage= "\nyour refferal code is 986532\n\n";
                    String shareMessage= Config.getSharedPreferences(getActivity(),"referralMessage");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;

            case R.id.txt_refferalCode:
                ClipboardManager cm = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(txt_refferalCode.getText());
                Toast.makeText(getActivity(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
