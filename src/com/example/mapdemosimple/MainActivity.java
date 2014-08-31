package com.example.mapdemosimple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.authorization.LoginForm;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helper.DataBaseHelp;
import com.helper.RequestReceiver;
import com.helper.WebserviceHelper;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements LocationListener ,
		RequestReceiver {
	LocationManager locationManager;
	boolean isNetworkEnabled;
	GoogleMap map;
	int check1 = 0;

	Location locationnew;
	Timer mytimer = null;
	boolean isGPSEnabled;
	Location locationfommethod;
	TextView refresh_time;
	double newCurrentlatitude = 0.0, newCurrentlongitude;
	String frm;
	Button btnTagMe, btnshowMe;
	DataBaseHelp objDBHelp = new DataBaseHelp(this);
	ArrayList<String> LatList = new ArrayList<String>();
	ArrayList<String> LongList = new ArrayList<String>();
	ArrayList<String> TimeList = new ArrayList<String>();
	ArrayList<String> AddressList = new ArrayList<String>();
	RelativeLayout loader1;
	int ServiceFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		intiviews();
		SetListener();
		// SetMap();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void intiviews() {
		// TODO Auto-generated method stub

		loader1 = (RelativeLayout) findViewById(R.id.loader1);

		refresh_time = (TextView) findViewById(R.id.refresh_time);
		map = ((MapFragment) this.getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		btnTagMe = (Button) findViewById(R.id.btnTagMe);
		btnshowMe = (Button) findViewById(R.id.btnshowMe);

	}

	private void SetListener() {
		// TODO Auto-generated method stub

		btnTagMe.setOnClickListener(layoutListener);
		btnshowMe.setOnClickListener(layoutListener);
	}

	OnClickListener layoutListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// Auto-generated method stub
			switch (v.getId()) {

			case R.id.timer_button: {
				// check1++;
				// mytimer.cancel();
				// mytimer = null;
				Intent in = new Intent(MainActivity.this, TimeInterval.class);
				startActivity(in);

			}
				break;
			case R.id.btnTagMe: {

				TagMeMethod();
			}
				break;

			case R.id.btnshowMe: {

				// ShowLocation();
				SetUsers();
			}
				break;

			default: {

			}
				break;

			}
		}

	};

	private void TagMeMethod() {
		// TODO Auto-generated method stub

		GetLatLongnew();

		if (newCurrentlatitude == 0.0 && newCurrentlongitude == 0.0) {

		} else {

			// http://www.apmocon.com/demos/mobapp_functions.php?action=insert_location&user_id=1&latitude=121&longitude=23&datakey=b629d485a61cehjuy7af4911b722d3
			ServiceFlag = 0;
			String LatString = String.valueOf(newCurrentlatitude);
			String LonString = String.valueOf(newCurrentlongitude);
			loader1.setVisibility(View.VISIBLE);
			WebserviceHelper helper = new WebserviceHelper(this);
			helper.addParam("action", "insert_location");
			helper.addParam("user_id", LoginForm.My_Id);
			helper.addParam("latitude", LatString);
			helper.addParam("longitude", LonString);
			helper.addParam("datakey", "b629d485a61cehjuy7af4911b722d3");
			helper.execute(helper.buildUrl());

			// /// Save Lat long with address in DB
			// String LatString = String.valueOf(newCurrentlatitude);
			// String LonString = String.valueOf(newCurrentlongitude);
			// Time dtNow = new Time();
			// dtNow.setToNow();
			// String lsNow = dtNow.format("%H:%M:%S");
			//
			// String AddTosave = getAddressFromLocation(newCurrentlatitude,
			// newCurrentlongitude);
			//
			//
			// objDBHelp.AddLATLONG(LatString, LonString, lsNow, AddTosave);
			// Toast.makeText(getApplicationContext(),
			// "Your location has been tagged", Toast.LENGTH_SHORT).show();
		}

	}

	private void SetUsers() {
		// TODO Auto-generated method stub

		MyAdapterCustom myadap = new MyAdapterCustom(this);

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.place_list);
		final ListView lstobj = (ListView) dialog.findViewById(R.id.placesList);

		lstobj.setAdapter(myadap);
		lstobj.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				dialog.dismiss();
				loader1.setVisibility(View.VISIBLE);

				Intent in = new Intent(MainActivity.this, TagLocationView.class);
				in.putExtra("ID", LoginForm.User_ids.get(pos));
				startActivity(in);

			}
		});

		ImageButton btn_CancelR = (ImageButton) dialog
				.findViewById(R.id.btn_CancelR);
		btn_CancelR.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	@Override
	protected void onResume() {
		super.onResume();

		loader1.setVisibility(View.GONE);
		SetMap();
		// if (check1 == 0) {
		//
		// } else {
		//
		// if (mytimer == null) {
		// mytimer = new Timer();
		// } else {
		// mytimer.cancel();
		// mytimer = new Timer();
		// }
		//
		// mytimer.schedule(new TimerTask() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		//
		// TimerMethod();
		// check1++;
		// }
		//
		// }, 0, TimeInterval.selected * 1000);
	}

	// }

	// public void TimerMethod() {
	// this.runOnUiThread(time_pick);
	// }
	//
	// private Runnable time_pick = new Runnable() {
	// public void run() {
	// try {
	//
	// SetMap();
	//
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// };

	private void SetMap() {

		Time dtNow = new Time();
		dtNow.setToNow();
		String lsNow = dtNow.format("%H:%M:%S");
		refresh_time.setText(lsNow);
		if (check1 == 0) {
			System.out.println("TTT0000");
			map.clear();
			NetReachability net = new NetReachability(this);
			boolean netcheck = net.isInternetOn();
			System.out.println("ssssssnetcheck =" + netcheck);
			if (netcheck == false) {

				Toast.makeText(getApplicationContext(),
						"No network connection Please try again later",
						Toast.LENGTH_SHORT).show();
				this.finish();

				if (mytimer == null) {

				} else {
					mytimer.cancel();

				}

			} else if (netcheck == true) {

				try {

					CameraUpdate center = CameraUpdateFactory
							.newLatLng(new LatLng(
									SelectionActivity.Currentlatitude,
									SelectionActivity.Currentlongitude));
					map.moveCamera(center);

					map.animateCamera(CameraUpdateFactory.zoomTo(17), 3000,
							null);
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
		} else {

			System.out.println("TTT1111");
			map.clear();
			NetReachability net = new NetReachability(this);
			boolean netcheck = net.isInternetOn();
			System.out.println("ssssssnetcheck =" + netcheck);
			if (netcheck == false) {
				if (mytimer == null) {

				} else {
					mytimer.cancel();

				}
				Toast.makeText(getApplicationContext(),
						"No network connection Please tryagain later",
						Toast.LENGTH_SHORT).show();
				this.finish();

			} else if (netcheck == true) {

				try {
					System.out.println("TTT22222");
					GetLatLongnew();
					System.out.println("TTT33333");
					CameraUpdate center = CameraUpdateFactory
							.newLatLng(new LatLng(newCurrentlatitude,
									newCurrentlongitude));
					map.moveCamera(center);
					map
					.setOnMapLongClickListener(new OnMapLongClickListener() {

						@Override
						public void onMapLongClick(LatLng onjLTLG) {
							// TODO Auto-generated method stub

							double lat=onjLTLG.latitude;
							System.out.println("LLLT = "+lat+" =LG ="+onjLTLG.longitude);
						

						}
					});

					map.animateCamera(CameraUpdateFactory.zoomTo(17), 1500,
							null);
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

	}

	public void GetLatLongnew() {

		newCurrentlatitude = 0.0;
		newCurrentlongitude = 0.0;
		locationnew = getLocation();
		System.out.println("LLLLL111");
		if (locationnew == null) {
			Toast.makeText(getApplicationContext(),
					"Error in getting current locatoin", Toast.LENGTH_SHORT)
					.show();

		} else {
			newCurrentlatitude = locationnew.getLatitude();
			newCurrentlongitude = locationnew.getLongitude();

		}
	}

	public Location getLocation() {

		System.out.println("LLL" + SelectionActivity.selection);
		if (SelectionActivity.selection.equals("NETWORK")) {

			locationManager = (LocationManager) this
					.getSystemService(LOCATION_SERVICE);

			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (isNetworkEnabled == false) {

				Toast.makeText(
						getApplicationContext(),
						"No network connection Available please check your settings",
						Toast.LENGTH_SHORT).show();

			} else {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 0,
						(LocationListener) this);
				Log.d("Network", "Network");
				if (locationManager != null) {
					locationfommethod = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (locationfommethod != null) {

					}
				}
			}
		}

		if (SelectionActivity.selection.equals("GPS")) {

			locationManager = (LocationManager) this
					.getSystemService(LOCATION_SERVICE);

			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			System.out.println("DDDDDDDD" + isGPSEnabled);
			if (isGPSEnabled == false) {

				Toast.makeText(getApplicationContext(), "GPS turned off",
						Toast.LENGTH_SHORT).show();

				this.finish();
				showSettingsAlert();

			} else {

				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0,
						(LocationListener) this);

				if (locationManager != null) {
					locationfommethod = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (locationfommethod != null) {

					}
				}
			}
		}

		return locationfommethod;
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
			return LoginForm.User_Names.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		@SuppressLint("DefaultLocale")
		public View getView(final int position, View view, ViewGroup parent) {
			// String address = null;

			LayoutInflater inflate = ((Activity) context).getLayoutInflater();
			view = inflate.inflate(R.layout.text_setting_data, null);
			TextView txtAddress = (TextView) view
					.findViewById(R.id.txtLocation);

			if (LoginForm.User_Names != null) {

				txtAddress.setText(LoginForm.User_Names.get(position));

			}

			return view;
		}
	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.delete);

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Intent intent1 = new Intent();
						intent1.setClassName("com.android.settings",
								"com.android.settings.SecuritySettings");
						startActivity(intent1);

					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",

		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	public String getAddressFromLocation(double lat, double lon) {
		String add = "";

		System.out.println("KKKKKKKKKK==");
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(lat, lon, 1);

			if (addresses.size() > 0) {

				for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)

					// addresses.get(0).getSubThoroughfare()
					// add += addresses.get(0).getAddressLine(i) + "\n";
					add = addresses.get(0).getFeatureName() + " , "
							+ addresses.get(0).getLocality() + " , "
							+ addresses.get(0).getAdminArea() + " , "
							+ addresses.get(0).getCountryName();

			}
			System.out.println("KKKKKKKKK1111111==" + add);
			return add;
			// Toast.makeText(context, add, Toast.LENGTH_SHORT).show();
		} catch (IOException e) {

			e.printStackTrace();
			return "";
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mytimer == null) {

		} else {
			mytimer.cancel();

		}
		this.finish();
	}

	@Override
	public void requestFinished(String result) {
		// TODO Auto-generated method stub

		System.out.println("AAAAAASSSSSSSSSSS" + result);
		if (ServiceFlag == 0) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				final JSONArray res = jsonObject.getJSONArray("W");
				String status = res.getJSONObject(0).getString("Status");

				System.out.println("AAAAAASSSSSSSSSSS-" + status);
				if (status.equals("false")) {

					loader1.setVisibility(View.GONE);

					Toast.makeText(getApplicationContext(), "Tagging Failed",
							1500).show();
				} else if (status.equals("true")) {

					loader1.setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(), "Tagging Success",
							1500).show();
				}

			} catch (JSONException e) {
				loader1.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(),
						"Error in server please try again", 1500).show();

			} catch (Exception e) {

				loader1.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(),
						"Error in network connection please try again later",
						1500).show();
			}

		}

		if (ServiceFlag == 1) {

		}
	}


}
