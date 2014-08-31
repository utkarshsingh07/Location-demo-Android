package com.helper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

public class WebserviceHelper extends AsyncTask<String, Integer, String> {

    private RequestReceiver mContext;
	private HttpClient httpClient;
	public static ArrayList<String> arrCountry = new ArrayList<String>();
	
	 String sUrl = "http://www.apmocon.com/demos/mobapp_functions.php?", url = "";

	 
	@SuppressWarnings("unused")
	private String method = null;
	private Map<String, String> paramMap = new HashMap<String, String>();
	private String errorMessage;
	private boolean error_flag = false;

	public WebserviceHelper() {
	}
	
	public WebserviceHelper(RequestReceiver context) {
		mContext = context;
	}

	WebserviceHelper(RequestReceiver context, String setMethod) {
		mContext = context;
		method = setMethod;
	}
	
	private void clearErrors() {
		this.errorMessage = null;
		this.error_flag = false;
	}

	private void setErrors(String theMessage) {
		this.errorMessage = theMessage;
		this.error_flag = true;
	}

	public void setMethod(String m) {
		method = m;
	}

	public void addParam(String key, String value) {
		paramMap.put(key, value);
	}

	@Override
	protected void onPreExecute() {
		httpClient = new DefaultHttpClient();
		this.clearErrors();
	}

	@Override
	protected String doInBackground(String... urls) {
		String data = null;

		try {
			String url = urls[0];
			HttpGet request = new HttpGet(url);
			HttpParams params = request.getParams();
			HttpConnectionParams.setSoTimeout(params, 60000); // 1 minute
			request.setParams(params);
			Log.v("connection timeout", String.valueOf(HttpConnectionParams.getConnectionTimeout(params)));
			Log.v("socket timeout",String.valueOf(HttpConnectionParams.getSoTimeout(params)));
			data = httpClient.execute(request, new BasicResponseHandler());

		} catch (IOException e) {
			
			this.setErrors("We are having problems connecting to the network.");
			Log.e("SPLASH PHOTOS WEB", "webservice IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// do some stuff or whatever
			Log.e("BloodBank", "webservice exception: " + e.getMessage());
			e.printStackTrace();
		}

		return data;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {

	}

	@Override
	protected void onPostExecute(String result) {
		
		System.out.println("helper Service result show =" + result);
		try {
		((RequestReceiver) mContext).requestFinished(result);
		} catch (Exception e) {
			// handle exception
		} 
	}

	public boolean errors_occurred() {
		return this.error_flag;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public String buildUrl() {

		url = sUrl;
		urlMethod();
		return url;
	}
	
	@SuppressWarnings("deprecation")
	public void urlMethod()
	{
		
		boolean first = true;
		for (Map.Entry<String, String> e : paramMap.entrySet()) {
			String key = e.getKey();
			String value = e.getValue();
			value = URLEncoder.encode(value);

			if (first) {
				first = false;
			} else {
				url += "&";
			}
			url += key + "=" + value;
		}
	
		System.out.println("UUUUUUUURRRRRRRLLLLLLL---"+url);
		}
	
	
}
