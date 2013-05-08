package com.example.shd1;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import onlineDB.DBDispatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class getStores {

	Context con;

	public getStores(Context c) {
		con = c;
		JSONObject jObject = null;
		DBDispatcher d = new DBDispatcher(c);

		try {

			JSONObject ob = d.storesLocations();
			System.out.println(ob.toString(4));
			JSONArray stores = ob.getJSONArray("stores");
			String store_name = null;
			double longitude, latitude;

			for (int i = 0; i < stores.length(); i++) {
				jObject = stores.getJSONObject(i);

				store_name = jObject.getString("store_name");
				latitude = jObject.getDouble("latitude");
				longitude = jObject.getDouble("longitude");

				System.out.println(store_name + " " + longitude + " "
						+ latitude);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	public Object testCall() {
		Log.i("GetStores", "Test Succeeded");
		return (String) "Alex";
	}

}
