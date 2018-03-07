package com.wayrtoo.excursion.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.adapter.SelectCityListAdapter;
import com.wayrtoo.excursion.models.CityListModel;
import com.wayrtoo.excursion.util.FontsOverride;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectCityListActivity extends AppCompatActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rv_list)
    RecyclerView rv_flight_list;

    @BindView(R.id.search_view)
    SearchView simpleSearchView;

    private ArrayList<CityListModel> cancellatioPolicyModels;
    private SelectCityListAdapter selectCityListAdapter;
    private boolean flag=true;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_trip_flight_list);
        ButterKnife.bind(this);
        mContext = this;
        tv_title.setText("Select City");
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");

        Bundle bundle = new Bundle();
        if(bundle!=null){
               cancellatioPolicyModels = (ArrayList<CityListModel>) getIntent().getExtras().getSerializable("City List");
        }


        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String text = newText;
               selectCityListAdapter.filter(text);

                return false;
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rv_flight_list.setLayoutManager(mLayoutManager);
        selectCityListAdapter = new SelectCityListAdapter(mContext, cancellatioPolicyModels,SelectCityListActivity.this);
        rv_flight_list.setAdapter(selectCityListAdapter);

    }

}
