package com.shoppingDroid.main;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shoppingDriod.main.R;
import com.shoppingDroid.jsonParsing.ItemData;
import com.shoppingDroid.jsonParsing.JsonParser;
import com.shoppingDroid.onlineDB.DBDispatcher;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

public class MapsActivity extends Activity {

	public double lng = 32.423000335693F, lat = 20.463399887085F;
	GoogleMap map ;
	
	private void addStoresToMap(){
		DBDispatcher d = new DBDispatcher(this);
		JsonParser jp = new JsonParser();
		ArrayList<ItemData> stores = null;
		// get stores from server
		try {
			JSONObject ob = d.storesLocations();
			stores = jp.parse(ob);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		double longitude, latitude ; 
		String storeName;
		LatLng latlang;
		for (ItemData st : stores) {
			latitude = Double.parseDouble(st.getValue(getResources().getString(
					R.string.DB_store_latitude)));
			longitude = Double.parseDouble(st.getValue(getResources().getString(
					R.string.DB_store_longitude)));
			storeName = st.getValue(getResources().getString(
					R.string.DB_store_name));
			latlang = new LatLng(latitude, longitude);
			map.addMarker(new MarkerOptions().position(latlang)
											.title(storeName));
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO investigate lazy marker loading
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap(); 
		
		// updating current location
		MainActivity.location.updateLocation();
		Location current = MainActivity.location.getLastLocation();
		lng = (float) current.getLongitude();
		lat = (float) current.getLatitude();
		// center map 
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
		map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_point)));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		addStoresToMap();
		
	}
	

}
