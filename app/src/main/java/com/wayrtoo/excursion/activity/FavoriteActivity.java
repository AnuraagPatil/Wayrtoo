package com.wayrtoo.excursion.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.util.FontsOverride;

import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        mContext = this;
        ButterKnife.bind(this);
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");
    }
}
