package com.autotechsolutions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.autotechsolutions.CustomView.CustomButton;
import com.autotechsolutions.utils.CommonMethods;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationTracksActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TextView titleTv;

    @BindView(R.id.btn_select)
    CustomButton btn_select;


    String final_location="";

    SupportMapFragment mapFragment;
    List<Address> addresseslList = null;

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picklocation);
        ButterKnife.bind(this);
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
        titleTv.setText("PICK A LOCATION");
        btn_select.setOnClickListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        init();
//        restoreValuesFromBundle(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int fineLoc_permission1 = ContextCompat.checkSelfPermission(LocationTracksActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int CoarseLoc_permission1 = ContextCompat.checkSelfPermission(LocationTracksActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (fineLoc_permission1 == PackageManager.PERMISSION_GRANTED && CoarseLoc_permission1 == PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
        } else {
            getPermission();
        }
    }

//    private void init() {
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        mSettingsClient = LocationServices.getSettingsClient(this);
//
//        mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                // location is received
//                mCurrentLocation = locationResult.getLastLocation();
//                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//
//                try {
//                    final LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//                    final Geocoder geocoder = new Geocoder(getApplicationContext());
//
//                    addresseslList = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
//
//                    String str = addresseslList.get(0).getLocality() + ",";
//                    str += addresseslList.get(0).getFeatureName() + ","
//                            + addresseslList.get(0).getAdminArea() + ","
//                            + addresseslList.get(0).getCountryName() + ","
//                            + addresseslList.get(0).getCountryCode() + ","
//                            + addresseslList.get(0).getPostalCode();
//                    mMap.addMarker(new MarkerOptions().position(current).title(str)).setDraggable(true);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 12.0f));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
//                    final_location = str;
//                    setInMapLocation(mCurrentLocation);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                updateLocationUI();
//            }
//        };
//        mRequestingLocationUpdates = false;
//        mLocationRequest = new LocationRequest();
////        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
////        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
//        builder.addLocationRequest(mLocationRequest);
//        mLocationSettingsRequest = builder.build();
//    }

    /**
     * Restoring values from saved instance state
     */
//    private void restoreValuesFromBundle(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey("is_requesting_updates")) {
//                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
//            }
//
//            if (savedInstanceState.containsKey("last_known_location")) {
//                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
//            }
//
//            if (savedInstanceState.containsKey("last_updated_on")) {
//                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
//            }
//        }
//        updateLocationUI();
//    }

//    private void updateLocationUI() {
//        if (mCurrentLocation != null) {
////            CommonMethods.hideProgressDialog();
//            try {
//                final LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//                final Geocoder geocoder = new Geocoder(getApplicationContext());
//
//                addresseslList = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
//
//                String str = addresseslList.get(0).getLocality() + ",";
//                str += addresseslList.get(0).getFeatureName() + ","
//                                + addresseslList.get(0).getAdminArea() + ","
//                                + addresseslList.get(0).getCountryName() + ","
//                                + addresseslList.get(0).getCountryCode() + ","
//                                + addresseslList.get(0).getPostalCode();
//                mMap.addMarker(new MarkerOptions().position(current).title(str)).setDraggable(true);
//                LatLng coordinate = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()); //Store these lat lng values somewhere. These should be constant.
//                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
//                        coordinate, 15);
//                mMap.animateCamera(location);
//                LatLng initialLoc= mMap.getCameraPosition().target;
//                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 12.0f));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
//                final_location = str;
//                setInMapLocation(mCurrentLocation);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
//        outState.putParcelable("last_known_location", mCurrentLocation);
//        outState.putString("last_updated_on", mLastUpdateTime);
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
//    private void startLocationUpdates() {
//        mSettingsClient
//                .checkLocationSettings(mLocationSettingsRequest)
//                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//                    @SuppressLint("MissingPermission")
//                    @Override
//                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                        Log.i("TAG", "All location settings are satisfied.");
//
////                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();
//                        //noinspection MissingPermission
//                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
//                                mLocationCallback, Looper.myLooper());
//                        updateLocationUI();
//                    }
//                })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        int statusCode = ((ApiException) e).getStatusCode();
//                        switch (statusCode) {
//                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                                Log.i("TAG", "Location settings are not satisfied. Attempting to upgrade " +
//                                        "location settings ");
//                                buildAlertMessageNoGps();
//                                break;
//                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                                String errorMessage = "Location settings are inadequate, and cannot be " +
//                                        "fixed here. Fix in Settings.";
//                                Log.e("TAG", errorMessage);
//
//                                //Toast.makeText(LocationTrackActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//                        }
//
//                        updateLocationUI();
//                    }
//                });
//    }


//    public void stopLocationUpdates() {
//        // Removing location updates
//        mFusedLocationClient
//                .removeLocationUpdates(mLocationCallback)
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                       // Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
////                        toggleButtons();
//                    }
//                });
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            // Check for the integer request code originally supplied to startResolutionForResult().
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        Log.e("TAG", "User agreed to make required location settings changes.");
//                        // Nothing to do. startLocationupdates() gets called in onResume again.
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Log.e("TAG", "User chose not to make required location settings changes.");
//                        mRequestingLocationUpdates = false;
//                        break;
//                }
//                break;
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    public void setInMapLocation(Location inMapLocation) {

        final Geocoder geocoder = new Geocoder(getApplicationContext());
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                try {
                    LatLng latLng_drag = marker.getPosition();
                    addresseslList = geocoder.getFromLocation(latLng_drag.latitude, latLng_drag.longitude, 1);

                    if (addresseslList.size() != 0) {
                        String str = addresseslList.get(0).getLocality() + ",";
                        str += addresseslList.get(0).getFeatureName() + ","
                                + addresseslList.get(0).getAdminArea() + ","
                                + addresseslList.get(0).getCountryName() + ","
                                + addresseslList.get(0).getCountryCode() + ","
                                + addresseslList.get(0).getPostalCode();

                        final_location = str;
                        mMap.setContentDescription(str);
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng_drag.latitude, latLng_drag.longitude)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select:
                if (!final_location.equals("")) {
                    CommonMethods.saveSharedPreferences(LocationTracksActivity.this, "current_location", "1");
                    Intent intent = new Intent();
                    intent.putExtra("current_location", final_location);
                    setResult(RESULT_OK, intent);
                    finish();
//                    CommonMethods.saveSharedPreferences(LocationTracksActivity.this, "current_location", final_location);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }


    //    LocationManager locationManager;
    //TODO NEW GPS CODE
    boolean gps_enabled = false;
    boolean network_enabled = false;

    private void getUserLocation() {
        // Get user location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(LocationTracksActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(LocationTracksActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(LocationTracksActivity.this)
                    .setMessage("GPS network not enable.")
                    .setPositiveButton("Location Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            Location mainlocation = null;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                mainlocation = location;

            } else if (location1 != null) {

                mainlocation = location1;
            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                mainlocation = location2;
            }

            if (mainlocation != null) {

                try {
                    final LatLng current = new LatLng(mainlocation.getLatitude(), mainlocation.getLongitude());
                    final Geocoder geocoder = new Geocoder(getApplicationContext());
                    addresseslList = geocoder.getFromLocation(mainlocation.getLatitude(), mainlocation.getLongitude(), 1);
                    String str = addresseslList.get(0).getLocality() + ",";
                    str += addresseslList.get(0).getAdminArea() + "," + addresseslList.get(0).getCountryName() + "," + addresseslList.get(0).getCountryCode() + "," + addresseslList.get(0).getPostalCode() + "," + addresseslList.get(0).getFeatureName() + "," + addresseslList.get(0).getPhone();

                    mMap.addMarker(new MarkerOptions().position(current).title(str)).setDraggable(true);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 12.0f));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                    final_location = str;
                    setInMapLocation(mainlocation);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    private void getPermission() {
        TedPermission.with(LocationTracksActivity.this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(LocationTracksActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }
}
