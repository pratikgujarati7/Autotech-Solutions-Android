package com.autotechsolutions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static android.content.Context.INPUT_METHOD_SERVICE;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;

public class Config {
//    public final static String URL = "http://credencetech.in/autotech-solution/";   //live Url
//    public final static String URL = "http://credencetech.in/autotech-solutionqa/";   //testing Url
    public final static String URL = "http://app.autotechsolutions.in/";   //Live Url
//    public final static String URL = "http://credencetech.net/autotech/";   // DEMO  Url  http://credencetech.net/autotech/api/register

    public final static String FailureMsg = "Oops!! Something went wrong, please try again...";
    public final static String add_user_car_bodyshop = "api/add_user_car_bodyshop";
    public interface OkButtonClicklistner{
        void OkButtonClick();
    }
    private static OkButtonClicklistner okButtonClicklistner;

    public static void showSnackBar(Activity activity, String message) {
        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#444444"));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#eeeeee"));
        textView.setTextSize(15);
        snackbar.show();
    }

//    public static boolean checkPlayServices(Activity activity) {
//        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
//        if (status != ConnectionResult.SUCCESS) {
//            GooglePlayServicesUtil.getErrorDialog(status, activity, 1001);
//            return false;
//        }
//        return true;
//    }


    public static void saveSharedPreferences(Context context, String key,
                                             String value) {
        SharedPreferences pref = context.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    public static boolean isConnectedToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static void showAlertForInternet(final Activity context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("Please make sure that you are connected to the internet");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.fail);

        alertDialog.setButton("Go to Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                context.finish();
            }
        });
        alertDialog.show();
    }

    public static JSONArray shuffleArray(JSONArray array) {
        JSONArray sliderArray = null;
        try {
            sliderArray = new JSONArray();
            Random rnd = new Random();
            for (int i = array.length() - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                // Simple swap
                JSONObject sliderRow = array.getJSONObject(index);
                sliderArray.put(sliderRow);
            }
        } catch (JSONException je) {
            Log.e("In Config", "Shuffle Slider : " + je.getMessage());
        }
        return sliderArray;
    }

//    public static void startService(Context context) {
//        Intent alarmIntent = new Intent(context,
//                AlarmReceiver.class);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
//                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        int interval = 3000;
//        Calendar calendar = Calendar.getInstance();
////            calendar.set(Calendar.HOUR_OF_DAY, 11);
////            calendar.set(Calendar.MINUTE, 00);
////            calendar.set(Calendar.SECOND, 01);
//
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), calendar.getTimeInMillis() + 10 * 1000, pendingIntent);
//
//    }

    public static void showDialog(Context context, String message){
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)
                .setGravity(Gravity.CENTER)
                .create();
        dialog.show();

        View errorView = dialog.getHolderView();
        TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
        TextView messageTv = (TextView) errorView.findViewById(R.id.messageTv);
        Button okBtn = (Button) errorView.findViewById(R.id.okBtn);
        titleTv.setVisibility(View.GONE);
        messageTv.setText(message);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void hideSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        View view = ((Activity)context).getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showValidationPopupConfirmation(final String mesaage, final Context context,OkButtonClicklistner listner) {
        final Dialog successDialog = new Dialog(context);
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successDialog.setContentView(R.layout.dialog_confirmation_layout);
        successDialog.setCancelable(false);
        okButtonClicklistner=listner;

        TextView titleTv = (TextView) successDialog.findViewById(R.id.titleTv);
        Button okBtn = (Button) successDialog.findViewById(R.id.yesBtn);
        Button noBtn = (Button) successDialog.findViewById(R.id.noBtn);
        titleTv.setVisibility(View.VISIBLE);

        titleTv.setText(mesaage);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
                okButtonClicklistner.OkButtonClick();
                //doAction(mesaage, context);
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

    public static void showValidationPopup(final String mesaage, final Context context,OkButtonClicklistner listner) {
        final Dialog successDialog = new Dialog(context);
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successDialog.setContentView(R.layout.dialog_ok_layout);
        successDialog.setCancelable(false);
        okButtonClicklistner=listner;

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
                //doAction(mesaage, context);
            }
        });

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        successDialog.getWindow().setLayout(width, height);

        successDialog.show();
    }



}
