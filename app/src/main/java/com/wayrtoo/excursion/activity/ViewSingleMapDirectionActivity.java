package com.wayrtoo.excursion.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.models.ActivitiesDetailsModel;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.GPSTracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewSingleMapDirectionActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback{

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
    private GoogleMap googleMap;
    private LatLng sydney, sydney1;
    private ArrayList<ActivitiesDetailsModel> activitiesDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        mContext = this;
        ButterKnife.bind(this);
        gpsTracker = new GPSTracker(mContext, ViewSingleMapDirectionActivity.this);
        gpsTracker.getLocation();
        Bundle b = getIntent().getExtras();
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");

        tv_title.setText("Map Details");

        if(b!=null){
            activitiesDetailsModel =(ArrayList<ActivitiesDetailsModel>)getIntent().getSerializableExtra("ListDetailModels");
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
    public void onMapReady( GoogleMap googleMap) {
        try {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            //mMapView.onCreate();
            googleMap=googleMap;
            mMapView.onResume(); // needed to get the map to display immediately
            //final LatLng currentPos = new LatLng(latitude, longitude);
            final LatLng currentPos = new LatLng(Double.valueOf(activitiesDetailsModel.get(0).getLatitude()),Double.valueOf(activitiesDetailsModel.get(0).getLongitude()));

                    googleMap.clear();
                    iconFactory = new IconGenerator(ViewSingleMapDirectionActivity.this);
                    sydney = new LatLng(latitude, longitude);
                    String add = getAddress(latitude, longitude);
                    iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                    markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Your Location"))).position(sydney).anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
                    googleMap.addMarker(markerOptions);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPos).zoom(10).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    if (activitiesDetailsModel != null && activitiesDetailsModel.size() > 0) {
                        for (int j = 0; j < activitiesDetailsModel.size(); j++) {

                            sydney1 = new LatLng(Double.valueOf(activitiesDetailsModel.get(j).getLatitude()), Double.valueOf(activitiesDetailsModel.get(j).getLongitude()));
                            markerOptions = new MarkerOptions().position(sydney1);
                            markerOptions.title(activitiesDetailsModel.get(j).getName());
                            markerOptions.icon(bitmapDescriptorFromVector(ViewSingleMapDirectionActivity.this, R.drawable.ic_pined));
                            googleMap.addMarker(markerOptions);
                        }
                    }

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {

                    for (int i = 0; i < activitiesDetailsModel.size(); i++) {
                        if (arg0.getTitle().equals(activitiesDetailsModel.get(i).getName())) {

                            final Dialog dialogMsg = new Dialog(ViewSingleMapDirectionActivity.this);
                            dialogMsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogMsg.setContentView(R.layout.event_detail_map_dialog);
                            dialogMsg.setCancelable(true);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialogMsg.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.gravity = Gravity.CENTER;
                            dialogMsg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dialogMsg.getWindow().setAttributes(lp);
                            dialogMsg.show();
                            CalculationByDistance(sydney, sydney1);
                            String Distance = String.format("%.2f", CalculationByDistance(sydney, sydney1));

                            TextView tvName = (TextView) dialogMsg.findViewById(R.id.tvName);
                            TextView tvServiceAdd = (TextView) dialogMsg.findViewById(R.id.tvServiceAdd);
                            TextView tvDistance = (TextView) dialogMsg.findViewById(R.id.tvDistance);
                            CardView cardViewCancel = (CardView) dialogMsg.findViewById(R.id.cardViewCancel);

                            tvName.setText(activitiesDetailsModel.get(0).getName());
                            tvServiceAdd.setText(activitiesDetailsModel.get(0).getAddress());
                            tvDistance.setText(Distance + " Km");

                            cardViewCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogMsg.cancel();
                                }
                            });

                        }
                    }
                }
            });



            String url = getMapsApiDirectionsUrl(sydney, sydney1);
            ReadTask downloadTask = new ReadTask();
            downloadTask.execute(url);


        } catch (Exception e) {
        }
    }



    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ViewSingleMapDirectionActivity.this, Locale.getDefault());
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


    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.e("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private String getMapsApiDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;

    }

    private class ReadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String data = "";
            try {
                MapHttpConnection http = new MapHttpConnection();
                data = http.readUr(url[0]);


            } catch (Exception e) {
                // TODO: handle exception
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }

    }

    public class MapHttpConnection {
        public String readUr(String mapsApiDirectionsUrl) throws IOException {
            String data = "";
            InputStream istream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(mapsApiDirectionsUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                istream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(istream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();


            } catch (Exception e) {
                Log.d("Exception reading url", e.toString());
            } finally {
                istream.close();
                urlConnection.disconnect();
            }
            return data;

        }
    }

    public class PathJSONParser {

        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            try {
                jRoutes = jObject.getJSONArray("routes");
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);
                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat",
                                        Double.toString(((LatLng) list.get(l)).latitude));
                                hm.put("lng",
                                        Double.toString(((LatLng) list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;

        }

        private List<LatLng> decodePoly(String encoded) {
            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            // TODO Auto-generated method stub
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {

            try{
                ArrayList<LatLng> points = null;
                PolylineOptions polyLineOptions = null;

                // traversing through routes
                if(routes.size()>0){
                    for (int i = 0; i < routes.size(); i++) {
                        points = new ArrayList<LatLng>();
                        polyLineOptions = new PolylineOptions();
                        List<HashMap<String, String>> path = routes.get(i);

                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }

                        polyLineOptions.addAll(points);
                        polyLineOptions.width(7);
                        polyLineOptions.color(Color.BLUE);
                        // polyLineOptions.clickable(true);
                    }
                }
                googleMap.addPolyline(polyLineOptions);
            }catch (Exception e){
                Snackbar.make(findViewById(R.id.mapView), "     Sorry , Route Not Found", Snackbar.LENGTH_LONG).show();
            }

        }
    }



}
