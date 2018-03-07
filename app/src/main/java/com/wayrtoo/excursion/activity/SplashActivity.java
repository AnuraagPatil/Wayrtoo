package com.wayrtoo.excursion.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.application.excursion;
import com.wayrtoo.excursion.models.CityListModel;
import com.wayrtoo.excursion.util.GPSTracker;
import com.wayrtoo.excursion.util.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.view)
    ImageView logo;


    private Context context;
    private SessionManager sessionManager;
    private GPSTracker gpsTracker;
    private LocationManager locationManager;
    private View v;
    private ArrayList<CityListModel> cityArrayList = new ArrayList<>();
    private String[] AppPermissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static int SPLASH_TIME_OUT = 5000;
    private Animation zoom;
    private excursion  excursion;

    CircularProgressView progressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        context = this;

        ButterKnife.bind(this);
        v = findViewById(R.id.view);

        progressView = (CircularProgressView) findViewById(R.id.progress_view);

        sessionManager = new SessionManager(context);
        sessionManager.setBaseUrl("http://guiddooworld.com/apiservice/Guiddoo_Service.svc/");
        zoom = AnimationUtils.loadAnimation(context,R.anim.fade_upper);
        excursion = new excursion();
        sessionManager.setSelectFragmentPosition("0");
        //excursion.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsTracker = new GPSTracker(context, SplashActivity.this);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            GPSTracker.showSettingsAlert(SplashActivity.this);
        }
        else
            {
            if (checkAppPermissions(context, AppPermissions)) {
                startAnimation();
                if (sessionManager.getCityStatus().equalsIgnoreCase("true")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            gpsTracker = new GPSTracker(context, SplashActivity.this);
                            gpsTracker.getLocation();
                            Log.e("Country Name--->", gpsTracker.getAddress());
                            sessionManager.setCountryName(gpsTracker.getAddress());

                            if(sessionManager.getFirstTime()){
                                Intent mainIntent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                                SplashActivity.this.startActivity(mainIntent);
                                SplashActivity.this.finish();
                            }else{
                                Intent mainIntent = new Intent(SplashActivity.this, IntroductionActivity.class);
                                SplashActivity.this.startActivity(mainIntent);
                                SplashActivity.this.finish();
                            }

                        }
                    }, SPLASH_TIME_OUT);

                } else if (gpsTracker.isNetworkAvailable()) {
                    getCityAPILIST();

                } else {
                    Snackbar.make(v, "Please check your internet connection or try again later.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            } else {
                ActivityCompat.requestPermissions(this, AppPermissions, 102);
            }

        }

    }

    private void getCityAPILIST() {
        int DEFAULT_TIMEOUT_GUIDDOO = 5 * 1000;
        String URL = sessionManager.getBaseUrl() + "utils/cities?api_key=0D1067102F935B4CC31E082BD45014D469E35268";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT_GUIDDOO);

        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String JSONResponse = new String(responseBody, "UTF-8");
                    getAllCityList(JSONResponse);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Snackbar.make(v, "Something went wrong. Please try again.", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                Snackbar.make(v, "Retrying...", Snackbar.LENGTH_SHORT).show();
            }

            public void onProgress(int bytesWritten, int totalSize) {
                // TODO Auto-generated method stub

            }

        });
    }

    private void getAllCityList(String jsonString) {

        Log.e("City Response-->", jsonString);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonData = jsonObject.getJSONObject("data");
            JSONObject jsonStatus = jsonData.getJSONObject("status");

            if (jsonStatus.getString("Success").equalsIgnoreCase("true")) {
                sessionManager.setCityStatus(jsonStatus.getString("Success"));
                sessionManager.setCityList(String.valueOf(jsonData.getJSONArray("cities")));
                JSONArray jsonArrayCity = jsonData.getJSONArray("cities");
                for (int i = 0; jsonArrayCity.length() > i; i++) {
                    CityListModel listModel = new CityListModel();
                    JSONObject jsonCity = jsonArrayCity.getJSONObject(i);
                    listModel.setCity_id(jsonCity.getString("city_id"));
                    listModel.setCity_name(jsonCity.getString("city_name"));
                    listModel.setCountry_name(jsonCity.getString("country_name"));
                    cityArrayList.add(listModel);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        gpsTracker = new GPSTracker(context, SplashActivity.this);
                        gpsTracker.getLocation();
                        sessionManager.setCountryName(gpsTracker.getAddress());

                        if(sessionManager.getFirstTime()){
                            Intent mainIntent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                            SplashActivity.this.startActivity(mainIntent);
                            SplashActivity.this.finish();
                        }else{
                            Intent mainIntent = new Intent(SplashActivity.this, IntroductionActivity.class);
                            SplashActivity.this.startActivity(mainIntent);
                            SplashActivity.this.finish();
                        }

                    }
                }, 4000);


            }else{
                Snackbar.make(v, "Something went wrong. Please check your internet connection or try again later.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        } catch (JSONException e) {
            Snackbar.make(v, "Something went wrong. Please check your internet connection or try again later.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            e.printStackTrace();
        }
    }


    boolean checkAppPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102) {
            //Log.e("permissions", permissions[0] + "," + permissions.length);
            //Log.e("grantResults", String.valueOf(grantResults[0]) + "," + grantResults.length);

            if (grantResults.length > 0) {

                if (checkAppPermissions(context, AppPermissions)) {
                    startAnimation();
                    if (sessionManager.getCityStatus().equalsIgnoreCase("true")) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                gpsTracker = new GPSTracker(context, SplashActivity.this);
                                gpsTracker.getLocation();
                                Log.e("Country Name--->", gpsTracker.getAddress());
                                sessionManager.setCountryName(gpsTracker.getAddress());

                                if(sessionManager.getFirstTime()){
                                    Intent mainIntent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                                    SplashActivity.this.startActivity(mainIntent);
                                    SplashActivity.this.finish();
                                }else{
                                    Intent mainIntent = new Intent(SplashActivity.this, IntroductionActivity.class);
                                    SplashActivity.this.startActivity(mainIntent);
                                    SplashActivity.this.finish();
                                }

                            }
                        }, SPLASH_TIME_OUT);
                    } else if (gpsTracker.isNetworkAvailable()) {
                        getCityAPILIST();
                    } else {
                        Snackbar.make(v, "Please check your internet connection or try again later.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                } else {
                    ActivityCompat.requestPermissions(this, AppPermissions, 102);
                }

            }
        }
    }

    public void startAnimation()
    {
        logo.startAnimation(zoom);

    }

}

