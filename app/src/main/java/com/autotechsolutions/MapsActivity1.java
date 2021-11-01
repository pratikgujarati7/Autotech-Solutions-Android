package com.autotechsolutions;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity1 extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    private GoogleMap mMap;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TextView titleTv;
    LocationManager locationManager;
    String provider;
    SupportMapFragment mapFragment;
    Context mContext = this;
    private String lattitude = "";
    private String longitude = "";
    private static final int REQUEST_LOCATION = 1;

    final int RQS_GooglePlayServices = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        if (android.os.Build.VERSION.SDK_INT >= 23) {
//            checkPermissions();
//        } else {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                buildAlertMessageNoGps();
//
//            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                getLocation();
//            }
//        }

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    List<Address> addresseslList = null;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
//        }
//        mMap.setMyLocationEnabled(true);
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
//        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
//        provider = locationManager.getBestProvider(criteria, false);

//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
//        }

//        if (provider != null && !provider.equals("")) {
//
//            // Get the location from the given provider
//            final Location location = locationManager.getLastKnownLocation(provider);
//
//            locationManager.requestLocationUpdates(provider, 20000, 1, this);
//
//            if (location != null) {
//                onLocationChanged(location);
//                final LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
//
//                final Geocoder geocoder = new Geocoder(getApplicationContext());
//
//                try {
//                    addresseslList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                    String str = addresseslList.get(0).getLocality() + ",";
//                    str += addresseslList.get(0).getAdminArea() + "," + addresseslList.get(0).getCountryName() + "," + addresseslList.get(0).getCountryCode() + "," + addresseslList.get(0).getPostalCode() + "," + addresseslList.get(0).getFeatureName() + "," + addresseslList.get(0).getPhone();
//                    mMap.addMarker(new MarkerOptions().position(current).title(str)).setDraggable(true);
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
//
//
//
//                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//                        @Override
//                        public void onMapLongClick(LatLng latLng) {
//
////                            mMap.addMarker(new MarkerOptions()
////                                    .position(latLng)
////                                    .draggable(true));
//
////                            try {
////                                Toast.makeText(MapsActivity1.this, "" + latLng, Toast.LENGTH_SHORT).show();
////                                addresseslList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
////                                String str = addresseslList.get(0).getLocality() + ",";
////                                str += addresseslList.get(0).getAdminArea() + "," + addresseslList.get(0).getCountryName() + "," + addresseslList.get(0).getCountryCode() + "," + addresseslList.get(0).getPostalCode() + "," + addresseslList.get(0).getFeatureName() + "," + addresseslList.get(0).getPhone();
////                                mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(str));
////                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng.latitude, latLng.longitude)));
////                            } catch (IOException e) {
////                                e.printStackTrace();
////                            }
//                        }
//                    });
//                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                        @Override
//                        public void onMapClick(LatLng latLng) {
//                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                        }
//                    });
//
//                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//                        @Override
//                        public void onMarkerDragStart(Marker marker) {
//
//                        }
//
//                        @Override
//                        public void onMarkerDrag(Marker marker) {
//
//                        }
//
//                        @Override
//                        public void onMarkerDragEnd(Marker marker) {
//
//                            try {
//                            LatLng latLng_drag = marker.getPosition();
//
////                            Toast.makeText(MapsActivity1.this, "" + latLng_drag, Toast.LENGTH_SHORT).show();
//                            addresseslList = geocoder.getFromLocation(latLng_drag.latitude, latLng_drag.longitude, 1);
//                            String str = addresseslList.get(0).getLocality() + ",";
//                            str += addresseslList.get(0).getAdminArea() + "," + addresseslList.get(0).getCountryName() + "," + addresseslList.get(0).getCountryCode() + "," + addresseslList.get(0).getPostalCode() + "," + addresseslList.get(0).getFeatureName() + "," + addresseslList.get(0).getPhone();
////                            mMap.addMarker(new MarkerOptions().position(new LatLng(latLng_drag.latitude, latLng_drag.longitude)).title(str));
//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng_drag.latitude, latLng_drag.longitude)));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            } else
//                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
//
//        } else {
//            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
//        }


        // Add a marker in Sydney and move the camera

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    protected void onResume() {
        super.onResume();


        if (android.os.Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
            }
        }
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//
//        if (resultCode == ConnectionResult.SUCCESS){
////            Toast.makeText(getApplicationContext(),
////                    "isGooglePlayServicesAvailable SUCCESS",
////                    Toast.LENGTH_LONG).show();
//        }else{
//            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
//        }
    }



    private void checkPermissions() {
        int coarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);

        if (coarseLocationPermission != PackageManager.PERMISSION_GRANTED || fineLocationPermission != PackageManager.PERMISSION_GRANTED ) {
            TedPermission.with(mContext)
                    .setPermissionListener(PermissionListner)
                    .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();

        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
            }

        }
    }


    PermissionListener PermissionListner = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {


        }
    };


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity1.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MapsActivity1.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity1.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        } else {

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {

                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                setInMapLocation(latti,longi);

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                setInMapLocation(latti,longi);

            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                setInMapLocation(latti,longi);

            }
        }
    }



    public void setInMapLocation(double lati,double longi)
    {

        final LatLng current = new LatLng(lati, longi);
        final Geocoder geocoder = new Geocoder(getApplicationContext());

        try {
            addresseslList = geocoder.getFromLocation(lati, longi, 1);
            String str = addresseslList.get(0).getLocality() + ",";
            str += addresseslList.get(0).getAdminArea() + "," + addresseslList.get(0).getCountryName() + "," + addresseslList.get(0).getCountryCode() + "," + addresseslList.get(0).getPostalCode() + "," + addresseslList.get(0).getFeatureName() + "," + addresseslList.get(0).getPhone();
            mMap.addMarker(new MarkerOptions().position(current).title(str)).setDraggable(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {

                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
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

//                            Toast.makeText(MapsActivity1.this, "" + latLng_drag, Toast.LENGTH_SHORT).show();
                        addresseslList = geocoder.getFromLocation(latLng_drag.latitude, latLng_drag.longitude, 1);
                        String str = addresseslList.get(0).getLocality() + ",";
                        str += addresseslList.get(0).getAdminArea() + "," + addresseslList.get(0).getCountryName() + "," + addresseslList.get(0).getCountryCode() + "," + addresseslList.get(0).getPostalCode() + "," + addresseslList.get(0).getFeatureName() + "," + addresseslList.get(0).getPhone();
//                            mMap.addMarker(new MarkerOptions().position(new LatLng(latLng_drag.latitude, latLng_drag.longitude)).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng_drag.latitude, latLng_drag.longitude)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void buildAlertMessageNoGps() {
//        isShowDialog = 1;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        requestForUserId();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
//                        requestForUserId();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        onBackPressed();
        return true;
    }
}
