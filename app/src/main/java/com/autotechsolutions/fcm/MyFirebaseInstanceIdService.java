package com.autotechsolutions.fcm;

import android.util.Log;

import com.autotechsolutions.Config;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by A on 23-02-2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Config.saveSharedPreferences(getApplicationContext(),"token",token);
        Log.e("In FirebaseIdService", "Token : " + token);
    }
}
