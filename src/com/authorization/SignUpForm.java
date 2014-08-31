package com.authorization;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapdemosimple.R;
import com.helper.NetReachability;
import com.helper.RequestReceiver;
import com.helper.WebserviceHelper;

public class SignUpForm extends Activity implements RequestReceiver {

	EditText edtName, edtUser, editMob, editAge, edPassword, edPasswordConfirm;
	TextView txtGender;
	Button btnSignUp;

	NetReachability net = new NetReachability(this);
	private Pattern emailPattern;
	private Matcher emailMatcher;
	RelativeLayout loader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_form);
		intitViews();
		setListners();
	}

	private void intitViews() {
		edtName = (EditText) findViewById(R.id.edtName);
		edtUser = (EditText) findViewById(R.id.edtUser);
		editMob = (EditText) findViewById(R.id.editMob);
		editAge = (EditText) findViewById(R.id.editAge);
		edPassword = (EditText) findViewById(R.id.edPassword);
		edPasswordConfirm = (EditText) findViewById(R.id.edPasswordConfirm);
		txtGender = (TextView) findViewById(R.id.txtGender);
		btnSignUp = (Button) findViewById(R.id.btnSignUp);

		loader = (RelativeLayout) findViewById(R.id.loader);

	}

	protected void callSignUpService() {
		// TODO Auto-generated method stub

		boolean netcheck = net.isInternetOn();
		if (netcheck == false) {

			Toast.makeText(getApplicationContext(),
					"Check your network connection", 1000).show();

		} else if (edtName.getText().toString().trim().equals("")
				|| edtName.getText().toString().trim().equals(null)) {

			Toast.makeText(getApplicationContext(), "Enter your name", 1000)
					.show();

		} else if (edtUser.getText().toString().trim().equals("")
				|| edtUser.getText().toString().trim().equals(null)) {

			Toast.makeText(getApplicationContext(), "Enter user name", 1000)
					.show();
		} else if (eMailValidation(edtUser.getText().toString().trim()) == false) {
			Toast.makeText(getApplicationContext(),
					"Enter correct email address", Toast.LENGTH_SHORT).show();
		} else if (editMob.getText().toString().trim().equals("")
				|| editMob.getText().toString().equals(null)) {
			Toast.makeText(getApplicationContext(), "Enter your mobile number",
					Toast.LENGTH_SHORT).show();
		}  else if (editMob.getText().toString().trim().length()<6) {
			Toast.makeText(getApplicationContext(), "Enter vald mobile number",
					Toast.LENGTH_SHORT).show();
		} else if (editAge.getText().toString().trim().equals("")
				|| editAge.getText().toString().equals(null)) {
			Toast.makeText(getApplicationContext(), "Enter your age",
					Toast.LENGTH_SHORT).show();
		}  else if (txtGender.getText().toString().equals("Select Gender")) {
			Toast.makeText(getApplicationContext(), "Please select gender",
					Toast.LENGTH_SHORT).show();
		} else if (edPassword.getText().toString().trim().equals("")
				|| edPassword.getText().toString().equals(null)) {
			Toast.makeText(getApplicationContext(), "Please enter password",
					Toast.LENGTH_SHORT).show();
		} else if (edPasswordConfirm.getText().toString().trim().equals("")
				|| edPasswordConfirm.getText().toString().equals(null)) {
			Toast.makeText(getApplicationContext(), "Please confirm password",
					Toast.LENGTH_SHORT).show();
		} else if (!edPasswordConfirm.getText().toString().trim()
				.equals(edPassword.getText().toString().trim())) {

			Toast.makeText(getApplicationContext(), "Password does not match",
					Toast.LENGTH_SHORT).show();
		} else {
			// edPasswordConfirm
			// http://www.apmocon.com/demos/mobapp_functions.php?action=signup&username=p@pp.com&password=p&age=10&gender=male&fullname=Parsi&mobile=90000&datakey=b629d485a61cehjuy7af4911b722d3
			loader.setVisibility(View.VISIBLE);
			// http://www.apmocon.com/demos/mobapp_functions.php?action=signup&username=aa&password=bb&age=10&gender=male&fullname=jam&mobile=asa&datakey=b629d485a61cehjuy7af4911b722d3
			WebserviceHelper helper = new WebserviceHelper(this);
			helper.addParam("action", "signup");
			helper.addParam("username", edtUser.getText().toString().trim());
			helper.addParam("password", edPassword.getText().toString().trim());
			helper.addParam("age", editAge.getText().toString().trim());
			helper.addParam("gender", txtGender.getText().toString().trim());
			helper.addParam("fullname", edtName.getText().toString().trim());
			helper.addParam("mobile", editMob.getText().toString().trim());
			helper.addParam("datakey", "b629d485a61cehjuy7af4911b722d3");
			helper.execute(helper.buildUrl());

		}

	}

	private void setListners() {
		// TODO Auto-generated method stub
		txtGender.setOnClickListener(layoutListener);
		btnSignUp.setOnClickListener(layoutListener);
	}

	public boolean eMailValidation(String email) {
		emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
				+ "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
		emailMatcher = emailPattern.matcher(email);
		return emailMatcher.matches();
	}

	OnClickListener layoutListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			// Auto-generated method stub
			switch (v.getId()) {

			case R.id.txtGender: {
				genderDialog();

			}

				break;
			case R.id.btnSignUp: {
				callSignUpService();

			}

				break;

			default: {

			}
				break;

			}

		}

	};

	protected void genderDialog() {
		// TODO Auto-generated method stub

		final Dialog objD = new Dialog(this);
		// objD.getWindow().setBackgroundDrawable(
		// new ColorDrawable(android.graphics.Color.TRANSPARENT));
		objD.requestWindowFeature(Window.FEATURE_NO_TITLE);

		objD.setContentView(R.layout.setting_list_with_header);

		ListView listView_with_h = (ListView) objD
				.findViewById(R.id.listView_with_h);
		TextView txt_header = (TextView) objD.findViewById(R.id.txt_header);
		txt_header.setText("Select Gender");
		ImageButton btnCancel = (ImageButton) objD.findViewById(R.id.btnCancel);

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				objD.dismiss();

			}
		});

		final String select[] = new String[] { "Male", "Female" };

		ArrayAdapter<String> list_adap = new ArrayAdapter<String>(this,
				R.layout.list_set_text, select);
		listView_with_h.setAdapter(list_adap);

		listView_with_h.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub

				txtGender.setText(select[pos]);
				objD.dismiss();
			}
		});

		objD.show();
	}

	@Override
	public void requestFinished(String result) {
		// TODO Auto-generated method stub

		System.out.println("Service result show =--" + result);

		try {

			JSONObject jsonObject = new JSONObject(result);
			JSONArray res = jsonObject.getJSONArray("W");
			String status = res.getJSONObject(0).getString("Status");

			System.out.println("Service result show =" + status);
			if (status.equals("false")) {
				// result failed error show in pop_up

				loader.setVisibility(View.GONE);

				Toast.makeText(getApplicationContext(),
						"User name cannot be duplicatate", 1500).show();

			} else if (status.equals("true")) {


				loader.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(),
						"Registration complet please login to continue", 2500)
						.show();
				this.finish();

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

}
