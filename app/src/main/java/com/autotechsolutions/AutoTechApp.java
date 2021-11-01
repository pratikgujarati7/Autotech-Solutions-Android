package com.autotechsolutions;

import android.app.Application;
import android.net.Uri;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.util.ArrayList;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class AutoTechApp extends Application {
    private static Application instance;
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        if (instance == null) {
            instance = this;

            Album.initialize(AlbumConfig.newBuilder(this)
                    .setAlbumLoader(new MediaLoader())
                    .setLocale(Locale.getDefault())
                    .build()
            );
        }
    }
    public static Application getInstance() {
        return instance;
    }
}
