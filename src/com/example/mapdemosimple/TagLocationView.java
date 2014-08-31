package com.example.mapdemosimple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helper.DataBaseHelp;
import com.helper.RequestReceiver;
import com.helper.WebserviceHelper;

public class TagLocationView extends Activity implements RequestReceiver {
	GoogleMap map;
	String getlat, getlng;
	String Address;
	// Double Latdouble, Lngdouble;
	FrameLayout frame_layout;
	ListView listViewTag;
	Button btn_tagList, btn_mapResults;

	RelativeLayout loader1;

	DataBaseHelp objDBHelp = new DataBaseHelp(this);
	ArrayList<Double> LatList = new ArrayList<Double>();
	ArrayList<Double> LongList = new ArrayList<Double>();
	ArrayList<String> TimeList = new ArrayList<String>();
	ArrayList<String> AddressList = new ArrayList<String>();

	String OpId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_location_view);
		map = ((MapFragment) this.getFragmentManager().findFragmentById(
				R.id.mapTAg)).getMap();

		initViews();
		frame_layout.setVisibility(View.INVISIBLE);
		listViewTag.setVisibility(View.VISIBLE);
		initData();

	}

	public void mapResults(View v) {

		btn_tagList.setBackgroundResource(R.drawable.result_unselected_btn);
		btn_mapResults.setBackgroundResource(R.drawable.result_selected_btn);
		frame_layout.setVisibility(View.VISIBLE);
		listViewTag.setVisibility(View.INVISIBLE);

	}

	public void tagList(View v) {

		btn_tagList.setBackgroundResource(R.drawable.result_selected_btn);
		btn_mapResults.setBackgroundResource(R.drawable.result_unselected_btn);
		frame_layout.setVisibility(View.INVISIBLE);
		listViewTag.setVisibility(View.VISIBLE);
	}

	private void SetList() {
		// TODO Auto-generated method stub

		MyAdapterCustom myadap = new MyAdapterCustom(this);
		listViewTag.setAdapter(myadap);

	}

	private void initViews() {
		// TODO Auto-generated method stub
		frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
		listViewTag = (ListView) findViewById(R.id.listViewTag);
		btn_tagList = (Button) findViewById(R.id.btn_tagList);
		btn_mapResults = (Button) findViewById(R.id.btn_mapResults);

		loader1 = (RelativeLayout) findViewById(R.id.loader1);

	}

	public class MyAdapterCustom extends BaseAdapter {
		Context context;
		LayoutInflater li;

		public MyAdapterCustom(Context _context) {
			context = _context;
			li = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		public int getCount() {
			return LatList.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		@SuppressLint("DefaultLocale")
		public View getView(final int position, View view, ViewGroup parent) {
			String address = null;

			LayoutInflater inflate = ((Activity) context).getLayoutInflater();
			view = inflate.inflate(R.layout.text_time_setting, null);
			TextView txtAddress = (TextView) view
					.findViewById(R.id.txtLocation);

			txtAddress.setText(AddressList.get(position));
			TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
			txtTime.setText(TimeList.get(position));

			// if (AddressList != null) {
			// address = AddressList.get(position);
			//
			// if (address != null && address.length() > 0) {
			// // address = address.replaceAll(" ", "");
			// txtAddress.setText(address);
			// }
			// }
			//
			// txtDate.setText(TimeList.get(position));

			return view;
		}
	}

	private void initData() {
		// TODO Auto-generated method stub

		Intent i1 = getIntent();
		OpId = i1.getStringExtra("ID");
		System.out.println("IIIIIIIIII" + OpId);
		// http://www.apmocon.com/demos/mobapp_functions.php?action=list_location&user_id=1&datakey=b629d485a61cehjuy7af4911b722d3

		loader1.setVisibility(View.VISIBLE);
		WebserviceHelper helper = new WebserviceHelper(this);
		helper.addParam("action", "list_location");
		helper.addParam("user_id", OpId);
		helper.addParam("datakey", "b629d485a61cehjuy7af4911b722d3");
		helper.execute(helper.buildUrl());

		// Cursor c1 = objDBHelp.Get_Curser_of_latlong();
		// LatList.clear();
		// LongList.clear();
		// TimeList.clear();
		// AddressList.clear();
		// if (c1.moveToFirst()) {
		//
		// do {
		// LatList.add(c1.getString(1));
		// LatDouble.add(Double.valueOf(c1.getString(1)));
		//
		// LongList.add(c1.getString(2));
		// LongDouble.add(Double.valueOf(c1.getString(2)));
		//
		// AddressList.add(c1.getString(3));
		// TimeList.add(c1.getString(4));
		//
		// } while (c1.moveToNext());
		// }
		//
		// System.out.println("TTTTTTTTTTT" + TimeList.get(0) + "--"
		// + AddressList.get(0));
	}

	private void SetMap() {

		// Time dtNow = new Time();
		// dtNow.setToNow();
		// String lsNow = dtNow.format("%H:%M:%S");

		map.clear();

		NetReachability net = new NetReachability(this);
		boolean netcheck = net.isInternetOn();
		System.out.println("ssssssnetcheck =" + netcheck);
		if (netcheck == false) {
			Toast.makeText(getApplicationContext(),
					"No network connection Please tray again later",
					Toast.LENGTH_SHORT).show();
			this.finish();
		} else if (netcheck == true) {

			try {

				Double camaracenterLat = 0.0, camaracenterLong = 0.0;
				for (int k = 0; k < LatList.size(); k++) {
					camaracenterLat = camaracenterLat + LatList.get(k);

				}
				camaracenterLat = (camaracenterLat)
						/ (Double.valueOf(LatList.size()));

				for (int l = 0; l < LongList.size(); l++) {
					camaracenterLong = camaracenterLong + LongList.get(l);

				}
				camaracenterLong = (camaracenterLong)
						/ (Double.valueOf(LongList.size()));

				CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
						camaracenterLat, camaracenterLong));
				map.moveCamera(center);

				for (int mk = 0; mk < LatList.size(); mk++) {
					map.addMarker(new MarkerOptions()
							.position(
									new LatLng(LatList.get(mk), LongList
											.get(mk)))
							.title(AddressList.get(mk))
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
				}

				map.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);

				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

				map.setMyLocationEnabled(true);
				map.getMyLocation();
				map.getUiSettings().setZoomControlsEnabled(true);
				map.getUiSettings().setCompassEnabled(true);

				map.getUiSettings().setMyLocationButtonEnabled(true);
				map.getUiSettings().setTiltGesturesEnabled(true);
				map.getUiSettings().setScrollGesturesEnabled(true);

			} catch (Exception e) {
				Log.e("Show mapview failed =", e.getMessage());
			}
		}
	}

	public String getAddressFromLocation(double lat, double lon) {
		String add = "";
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(lat, lon, 1);

			if (addresses.size() > 0) {

				System.out.println("KKKKKKKKKK==" + add);
				for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)

					add = addresses.get(0).getFeatureName() + " , "
							+ addresses.get(0).getLocality() + " , "
							+ addresses.get(0).getAdminArea() + " , "
							+ addresses.get(0).getCountryName();

			}

			System.out.println("KKKKKKKKKK==" + add);
			return add;
			// Toast.makeText(context, add, Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			System.out.println("BBBBBBBBBBBBBB" + e.getMessage());
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public void requestFinished(String result) {
		// TODO Auto-generated method stub

		System.out.println("AAAAAASSSSSSSSSSS" + result);
		// {"W":[{"Status":"true"},{"location_id":"16","latitude":"22.7222385","longitude":"75.9194672","tagged_time":"2014-05-26 00:00:00","user_id":"1"}]}

		try {
			JSONObject jsonObject = new JSONObject(result);
			final JSONArray res = jsonObject.getJSONArray("W");
			String status = res.getJSONObject(0).getString("Status");

			System.out.println("AAAAAASSSSSSSSSSS-" + status);
			if (status.equals("false")) {

				loader1.setVisibility(View.GONE);

				Toast.makeText(getApplicationContext(), "No data tagged", 1500)
						.show();
			} else if (status.equals("true")) {

				LatList.clear();
				LongList.clear();
				AddressList.clear();
				TimeList.clear();
				JSONObject jObje;

				for (int k = 1; k < res.length(); k++) {
					//
					jObje = res.getJSONObject(k);
					LatList.add(Double.valueOf(jObje.getString("latitude")));
					LongList.add(Double.valueOf(jObje.getString("longitude")));
					AddressList.add(getAddressFromLocation(
							Double.valueOf(jObje.getString("latitude")),
							Double.valueOf(jObje.getString("longitude"))));
					TimeList.add(jObje.getString("tagged_time"));
				}

				loader1.setVisibility(View.GONE);
				SetList();
				SetMap();

			}

		} catch (JSONException e) {
			loader1.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(),
					"Error in server please try again", 1500).show();

		} catch (Exception e) {

			loader1.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(),
					"Error in network connection please try again later", 1500)
					.show();
		}

	}

	// @Override
	// public void onMapLongClick(LatLng onjLTLG) {
	//
	// map.addMarker(new MarkerOptions()
	// .position(onjLTLG)
	// .title("You are here")
	// .icon(BitmapDescriptorFactory
	// .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
	// double lat = onjLTLG.latitude;
	// double lng = onjLTLG.longitude;
	//
	// Toast.makeText(getApplicationContext(), lat + "---" + lng, 1500).show();
	//
	// }

}
