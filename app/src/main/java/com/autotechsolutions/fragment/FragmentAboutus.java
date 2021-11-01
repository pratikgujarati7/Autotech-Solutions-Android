package com.autotechsolutions.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autotechsolutions.Config;
import com.autotechsolutions.MainActivity;
import com.autotechsolutions.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentAboutus extends Fragment {
    @BindView(R.id.contentTv)
    TextView contentTv;
    @BindView(R.id.activity_about_us)
    LinearLayout activityAboutUs;
    Unbinder unbinder;

//    @BindView(R.id.txt_vision)
//    CustomTextView txt_vision;
//
//    @BindView(R.id.txt_mission)
//    CustomTextView txt_mission;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_aboutus, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).showToolbarTitle("ABOUT US");
        unbinder=ButterKnife.bind(this, rootView);
        //((MainActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us));

//        contentTv.setText(""+ Config.getSharedPreferences(getActivity(),"aboutustext"));

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
