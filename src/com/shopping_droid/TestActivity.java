package com.shopping_droid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		String serverURL = "http://localhost/run";
		InputStream is = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpPost = new HttpGet(serverURL);
		HttpResponse response;
		String result ;
		
		try {
			response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");

			result = sb.toString();
			JSONObject jObject = new JSONObject(result);
			
			for (int i = 0 ; i < jObject.length(); i++){
				String store_name = null;
				double longitude, latitude;
				
				store_name = jObject.getString("store_name");
				latitude = jObject.getDouble("latitude");
				longitude = jObject.getDouble("longitude");
				
				System.out.println(store_name +" "+ longitude+" "+latitude);
				
			} 
			
		} catch (ClientProtocolException e) { e.printStackTrace();
		} catch (IOException e) 			{ e.printStackTrace();
		} catch (JSONException e) 			{ e.printStackTrace();
		}

	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

}
