package com.wayrtoo.excursion.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.activity.ActivitiesAllListsActivity;
import com.wayrtoo.excursion.activity.ViewAllMapActivity;
import com.wayrtoo.excursion.adapter.ActivitiesFiveListFragmentAdapter;
import com.wayrtoo.excursion.models.ActivitiesListModel;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.GPSTracker;
import com.wayrtoo.excursion.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
  Created by Ishwar on 20/12/17.
 */
public class ActivityFragment extends Fragment implements LocationListener {


	@BindView(R.id.show_details)
	RelativeLayout show_details;

	@BindView(R.id.city_background)
	ImageView city_background;

	@BindView(R.id.txt_title)
	TextView txt_title;

	@BindView(R.id.txt_title_sub)
	TextView txt_title_sub;

	@BindView(R.id.rv_list)
	RecyclerView rv_ticket_list;

	@BindView(R.id.show_all_list)
	CardView show_all_list;

	@BindView(R.id.total_list_size)
	TextView total_list_size;

	@BindView(R.id.show_all_map)
	CardView show_all_map;

	@BindView(R.id.total_map_size)
	TextView total_map_size;

	@BindView(R.id.mapView)
	MapView mMapView;

	private Context mContext;
	private double latitude, longitude;
	private LatLng sydney;
	private MarkerOptions markerOptions;
	private GoogleMap googleMap;
	private IconGenerator iconFactory;
	private FrameLayout fragmentContainer;
	private SessionManager sessionManager;
	private GPSTracker gpsTracker;
	private ActivitiesFiveListFragmentAdapter activitiesFiveListFragmentAdapter;
	private ArrayList<ActivitiesListModel> activitiesListModels;
	private int Count = 0;
	private String ResponceData = "";

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_activity, container, false);
		ButterKnife.bind(this, view);
		mContext = getContext();
		sessionManager = new SessionManager(mContext);
		gpsTracker = new GPSTracker(mContext, getActivity());
		gpsTracker.getLocation();
		rv_ticket_list.setNestedScrollingEnabled(false);
		mMapView.setNestedScrollingEnabled(false);
		initDemoList(view);
		willBeDisplayed();
		FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");


		new LoadAllData().execute();

		Glide.with(mContext)
				.load(sessionManager.getCityImage())
				.asBitmap()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.animate(R.anim.fade_in)
				.centerCrop()
				.into(city_background);

		show_details.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ActivitiesAllListsActivity.class);
				intent.putExtra("ListModels", (ArrayList<ActivitiesListModel>) activitiesListModels);
				startActivity(intent);
			}
		});

		show_all_list.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ActivitiesAllListsActivity.class);
				intent.putExtra("ListModels", (ArrayList<ActivitiesListModel>) activitiesListModels);
				startActivity(intent);
			}
		});

		show_all_map.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ViewAllMapActivity.class);
				intent.putExtra("ListModels", (ArrayList<ActivitiesListModel>) activitiesListModels);
				startActivity(intent);
			}
		});


		try {
			latitude = gpsTracker.getLatitude();
			longitude = gpsTracker.getLongitude();
			mMapView.onCreate(savedInstanceState);
			mMapView.onResume(); // needed to get the map to display immediately


			//final LatLng currentPos = new LatLng(latitude, longitude);
			final LatLng currentPos = new LatLng(Double.valueOf(activitiesListModels.get(0).getLatitude()),Double.valueOf(activitiesListModels.get(0).getLongitude()));
			mMapView.getMapAsync(new OnMapReadyCallback() {
				@Override
				public void onMapReady(GoogleMap mMap) {
					googleMap = mMap;
					googleMap.clear();
					googleMap.getUiSettings().setScrollGesturesEnabled(false);
					googleMap.getUiSettings().setZoomGesturesEnabled(true);
					iconFactory = new IconGenerator(getActivity());
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
							markerOptions.icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_pined));
							googleMap.addMarker(markerOptions);

						}
					}


					/*LatLng sydney = new LatLng(latitude, longitude);
                    markerOptions = new MarkerOptions().position(sydney);
					markerOptions.title(getAddress(latitude,longitude));
					markerOptions.icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_pined));
					googleMap.addMarker(markerOptions);
					CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPos).zoom(15).build();
					googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
				}
			});

		} catch (Exception e) {
		}

		return view;
	}

	private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
		Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
		vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
		Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		vectorDrawable.draw(canvas);
		return BitmapDescriptorFactory.fromBitmap(bitmap);
	}

	private String getAddress(double latitude, double longitude) {
		StringBuilder result = new StringBuilder();
		try {
			Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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

	private void initDemoList(View view) {
		fragmentContainer = (FrameLayout) view.findViewById(R.id.fragment_container);
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
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
	public void onStatusChanged(String s, int i, Bundle bundle) {
	}

	@Override
	public void onProviderEnabled(String s) {
	}

	@Override
	public void onProviderDisabled(String s) {
	}

	private class LoadAllData extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			if (sessionManager.getCityId().equalsIgnoreCase("5")) {
				ResponceData = sessionManager.getCityAbuDhabi();
			} else if (sessionManager.getCityId().equalsIgnoreCase("6")) {
				ResponceData = sessionManager.getCityDubai();
			} else if (sessionManager.getCityId().equalsIgnoreCase("10")) {
				ResponceData = sessionManager.getCityBali();
			} else if (sessionManager.getCityId().equalsIgnoreCase("11")) {
				ResponceData = sessionManager.getCitySingapore();
			} else if (sessionManager.getCityId().equalsIgnoreCase("16")) {
				ResponceData = sessionManager.getCityBangkok();
			} else if (sessionManager.getCityId().equalsIgnoreCase("18")) {
				ResponceData = sessionManager.getCityHongKong();
			} else if (sessionManager.getCityId().equalsIgnoreCase("22")) {
				ResponceData = sessionManager.getCityKaulaLumpur();
			} else if (sessionManager.getCityId().equalsIgnoreCase("27")) {
				ResponceData = sessionManager.getCityLangkawi();
			} else if (sessionManager.getCityId().equalsIgnoreCase("28")) {
				ResponceData = sessionManager.getCityPenang();
			}

			Log.e("Activity Session Res-->", ResponceData);

			if(ResponceData!=null && !ResponceData.equalsIgnoreCase("")){
				try {
					JSONArray jsonArrayActivity = new JSONArray(ResponceData);
					activitiesListModels = new ArrayList<ActivitiesListModel>();
					Count = jsonArrayActivity.length();

					for (int i = 0; jsonArrayActivity.length() > i; i++) {

						ActivitiesListModel listModel = new ActivitiesListModel();
						JSONObject jsonActivity = jsonArrayActivity.getJSONObject(i);
						listModel.setTour_id(jsonActivity.getString("tour_id"));
						listModel.setAddress(jsonActivity.getString("address"));
						listModel.setCategory(jsonActivity.getString("category"));
						listModel.setCity(jsonActivity.getString("city"));
						listModel.setCurrency(jsonActivity.getString("currency"));

						JSONObject jsonDuration = jsonActivity.getJSONObject("duration");
						listModel.setDay(jsonDuration.getString("day"));
						listModel.setHour(jsonDuration.getString("hour"));
						listModel.setMin(jsonDuration.getString("min"));


						listModel.setCategory(jsonActivity.getString("featured_image"));
						listModel.setName(jsonActivity.getString("name"));
						listModel.setRating(jsonActivity.getString("rating"));
						listModel.setShort_description(jsonActivity.getString("short_description"));
						listModel.setStarting_from_price(jsonActivity.getString("starting_from_price"));
						listModel.setLatitude(jsonActivity.getString("latitude"));
						listModel.setLongitude(jsonActivity.getString("longitude"));

						activitiesListModels.add(listModel);
					}


				} catch (JSONException e) {
					Snackbar.make(getActivity().findViewById(R.id.fragment_container), "No Record Found ,Please Check Internet Connection!", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
					e.printStackTrace();
				}
			}else{
				changeFragment(R.id.fragment_container, new NoInternetFragment());
				Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Please Check Internet Connection!", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}


			return "Executed";
		}


		@Override
		protected void onPostExecute(String result) {

			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
			rv_ticket_list.setLayoutManager(mLayoutManager);
			activitiesFiveListFragmentAdapter = new ActivitiesFiveListFragmentAdapter(mContext, activitiesListModels,getActivity());
			rv_ticket_list.setAdapter(activitiesFiveListFragmentAdapter);

			total_list_size.setText(Count + " result for " + sessionManager.getCityName());
			txt_title_sub.setText(Count + " result for Activities");
			total_map_size.setText( Count + " map for " + sessionManager.getCityName());
		}


	}

	public void changeFragment(int id, Fragment fragment) {
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(id, fragment);
		fragmentTransaction.addToBackStack("" + fragment.getClass().getName());
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void willBeDisplayed() {
		if (fragmentContainer != null) {
			Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
			fragmentContainer.startAnimation(fadeIn);
		}
	}

}
