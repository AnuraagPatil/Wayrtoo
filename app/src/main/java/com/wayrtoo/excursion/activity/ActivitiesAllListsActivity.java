package com.wayrtoo.excursion.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.adapter.ActivitiesAllListAdapter;
import com.wayrtoo.excursion.models.ActivitiesListModel;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitiesAllListsActivity extends AppCompatActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_filter)
    ImageView iv_filter;

    @BindView(R.id.rv_list)
    RecyclerView rv_ticket_list;

    @BindView(R.id.tv_Sort_Text)
    TextView tv_Sort_Text;

    @BindView(R.id.tv_bottom_sort)
    CardView tv_bottom_sort;

    private View v;
    private Context mContext;
    private ActivitiesAllListAdapter activitiesListAdapter;
    private ArrayList<ActivitiesListModel> activitiesListModels;
    private SessionManager sessionManager;
    private ArrayList<ActivitiesListModel> range_filter_arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);
        mContext = this;
        ButterKnife.bind(this);
        sessionManager = new SessionManager(mContext);
        v = findViewById(R.id.view);
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");
        sessionManager.setSorting_Position("");
        tv_Sort_Text.setText("Sort Results");
        sessionManager.setFilterMin("0");
        sessionManager.setFilterMax("5000");
        getAllData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle b = getIntent().getExtras();
        if(b!=null){
            tv_title.setText(sessionManager.getCityName()+" Results");
            activitiesListModels =(ArrayList<ActivitiesListModel>)getIntent().getSerializableExtra("ListModels");
            Log.e("list of Array-->", String.valueOf(activitiesListModels.size()));
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rv_ticket_list.setLayoutManager(mLayoutManager);
           /* activitiesListAdapter = new ActivitiesAllListAdapter(mContext, activitiesListModels,ActivitiesAllListsActivity.this);
            rv_ticket_list.setAdapter(activitiesListAdapter);*/
        }else {
            Snackbar.make(findViewById(R.id.rl_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
        }

        range_filter_arraylist = new ArrayList<ActivitiesListModel>();

        Double min = new Double(Double.valueOf(sessionManager.getFilterMin()));
        int priceMin = min.intValue();
        Log.e("price Min --->",String.valueOf(priceMin));

        Double max = new Double(Double.valueOf(sessionManager.getFilterMax()));
        int priceMax = max.intValue();
        Log.e("price Max --->",String.valueOf(priceMax));

        for (int i = 0; i < activitiesListModels.size(); i++) {

            Double d = new Double(Double.valueOf(activitiesListModels.get(i).getStarting_from_price()));
            int price = d.intValue();
            Log.e("Price All --->",String.valueOf(price));
            if(price >= min && price <= max){
                range_filter_arraylist.add(activitiesListModels.get(i));
            }
        }
        activitiesListAdapter = new ActivitiesAllListAdapter(mContext, range_filter_arraylist,ActivitiesAllListsActivity.this);
        rv_ticket_list.setAdapter(activitiesListAdapter);

        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(mContext,FilterActivity.class);
                startActivity(intent);
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_bottom_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
                dialog.setContentView(R.layout.sorting_dialog);

                final TextView tv_popularity,tv_price_High,tv_price_Low,tv_distance;

                tv_popularity = (TextView)dialog.findViewById(R.id.tv_popularity);
                tv_price_High = (TextView)dialog.findViewById(R.id.tv_price_High);
                tv_price_Low = (TextView)dialog.findViewById(R.id.tv_price_Low);
                tv_distance = (TextView)dialog.findViewById(R.id.tv_distance);


                if(sessionManager.getSortingPosition().equalsIgnoreCase("0")){
                    tv_popularity.setTextColor(Color.parseColor("#FF7D60"));
                }else if(sessionManager.getSortingPosition().equalsIgnoreCase("1")){
                    tv_price_High.setTextColor(Color.parseColor("#FF7D60"));
                }else if(sessionManager.getSortingPosition().equalsIgnoreCase("2")){
                    tv_price_Low.setTextColor(Color.parseColor("#FF7D60"));
                }else if(sessionManager.getSortingPosition().equalsIgnoreCase("3")){
                    tv_distance.setTextColor(Color.parseColor("#FF7D60"));
                }


                tv_popularity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_Sort_Text.setText(tv_popularity.getText().toString());
                        tv_popularity.setTextColor(Color.parseColor("#FF7D60"));
                        tv_price_High.setTextColor(Color.parseColor("#000000"));
                        tv_price_Low.setTextColor(Color.parseColor("#000000"));
                        tv_distance.setTextColor(Color.parseColor("#000000"));
                        sessionManager.setSorting_Position("0");
                        dialog.dismiss();
                        Collections.sort(range_filter_arraylist, new Comparator< ActivitiesListModel >() {
                            @Override public int compare(ActivitiesListModel p1, ActivitiesListModel p2) {
                                activitiesListAdapter = new ActivitiesAllListAdapter(mContext, range_filter_arraylist,ActivitiesAllListsActivity.this);
                                rv_ticket_list.setAdapter(activitiesListAdapter);
                                Double d = new Double(Double.valueOf(p1.getRating()));
                                int i = d.intValue();
                                Double d2 = new Double(Double.valueOf(p2.getRating()));
                                int i2 = d2.intValue();
                                return i2 - i; // Highest
                            }
                        });
                    }
                });

                tv_price_High.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_Sort_Text.setText(tv_price_High.getText().toString());
                        tv_popularity.setTextColor(Color.parseColor("#000000"));
                        tv_price_High.setTextColor(Color.parseColor("#FF7D60"));
                        tv_price_Low.setTextColor(Color.parseColor("#000000"));
                        tv_distance.setTextColor(Color.parseColor("#000000"));
                        sessionManager.setSorting_Position("1");
                        dialog.dismiss();



                        Collections.sort(range_filter_arraylist, new Comparator< ActivitiesListModel >() {
                            @Override public int compare(ActivitiesListModel p1, ActivitiesListModel p2) {
                                activitiesListAdapter = new ActivitiesAllListAdapter(mContext, range_filter_arraylist,ActivitiesAllListsActivity.this);
                                rv_ticket_list.setAdapter(activitiesListAdapter);
                                Double d = new Double(Double.valueOf(p1.getStarting_from_price()));
                                int i = d.intValue();
                                Double d2 = new Double(Double.valueOf(p2.getStarting_from_price()));
                                int i2 = d2.intValue();
                                return i2 - i; // Highest
                            }
                        });


                    }
                });

                tv_price_Low.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_Sort_Text.setText(tv_price_Low.getText().toString());
                        tv_popularity.setTextColor(Color.parseColor("#000000"));
                        tv_price_High.setTextColor(Color.parseColor("#000000"));
                        tv_price_Low.setTextColor(Color.parseColor("#FF7D60"));
                        tv_distance.setTextColor(Color.parseColor("#000000"));
                        sessionManager.setSorting_Position("2");
                        dialog.dismiss();
                        Collections.sort(range_filter_arraylist, new Comparator< ActivitiesListModel >() {
                            @Override public int compare(ActivitiesListModel p1, ActivitiesListModel p2) {
                                activitiesListAdapter = new ActivitiesAllListAdapter(mContext, range_filter_arraylist,ActivitiesAllListsActivity.this);
                                rv_ticket_list.setAdapter(activitiesListAdapter);
                                Double d = new Double(Double.valueOf(p1.getStarting_from_price()));
                                int i = d.intValue();
                                Double d2 = new Double(Double.valueOf(p2.getStarting_from_price()));
                                int i2 = d2.intValue();
                                return i - i2; // Lowest
                            }
                        });
                    }
                });

                tv_distance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_Sort_Text.setText(tv_distance.getText().toString());
                        tv_popularity.setTextColor(Color.parseColor("#000000"));
                        tv_price_High.setTextColor(Color.parseColor("#000000"));
                        tv_price_Low.setTextColor(Color.parseColor("#000000"));
                        tv_distance.setTextColor(Color.parseColor("#FF7D60"));
                        sessionManager.setSorting_Position("3");
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });


    }

    public void getAllData(){
        Bundle b = getIntent().getExtras();
        if(b!=null){
            tv_title.setText(sessionManager.getCityName()+" Results");
            activitiesListModels =(ArrayList<ActivitiesListModel>)getIntent().getSerializableExtra("ListModels");
            Log.e("list of Array-->", String.valueOf(activitiesListModels.size()));
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rv_ticket_list.setLayoutManager(mLayoutManager);
           /* activitiesListAdapter = new ActivitiesAllListAdapter(mContext, activitiesListModels,ActivitiesAllListsActivity.this);
            rv_ticket_list.setAdapter(activitiesListAdapter);*/
        }else {
            Snackbar.make(findViewById(R.id.rl_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
        }
    }
}
