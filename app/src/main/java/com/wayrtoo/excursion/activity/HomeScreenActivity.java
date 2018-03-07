package com.wayrtoo.excursion.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.fragment.ComboDealFragment;
import com.wayrtoo.excursion.fragment.ComimgSoonFragment;
import com.wayrtoo.excursion.fragment.DiningFragment;
import com.wayrtoo.excursion.fragment.EntertainmentFragment;
import com.wayrtoo.excursion.fragment.NoInternetFragment;
import com.wayrtoo.excursion.fragment.TicketFragment;
import com.wayrtoo.excursion.fragment.ActivityFragment;
import com.wayrtoo.excursion.models.ActivitiesListModel;
import com.wayrtoo.excursion.models.CityListModel;
import com.wayrtoo.excursion.util.CustomResponseDialog;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.GPSTracker;
import com.wayrtoo.excursion.util.SessionManager;

import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/*
  Created by Ishwar on 20/12/17.
 */
public class HomeScreenActivity extends AppCompatActivity  {

    @BindView(R.id.tv_pool)
    TextView pool;

    @BindView(R.id.select_city)
    TextView select_city;

    /*@BindView(R.id.spinner_cityList)
    Spinner spinner_cityList;*/

    @BindView(R.id.iv_favorite)
    ImageView iv_favorite;

    @BindView(R.id.iv_profile)
    ImageView iv_profile;

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    @BindView(R.id.search)
    FloatingActionButton search;

    private Context mContext;
    private SessionManager sessionManager;
    private GPSTracker gpsTracker;
    private CustomResponseDialog customResponseDialog;
    private ArrayList<CityListModel> cityArrayList = new ArrayList<>();
    private ArrayList<ActivitiesListModel> activitiesListModels;
    private String JSONResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mContext = this;
        ButterKnife.bind(this);
        customResponseDialog = new CustomResponseDialog(mContext);
        sessionManager = new SessionManager(mContext);
        gpsTracker = new GPSTracker(mContext, HomeScreenActivity.this);
        getAllCityName();
        //FontsOverride.setDefaultFont(this, "SERIF", "fonts/Raleway-Medium.ttf");
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");


        Log.e("List Arry City---->", sessionManager.getCityList());

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_ticket, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_activity, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_dinig, R.color.color_tab_3);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_4, R.drawable.ic_entertainment, R.color.color_tab_4);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tab_5, R.drawable.ic_combo_deal, R.color.color_tab_5);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);


        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);

// Enable the translation of the FloatingActionButton
        bottomNavigation.manageFloatingActionButtonBehavior(search);

// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F44336"));//#347038
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        bottomNavigation.setForceTint(true);

        //bottomNavigation.setTranslucentNavigationEnabled(true);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        //bottomNavigation.setColored(true);

        //bottomNavigation.setCurrentItem(0);


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    changeFragment(R.id.fragment_container, new TicketFragment());
                    sessionManager.setSelectFragmentPosition("0");
                } else if (position == 1) {
                    changeFragment(R.id.fragment_container, new ActivityFragment());
                    sessionManager.setSelectFragmentPosition("1");
                } else if (position == 2) {
                    changeFragment(R.id.fragment_container, new DiningFragment());
                    sessionManager.setSelectFragmentPosition("2");
                } else if (position == 3) {
                    changeFragment(R.id.fragment_container, new EntertainmentFragment());
                    sessionManager.setSelectFragmentPosition("3");
                } else if (position == 4) {
                    changeFragment(R.id.fragment_container, new ComimgSoonFragment());
                    sessionManager.setSelectFragmentPosition("4");
                }

                return true;
            }
        });

        select_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,SelectCityListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("City List", cityArrayList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,FavoriteActivity.class);
                startActivity(intent);
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getAllCityName() {
        JSONArray jsonArrayCity = null;   //  sessionManager.getCityList();
        try {
            jsonArrayCity = new JSONArray(sessionManager.getCityList());
            List<String> list = new ArrayList<String>();
            for (int i = 0; jsonArrayCity.length() > i; i++) {
                CityListModel listModel = new CityListModel();
                JSONObject jsonCity = jsonArrayCity.getJSONObject(i);
                listModel.setCity_id(jsonCity.getString("city_id"));
                listModel.setCity_name(jsonCity.getString("city_name"));
                listModel.setGrn_country_code(jsonCity.getString("grn_country_code"));
                listModel.setTbo_country_code(jsonCity.getString("tbo_country_code"));
                listModel.setFeatured_image(jsonCity.getString("featured_image"));
                list.add(jsonCity.getString("city_name"));
                listModel.setCountry_name(jsonCity.getString("country_name"));
                cityArrayList.add(listModel);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    public void changeFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack("" + fragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();

        select_city.setText(sessionManager.getCityName());

       if (sessionManager.getCityId().equalsIgnoreCase("5")) {
            if (sessionManager.getCityAbuDhabi().equalsIgnoreCase("")) {
                CallAPI();
            } else {
                ChangeActivityCall();
            }

        } else if (sessionManager.getCityId().equalsIgnoreCase("6")) {
            if (sessionManager.getCityDubai().equalsIgnoreCase("")) {
                CallAPI();
            } else {
                ChangeActivityCall();
            }

        } else if (sessionManager.getCityId().equalsIgnoreCase("10")) {
            if (sessionManager.getCityBali().equalsIgnoreCase("")) {
                CallAPI();
            } else {
                ChangeActivityCall();
            }

        } else if (sessionManager.getCityId().equalsIgnoreCase("11")) {
            if (sessionManager.getCitySingapore().equalsIgnoreCase("")) {
                CallAPI();
            } else {
                ChangeActivityCall();
            }

        } else if (sessionManager.getCityId().equalsIgnoreCase("16")) {
            if (sessionManager.getCityBangkok().equalsIgnoreCase("")) {
                CallAPI();
            } else {
                ChangeActivityCall();
            }

        } else if (sessionManager.getCityId().equalsIgnoreCase("18")) {
            if (sessionManager.getCityHongKong().equalsIgnoreCase("")) {
                CallAPI();
            } else {
                ChangeActivityCall();
            }

        } else if (sessionManager.getCityId().equalsIgnoreCase("22")) {
            if (sessionManager.getCityKaulaLumpur().equalsIgnoreCase("")) {
                CallAPI();
            } else {
                ChangeActivityCall();
            }

        } else if (sessionManager.getCityId().equalsIgnoreCase("27")) {
            if (sessionManager.getCityLangkawi().equalsIgnoreCase("")) {
                CallAPI();
            } else {
                ChangeActivityCall();
            }

        } else if (sessionManager.getCityId().equalsIgnoreCase("28")) {
            if (sessionManager.getCityPenang().equalsIgnoreCase("")) {
                CallAPI();
            } else {
                ChangeActivityCall();
            }

        }
    }


    private void CallAPI() {

        if (gpsTracker.isNetworkAvailable()) {
            getActivityList();
        } else {
            changeFragment(R.id.fragment_container, new NoInternetFragment());
            Snackbar.make(findViewById(R.id.fragment_container), "Please check your internet connection.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void getActivityList() {
        customResponseDialog.showCustomDialog();
        int DEFAULT_TIMEOUT_GUIDDOO = 5 * 1000;
        String URL = sessionManager.getBaseUrl() + "products/activities/info?city_id=" + sessionManager.getCityId() + "&api_key=0D1067102F935B4CC31E082BD45014D469E35268";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT_GUIDDOO);

        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                customResponseDialog.hideCustomeDialog();

                try {
                    JSONResponse = new String(responseBody, "UTF-8");

                    new CallData().execute(JSONResponse);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                customResponseDialog.hideCustomeDialog();
                Snackbar.make(findViewById(R.id.fragment_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onRetry(int retryNo) {
                customResponseDialog.hideCustomeDialog();
                Snackbar.make(findViewById(R.id.fragment_container), "Retrying...", Snackbar.LENGTH_LONG).show();
            }

            public void onProgress(int bytesWritten, int totalSize) {
                // TODO Auto-generated method stub

            }

        });
    }


    private class CallData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String s = params[0];


            try {
                Log.e("Activity Net Resp-->", s);
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonData = jsonObject.getJSONObject("data");
                JSONObject jsonStatus = jsonData.getJSONObject("status");

                if (jsonStatus.getString("Success").equalsIgnoreCase("true")) {
                    sessionManager.setActivtityStatus(jsonStatus.getString("Success"));

                    JSONArray jsonArrayActivity = jsonData.getJSONArray("activitieslist");
                    activitiesListModels = new ArrayList<ActivitiesListModel>();
                    for (int i = 0; jsonArrayActivity.length() > i; i++) {
                        ActivitiesListModel listModel = new ActivitiesListModel();
                        JSONObject jsonActivity = jsonArrayActivity.getJSONObject(i);
                        listModel.setTour_id(jsonActivity.getString("tour_id"));
                        listModel.setAddress(jsonActivity.getString("address"));
                        listModel.setCategory(jsonActivity.getString("category"));
                        listModel.setCity(jsonActivity.getString("city"));
                        listModel.setCurrency(jsonActivity.getString("currency"));

                        JSONObject jsonDuration = jsonActivity.getJSONObject("duration");
                        listModel.setDay(jsonDuration.getString("day"));
                        listModel.setHour(jsonDuration.getString("hour"));
                        listModel.setMin(jsonDuration.getString("min"));
                        listModel.setCategory(jsonActivity.getString("featured_image"));
                        listModel.setName(jsonActivity.getString("name"));
                        listModel.setRating(jsonActivity.getString("rating"));
                        listModel.setShort_description(jsonActivity.getString("short_description"));
                        listModel.setStarting_from_price(jsonActivity.getString("starting_from_price"));
                        listModel.setLatitude(jsonActivity.getString("latitude"));
                        listModel.setLongitude(jsonActivity.getString("longitude"));

                        activitiesListModels.add(listModel);
                    }

                    if (sessionManager.getCityId().equalsIgnoreCase("5")) {
                        sessionManager.setCityAbuDhabi(String.valueOf(jsonData.getJSONArray("activitieslist")));
                    } else if (sessionManager.getCityId().equalsIgnoreCase("6")) {
                        sessionManager.setCityDubai(String.valueOf(jsonData.getJSONArray("activitieslist")));
                    } else if (sessionManager.getCityId().equalsIgnoreCase("10")) {
                        sessionManager.setCityBali(String.valueOf(jsonData.getJSONArray("activitieslist")));
                    } else if (sessionManager.getCityId().equalsIgnoreCase("11")) {
                        sessionManager.setCitySingapore(String.valueOf(jsonData.getJSONArray("activitieslist")));
                    } else if (sessionManager.getCityId().equalsIgnoreCase("16")) {
                        sessionManager.setCityBankok(String.valueOf(jsonData.getJSONArray("activitieslist")));
                    } else if (sessionManager.getCityId().equalsIgnoreCase("18")) {
                        sessionManager.setCityHongKong(String.valueOf(jsonData.getJSONArray("activitieslist")));
                    } else if (sessionManager.getCityId().equalsIgnoreCase("22")) {
                        sessionManager.setCityKualaLumpur(String.valueOf(jsonData.getJSONArray("activitieslist")));
                    } else if (sessionManager.getCityId().equalsIgnoreCase("27")) {
                        sessionManager.setCityLangKawi(String.valueOf(jsonData.getJSONArray("activitieslist")));
                    } else if (sessionManager.getCityId().equalsIgnoreCase("28")) {
                        sessionManager.setCityPenang(String.valueOf(jsonData.getJSONArray("activitieslist")));
                    }


                } else {
                    Snackbar.make(findViewById(R.id.fragment_container), "Something went wrong. Please check your internet connection or try again later.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            } catch (JSONException e) {
                Snackbar.make(findViewById(R.id.fragment_container), "Something went wrong. Please check your internet connection or try again later.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                e.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            ChangeActivityCall();
        }

        @Override
        protected void onPreExecute() {
        }

    }


    private void ChangeActivityCall() {
        try {
            if (sessionManager.getSelectFragmentPosition().equalsIgnoreCase("0")) {
                changeFragment(R.id.fragment_container, new TicketFragment());
            } else if (sessionManager.getSelectFragmentPosition().equalsIgnoreCase("1")) {
                changeFragment(R.id.fragment_container, new ActivityFragment());
            } else if (sessionManager.getSelectFragmentPosition().equalsIgnoreCase("2")) {
                changeFragment(R.id.fragment_container, new DiningFragment());
            } else if (sessionManager.getSelectFragmentPosition().equalsIgnoreCase("3")) {
                changeFragment(R.id.fragment_container, new EntertainmentFragment());
            } else if (sessionManager.getSelectFragmentPosition().equalsIgnoreCase("4")) {
                changeFragment(R.id.fragment_container, new ComimgSoonFragment());
            }

        } catch (OutOfMemoryError e) {
            changeFragment(R.id.fragment_container, new TicketFragment());
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
