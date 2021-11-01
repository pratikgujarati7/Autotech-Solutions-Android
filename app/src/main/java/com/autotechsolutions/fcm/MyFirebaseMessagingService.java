package com.autotechsolutions.fcm;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.autotechsolutions.MainActivity;
import com.autotechsolutions.MobileNumber;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by A on 23-02-2018.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private Context context = this;
    private String TAG = "RewardStamp";
    String code;
    int isForeground = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            //handleNotification(remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                //handleDataMessage(json);
                String title = null;
                String message = null;
                try {
//                    JSONObject data = json.getJSONObject("Message");
                    title = json.getString("notification_title");
                    message = json.getString("notification_message");
                    code = json.getString("code");
                    if (code.equals("101")) {
                        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
                        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                            if (getApplicationContext().getPackageName().equals(appProcess.processName)) {
                                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                                    Log.e("Foreground App", appProcess.processName);
                                    isForeground = 1;
                                    Intent intent1 = new Intent(context, MobileNumber.class);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent1.putExtra("message", message);
                                    startActivity(intent1);
                                    // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                }
                            }
                        }
                    }
                    Log.e(TAG, "title: " + title);
                    Log.e(TAG, "message: " + message);
                    Log.e(TAG, "Code: " + code);
                } catch (JSONException e) {
                    Log.e(TAG, "Json Exception: " + e.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }



                /*Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 100,
                        intent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder notificationBuilder = new
                        NotificationCompat.Builder(this)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager)
                                getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(100, notificationBuilder.build());*/

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void sendNotification(String messageBody) {
        String title = "", message = "";
        int code = 101;
        int order_id = 0;
        try {
            JSONObject jsonObject = new JSONObject(messageBody);
            Log.e("PushResponse:", jsonObject + "");
            code = jsonObject.getInt("code");
            title = jsonObject.getString("notification_title");
            message = jsonObject.getString("notification_message");
//            order_id = jsonObject.getInt("order_id");
        } catch (JSONException e) {
            Log.e("Exception", "Exception : " + e.getMessage());
        }
        Intent intent;

        intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (code == 101) {
//            intent = new Intent(this, OrderHistory.class);
//        } else if (code == 102) {
//            intent = new Intent(this, OffersActivity.class);
//        } else if (code == 103) {
//            intent = new Intent(this, MainActivity1.class);
//        } else if (code == 104) {
//            intent = new Intent(this, Feedback.class);
//            intent.putExtra("orderId","" + order_id);
//        }
//        else {
        //intent = new Intent(this, MainActivity.class);
//        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        /*Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());*/
    }

//    private int getNotificationIcon(android.support.v4.app.NotificationCompat.Builder notificationBuilder) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return R.drawable.bitcoin;
//        } else {
//            return R.mipmap.ic_launcher;
//        }
//    }


}
