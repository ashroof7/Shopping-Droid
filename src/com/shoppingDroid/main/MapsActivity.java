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
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MapsActivity extends Activity implements LoaderCallbacks<ArrayList<Store>>  {

	public double lng = 32.423000335693F, lat = 20.463399887085F;
	GoogleMap map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		getLoaderManager().initLoader(0, null, this).forceLoad();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// updating current location
		MainActivity.location.updateLocation();
		Location current = MainActivity.location.getLastLocation();
		lng = (float) current.getLongitude();
		lat = (float) current.getLatitude();
		// center map
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),
				15));
		map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(
				BitmapDescriptorFactory.fromResource(R.drawable.map_point)));
		Log.wtf("Map-onCreate", "a3tekom we anzel el besin m3akom we bo2 bo2 bo2");
	}

	
	@Override
	public Loader<ArrayList<Store>> onCreateLoader(int arg0, Bundle arg1) {
		return new MapLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Store>> loader,	ArrayList<Store> stores) {
		LatLng latlang;
		if (stores == null) {
			Toast.makeText(this, getString(R.string.diag_no_stores), Toast.LENGTH_LONG).show();
			Log.i("Maps", "couldn't fetch stores");
			return ;
		}		
		for (Store st : stores) {
			latlang = new LatLng(st.getLatitude(), st.getLongitude());
			map.addMarker(new MarkerOptions().position(latlang).title(
					st.getName()));
		}	
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Store>> arg0) {
		
	}
	
}

class MapLoader extends AsyncTaskLoader<ArrayList<Store>> {

	public MapLoader(Context context) {
		super(context);
	}

	@Override
	public ArrayList<Store> loadInBackground() {
		Log.wtf("taskLoader", "wasal ya ged3an");
		MainActivity.location.updateLocation();
		Location loc = MainActivity.location.getLastLocation();
		return new DataFetcher(getContext(), loc.getLatitude(), loc.getLongitude(), "").stores();
	}
}
