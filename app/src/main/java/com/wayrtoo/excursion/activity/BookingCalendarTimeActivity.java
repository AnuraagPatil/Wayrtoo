package com.wayrtoo.excursion.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.adapter.BookingTimingAdapter;
import com.wayrtoo.excursion.models.ActivitiesCancellatioPolicyModel;
import com.wayrtoo.excursion.models.ActivitiesDetailsModel;
import com.wayrtoo.excursion.models.ActivitiesOperationTimingModel;
import com.wayrtoo.excursion.models.ActivitiesPricingDetailsModel;
import com.wayrtoo.excursion.models.TimingModel;
import com.wayrtoo.excursion.util.CustomResponseDialog;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.GPSTracker;
import com.wayrtoo.excursion.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class BookingCalendarTimeActivity extends AppCompatActivity {
    @BindView(R.id.tv_back)
    TextView tv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.datePicker)
    CalendarView datePicker;

    @BindView(R.id.tv_selected_date)
    TextView tv_selected_date;

    @BindView(R.id.tv_selected_time)
    TextView tv_selected_time;

    @BindView(R.id.rv_list)
    RecyclerView rv_ticket_list;

    @BindView(R.id.btn_Pay_Now)
    CardView btn_Pay_Now;

    private String Event_ID;
    private Context mContext;
    private SessionManager sessionManager;
    private GPSTracker gpsTracker;
    private CustomResponseDialog customResponseDialog;
    private ArrayList<ActivitiesDetailsModel> activitiesDetailstModels;
    private ArrayList<ActivitiesPricingDetailsModel> pricingDetailsModels;
    private ArrayList<ActivitiesOperationTimingModel> activitiesOperationTiming;
    private ArrayList<ActivitiesCancellatioPolicyModel> cancellatioPolicyModels;
    private ArrayList<TimingModel> timingModels;
    private BookingTimingAdapter activitiesListAdapter;
    private boolean setflagTiming;
    private Calendar myCalendar;

    private double latitude, longitude, Adult_Total_Val = 0.0, Child_Total_Val = 0.0, Final_Cost = 0.0;
    private int adult_no = 1, child_no = 0;
    boolean isTextViewClicked = false, FlagDate=false,FlagTime=false;
    private int mHour, mMinute;
    private String time = "",Selected_Date,Selected_Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        mContext = this;
        ButterKnife.bind(this);
        sessionManager = new SessionManager(mContext);
        customResponseDialog = new CustomResponseDialog(mContext);
        gpsTracker = new GPSTracker(mContext, this);
        myCalendar = Calendar.getInstance();
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");

        pricingDetailsModels = new ArrayList<ActivitiesPricingDetailsModel>();
        Date date = new Date();
        datePicker.setMinDate(date.getTime());

        Bundle b = getIntent().getExtras();
        if (b != null) {
            Event_ID = getIntent().getStringExtra("EventID");
            tv_title.setText(getIntent().getStringExtra("EventName"));
            cancellatioPolicyModels =(ArrayList<ActivitiesCancellatioPolicyModel>)getIntent().getSerializableExtra("CancellationPolicyModels");
            activitiesDetailstModels = (ArrayList<ActivitiesDetailsModel>) getIntent().getSerializableExtra("ListDetailModels");
            if (gpsTracker.isNetworkAvailable()) {
                getPriceDetails();
            } else {
                Snackbar.make(findViewById(R.id.coordinate_container), "Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else {
            Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
        }

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_Pay_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FlagDate) {

                    if (FlagTime) {
                        GetAllBookDetails();
                        /*FlagDate = false;
                        FlagTime = false;*/

                    } else {
                        tv_selected_time.startAnimation(shakeError());
                        Snackbar.make(findViewById(R.id.coordinate_container), "Please Select Time", Snackbar.LENGTH_LONG).show();
                        tv_selected_time.setText("Please Select Time");
                        tv_selected_time.setTextColor(Color.parseColor("#DB6556"));
                        FlagTime = false;
                    }

                } else {
                    tv_selected_date.startAnimation(shakeError());
                    Snackbar.make(findViewById(R.id.coordinate_container), "Please Select Date", Snackbar.LENGTH_LONG).show();
                    tv_selected_date.setText("Please Select Date");
                    tv_selected_date.setTextColor(Color.parseColor("#DB6556"));
                    FlagDate = false;
                }

            }
        });

        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);



                if(pricingDetailsModels.size()>0){
                    try {

                        String myFormat = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);
                        String day = new DateFormatSymbols().getShortWeekdays()[dayOfWeek];

                        String weekDay;
                        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
                        weekDay = dayFormat.format(myCalendar.getTime());

                        ArrayList<ActivitiesOperationTimingModel> arrayList = activitiesDetailstModels.get(0).getOperationTimingModels();
                        boolean findDate = true;

                        String oeStartDateStr = pricingDetailsModels.get(0).getFrom_date();
                        String oeEndDateStr = pricingDetailsModels.get(0).getTo_date();

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        Date startDate = null;
                        Date endDate = null;

                        startDate = dateFormat.parse(oeStartDateStr);
                        endDate = dateFormat.parse(oeEndDateStr);
                        Date d = myCalendar.getTime();
                        String currDt = sdf.format(d);

                        if ((d.after(startDate) && (d.before(endDate))) || (currDt.equals(sdf.format(startDate)) || currDt.equals(sdf.format(endDate)))) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                final String status = arrayList.get(i).getDay().toLowerCase();
                                if (status.equalsIgnoreCase(day)) {
                                    tv_selected_date.setText(day + " , " + sdf.format(myCalendar.getTime()) /*+" "+activitiesOperationTiming.get(0).getBooking_timings()*/);
                                    Selected_Date = tv_selected_date.getText().toString();
                                    tv_selected_date.setTextColor(Color.parseColor("#347038"));
                                    FlagDate = true;
                                    break;
                                } else if (arrayList.get(i).getDay().equalsIgnoreCase("daily")) {
                                    tv_selected_date.setText(day + " , " + sdf.format(myCalendar.getTime()) /*+" "+activitiesOperationTiming.get(0).getBooking_timings()*/);
                                    Selected_Date = tv_selected_date.getText().toString();
                                    tv_selected_date.setTextColor(Color.parseColor("#347038"));
                                    FlagDate = true;
                                    break;
                                } else {
                                    //ll_select_date.startAnimation(shakeError());
                                    //Snackbar.make(findViewById(R.id.coordinate_container), "Selected day not available in operational timing", Snackbar.LENGTH_LONG).show();
                                    tv_selected_date.setText("Check Operational Day");
                                    tv_selected_date.setTextColor(Color.parseColor("#DB6556"));
                                    FlagDate = false;
                                }
                            }
                        } else {
                            tv_selected_date.startAnimation(shakeError());
                            Snackbar.make(findViewById(R.id.coordinate_container), "Selected date not available in operational date range", Snackbar.LENGTH_LONG).show();
                            tv_selected_date.setText("Check Operational Date Range ");
                            tv_selected_date.setTextColor(Color.parseColor("#DB6556"));
                            FlagDate = false;
                        }

                    } catch (ParseException e) {
                        Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else{
                    Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong.pricing details not found.", Snackbar.LENGTH_LONG).show();
                }



            }
        });
    }

    private void getPriceDetails() {
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
    }

    private class CallPriceData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String s = strings[0];

            Log.e("Price Detail Resp-->", s);
            try {
                pricingDetailsModels = new ArrayList<ActivitiesPricingDetailsModel>();
                activitiesOperationTiming = new ArrayList<ActivitiesOperationTimingModel>();
                timingModels = new ArrayList<TimingModel>();

                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonData = jsonObject.getJSONObject("data");
                JSONObject jsonStatus = jsonData.getJSONObject("status");

                if (jsonStatus.getString("Success").equalsIgnoreCase("true")) {
                    ActivitiesPricingDetailsModel pricingModel = new ActivitiesPricingDetailsModel();
                    JSONArray activityListDay = jsonData.getJSONArray("operational_timings");

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

                    pricingModel.setOperationTimingModels(activitiesOperationTiming);


                    JSONArray jsonActivity = jsonData.getJSONArray("pricing");
                    for (int i = 0; i < jsonActivity.length(); i++) {
                        JSONObject activityJSON = jsonActivity.getJSONObject(i);
                        // ActivitiesPricingDetailsModel pricingModel = new ActivitiesPricingDetailsModel();
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
               // Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please check your internet connection or try again later.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                e.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rv_ticket_list.setHasFixedSize(true);
            rv_ticket_list.setNestedScrollingEnabled(false);
            rv_ticket_list.setLayoutManager(mLayoutManager);
            if (activitiesOperationTiming.get(0).getDay().equalsIgnoreCase("daily")) {
                setflagTiming = true;
            } else {
                setflagTiming = false;
            }
            activitiesListAdapter = new BookingTimingAdapter(mContext, timingModels, setflagTiming, new BookingTimingAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(TimingModel item, LinearLayout layout) {
                    tv_selected_time.setText(item.getTiming());
                    tv_selected_time.setTextColor(Color.parseColor("#347038"));
                    FlagTime = true;
                    Selected_Time = item.getTiming();

                }
            });
            rv_ticket_list.setAdapter(activitiesListAdapter);
        }
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    private void GetAllBookDetails() {


        if (pricingDetailsModels.size() > 0) {

            final Dialog dialogMsg = new Dialog(BookingCalendarTimeActivity.this);
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
            ImageView cardViewCancel = (ImageView) dialogMsg.findViewById(R.id.imageView_close);

            final LinearLayout ll_adult = (LinearLayout) dialogMsg.findViewById(R.id.ll_adult);
            final LinearLayout ll_child = (LinearLayout) dialogMsg.findViewById(R.id.ll_child);
            final LinearLayout ll_infant = (LinearLayout) dialogMsg.findViewById(R.id.ll_infant);
            final LinearLayout ll_select_date = (LinearLayout) dialogMsg.findViewById(R.id.ll_select_date);

            final TextView tv_date_time = (TextView) dialogMsg.findViewById(R.id.tv_date_time);
            final TextView tv_time = (TextView) dialogMsg.findViewById(R.id.tv_time);
            final TextView tv_operational_date = (TextView) dialogMsg.findViewById(R.id.tv_operational_date);
            final TextView tv_operational_timing = (TextView) dialogMsg.findViewById(R.id.tv_operational_timing);
            final TextView tv_duration = (TextView) dialogMsg.findViewById(R.id.tv_duration);


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

            tv_date_time.setText(Selected_Date);
            tv_time.setText(Selected_Time);


            tv_adult_total.setText(pricingDetailsModels.get(0).getCurrency() + " " + pricingDetailsModels.get(0).getAdult_price());
            tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency() + " " + pricingDetailsModels.get(0).getAdult_price());

            Double per_Adult_Price = Double.valueOf(pricingDetailsModels.get(0).getAdult_price());
            Adult_Total_Val = per_Adult_Price * adult_no;
            String result = String.format("%.2f", Adult_Total_Val);
            tv_adult_total.setText(pricingDetailsModels.get(0).getCurrency() + " " + result);
            Final_Cost = Adult_Total_Val + Child_Total_Val;
            String final_result = String.format("%.2f", Final_Cost);
            tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency() + " " + final_result);

            tv_operational_date.setText("Available Date - " + pricingDetailsModels.get(0).getFrom_date() + " To " + pricingDetailsModels.get(0).getTo_date());
            if(activitiesDetailstModels.get(0).getDay().equalsIgnoreCase("00")){
                tv_duration.setText("Duration - " + activitiesDetailstModels.get(0).getHour() + " hr " + activitiesDetailstModels.get(0).getMin() + " min");
            }else{
                tv_duration.setText("Duration - " + activitiesDetailstModels.get(0).getDay() + " day " + activitiesDetailstModels.get(0).getHour() + " hr " + activitiesDetailstModels.get(0).getMin() + " min");
            }

            tv_operational_timing.setText("Timing - " + activitiesOperationTiming.get(0).getFrom_Time() + " to " + activitiesOperationTiming.get(0).getTo_Time());

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
                    if (adult_no > 1) {
                        adult_no--;
                        tv_adult_number.setText(String.valueOf(adult_no));
                        Double per_Adult_Price = Double.valueOf(pricingDetailsModels.get(0).getAdult_price());
                        Adult_Total_Val = per_Adult_Price * adult_no;
                        String result = String.format("%.2f", Adult_Total_Val);
                        tv_adult_total.setText(pricingDetailsModels.get(0).getCurrency() + " " + result);
                        Final_Cost = Adult_Total_Val + Child_Total_Val;
                        String final_result = String.format("%.2f", Final_Cost);
                        tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency() + " " + final_result);
                    } else {
                        ll_adult.startAnimation(shakeError());
                    }
                }
            });
            tv_adult_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adult_no >= 1 && adult_no < Integer.valueOf(pricingDetailsModels.get(0).getTo_pax_count())) {
                        adult_no++;
                        tv_adult_number.setText(String.valueOf(adult_no));
                        Double per_Adult_Price = Double.valueOf(pricingDetailsModels.get(0).getAdult_price());
                        Adult_Total_Val = per_Adult_Price * adult_no;
                        String result = String.format("%.2f", Adult_Total_Val);
                        tv_adult_total.setText(pricingDetailsModels.get(0).getCurrency() + " " + result);
                        Final_Cost = Adult_Total_Val + Child_Total_Val;
                        String final_result = String.format("%.2f", Final_Cost);
                        tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency() + " " + final_result);
                    } else {
                        ll_adult.startAnimation(shakeError());
                    }
                }
            });

            tv_child_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (child_no > 0) {
                        child_no--;
                        tv_child_number.setText(String.valueOf(child_no));
                        Double per_Child_Price = Double.valueOf(pricingDetailsModels.get(0).getChild_price());
                        Child_Total_Val = per_Child_Price * child_no;
                        String result = String.format("%.2f", Child_Total_Val);
                        tv_child_total.setText(pricingDetailsModels.get(0).getCurrency() + " " + result);
                        Final_Cost = Adult_Total_Val + Child_Total_Val;
                        String final_result = String.format("%.2f", Final_Cost);
                        tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency() + " " + final_result);
                    } else {
                        ll_child.startAnimation(shakeError());
                    }
                }
            });

            tv_child_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (child_no >= 0 && child_no < Integer.valueOf(pricingDetailsModels.get(0).getTo_pax_count())) {
                        child_no++;
                        tv_child_number.setText(String.valueOf(child_no));
                        Double per_Child_Price = Double.valueOf(pricingDetailsModels.get(0).getChild_price());
                        Child_Total_Val = per_Child_Price * child_no;
                        String result = String.format("%.2f", Child_Total_Val);
                        tv_child_total.setText(pricingDetailsModels.get(0).getCurrency() + " " + result);
                        Final_Cost = Adult_Total_Val + Child_Total_Val;
                        String final_result = String.format("%.2f", Final_Cost);
                        tv_final_cost.setText(pricingDetailsModels.get(0).getCurrency() + " " + final_result);
                    } else {
                        ll_child.startAnimation(shakeError());
                    }
                }
            });

            cardViewCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMsg.dismiss();
                    FlagDate = false;
                    FlagTime = false;
                }
            });

            cardViewBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, BookingPaymentActivity.class);
                    intent.putExtra("Titile", tv_title.getText().toString());
                    intent.putExtra("Date", tv_date_time.getText().toString());
                    intent.putExtra("Time", tv_time.getText().toString());
                    intent.putExtra("Duration",tv_duration.getText().toString());
                    intent.putExtra("No_of_Adult", tv_adult_number.getText().toString());
                    intent.putExtra("No_of_Child", tv_child_number.getText().toString());
                    intent.putExtra("Total_Cost", tv_final_cost.getText().toString());
                    intent.putExtra("CancellationPolicyModels", (ArrayList<ActivitiesCancellatioPolicyModel>) cancellatioPolicyModels);
                    startActivity(intent);
                   /* FlagDate = false;
                    FlagTime = false;*/
                    dialogMsg.dismiss();
                }
            });

        } else {
            Snackbar.make(findViewById(R.id.coordinate_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
        }


    }

    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        SimpleDateFormat formatShort = new SimpleDateFormat("hh:mm aa", Locale.US);
        return formatShort.format(tme);
    }
}
