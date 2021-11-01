package com.autotechsolutions.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.autotechsolutions.MainActivity;
import com.autotechsolutions.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentTermsCondition extends Fragment {
    @BindView(R.id.webView1)
    WebView webView1;


    //    @BindView(R.id.txt_registration)
//    CustomTextView txt_registration;
//
//    @BindView(R.id.txt_rules)
//    CustomTextView txt_rules;
    Unbinder unbinder;
    private ProgressDialog pd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_termsandcondition, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).showToolbarTitle("TERMS AND CONDITION");
        ButterKnife.bind(this, rootView);
        unbinder = ButterKnife.bind(this, rootView);
        //((MainActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us));

        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setAllowContentAccess(true);
        webView1.setWebViewClient(new MyWebViewClient());
        webView1.loadUrl("http://app.autotechsolutions.in/autotech_tnc.html");

        return rootView;

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!getActivity().isFinishing()) {
                if ((pd != null) && pd.isShowing()) {
                    pd.dismiss();
                }
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
