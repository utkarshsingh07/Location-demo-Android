package com.example.mapdemosimple;

import java.util.ArrayList;

import com.authorization.LoginForm;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectionActivity extends Activity implements LocationListener {

	LocationManager locationManager;
	boolean isNetworkEnabled;
	boolean isGPSEnabled;
	Location locationfommethod;
	boolean canGetLocation = false;
	TextView Net_provider, Gps_provider;
	Location location;
	static String selection;
	static double Currentlatitude, Currentlongitude;
	RelativeLayout loader;
	ImageButton logoutButton;

	SharedPreferences prefs;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialod_net_selection);
		Net_provider = (TextView) findViewById(R.id.txt1);
		Gps_provider = (TextView) findViewById(R.id.txt2);
		logoutButton = (ImageButton) findViewById(R.id.logoutButton);
		loader = (RelativeLayout) findViewById(R.id.loader);

		for (int i = 0; i < LoginForm.User_ids.size(); i++) {

			System.out.println("TTTTTTT1111" + LoginForm.User_Names.get(i));

		}
		Net_provider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selection = "NETWORK";
				GetLatLong("NETWORK");

			}
		});
		Gps_provider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetLatLong("GPS");
				selection = "GPS";
			}
		});

		logoutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogLogout();
			}
		});
	}


	public void dialogLogout() {
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		
		 // Setting Dialog Title
		
		 // Setting Dialog Message
		 alertDialog.setMessage("Are you sure want to logout from app?");
		
		 // Setting Icon to Dialog
		 // alertDialog.setIcon(R.drawable.delete);
		
		 // On pressing Settings button
		 alertDialog.setPositiveButton("Yes",
		 new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int which) {
		
		 prefs = SelectionActivity.this.getSharedPreferences("LoginData", 0);
		 editor = prefs.edit();
		
		 editor.putString("EMail", null);
		 editor.putString("Password", null);
		 editor.commit();
		 Intent i1=new Intent(SelectionActivity.this,LoginForm.class);
		 startActivity(i1);
		 SelectionActivity.this.finish();
		 }
		 });
		
		 // on pressing cancel button
		 alertDialog.setNegativeButton("No",
		
		 new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int which) {
		 dialog.cancel();
		 }
		 });
		
		 alertDialog.show();
		 
	
		
	}

	public Location getLocation(String from) {

		Currentlatitude = 0.0;
		Currentlongitude = 0.0;

		if (from.equals("NETWORK")) {

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
						LocationManager.NETWORK_PROVIDER, 1000, 0,
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

		if (from.equals("GPS")) {

			locationManager = (LocationManager) this
					.getSystemService(LOCATION_SERVICE);

			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			System.out.println("DDDDDDDD" + isGPSEnabled);
			if (isGPSEnabled == false) {

				showSettingsAlert();

			} else {

				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 1000, 0,
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loader.setVisibility(View.GONE);

	}

	public void GetLatLong(String from) {

		location = getLocation(from);

		if (from.equals("NETWORK")) {
			location = getLocation(from);
			if (location == null) {
				Toast.makeText(getApplicationContext(),
						"Error in getting current location please wait",
						Toast.LENGTH_SHORT).show();

			} else {
				Currentlatitude = location.getLatitude();
				Currentlongitude = location.getLongitude();
				System.out.println("TTTTTTTTTTTT" + Currentlatitude + "---"
						+ Currentlongitude);
				loader.setVisibility(View.VISIBLE);
				Intent in = new Intent(this, MainActivity.class);
				startActivity(in);

			}
		}
		if (from.equals("GPS")) {
			System.out.println("GGGGGGG=" + isGPSEnabled);
			if (isGPSEnabled == true) {
				System.out.println("GGGGGGG111=" + location);
				if (location == null) {
					Toast.makeText(getApplicationContext(),
							"Error in getting current location please wait",
							Toast.LENGTH_SHORT).show();

				} else {
					Currentlatitude = location.getLatitude();
					Currentlongitude = location.getLongitude();
					System.out.println("TTTTTTTTTTTT" + Currentlatitude + "---"
							+ Currentlongitude);
					loader.setVisibility(View.VISIBLE);
					Intent in = new Intent(this, MainActivity.class);
					startActivity(in);

				}
			} else {
				if (isGPSEnabled == false) {

					Toast.makeText(getApplicationContext(), "No GPS found ",
							Toast.LENGTH_SHORT).show();
				}
			}
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

						// Intent intent1 = new Intent();
						// intent1.setClassName("com.android.settings",
						// "com.android.settings.SecuritySettings");
						// startActivity(intent1);
						Intent viewIntent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(viewIntent);

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

		diialogExit();
	}

	public void diialogExit() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title

		// Setting Dialog Message
		alertDialog.setMessage("Do you want exit from app ?");

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.delete);

		// On pressing Settings button
		alertDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						SelectionActivity.this.finish();
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("No",

		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		alertDialog.show();
	}

}
