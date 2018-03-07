package com.wayrtoo.excursion.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.models.ActivitiesCancellatioPolicyModel;

import java.util.ArrayList;

/**
 * Created by Ishwar on 30/12/2017.
 */

public class CancellationPolicyAdapter extends RecyclerView.Adapter<CancellationPolicyAdapter.MyViewHolder> {
    Context context;
    ArrayList<ActivitiesCancellatioPolicyModel> cityListModels=new ArrayList<>();
    LayoutInflater inflter;

        public CancellationPolicyAdapter(Context applicationContext, ArrayList<ActivitiesCancellatioPolicyModel> listModels) {
        this.context = applicationContext;
        this.cityListModels = listModels;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cancellation_policy, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_cancellation_hours.setText(cityListModels.get(holder.getAdapterPosition()).getFrom_hr()+" - "+cityListModels.get(holder.getAdapterPosition()).getTo_hr()+" hrs");
        if(cityListModels.get(holder.getAdapterPosition()).getChargeable_by_percentage().equalsIgnoreCase("true")){
            holder.tv_cancellation_percentage.setText(cityListModels.get(holder.getAdapterPosition()).getCharge()+" % refund");
        }else{
            holder.tv_cancellation_percentage.setText(cityListModels.get(holder.getAdapterPosition()).getCharge());
        }


    }

    @Override
    public int getItemCount() {
        return cityListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_cancellation_hours,tv_cancellation_percentage;

        private LinearLayout ll_content_click;
        public MyViewHolder(View view) {
            super(view);
            tv_cancellation_hours = (TextView) view.findViewById(R.id.tv_cancellation_hours);
            tv_cancellation_percentage = (TextView) view.findViewById(R.id.tv_cancellation_percentage);


        }
    }
}