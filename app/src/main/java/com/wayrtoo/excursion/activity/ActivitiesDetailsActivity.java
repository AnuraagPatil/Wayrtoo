package com.wayrtoo.excursion.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.adapter.CancellationPolicyAdapter;
import com.wayrtoo.excursion.adapter.OperationalTimingAdapter;
import com.wayrtoo.excursion.models.ActivitiesCancellatioPolicyModel;
import com.wayrtoo.excursion.models.ActivitiesDetailsModel;
import com.wayrtoo.excursion.models.ActivitiesListModel;
import com.wayrtoo.excursion.models.ActivitiesOperationTimingModel;
import com.wayrtoo.excursion.models.ActivitiesPricingDetailsModel;
import com.wayrtoo.excursion.models.ImagesListModel;
import com.wayrtoo.excursion.models.TimingModel;
import com.wayrtoo.excursion.util.CustomResponseDialog;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.GPSTracker;
import com.wayrtoo.excursion.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ActivitiesDetailsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    @BindView(R.id.tv_title)
    TextView toolbar_title;

    @BindView(R.id.tv_back)
    TextView go_back;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.tv_city)
    TextView tv_city;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_duration)
    TextView tv_duration;

    @BindView(R.id.tv_short_description)
    TextView tv_short_description;

    @BindView(R.id.rb_rating)
    RatingBar rb_rating;

    @BindView(R.id.tv_long_description)
    TextView tv_long_description;

    @BindView(R.id.redirect_direction)
    TextView redirect_direction;

    @BindView(R.id.read_more)
    TextView read_more;

    @BindView(R.id.tv_inclusion_read_more)
    TextView tv_inclusion_read_more;

    @BindView(R.id.slider)
    SliderLayout sliderLayout;

    @BindView(R.id.tv_available_today)
    TextView tv_available_today;

    @BindView(R.id.tv_available_today_duration)
    TextView tv_available_today_duration;

    @BindView(R.id.tv_available_today_read_more)
    TextView tv_available_today_read_more;

    @BindView(R.id.tv_cancellation_policy)
    TextView tv_cancellation_policy;

    @BindView(R.id.tv_inclusion)
    TextView tv_inclusion;

    @BindView(R.id.tv_highlights)
    TextView tv_highlights;

    @BindView(R.id.book_now)
    CardView book_now;


    private String Event_ID;
    private Context mContext;
    private SessionManager sessionManager;
    private GPSTracker gpsTracker;
    private Calendar myCalendar;
    private int mHour, mMinute;
    private String time = "";
    private CustomResponseDialog customResponseDialog;
    private ArrayList<ActivitiesDetailsModel> activitiesDetailstModels;
    private ArrayList<ActivitiesOperationTimingModel> activitiesOperationTiming;
    private ArrayList<ActivitiesCancellatioPolicyModel> cancellatioPolicyModels;
    private ArrayList<ActivitiesListModel> activitiesListModels;
    private ArrayList<TimingModel> timingModels;
    private ArrayList<ActivitiesPricingDetailsModel> pricingDetailsModels;
    private ArrayList<ImagesListModel> imagesListModels;
    private ArrayList<String> arrayListHighLight;
    private ArrayList<String> arrayListInclusion;
    private String[] days = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    boolean isTextViewClicked = false, FlagDate=false,FlagTime=false;
    private double latitude, longitude,Adult_Total_Val=0.0,Child_Total_Val=0.0,Final_Cost=0.0;
    private int adult_no = 1,child_no=0,Position;
    private MarkerOptions markerOptions;
    private IconGenerator iconFactory;
    private SupportMapFragment mMapView;
    HashMap<String, String> Hash_file_maps;
    private boolean isInclusionClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        mContext = this;
        ButterKnife.bind(this);
        sessionManager = new SessionManager(mContext);
        customResponseDialog = new CustomResponseDialog(mContext);
        gpsTracker = new GPSTracker(mContext, this);
        myCalendar = Calendar.getInstance();

        Hash_file_maps = new HashMap<String, String>();
        Bundle b = getIntent().getExtras();
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");
        mMapView = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        if (b != null) {
            Event_ID = getIntent().getStringExtra("EventID");

            toolbar_title.setText(getIntent().getStringExtra("EventName"));

            Position = getIntent().getIntExtra("Position",0);
            Log.e("Position---->",String.valueOf(Position));
            //Position = Integer.valueOf(getIntent().getStringExtra("Position"));
            activitiesListModels =(ArrayList<ActivitiesListModel>)getIntent().getSerializableExtra("ActivityListModel");

        } else {
            Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
        }

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (gpsTracker.isNetworkAvailable()) {
            getActivityDetails();
        } else {
            Snackbar.make(findViewById(R.id.coordinate_container), "Please check your internet connection.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextViewClicked) {
                    tv_long_description.setMaxLines(2);
                    isTextViewClicked = false;
                    read_more.setText("READ MORE");
                } else {
                    tv_long_description.setMaxLines(Integer.MAX_VALUE);
                    isTextViewClicked = true;
                    read_more.setText("HIDE DETAILS");
                }
            }
        });

        tv_inclusion_read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInclusionClicked) {
                    tv_inclusion.setMaxLines(2);
                    isInclusionClicked = false;
                    tv_inclusion_read_more.setText("READ MORE");
                } else {
                    tv_inclusion.setMaxLines(Integer.MAX_VALUE);
                    isInclusionClicked = true;
                    tv_inclusion_read_more.setText("HIDE DETAILS");
                }
            }
        });



//        if (activitiesListModels.get(Position).getSelected()) {
//            iv_like.setImageResource(R.drawable.ic_like);
//        }
//        else {
//            iv_like.setImageResource(R.drawable.ic_unlike);
//        }
//
//        iv_like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (activitiesListModels.get(Position).getSelected()) {
//                    iv_like.setImageResource(R.drawable.ic_unlike);
//                    activitiesListModels.get(Position).setSelected(false);
//                }
//                else {
//                    iv_like.setImageResource(R.drawable.ic_like);
//                    activitiesListModels.get(Position).setSelected(true);
//                }
//            }
//        });

        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,BookingCalendarTimeActivity.class);
                intent.putExtra("EventID", Event_ID);
                intent.putExtra("EventName", getIntent().getStringExtra("EventName"));
                intent.putExtra("ListDetailModels", (ArrayList<ActivitiesDetailsModel>) activitiesDetailstModels);
                intent.putExtra("CancellationPolicyModels", (ArrayList<ActivitiesCancellatioPolicyModel>) cancellatioPolicyModels);
                startActivity(intent);

               /* if (gpsTracker.isNetworkAvailable()) {
                    getPriceDetails();
                } else {
                    Snackbar.make(findViewById(R.id.coordinate_container), "Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }*/


            }
        });

        redirect_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewSingleMapDirectionActivity.class);
                intent.putExtra("ListDetailModels", (ArrayList<ActivitiesDetailsModel>) activitiesDetailstModels);
                startActivity(intent);
            }
        });

        tv_available_today_read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activitiesOperationTiming.size() > 0) {
                    final Dialog dialogMsg = new Dialog(ActivitiesDetailsActivity.this);
                    dialogMsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogMsg.setContentView(R.layout.event_detail_operation_timing_dialog);
                    dialogMsg.setCancelable(true);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialogMsg.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.gravity = Gravity.CENTER;
                    dialogMsg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialogMsg.getWindow().setAttributes(lp);
                    dialogMsg.show();

                    RecyclerView rv_list = (RecyclerView) dialogMsg.findViewById(R.id.rv_list);
                    ImageView cardViewCancel = (ImageView) dialogMsg.findViewById(R.id.imageView_close);
                    rv_list.setNestedScrollingEnabled(false);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    rv_list.setLayoutManager(mLayoutManager);
                    OperationalTimingAdapter timingAdapter = new OperationalTimingAdapter(getApplicationContext(), activitiesOperationTiming);
                    rv_list.setAdapter(timingAdapter);

                    cardViewCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogMsg.cancel();
                        }
                    });

                } else {
                    Snackbar.make(findViewById(R.id.coordinate_container), "Operational timing record not found. Please try again later.", Snackbar.LENGTH_LONG).show();
                }

            }
        });

        tv_cancellation_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cancellatioPolicyModels.size() > 0) {

                    final Dialog dialogMsg = new Dialog(ActivitiesDetailsActivity.this);
                    dialogMsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogMsg.setContentView(R.layout.event_detail_cancellation_policy_dialog);
                    dialogMsg.setCancelable(true);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialogMsg.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.gravity = Gravity.CENTER;
                    dialogMsg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialogMsg.getWindow().setAttributes(lp);
                    dialogMsg.show();

                    RecyclerView rv_list = (RecyclerView) dialogMsg.findViewById(R.id.rv_list);
                    ImageView cardViewCancel = (ImageView) dialogMsg.findViewById(R.id.imageView_close);
                    rv_list.setNestedScrollingEnabled(false);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    rv_list.setLayoutManager(mLayoutManager);
                    CancellationPolicyAdapter cancelAdapter = new CancellationPolicyAdapter(getApplicationContext(), cancellatioPolicyModels);
                    rv_list.setAdapter(cancelAdapter);

                    cardViewCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogMsg.cancel();
                        }
                    });

                } else {
                    Snackbar.make(findViewById(R.id.coordinate_container), "Cancellation policy record not found. Please try again later.", Snackbar.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            //mMapView.onCreate();
            mMapView.onResume();
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            final LatLng currentPos = new LatLng(Double.valueOf(activitiesDetailstModels.get(0).getLatitude()), Double.valueOf(activitiesDetailstModels.get(0).getLongitude()));

            googleMap.clear();
            iconFactory = new IconGenerator(ActivitiesDetailsActivity.this);
            LatLng sydney = new LatLng(latitude, longitude);
            String add = getAddress(latitude, longitude);
            iconFactory.setStyle(IconGenerator.STYLE_GREEN);
            markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(add))).position(sydney).anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            googleMap.addMarker(markerOptions);

            LatLng sydney1 = new LatLng(Double.valueOf(activitiesDetailstModels.get(0).getLatitude()), Double.valueOf(activitiesDetailstModels.get(0).getLongitude()));
            markerOptions = new MarkerOptions().position(sydney1);
            markerOptions.title(activitiesDetailstModels.get(0).getName());
            markerOptions.icon(bitmapDescriptorFromVector(ActivitiesDetailsActivity.this, R.drawable.ic_pined));
            googleMap.addMarker(markerOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPos).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        } catch (Exception e) {
        }

    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ActivitiesDetailsActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                // result.append(address.getAddressLine(1)).append("\n");
                result.append(address.getLocality() + " " + address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getActivityDetails() {
        customResponseDialog.showCustomDialog();
        int DEFAULT_TIMEOUT_GUIDDOO = 5 * 1000;
        String URL = sessionManager.getBaseUrl() + "products/activities/details?tour_id=" + Event_ID + "&api_key=0D1067102F935B4CC31E082BD45014D469E35268";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT_GUIDDOO);

        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                customResponseDialog.hideCustomeDialog();

                try {
                    String JSONResponse = new String(responseBody, "UTF-8");

                    new CallData().execute(JSONResponse);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                customResponseDialog.hideCustomeDialog();
                Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onRetry(int retryNo) {
                customResponseDialog.hideCustomeDialog();
                Snackbar.make(findViewById(R.id.coordinate_container), "Retrying...", Snackbar.LENGTH_LONG).show();
            }

            public void onProgress(int bytesWritten, int totalSize) {
                // TODO Auto-generated method stub

            }

        });
    }

   /* private void getPriceDetails() {
        customResponseDialog.showCustomDialog();
        int DEFAULT_TIMEOUT_GUIDDOO = 5 * 1000;
        String URL = sessionManager.getBaseUrl() + "products/activities/pricing?tour_id=" + Event_ID + "&api_key=0D1067102F935B4CC31E082BD45014D469E35268";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT_GUIDDOO);

        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                customResponseDialog.hideCustomeDialog();

                try {
                    String JSONResponse = new String(responseBody, "UTF-8");

                    new CallPriceData().execute(JSONResponse);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                customResponseDialog.hideCustomeDialog();
                Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onRetry(int retryNo) {
                customResponseDialog.hideCustomeDialog();
                Snackbar.make(findViewById(R.id.coordinate_container), "Retrying...", Snackbar.LENGTH_LONG).show();
            }

            public void onProgress(int bytesWritten, int totalSize) {
                // TODO Auto-generated method stub

            }

        });
    }*/

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

   /* private class CallPriceData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String s = strings[0];

            Log.e("Price Detail Resp-->", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonData = jsonObject.getJSONObject("data");
                JSONObject jsonStatus = jsonData.getJSONObject("status");

                if (jsonStatus.getString("Success").equalsIgnoreCase("true")) {
                    pricingDetailsModels = new ArrayList<ActivitiesPricingDetailsModel>();

                    JSONArray jsonActivity = jsonData.getJSONArray("pricing");
                    for (int i = 0; i < jsonActivity.length(); i++) {
                        JSONObject activityJSON = jsonActivity.getJSONObject(i);
                        ActivitiesPricingDetailsModel pricingModel = new ActivitiesPricingDetailsModel();
                        pricingModel.setCurrency(activityJSON.getString("currency"));
                        pricingModel.setFrom_date(activityJSON.getString("from_date"));

                        JSONArray jsonpricing = activityJSON.getJSONArray("group_pricing");
                        for (int j = 0; j < jsonpricing.length(); j++) {
                            JSONObject jsonObjectPrice = jsonpricing.getJSONObject(j);
                            pricingModel.setAdult_price(jsonObjectPrice.getString("adult_price"));
                            pricingModel.setChild_price(jsonObjectPrice.getString("child_price"));
                            pricingModel.setFrom_pax_count(jsonObjectPrice.getString("from_pax_count"));
                            pricingModel.setInfant_price(jsonObjectPrice.getString("infant_price"));
                            pricingModel.setInventory(jsonObjectPrice.getString("inventory"));
                            pricingModel.setTo_pax_count(jsonObjectPrice.getString("to_pax_count"));
                        }
                        pricingModel.setTo_date(activityJSON.getString("to_date"));
                        pricingDetailsModels.add(pricingModel);
                    }

                } else {
                    Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please check your internet connection or try again later.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } catch (JSONException e) {
                Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please check your internet connection or try again later.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                e.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {

            if (pricingDetailsModels.size() > 0) {

                final Dialog dialogMsg = new Dialog(ActivitiesDetailsActivity.this);
                dialogMsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMsg.setContentView(R.layout.event_detail_pricing_dialog);
                dialogMsg.setCancelable(false);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogMsg.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;
                dialogMsg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogMsg.getWindow().setAttributes(lp);
                dialogMsg.show();


                CardView cardViewBooking = (CardView) dialogMsg.findViewById(R.id.cardViewBooking);
                CardView cardViewCancel = (CardView)dialogMsg.findViewById(R.id.cardViewCancel);

                final LinearLayout ll_adult = (LinearLayout) dialogMsg.findViewById(R.id.ll_adult);
                final LinearLayout ll_child = (LinearLayout) dialogMsg.findViewById(R.id.ll_child);
                final LinearLayout ll_infant = (LinearLayout) dialogMsg.findViewById(R.id.ll_infant);
                final LinearLayout ll_select_date = (LinearLayout) dialogMsg.findViewById(R.id.ll_select_date);

                final TextView tv_date_time = (TextView) dialogMsg.findViewById(R.id.tv_date_time);
                final TextView tv_time = (TextView)dialogMsg.findViewById(R.id.tv_time);
                final TextView tv_operational_date = (TextView) dialogMsg.findViewById(R.id.tv_operational_date);
                final TextView tv_operational_timing = (TextView)dialogMsg.findViewById(R.id.tv_operational_timing);
                final TextView tv_duration = (TextView)dialogMsg.findViewById(R.id.tv_duration);


                final TextView tv_adult_price = (TextView) dialogMsg.findViewById(R.id.tv_adult_price);
                final TextView tv_adult_minus = (TextView) dialogMsg.findViewById(R.id.tv_adult_minus);
                final TextView tv_adult_number = (TextView) dialogMsg.findViewById(R.id.tv_adult_number);
                final TextView tv_adult_plus = (TextView) dialogMsg.findViewById(R.id.tv_adult_plus);
                final TextView tv_adult_total = (TextView) dialogMsg.findViewById(R.id.tv_adult_total);

                final TextView tv_child_price = (TextView) dialogMsg.findViewById(R.id.tv_child_price);
                final TextView tv_child_minus = (TextView) dialogMsg.findViewById(R.id.tv_child_minus);
                final TextView tv_child_number = (TextView) dialogMsg.findViewById(R.id.tv_child_number);
                final TextView tv_child_plus = (TextView) dialogMsg.findViewById(R.id.tv_child_plus);
                final TextView tv_child_total = (TextView) dialogMsg.findViewById(R.id.tv_child_total);
                final TextView tv_final_cost = (TextView) dialogMsg.findViewById(R.id.tv_final_cost);

                tv_adult_total.setText(pricingDetailsModels.get(0).getCurrency()+" "+pricingDetailsModels.get(0).getAdult_price());
                tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency()+" "+pricingDetailsModels.get(0).getAdult_price());

                Double per_Adult_Price= Double.valueOf(pricingDetailsModels.get(0).getAdult_price());
                Adult_Total_Val= per_Adult_Price* adult_no;
                String result = String.format("%.2f", Adult_Total_Val);
                tv_adult_total.setText(pricingDetailsModels.get(0).getCurrency()+" "+result);
                Final_Cost = Adult_Total_Val+Child_Total_Val;
                String final_result = String.format("%.2f", Final_Cost);
                tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency()+" "+final_result);

                tv_operational_date.setText("Available Date - "+pricingDetailsModels.get(0).getFrom_date()+" To "+pricingDetailsModels.get(0).getTo_date());
                tv_duration.setText("Duration- D:"+ activitiesDetailstModels.get(0).getDay() + " - Hr:" +activitiesDetailstModels.get(0).getHour() + " - Min:" +activitiesDetailstModels.get(0).getMin());
                tv_operational_timing.setText("Timing- "+activitiesOperationTiming.get(0).getFrom_Time()+" to "+activitiesOperationTiming.get(0).getTo_Time());

                if (pricingDetailsModels.get(0).getAdult_price().equalsIgnoreCase("0")) {
                    ll_adult.setVisibility(View.GONE);
                } else {
                    tv_adult_price.setText(pricingDetailsModels.get(0).getCurrency() + " " + pricingDetailsModels.get(0).getAdult_price() + " per head");

                }

                if (pricingDetailsModels.get(0).getChild_price().equalsIgnoreCase("0")) {
                    ll_child.setVisibility(View.GONE);
                } else {
                    tv_child_price.setText(pricingDetailsModels.get(0).getCurrency() + " " + pricingDetailsModels.get(0).getChild_price() + " per head");
                }

                if (pricingDetailsModels.get(0).getInfant_price().equalsIgnoreCase("0")) {
                    ll_infant.setVisibility(View.GONE);
                } else {
                    //tv_child_price.setText(pricingDetailsModels.get(0).getChild_price());

                }

                tv_adult_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(adult_no>1 ){
                            adult_no--;
                            tv_adult_number.setText(String.valueOf(adult_no));
                            Double per_Adult_Price= Double.valueOf(pricingDetailsModels.get(0).getAdult_price());
                            Adult_Total_Val= per_Adult_Price* adult_no;
                            String result = String.format("%.2f", Adult_Total_Val);
                            tv_adult_total.setText(pricingDetailsModels.get(0).getCurrency()+" "+result);
                            Final_Cost = Adult_Total_Val+Child_Total_Val;
                            String final_result = String.format("%.2f", Final_Cost);
                            tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency()+" "+final_result);
                        }else{
                            ll_adult.startAnimation(shakeError());
                        }
                    }
                });
                tv_adult_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(adult_no>=1 && adult_no<Integer.valueOf(pricingDetailsModels.get(0).getTo_pax_count())){
                            adult_no++;
                            tv_adult_number.setText(String.valueOf(adult_no));
                            Double per_Adult_Price= Double.valueOf(pricingDetailsModels.get(0).getAdult_price());
                            Adult_Total_Val= per_Adult_Price* adult_no;
                            String result = String.format("%.2f", Adult_Total_Val);
                            tv_adult_total.setText(pricingDetailsModels.get(0).getCurrency()+" "+result);
                            Final_Cost = Adult_Total_Val+Child_Total_Val;
                            String final_result = String.format("%.2f", Final_Cost);
                            tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency()+" "+final_result);
                        }else{
                            ll_adult.startAnimation(shakeError());
                        }
                    }
                });

                tv_child_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(child_no>0 ){
                            child_no--;
                            tv_child_number.setText(String.valueOf(child_no));
                            Double per_Child_Price= Double.valueOf(pricingDetailsModels.get(0).getChild_price());
                            Child_Total_Val= per_Child_Price* child_no;
                            String result = String.format("%.2f", Child_Total_Val);
                            tv_child_total.setText(pricingDetailsModels.get(0).getCurrency()+" "+result);
                            Final_Cost = Adult_Total_Val+Child_Total_Val;
                            String final_result = String.format("%.2f", Final_Cost);
                            tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency()+" "+final_result);
                        }else{
                            ll_child.startAnimation(shakeError());
                        }
                    }
                });

                tv_child_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(child_no>=0 && child_no<Integer.valueOf(pricingDetailsModels.get(0).getTo_pax_count())){
                            child_no++;
                            tv_child_number.setText(String.valueOf(child_no));
                            Double per_Child_Price= Double.valueOf(pricingDetailsModels.get(0).getChild_price());
                            Child_Total_Val= per_Child_Price* child_no;
                            String result = String.format("%.2f", Child_Total_Val);
                            tv_child_total.setText(pricingDetailsModels.get(0).getCurrency()+" "+result);
                            Final_Cost = Adult_Total_Val+Child_Total_Val;
                            String final_result = String.format("%.2f", Final_Cost);
                            tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency()+" "+final_result);
                        }else{
                            ll_child.startAnimation(shakeError());
                        }
                    }
                });


                tv_date_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                String myFormat = "dd/MM/yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                                int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);
                                String day = new DateFormatSymbols().getShortWeekdays()[dayOfWeek];

                                String weekDay;
                                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
                                weekDay = dayFormat.format(myCalendar.getTime());

                                ArrayList<ActivitiesOperationTimingModel> arrayList =activitiesDetailstModels.get(0).getOperationTimingModels();
                                boolean findDate=true;

                                String oeStartDateStr = pricingDetailsModels.get(0).getFrom_date();
                                String oeEndDateStr = pricingDetailsModels.get(0).getTo_date();

                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                Date startDate = null;
                                Date endDate =null;

                                try {
                                    startDate = dateFormat.parse(oeStartDateStr);
                                    endDate = dateFormat.parse(oeEndDateStr);
                                    Date d = myCalendar.getTime();
                                    String currDt = sdf.format(d);

                                    if((d.after(startDate) && (d.before(endDate))) || (currDt.equals(sdf.format(startDate)) ||currDt.equals(sdf.format(endDate)))){
                                        for(int i=0;i<arrayList.size();i++){
                                            final String status = arrayList.get(i).getDay().toLowerCase();
                                            if(status.equalsIgnoreCase(day)){
                                                tv_date_time.setText(day + " , " + sdf.format(myCalendar.getTime())*//*+" "+activitiesOperationTiming.get(0).getBooking_timings()*//*);
                                                tv_date_time.setTextColor(Color.parseColor("#347038"));
                                                FlagDate=true;
                                                break;
                                            }else if(arrayList.get(i).getDay().equalsIgnoreCase("daily")){
                                                tv_date_time.setText(day + " , " + sdf.format(myCalendar.getTime())*//*+" "+activitiesOperationTiming.get(0).getBooking_timings()*//*);
                                                tv_date_time.setTextColor(Color.parseColor("#347038"));
                                                FlagDate=true;
                                                break;
                                            }else{
                                                //ll_select_date.startAnimation(shakeError());
                                                //Snackbar.make(findViewById(R.id.coordinate_container), "Selected day not available in operational timing", Snackbar.LENGTH_LONG).show();
                                                tv_date_time.setText("Check Operational Day");
                                                tv_date_time.setTextColor(Color.parseColor("#DB6556"));
                                                FlagDate=false;
                                            }
                                        }
                                    }
                                    else{
                                        ll_select_date.startAnimation(shakeError());
                                        Snackbar.make(findViewById(R.id.coordinate_container), "Selected date not available in operational date range", Snackbar.LENGTH_LONG).show();
                                        tv_date_time.setText("Check Operational Date Range ");
                                        tv_date_time.setTextColor(Color.parseColor("#DB6556"));
                                        FlagDate=false;
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        new DatePickerDialog(mContext, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });

                tv_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Get Current Time
                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                        time = " " + getTime(hourOfDay, minute);

                                        SimpleDateFormat Tsdf = new SimpleDateFormat("hh:mm aa", Locale.US);

                                        try {
                                            Date date1 = Tsdf.parse(getTime(hourOfDay, minute));
                                            if(activitiesOperationTiming.size()>0 && activitiesOperationTiming!=null){
                                                Date from_Date = Tsdf.parse(activitiesOperationTiming.get(0).getFrom_Time());
                                                Date to_Date = Tsdf.parse(activitiesOperationTiming.get(0).getTo_Time());
                                                Log.e("Selected Time",time);
                                                Log.e("From Time",activitiesOperationTiming.get(0).getFrom_Time());
                                                Log.e("To Time",activitiesOperationTiming.get(0).getTo_Time());

                                                if(date1.before(to_Date) && date1.after(from_Date)) {
                                                    tv_time.setText(getTime(hourOfDay, minute));
                                                    tv_time.setTextColor(Color.parseColor("#347038"));
                                                    FlagTime=true;
                                                } else {
                                                    ll_select_date.startAnimation(shakeError());
                                                    Snackbar.make(findViewById(R.id.coordinate_container), "Please Select Operational Time", Snackbar.LENGTH_LONG).show();
                                                    tv_time.setText("Select Operational Time");
                                                    tv_time.setTextColor(Color.parseColor("#DB6556"));
                                                    FlagTime=false;
                                                }
                                            }

                                        } catch (ParseException e){
                                            e.printStackTrace();
                                        }

                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();

                    }
                });


                cardViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogMsg.dismiss();
                        FlagDate=false;
                        FlagTime=false;
                    }
                });

                cardViewBooking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(FlagDate){
                            if(FlagTime){
                                Intent intent = new Intent(mContext, BookingPaymentActivity.class);
                                intent.putExtra("Titile",tv_title.getText().toString());
                                intent.putExtra("Date",tv_date_time.getText().toString());
                                intent.putExtra("Time",tv_time.getText().toString());
                                intent.putExtra("Duration",tv_available_today_duration.getText().toString());
                                intent.putExtra("No_of_Adult",tv_adult_number.getText().toString());
                                intent.putExtra("No_of_Child",tv_child_number.getText().toString());
                                intent.putExtra("Total_Cost",tv_final_cost.getText().toString());
                                intent.putExtra("CancellationPolicyModels", (ArrayList<ActivitiesCancellatioPolicyModel>) cancellatioPolicyModels);
                                startActivity(intent);
                                FlagDate=false;
                                FlagTime=false;
                                dialogMsg.dismiss();
                            }else{
                                ll_select_date.startAnimation(shakeError());
                                Snackbar.make(findViewById(R.id.coordinate_container), "Please Select Time", Snackbar.LENGTH_LONG).show();
                                tv_time.setText("Please Select Time");
                                tv_time.setTextColor(Color.parseColor("#DB6556"));
                                FlagTime=false;
                            }

                        }else {
                            ll_select_date.startAnimation(shakeError());
                            Snackbar.make(findViewById(R.id.coordinate_container), "Please Select Date", Snackbar.LENGTH_LONG).show();
                            tv_date_time.setText("Please Select Date");
                            tv_date_time.setTextColor(Color.parseColor("#DB6556"));
                            FlagDate=false;
                        }
                    }
                });

            } else {
                Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
            }


            super.onPostExecute(s);
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        FlagDate=false;
    }

    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        SimpleDateFormat formatShort = new SimpleDateFormat("hh:mm aa", Locale.US);
        return formatShort.format(tme);
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    private class CallData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String s = params[0];

            Log.e("Activity Detail Resp-->", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonData = jsonObject.getJSONObject("data");
                JSONObject jsonStatus = jsonData.getJSONObject("status");

                if (jsonStatus.getString("Success").equalsIgnoreCase("true")) {
                    //sessionManager.setActivtityStatus(jsonStatus.getString("Success"));
                    //sessionManager.setActivityList(String.valueOf(jsonData.getJSONArray("activityDetail")));

                    activitiesDetailstModels = new ArrayList<ActivitiesDetailsModel>();

                    ActivitiesDetailsModel listModel = new ActivitiesDetailsModel();
                    JSONObject jsonActivity = jsonData.getJSONObject("activityDetail");
                    listModel.setAddress(jsonActivity.getString("address"));
                    listModel.setCategory(jsonActivity.getString("category"));
                    listModel.setCity(jsonActivity.getString("city"));
                    listModel.setCurrency(jsonActivity.getString("currency"));

                    JSONObject jsonDuration = jsonActivity.getJSONObject("duration");
                    listModel.setDay(jsonDuration.getString("day"));
                    listModel.setHour(jsonDuration.getString("hour"));
                    listModel.setMin(jsonDuration.getString("min"));

                    JSONObject jsonChildAge = jsonActivity.getJSONObject("child_age");
                    listModel.setChild_max_age(jsonChildAge.getString("max_age"));
                    listModel.setChild_min_age(jsonChildAge.getString("min_age"));


                    listModel.setCategory(jsonActivity.getString("featured_image"));
                    listModel.setName(jsonActivity.getString("name"));
                    listModel.setRating(jsonActivity.getString("rating"));
                    listModel.setShort_description(jsonActivity.getString("short_description"));
                    listModel.setStarting_from_price(jsonActivity.getString("starting_from_price"));
                    listModel.setDescription(jsonActivity.getString("description"));
                    listModel.setAvailable_for_child(jsonActivity.getString("available_for_child"));
                    listModel.setLatitude(jsonActivity.getString("latitude"));
                    listModel.setLongitude(jsonActivity.getString("longitude"));

                    JSONArray jsonArrayHighlight = jsonActivity.getJSONArray("highlights");
                    arrayListHighLight = new ArrayList<String>();
                    for(int l=0;l<jsonArrayHighlight.length();l++){
                        arrayListHighLight.add(jsonArrayHighlight.get(l).toString());
                    }
                    listModel.setHighlights(arrayListHighLight);

                    JSONArray jsonArrayInclision = jsonActivity.getJSONArray("inclusion");
                    arrayListInclusion = new ArrayList<String>();
                    for(int l=0;l<jsonArrayInclision.length();l++){
                        arrayListInclusion.add(jsonArrayInclision.get(l).toString());
                    }
                    listModel.setInclusion(arrayListInclusion);


                    JSONArray cancellation_policy = jsonActivity.getJSONArray("cancellation_policy");
                    cancellatioPolicyModels = new ArrayList<ActivitiesCancellatioPolicyModel>();
                    for (int k = 0; k < cancellation_policy.length(); k++) {
                        JSONObject activityJSON = cancellation_policy.getJSONObject(k);
                        ActivitiesCancellatioPolicyModel cancellatioPolicyModel = new ActivitiesCancellatioPolicyModel();
                        cancellatioPolicyModel.setCharge(activityJSON.getString("charge"));
                        cancellatioPolicyModel.setChargeable_by_percentage(activityJSON.getString("chargeable_by_percentage"));
                        cancellatioPolicyModel.setFrom_hr(activityJSON.getString("from_hr"));
                        cancellatioPolicyModel.setTo_hr(activityJSON.getString("to_hr"));

                        cancellatioPolicyModels.add(cancellatioPolicyModel);
                    }

                    listModel.setCancellatioPolicyModels(cancellatioPolicyModels);


                    JSONArray images = jsonActivity.getJSONArray("images");
                    imagesListModels = new ArrayList<ImagesListModel>();
                    for (int k = 0; k < images.length(); k++) {
                        JSONObject activityJSON = images.getJSONObject(k);
                        ImagesListModel imagesModel = new ImagesListModel();
                        imagesModel.setDisplay_Position(activityJSON.getString("Display_Position"));
                        imagesModel.setImagePath(activityJSON.getString("ImagePath"));
                        imagesListModels.add(imagesModel);
                    }

                    listModel.setImagesListModels(imagesListModels);


                    JSONArray activityListDay = jsonActivity.getJSONArray("operational_timings");
                    activitiesOperationTiming = new ArrayList<ActivitiesOperationTimingModel>();
                    timingModels = new ArrayList<TimingModel>();
                    for (int k = 0; k < activityListDay.length(); k++) {
                        JSONObject activityJSON = activityListDay.getJSONObject(k);
                        ActivitiesOperationTimingModel operationTimingModel = new ActivitiesOperationTimingModel();
                        operationTimingModel.setDay(activityJSON.getString("Day"));
                        JSONArray TourTimings = activityJSON.getJSONArray("TourTimings");
                        for (int j = 0; j < TourTimings.length(); j++) {
                            JSONObject activitytime = TourTimings.getJSONObject(j);
                            operationTimingModel.setFrom_Time(activitytime.getString("From_Time"));
                            operationTimingModel.setTo_Time(activitytime.getString("To_Time"));
                        }
                        JSONArray bookingtime = activityJSON.getJSONArray("booking_timings");

                        for (int j = 0; j < bookingtime.length(); j++) {
                            TimingModel timingModel = new TimingModel();
                            timingModel.setTiming(bookingtime.get(j).toString());
                            timingModels.add(timingModel);
                        }
                        operationTimingModel.setBooking_timings(timingModels);
                        activitiesOperationTiming.add(operationTimingModel);
                    }

                    listModel.setOperationTimingModels(activitiesOperationTiming);

                    activitiesDetailstModels.add(listModel);


                } else {
                    Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please check your internet connection or try again later.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            } catch (JSONException e) {
                Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please check your internet connection or try again later.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                e.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            mMapView.getMapAsync(ActivitiesDetailsActivity.this);

            toolbar_title.setText(activitiesDetailstModels.get(0).getName());

            rb_rating.setRating(Float.parseFloat(activitiesDetailstModels.get(0).getRating()));
            tv_price.setText(activitiesDetailstModels.get(0).getCurrency() + " " + activitiesDetailstModels.get(0).getStarting_from_price());
            tv_city.setText(activitiesDetailstModels.get(0).getCity());
            tv_address.setText(activitiesDetailstModels.get(0).getAddress());
            if(activitiesDetailstModels.get(0).getDay().equalsIgnoreCase("00")){
                tv_duration.setText( activitiesDetailstModels.get(0).getHour() + " hr " + activitiesDetailstModels.get(0).getMin() + " min");
            }else{
                tv_duration.setText(activitiesDetailstModels.get(0).getDay() + " day " + activitiesDetailstModels.get(0).getHour() + " hr " + activitiesDetailstModels.get(0).getMin() + " min");
            }
            tv_short_description.setText(activitiesDetailstModels.get(0).getShort_description());
            tv_long_description.setText(Html.fromHtml(activitiesDetailstModels.get(0).getDescription()));

            if(arrayListInclusion.size()>0 && arrayListInclusion!=null){
                String toPrint = "";
                for(int i=0;i<arrayListInclusion.size();i++){
                    toPrint += " * "+arrayListInclusion.get(i)+".\n";
                }
                tv_inclusion.setText(toPrint);
            }

            if(arrayListHighLight.size()>0 && arrayListHighLight!=null){
                StringBuffer  Highlite=new StringBuffer();
                for(int i=0;i<arrayListHighLight.size();i++){
                    Highlite.append(" * "+arrayListHighLight.get(i).toString()+".").append('\n');
                }
                tv_highlights.setText(Highlite.toString());
            }

            if(activitiesOperationTiming.size()>0 && activitiesOperationTiming!=null){
                tv_available_today.setText("Start Booking at " + activitiesOperationTiming.get(0).getBooking_timings().get(0).getTiming());
            }

            if(activitiesDetailstModels.get(0).getDay().equalsIgnoreCase("00")){
                tv_available_today_duration.setText("Duration- " + activitiesDetailstModels.get(0).getHour() + " hr " + activitiesDetailstModels.get(0).getMin() + " min");
            }else{
                tv_available_today_duration.setText("Duration- " + activitiesDetailstModels.get(0).getDay() + " day " + activitiesDetailstModels.get(0).getHour() + " hr " + activitiesDetailstModels.get(0).getMin() + " min");
            }


            Hash_file_maps.put("Abu Dhabi", "https://guiddooimages.blob.core.windows.net/booking-engine-images/abudhabi.jpg");
            Hash_file_maps.put("Dubai", "https://guiddooimages.blob.core.windows.net/booking-engine-images/dubai.jpg");
            Hash_file_maps.put("Bali", "https://guiddooimages.blob.core.windows.net/booking-engine-images/bali.jpg");
            Hash_file_maps.put("Singapore", "https://guiddooimages.blob.core.windows.net/booking-engine-images/Singapore.jpg");
            Hash_file_maps.put("Bangkok", "https://guiddooimages.blob.core.windows.net/booking-engine-images/Bangkok.jpg");
            Hash_file_maps.put("Hong kong", "https://guiddooimages.blob.core.windows.net/booking-engine-images/hongkong.jpg");

            for (String name : Hash_file_maps.keySet()) {

                TextSliderView textSliderView = new TextSliderView(ActivitiesDetailsActivity.this);
                textSliderView
                        .description(name)
                        .image(Hash_file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);
                sliderLayout.addSlider(textSliderView);
            }
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(2000);
        }

        @Override
        protected void onPreExecute() {
        }

    }

}
