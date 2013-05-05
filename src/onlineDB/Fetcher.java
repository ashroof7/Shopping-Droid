package onlineDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

class Fetcher extends AsyncTask<String, Integer, JSONObject> {


	private InputStream is;

	HttpClient httpClient;
	HttpGet httpGet;
	HttpResponse response;
	String result;
	HttpEntity entity;
	BufferedReader reader;
	StringBuilder sb;

	public Fetcher() {
		super();
		httpClient = new DefaultHttpClient();
	}

	

	@Override
	protected JSONObject doInBackground(String...param) {
		JSONObject jObject = null;
		try {
			httpGet = new HttpGet(param[0]);
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			is = entity.getContent();

			reader = new BufferedReader(new InputStreamReader(is,"UTF-8" ), 8);
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

		return jObject;
		
		
		/*
		 * for (int i = 0 ; i < jObject.length(); i++){ 
		 * String store_name =
		 * null; double longitude, latitude;
		 * 
		 * store_name = jObject.getString("store_name"); latitude =
		 * jObject.getDouble("latitude"); longitude =
		 * jObject.getDouble("longitude");
		 * 
		 * System.out.println(store_name +" "+ longitude+" "+latitude);
		 * 
		 * }
		 */
	}
}