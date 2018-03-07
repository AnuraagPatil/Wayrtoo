package com.wayrtoo.excursion.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.models.CityListModel;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.SessionManager;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by Ishwar on 08/01/2018.
 */

public class SelectCityListAdapter extends RecyclerView.Adapter<SelectCityListAdapter.MyViewHolder> {
    Context context;
    ArrayList<CityListModel> cityListModels = new ArrayList<>();
    LayoutInflater inflter;
    Activity activity;
    private SessionManager sessionManager;
    public ArrayList<CityListModel> arraylist;



    public SelectCityListAdapter(Context applicationContext, ArrayList<CityListModel> listModels, Activity activity) {
        this.context = applicationContext;
        this.cityListModels = listModels;
        this.activity = activity;
        this.arraylist = new ArrayList<CityListModel>();
        this.arraylist.addAll(listModels);

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_add_flight_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_Short_City.setText(cityListModels.get(holder.getAdapterPosition()).getTbo_country_code());
        holder.tv_Long_City.setText(cityListModels.get(holder.getAdapterPosition()).getCity_name());

        holder.ll_Selected_Row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   sessionManager.setCityID(cityListModels.get(holder.getAdapterPosition()).getCity_id());
                   sessionManager.setCityName(cityListModels.get(holder.getAdapterPosition()).getCity_name());
                   sessionManager.setCityImage(cityListModels.get(holder.getAdapterPosition()).getFeatured_image());
                   activity.finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cityListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Short_City, tv_Long_City;
        private LinearLayout ll_Selected_Row;


        public MyViewHolder(View view) {
            super(view);
            sessionManager = new SessionManager(context);
            FontsOverride.setDefaultFont(context, "SERIF", "fonts/DroidSerif-Regular.ttf");
            tv_Short_City = (TextView) view.findViewById(R.id.tv_Short_City);
            tv_Long_City = (TextView) view.findViewById(R.id.tv_Long_City);
            ll_Selected_Row = (LinearLayout) view.findViewById(R.id.ll_Selected_Row);
        }

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        cityListModels.clear();
        if (charText.length() == 0) {
            cityListModels.addAll(arraylist);
        } else {
            for (CityListModel wp : arraylist) {
                if (wp.getCity_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    cityListModels.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}