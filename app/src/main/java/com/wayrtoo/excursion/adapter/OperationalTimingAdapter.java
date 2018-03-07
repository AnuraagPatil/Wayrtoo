package com.wayrtoo.excursion.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.models.ActivitiesOperationTimingModel;

import java.util.ArrayList;

/**
 Created by Ishwar on 30/12/2017.
 */

public class OperationalTimingAdapter extends RecyclerView.Adapter<OperationalTimingAdapter.MyViewHolder> {
    Context context;
    ArrayList<ActivitiesOperationTimingModel> cityListModels=new ArrayList<>();
    LayoutInflater inflter;

        public OperationalTimingAdapter(Context applicationContext, ArrayList<ActivitiesOperationTimingModel> listModels) {
        this.context = applicationContext;
        this.cityListModels = listModels;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_operational_timing, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_days.setText(cityListModels.get(holder.getAdapterPosition()).getDay());
        holder.tv_booking_time.setText(cityListModels.get(holder.getAdapterPosition()).getBooking_timings().get(0).getTiming());
        holder.tv_from_time.setText(cityListModels.get(holder.getAdapterPosition()).getFrom_Time());
        holder.tv_to_time.setText(cityListModels.get(holder.getAdapterPosition()).getTo_Time());
    }

    @Override
    public int getItemCount() {
        return cityListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_days,tv_booking_time,tv_from_time,tv_to_time;

        private LinearLayout ll_content_click;
        public MyViewHolder(View view) {
            super(view);
            tv_days = (TextView) view.findViewById(R.id.tv_days);
            tv_booking_time = (TextView) view.findViewById(R.id.tv_booking_time);
            tv_from_time = (TextView) view.findViewById(R.id.tv_from_time);
            tv_to_time = (TextView) view.findViewById(R.id.tv_to_time);


        }
    }
}