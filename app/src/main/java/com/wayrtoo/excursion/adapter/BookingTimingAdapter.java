package com.wayrtoo.excursion.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.models.TimingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ishwar on 26/12/2017.
 */

public class BookingTimingAdapter extends RecyclerView.Adapter<BookingTimingAdapter.MyViewHolder> {
    private List<TimingModel> activityList;
    private Context mContext;
    private Activity activity;
    private View vv;
    private List<TimingModel> items;
    private OnItemClickListener listener;
    private int row_index=-1;
    private  boolean flag;
    private int num = 1;

    public BookingTimingAdapter(Context mContext, ArrayList<TimingModel> flightsListModels, boolean flag, OnItemClickListener onItemClickListener) {
        this.activity=activity;
        this.mContext=mContext;
        this.listener = onItemClickListener;
        this.items=flightsListModels;
        this.activityList = flightsListModels;
        this.flag = flag;
    }

    public interface OnItemClickListener {
        void onItemClick(TimingModel item, LinearLayout layout);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_booking_time;
        private LinearLayout ll_content_click;
        public MyViewHolder(View view) {
            super(view);
            ll_content_click = (LinearLayout)view.findViewById(R.id.ll_content_click);
            tv_booking_time = (TextView)view.findViewById(R.id.tv_booking_time);

        }

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_timing_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.tv_booking_time.setText(activityList.get(holder.getAdapterPosition()).getTiming());

        holder.ll_content_click.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(items.get(position),holder.ll_content_click);
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if(row_index==position){
            holder.ll_content_click.setBackgroundColor(Color.parseColor("#FF7D60"));
        }else{
            holder.ll_content_click.setBackgroundColor(Color.parseColor("#ffffff"));
        }

    }
    @Override
    public int getItemCount() {
        if(flag){
            return activityList.size();
        }else{
            if(num*1 > activityList.size()){
                return activityList.size();
            }else{
                return num*1;
            }

        }
        //return activityList.size();
    }

}