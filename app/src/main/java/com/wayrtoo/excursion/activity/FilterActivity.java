package com.wayrtoo.excursion.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_Filter_Cancel)
    TextView tv_Filter_Cancel;

    @BindView(R.id.tv_Filter_Reset)
    TextView tv_Filter_Reset;

    @BindView(R.id.rangeSeekbar)
    CrystalRangeSeekbar rangeSeekbar;

    @BindView(R.id.textMin)
    TextView textMin;

    @BindView(R.id.textMax)
    TextView textMax;

    @BindView(R.id.tv_Apply_Filter)
    TextView tv_Apply_Filter;

    private Context mContext;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mContext = this;
        ButterKnife.bind(this);
        sessionManager = new SessionManager(mContext);
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Double min = new Double(Double.valueOf(sessionManager.getFilterMin()));
        int SeekMin = min.intValue();
        rangeSeekbar.setLeft(SeekMin);

        Double max  = new Double(Double.valueOf(sessionManager.getFilterMax()));
        int SeekMax = max.intValue();
        rangeSeekbar.setRight(SeekMax);

        tv_Filter_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                sessionManager.setFilterMin("0");
                sessionManager.setFilterMax("5000");
            }
        });

        tv_Filter_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setFilterMin("0");
                sessionManager.setFilterMax("5000");
            }
        });

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                textMin.setText(String.valueOf(minValue));
                textMax.setText(String.valueOf(maxValue));
                sessionManager.setFilterMin(String.valueOf(minValue));
                sessionManager.setFilterMax(String.valueOf(maxValue));

            }
        });

        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.e("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));

            }
        });

        tv_Apply_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }
}
