package com.autotechsolutions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.autotechsolutions.Response.Common;
import com.autotechsolutions.fragment.FragmentAboutus;
import com.autotechsolutions.fragment.FragmentAddCar;
import com.autotechsolutions.fragment.FragmentAddressBook;
import com.autotechsolutions.fragment.FragmentHome;
import com.autotechsolutions.fragment.FragmentNotification;
import com.autotechsolutions.fragment.FragmentProfile;
import com.autotechsolutions.fragment.FragmentReffralAndEarn;
import com.autotechsolutions.fragment.FragmentTermsCondition;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    public static int selectedPos = -1, lastSelId = -1, selId = -1, isFirst = 1;
    private String from="";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("SplashTime","mainactivity");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu, getApplicationContext().getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        Intent intent=getIntent();
        if (intent.hasExtra("from")){
            from=intent.getStringExtra("from");
            if (from.equals("addCar")){
                selId = R.id.nav_my_cars;
                lastSelId = R.id.nav_my_cars;
                displayView(R.id.nav_my_cars);
            }else {
                selId = R.id.nav_home;
                lastSelId = R.id.nav_home;
                displayView(R.id.nav_home);
            }


        }else {
            selId = R.id.nav_home;
            lastSelId = R.id.nav_home;
            displayView(R.id.nav_home);
        }

    }

    public void displayView(int itemId) {
//        selectedPos = position;
        Fragment fragment = null;
        MenuItem menuItem = null;
        final Intent intent = null;
        String title = getString(R.string.app_name);
        String tag = "";
        switch (itemId) {
            case R.id.nav_home:
                fragment = new FragmentHome();
                tag = "Home";
                break;
            case R.id.nav_my_cars:
                fragment = new FragmentAddCar();
                tag = "Add Your Cars";
                break;

            case R.id.nav_profile:
                fragment = new FragmentProfile();

                tag = "Profile";
                break;

            case R.id.nav_addressbook:
                fragment = new FragmentAddressBook();

                tag = "AddressBook";
                break;
            case R.id.nav_notifications:
                fragment = new FragmentNotification();
                tag = "Notification";
                break;
            case R.id.nav_refer:
                fragment = new FragmentReffralAndEarn();

                tag = "Refer";
                break;
            case R.id.nav_terms:
                fragment = new FragmentTermsCondition();
                tag = "Terms";
                break;
            case R.id.nav_about:
                fragment = new FragmentAboutus();
                tag = "About";
                break;

            case R.id.nav_logout:
                CommonMethods.showValidationPopupConfirmation("Are you sure you want to logout?", MainActivity.this, new CommonMethods.upDateButtonClicklistner() {
                    @Override
                    public void updateButtonClick() {
                        logout();
                    }
                });
                break;
            default:
                break;
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container_body, fragment,"");
            fragmentTransaction.replace(R.id.container_body, fragment);
            if (isFirst > 1)
                fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    private void logout() {
        if (CommonMethods.isNetwork(MainActivity.this)) {
            CommonMethods.showProgressDialog(MainActivity.this, "Loading");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.logout(Config.getSharedPreferences(MainActivity.this, "userID"), Config.getSharedPreferences(MainActivity.this, "accessToken"));
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    CommonMethods.hideProgressDialog();
                    Common c = response.body();
                    Log.e("FragmentRedeem", "Response: success: " + new Gson().toJson(response.body()));
//                    if (c.getCode() == 100) {
                    Intent intent = null;
                    if (c != null) {
                        if (c.getCode() == 100) {
                            Config.saveSharedPreferences(MainActivity.this, "islogin", "");
                            Config.saveSharedPreferences(MainActivity.this, "userID", "");
                            Config.saveSharedPreferences(MainActivity.this, "accessToken", "");
                            Intent intent1 = new Intent(MainActivity.this, MobileNumber.class);
                            startActivity(intent1);
                            finish();

                        } else if (c.getCode() == 102) {
                            CommonMethods.showValidationPopup(c.getMessage(), MainActivity.this, new CommonMethods.OkButtonClicklistner() {
                                @Override
                                public void OkButtonClick() {
                                    Intent intent = new Intent(MainActivity.this, MobileNumber.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                        }
                    }else {
                        CommonMethods.showValidationPopup(Config.FailureMsg, MainActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    CommonMethods.hideProgressDialog();
                    Log.e("FragmentRedeem", "onFailure: " + t.toString());
                    CommonMethods.showValidationPopup(Config.FailureMsg, MainActivity.this);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        int backStackCount=0;
        if (from.equals("addCar")){
            backStackCount=0;
        }else if (from.equals("")){
            backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        }else {
            backStackCount=0;
        }

        Log.e("In MainAct", "On Back Pressed Count : " + backStackCount);
        if (backStackCount >= 1) {
            getSupportFragmentManager().popBackStack();
            if (backStackCount > 1) {
                String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(backStackCount - 2).getName();
                Log.e("In MainAct", "FrgmtTag : " + fragmentTag);
                if (fragmentTag.equalsIgnoreCase("Home")) {
                    setNavigationItemOnBackPressed(R.id.nav_home);
                } else if (fragmentTag.equalsIgnoreCase("Add Your Cars")) {
                    setNavigationItemOnBackPressed(R.id.nav_my_cars);
                } else if (fragmentTag.equalsIgnoreCase("Profile")) {
                    setNavigationItemOnBackPressed(R.id.nav_profile);
                } else if (fragmentTag.equalsIgnoreCase("AddressBook")) {
                    setNavigationItemOnBackPressed(R.id.nav_addressbook);
                } else if (fragmentTag.equalsIgnoreCase("Notification")) {
                    setNavigationItemOnBackPressed(R.id.nav_notifications);
                } else if (fragmentTag.equalsIgnoreCase("Refer")) {
                    setNavigationItemOnBackPressed(R.id.nav_refer);
                } else if (fragmentTag.equalsIgnoreCase("Terms")) {
                    setNavigationItemOnBackPressed(R.id.nav_terms);
                } else if (fragmentTag.equalsIgnoreCase("About")) {
                    setNavigationItemOnBackPressed(R.id.nav_about);
                }else{
                    setNavigationItemOnBackPressed(R.id.nav_home);
                }
            } else {
                setNavigationItemOnBackPressed(R.id.nav_home);
            }
        } else {
            if (from.equals("addCar")){
                finish();
            }else if (from.equals("")){
                if (doubleBackToExitPressedOnce) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;

                    }
                }, 2000);
            }else{
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_notification) {
            selId = R.id.nav_notifications;
            ++isFirst;
            if (lastSelId != selId) {
                displayView(R.id.nav_notifications);
            } else {
                drawer.closeDrawer(GravityCompat.START);
            }
            lastSelId = selId;

            return true;

        }

        if (id == R.id.action_profile) {
            selId = R.id.nav_profile;
            ++isFirst;
            if (lastSelId != selId) {
                displayView(R.id.nav_profile);
            } else {
                drawer.closeDrawer(GravityCompat.START);
            }
            lastSelId = selId;

            return true;
        }
        if (id == R.id.action_home) {
            selId = R.id.nav_home;
            ++isFirst;
            if (lastSelId != selId) {
                displayView(R.id.nav_home);
            } else {
                drawer.closeDrawer(GravityCompat.START);
            }
            lastSelId = selId;

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        selId = item.getItemId();
        ++isFirst;
        if (lastSelId != selId) {
            displayView(item.getItemId());
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
        lastSelId = selId;

        return true;
    }

    private void setNavigationItemOnBackPressed(int itemId) {
        selId = itemId;
        lastSelId = itemId;
        navigationView.setCheckedItem(itemId);
    }


    public void showToolbarTitle(String title) {
        TextView titleTv = (TextView) toolbar.findViewById(R.id.titleTv);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText(title);
    }
    public void callhome() {
        selId = R.id.action_home;
        ++isFirst;
        if (lastSelId != selId) {
            displayView(R.id.action_home);
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
        lastSelId = selId;
    }
}
