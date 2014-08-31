package com.authorization;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapdemosimple.R;
import com.example.mapdemosimple.SelectionActivity;
import com.helper.NetReachability;
import com.helper.RequestReceiver;
import com.helper.WebserviceHelper;

public class LoginForm extends Activity implements RequestReceiver {
	EditText edtUserName, edtPass;
	Button btnLogin;
	TextView txtcreatenew;
	NetReachability net = new NetReachability(this);
	private Pattern emailPattern;
	private Matcher emailMatcher;
	RelativeLayout loader;

	public static ArrayList<String> User_ids = new ArrayList<String>();
	public static ArrayList<String> User_Names = new ArrayList<String>();
	public static String My_Id;

	SharedPreferences prefs;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_form);
		initViews();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub

		prefs = LoginForm.this.getSharedPreferences("LoginData", 0);
		System.out.println("AAAAAAAAAASSSSSSS---"
				+ prefs.getString("EMail", null));
		if (prefs.getString("EMail", null) != null
				&& prefs.getString("Password", null) != null) {

			loader.setVisibility(View.VISIBLE);
			WebserviceHelper helper = new WebserviceHelper(this);
			helper.addParam("action", "login");
			helper.addParam("username", prefs.getString("EMail", null));
			helper.addParam("password", prefs.getString("Password", null));
			helper.addParam("datakey", "b629d485a61cehjuy7af4911b722d3");
			helper.execute(helper.buildUrl());

		}
	}

	private void initViews() {
		// TODO Auto-generated method stub
		edtUserName = (EditText) findViewById(R.id.edtUserName);
		edtPass = (EditText) findViewById(R.id.edtPass);
		btnLogin = (Button) findViewById(R.id.btnLogin);

		loader = (RelativeLayout) findViewById(R.id.loader);
		loader.setVisibility(View.GONE);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ServiceCall();
			}

		});

		txtcreatenew = (TextView) findViewById(R.id.txtcreatenew);
		txtcreatenew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i1 = new Intent(LoginForm.this, SignUpForm.class);
				startActivity(i1);
			}
		});

	}

	private void ServiceCall() {

		boolean netcheck = net.isInternetOn();

		if (netcheck == false) {

			Toast.makeText(getApplicationContext(),
					"Check your network connection", 1000).show();

		} else if (edtUserName.getText().toString().trim().equals("")
				|| edtUserName.getText().toString().equals(null)) {

			Toast.makeText(getApplicationContext(),
					"Please enter username (Email id)", 1000).show();

		} else if (eMailValidation(edtUserName.getText().toString().trim()) == false) {
			Toast.makeText(getApplicationContext(),
					"Enter correct email address", Toast.LENGTH_SHORT).show();
		} else if (edtPass.getText().toString().trim().equals("")
				|| edtPass.getText().toString().equals(null)) {
			Toast.makeText(getApplicationContext(), "Enter your password",
					Toast.LENGTH_SHORT).show();
		} else {

			prefs = LoginForm.this.getSharedPreferences("LoginData", 0);
			editor = prefs.edit();

			editor.putString("EMail", edtUserName.getText().toString().trim());
			editor.putString("Password", edtPass.getText().toString().trim());
			editor.commit();

			// http://www.apmocon.com/demos/mobapp_functions.php?action=login&username=tt&password=bb&datakey=b629d485a61cehjuy7af4911b722d3
			loader.setVisibility(View.VISIBLE);
			WebserviceHelper helper = new WebserviceHelper(this);
			helper.addParam("action", "login");
			helper.addParam("username", edtUserName.getText().toString().trim());
			helper.addParam("password", edtPass.getText().toString().trim());
			helper.addParam("datakey", "b629d485a61cehjuy7af4911b722d3");
			helper.execute(helper.buildUrl());

		}

	}

	@Override
	public void requestFinished(String result) {
		// TODO Auto-generated method stub

		loader.setVisibility(View.GONE);
		System.out.println("Service result show =--" + result);

		try {
			// "W":[{"Status":"true"},{"user_id":"1","user_name":"aa","full_name":"jam"},{"user_id":"2","user_name":"tt","full_name":"jam"},{"user_id":"3","user_name":"Parsi","full_name":"jam"},{"user_id":"4","user_name":"p@pp.com","full_name":"Parsi"},{"user_id":"5","user_name":"a@a.com","full_name":"aaa"}]}
			JSONObject jsonObject = new JSONObject(result);
			final JSONArray res = jsonObject.getJSONArray("W");
			String status = res.getJSONObject(0).getString("Status");

			User_ids.clear();
			User_Names.clear();

			if (status.equals("false")) {

				// result failed error show in pop_up

				loader.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(),
						"Incorrect email or password", 1500).show();

			} else if (status.equals("true")) {

				JSONObject jObje = res.getJSONObject(res.length() - 1);

				My_Id = jObje.getString("user_id");
				User_ids.add(My_Id);
				User_Names.add("My Tags");
				for (int k = 1; k < res.length() - 1; k++) {

					jObje = res.getJSONObject(k);
					User_ids.add(jObje.getString("user_id"));
					User_Names.add(jObje.getString("full_name"));
				}

				Intent i2 = new Intent(this, SelectionActivity.class);
				startActivity(i2);
				this.finish();
				loader.setVisibility(View.GONE);

			}
		} catch (JSONException e) {

			loader.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(),
					"Error in server please try again", 1500).show();
		} catch (Exception e) {

			loader.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(),
					"Error in network connection please try again later", 1500)
					.show();
		}

	}

	public boolean eMailValidation(String email) {
		emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
				+ "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
		emailMatcher = emailPattern.matcher(email);
		return emailMatcher.matches();
	}
}
