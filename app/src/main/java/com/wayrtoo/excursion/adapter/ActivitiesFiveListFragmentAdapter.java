package com.wayrtoo.excursion.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.activity.ActivitiesDetailsActivity;
import com.wayrtoo.excursion.models.ActivitiesListModel;
import com.wayrtoo.excursion.util.GPSTracker;
import com.wayrtoo.excursion.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ishwar on 26/12/2017.
 */

public class ActivitiesFiveListFragmentAdapter extends RecyclerView.Adapter<ActivitiesFiveListFragmentAdapter.MyViewHolder> {
    private List<ActivitiesListModel> activityList;
    private Context mContext;
    private Activity activity;
    private SessionManager sessionManager;
    private GPSTracker gpsTracker;
    private View vv;
    private int num = 1;

    public ActivitiesFiveListFragmentAdapter(Context mContext, List<ActivitiesListModel> activityList, Activity activity) {
        this.activity=activity;
        this.mContext=mContext;
        this.activityList = activityList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name,tv_city,tv_duration,tv_address,tv_price;
        private RatingBar rb_rating;
        private ImageView iv_image ,iv_like;
        private ProgressBar progressBar;

        private LinearLayout ll_content_click;

        public MyViewHolder(View view) {
            super(view);
            sessionManager = new SessionManager(mContext);
            gpsTracker = new GPSTracker(mContext, activity);
            vv = (LinearLayout)view.findViewById(R.id.ll_content_click) ;
            ll_content_click=(LinearLayout)view.findViewById(R.id.ll_content_click) ;
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            rb_rating = (RatingBar) view.findViewById(R.id.rb_rating);
            tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            iv_image = (ImageView)view.findViewById(R.id.iv_image);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_activty_small_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_name.setText(activityList.get(holder.getAdapterPosition()).getName());
        holder.rb_rating.setRating(Float.parseFloat(activityList.get(holder.getAdapterPosition()).getRating()));
        //holder.rb_rating.setRating(Float.parseFloat("2"));
        if(activityList.get(holder.getAdapterPosition()).getDay().equalsIgnoreCase("00")){
            holder.tv_duration.setText("Duration- "+activityList.get(holder.getAdapterPosition()).getHour()+" hr "+activityList.get(holder.getAdapterPosition()).getMin()+" min");
        }else{
            holder.tv_duration.setText("Duration- "+activityList.get(holder.getAdapterPosition()).getDay()+" day "+activityList.get(holder.getAdapterPosition()).getHour()+" hr "+activityList.get(holder.getAdapterPosition()).getMin()+" min");
        }
        holder.tv_address.setText(activityList.get(holder.getAdapterPosition()).getAddress());

        holder.tv_price.setText(activityList.get(holder.getAdapterPosition()).getCurrency()+"-"+activityList.get(holder.getAdapterPosition()).getStarting_from_price());

        Glide.with(mContext)
                .load(sessionManager.getCityImage())
                //.load(activityList.get(holder.getAdapterPosition()).getFeatured_image())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(R.anim.fade_in)
                .into(holder.iv_image);
        holder.progressBar.setVisibility(View.GONE);

        holder.ll_content_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsTracker.isNetworkAvailable()) {
                    Intent intent=new Intent(mContext, ActivitiesDetailsActivity.class);
                    intent.putExtra("EventID", activityList.get(holder.getAdapterPosition()).getTour_id());
                    intent.putExtra("EventName", activityList.get(holder.getAdapterPosition()).getName());
                    intent.putExtra("Image", activityList.get(holder.getAdapterPosition()).getFeatured_image());
                    intent.putExtra("ActivityListModel", (ArrayList<ActivitiesListModel>) activityList);
                    intent.putExtra("Position",position);
                    mContext.startActivity(intent);
                }else{
                    Snackbar.make(vv, "Please Check Internet Connection !", Snackbar.LENGTH_LONG).show();
                }
            }
        });


    }
    @Override
    public int getItemCount() {


        try {
            if(num*5 > activityList.size()){
                return activityList.size();
            }else{
                return num*5;
            }

        }catch (Exception e){
          return 0;
        }
        //return num*5;
    }

}