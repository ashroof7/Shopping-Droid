package com.shoppingDroid.main;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shoppingDriod.main.R;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MapsActivity extends Activity {

	public double lng = 32.423000335693F, lat = 20.463399887085F;
	GoogleMap map ;
	
	private void addStoresToMap(){
		DataFetcher df = new DataFetcher(this, lat, lng, "");
		ArrayList<Store> stores = df.stores();
		
		if (stores == null){
			Toast.makeText(this, getString(R.string.diag_no_stores), Toast.LENGTH_LONG).show();
			Log.i("Maps", "couldn't fetch stores");
			return;
		}
		
		LatLng latlang;
		for (Store st : stores) {
			latlang = new LatLng(st.getLatitude(), st.getLongitude());
			map.addMarker(new MarkerOptions().position(latlang)
											.title(st.getName()));
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
