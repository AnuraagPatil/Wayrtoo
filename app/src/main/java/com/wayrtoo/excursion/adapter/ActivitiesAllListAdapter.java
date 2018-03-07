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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.activity.ActivitiesDetailsActivity;
import com.wayrtoo.excursion.models.ActivitiesListModel;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.GPSTracker;
import com.wayrtoo.excursion.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ishwar on 26/12/2017.
 */

public class ActivitiesAllListAdapter extends RecyclerView.Adapter<ActivitiesAllListAdapter.MyViewHolder> {
    private List<ActivitiesListModel> activityList;
    private Context mContext;
    private Activity activity;
    private GPSTracker gpsTracker;
    private SessionManager sessionManager;
    private View vv;
    private int row_index=-1;

    public ActivitiesAllListAdapter(Context mContext, List<ActivitiesListModel> activityList, Activity activity) {
        this.activity = activity;
        this.mContext = mContext;
        this.activityList = activityList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_city, tv_duration, tv_address, tv_price;
        public RatingBar rb_rating;
        public ImageView iv_image, iv_like;
        private LinearLayout ll_content_click;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            sessionManager = new SessionManager(mContext);
            gpsTracker = new GPSTracker(mContext, activity);
            FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");
            vv = (LinearLayout) view.findViewById(R.id.ll_content_click);
            ll_content_click = (LinearLayout) view.findViewById(R.id.ll_content_click);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            rb_rating = (RatingBar) view.findViewById(R.id.rb_rating);
            tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            iv_like = (ImageView) view.findViewById(R.id.iv_like);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_activty_big_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_name.setText(activityList.get(holder.getAdapterPosition()).getName());
        holder.rb_rating.setRating(Float.parseFloat(activityList.get(holder.getAdapterPosition()).getRating()));
        //holder.rb_rating.setRating(Float.parseFloat("2"));
        if (activityList.get(holder.getAdapterPosition()).getDay().equalsIgnoreCase("00")) {
            holder.tv_duration.setText("Duration- " + activityList.get(holder.getAdapterPosition()).getHour() + " hr " + activityList.get(holder.getAdapterPosition()).getMin() + " min");
        } else {
            holder.tv_duration.setText("Duration- " + activityList.get(holder.getAdapterPosition()).getDay() + " day " + activityList.get(holder.getAdapterPosition()).getHour() + " hr " + activityList.get(holder.getAdapterPosition()).getMin() + " min");
        }
        holder.tv_address.setText(activityList.get(holder.getAdapterPosition()).getAddress());
        holder.tv_price.setText(activityList.get(holder.getAdapterPosition()).getCurrency() + "-" + activityList.get(holder.getAdapterPosition()).getStarting_from_price());


        Glide.with(mContext)
                .load(sessionManager.getCityImage())
                //.load(activityList.get(holder.getAdapterPosition()).getFeatured_image())
                .placeholder(R.drawable.logo)
                //.asBitmap()
                .error(R.drawable.logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(R.anim.fade_in)
                // .centerCrop()
                //.override(120, 50)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                       // holder.progressBar.setVisibility(View.GONE);
                        holder.iv_image.setImageResource(R.drawable.logo);
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.iv_image);





        holder.ll_content_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsTracker.isNetworkAvailable()) {
                    Intent intent = new Intent(mContext, ActivitiesDetailsActivity.class);
                    intent.putExtra("EventID", activityList.get(holder.getAdapterPosition()).getTour_id());
                    intent.putExtra("EventName", activityList.get(holder.getAdapterPosition()).getName());
                    intent.putExtra("Image", activityList.get(holder.getAdapterPosition()).getFeatured_image());
                    intent.putExtra("ActivityListModel", (ArrayList<ActivitiesListModel>) activityList);
                    intent.putExtra("Position",position);
                    mContext.startActivity(intent);
                } else {
                    Snackbar.make(vv, "Please Check Internet Connection !", Snackbar.LENGTH_LONG).show();
                }

            }
        });

        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityList.get(holder.getAdapterPosition()).getSelected()) {
                    holder.iv_like.setImageResource(R.drawable.ic_unlike);
                    activityList.get(holder.getAdapterPosition()).setSelected(false);
                }
                else {
                    holder.iv_like.setImageResource(R.drawable.ic_like);
                    activityList.get(holder.getAdapterPosition()).setSelected(true);
                }

            }
        });


        if (activityList.get(holder.getAdapterPosition()).getSelected()) {
            holder.iv_like.setImageResource(R.drawable.ic_like);
        }
        else {
            holder.iv_like.setImageResource(R.drawable.ic_unlike);

        }
    }

    @Override
    public int getItemCount() {
        return activityList.size();

    }

}