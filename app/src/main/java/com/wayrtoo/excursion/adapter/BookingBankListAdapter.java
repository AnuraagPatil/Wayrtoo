package com.wayrtoo.excursion.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.models.BankListModel;

import java.util.ArrayList;

/**
 * Created by Ishwar on 30/12/2017.
 */

public class BookingBankListAdapter extends RecyclerView.Adapter<BookingBankListAdapter.MyViewHolder> {
    Context context;
    ArrayList<BankListModel> cityListModels=new ArrayList<>();
    LayoutInflater inflter;

        public BookingBankListAdapter(Context applicationContext, ArrayList<BankListModel> listModels) {
        this.context = applicationContext;
        this.cityListModels = listModels;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_bank_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_bank_name.setText(cityListModels.get(holder.getAdapterPosition()).getBank_name());
    }

    @Override
    public int getItemCount() {
        return cityListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_bank_name;

        private LinearLayout ll_content_click;
        public MyViewHolder(View view) {
            super(view);
            tv_bank_name = (TextView) view.findViewById(R.id.tv_bank_name);


        }
    }
}