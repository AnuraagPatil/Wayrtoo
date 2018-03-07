package com.wayrtoo.excursion.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.models.ActivitiesListModel;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.GPSTracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAllMapActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback{

    @BindView(R.id.tv_back)
    TextView tv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private Context mContext;
    private double latitude, longitude;
    private MarkerOptions markerOptions;
    private IconGenerator iconFactory;
    private GPSTracker gpsTracker;
    SupportMapFragment mMapView;
    private ArrayList<ActivitiesListModel> activitiesListModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        mContext = this;
        ButterKnife.bind(this);
        gpsTracker = new GPSTracker(mContext, ViewAllMapActivity.this);
        gpsTracker.getLocation();
        Bundle b = getIntent().getExtras();
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");
        tv_title.setText("Map Details");

        if(b!=null){
            activitiesListModels =(ArrayList<ActivitiesListModel>)getIntent().getSerializableExtra("ListModels");
        }else {
            Snackbar.make(findViewById(R.id.mapView), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
        }
         mMapView = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mMapView.getMapAsync(this);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        try {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            mMapView.onResume();
            final LatLng currentPos = new LatLng(Double.valueOf(activitiesListModels.get(0).getLatitude()),Double.valueOf(activitiesListModels.get(0).getLongitude()));
                    googleMap.clear();
                    iconFactory = new IconGenerator(ViewAllMapActivity.this);
                    LatLng sydney = new LatLng(latitude, longitude);
                    String add = getAddress(latitude, longitude);
                    iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                    markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(add))).position(sydney).anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
                    googleMap.addMarker(markerOptions);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPos).zoom(10).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    if (activitiesListModels != null && activitiesListModels.size() > 0) {
                        for (int j = 0; j < activitiesListModels.size(); j++) {
                            LatLng sydney1 = new LatLng(Double.valueOf(activitiesListModels.get(j).getLatitude()), Double.valueOf(activitiesListModels.get(j).getLongitude()));
                            markerOptions = new MarkerOptions().position(sydney1);
                            markerOptions.title(activitiesListModels.get(j).getName());
                            markerOptions.icon(bitmapDescriptorFromVector(ViewAllMapActivity.this, R.drawable.ic_pined));
                            googleMap.addMarker(markerOptions);
                        }
                    }




            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {

                    for (int i = 0; i < activitiesListModels.size(); i++) {
                        if (arg0.getTitle().equals(activitiesListModels.get(i).getName())) {
                            if(gpsTracker.isNetworkAvailable()){
                                Intent intent = new Intent(ViewAllMapActivity.this, ActivitiesDetailsActivity.class);
                                intent.putExtra("EventID", activitiesListModels.get(i).getTour_id());
                                intent.putExtra("EventName", activitiesListModels.get(i).getName());
                                intent.putExtra("Image", activitiesListModels.get(i).getFeatured_image());
                                startActivity(intent);
                            }else{
                                Snackbar.make(findViewById(R.id.mapView), "Please Check Internet Connection !", Snackbar.LENGTH_LONG).show();
                            }

                        }
                    }
                }
            });


					/*LatLng sydney = new LatLng(latitude, longitude);
                    markerOptions = new MarkerOptions().position(sydney);
					markerOptions.title(getAddress(latitude,longitude));
					markerOptions.icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_pined));
					googleMap.addMarker(markerOptions);
					CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPos).zoom(15).build();
					googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        } catch (Exception e) {
        }
    }


    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ViewAllMapActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                // result.append(address.getAddressLine(1)).append("\n");
                result.append(address.getLocality() + " " + address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mMapView.onResume();
        }catch (Exception e){

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            mMapView.onPause();
        }catch (Exception e){
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
