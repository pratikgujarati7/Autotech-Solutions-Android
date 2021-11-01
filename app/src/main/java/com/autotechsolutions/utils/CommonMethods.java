package com.autotechsolutions.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.SpannableString;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonMethods {


    public static ArrayList<Uri> images_url1 = new ArrayList<>();

    public static ArrayList<Uri> getImages_url() {
        return images_url1;
    }

    public static void setImages_url(ArrayList<Uri> images_url) {
        images_url1 = images_url;
    }

    public static void addImagesUrl(Uri s)
    {
//        images_url1.add(s);
//        images_url1.add(s);
        images_url1.add( (images_url1.size() == 0 ? 0:images_url1.size()-1), s);
    }

    public static void removeImage(int i)
    {
        images_url1.remove(i);
    }








    private static ProgressDialog progressDialog;

    public interface OkButtonClicklistner {
        void OkButtonClick();
    }

    public interface upDateButtonClicklistner {
        void updateButtonClick();

    }

    private static OkButtonClicklistner okButtonClicklistner;
    private static upDateButtonClicklistner upDateButtonClicklistner;


    public static void saveSharedPreferences(Context context, String key,
                                             String value) {
        SharedPreferences pref = context.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String getSharedPreferences(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }



    public static void showProgressDialog(Context context, String msg) {
        if (!((Activity) context).isFinishing()) {
            WeakReference<Context> weakActivity = new WeakReference<>(context);
            SpannableString spannableString = new SpannableString(msg);
//            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), "fonts/Montserrat-Regular.ttf"));
//            spannableString.setSpan(typefaceSpan, 0, msg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            progressDialog = ProgressDialog.show(weakActivity.get(), null, spannableString, false, false);
//            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            progressDialog.setContentView(R.layout.progress_bar);
        }
    }

    /**
     * Hide Progress Dialog
     */
    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public static boolean isNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean regnumonevalidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[A-Za-z]{2}-[0-9]{2}-[A-Za-z]{1,2}-[0-9]{4}$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static boolean findnumeric(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[A-Za-z]{1}[0-9]{1}$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showValidationPopup(final String mesaage, final Context context) {
        final Dialog successDialog = new Dialog(context);
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successDialog.setContentView(R.layout.dialog_done_layout);
        successDialog.setCancelable(false);

        TextView titleTv = (TextView) successDialog.findViewById(R.id.titleTv);
        TextView messageTv = (TextView) successDialog.findViewById(R.id.messageTv);
        Button okBtn = (Button) successDialog.findViewById(R.id.okBtn);
        titleTv.setVisibility(View.GONE);

        messageTv.setText(mesaage);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
//                doAction(mesaage, context);
            }
        });

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        successDialog.getWindow().setLayout(width, height);

        successDialog.show();
    }
    public static void showValidationPopup(final String mesaage, final Context context, OkButtonClicklistner listner) {
        final Dialog successDialog = new Dialog(context);
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successDialog.setContentView(R.layout.dialog_done_layout);
        successDialog.setCancelable(false);
        okButtonClicklistner = listner;
        TextView titleTv = (TextView) successDialog.findViewById(R.id.titleTv);
        TextView messageTv = (TextView) successDialog.findViewById(R.id.messageTv);
        Button okBtn = (Button) successDialog.findViewById(R.id.okBtn);
        titleTv.setVisibility(View.GONE);

        messageTv.setText(mesaage);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
                okButtonClicklistner.OkButtonClick();
            }
        });

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        successDialog.getWindow().setLayout(width, height);

        successDialog.show();
    }

    public static void showValidationPopupConfirmation(final String mesaage, final Context context, upDateButtonClicklistner listner) {
        final Dialog successDialog = new Dialog(context);
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successDialog.setContentView(R.layout.dialog_confirmation_layout);
        successDialog.setCancelable(false);
        upDateButtonClicklistner = listner;

        CustomTextView titleTv = (CustomTextView) successDialog.findViewById(R.id.titleTv);
        CustomButton okBtn = (CustomButton) successDialog.findViewById(R.id.yesBtn);
        CustomButton noBtn = (CustomButton) successDialog.findViewById(R.id.noBtn);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText(mesaage);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
                upDateButtonClicklistner.updateButtonClick();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                successDialog.dismiss();

            }
        });
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        successDialog.getWindow().setLayout(width, height);

        successDialog.show();
    }

}
