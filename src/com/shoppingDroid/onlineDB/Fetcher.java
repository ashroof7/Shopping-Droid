package com.shoppingDroid.onlineDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

class Fetcher extends AsyncTask<String, Integer, JSONObject> {

	private InputStream is;

	HttpClient httpClient;
	HttpGet httpGet;
	HttpResponse response;
	String result;
	HttpEntity entity;
	BufferedReader reader;
	StringBuilder sb;
	Context con;
//	ProgressDialog diag;
	boolean checkConnection;

	public Fetcher(Context cont) {
		super();
		con = cont;
		httpClient = new DefaultHttpClient();
//		diag = new ProgressDialog(con);

	}

	private boolean hasConnection(Context con) {
		ConnectivityManager connectivity = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
//		diag.setMessage(con.getString(R.string.diag_waiting_response));
//		diag.show();
		checkConnection = hasConnection(con);
	}

	@Override
	protected JSONObject doInBackground(String... param) {

		JSONObject jObject = null;
		if (checkConnection) {
			try {
				URL url = new URL(param[0]);
				Log.i("url ", param[0]);
				try {
					url.openConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					httpGet = new HttpGet(url.toURI());
					Log.i("url ", "getting request");
				} catch (URISyntaxException e) {
					Log.e("uri conversion error", e.toString());
				}
				try {
					if (httpGet == null)
						return null;
					response = httpClient.execute(httpGet);
					Log.i("url ", "executing get");
				} catch (Exception e) {
					// Toast.makeText(this, "There is no Connection",
					// Toast.LENGTH_LONG).show();
				}

				if (response == null)
					return null;
				publishProgress(5);
				Log.i("Status code", ""
						+ response.getStatusLine().getStatusCode());
				Log.i("Reason phrase", ""
						+ response.getStatusLine().getReasonPhrase());
				entity = response.getEntity();
				is = entity.getContent();
				reader = new BufferedReader(new InputStreamReader(is, "UTF-8"),
						8);
				sb = new StringBuilder();

				String line = null;
				while ((line = reader.readLine()) != null)
					sb.append(line + "\n");

				result = sb.toString();
				jObject = new JSONObject(result);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return jObject;

	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);

		Log.i("in postExec", "dismissing now");
//		diag.dismiss();
		if (!checkConnection) {
//			Toast.makeText(con, con.getString(R.string.diag_no_connection), Toast.LENGTH_LONG).show();
			Log.i("in postExec", "no connection");
		} else {
			if (result != null) {
				Log.i("in postExec", "got data");
//				Toast.makeText(con, con.getString(R.string.diag_data_retrieved), Toast.LENGTH_SHORT)
//						.show();
			} else {
				Log.i("in postExec", "server down");
//				Toast.makeText(con, con.getString(R.string.diag_server_down), Toast.LENGTH_LONG).show();
			}
		}
	}
}