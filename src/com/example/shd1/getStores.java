package com.example.shd1;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import onlineDB.DBDispatcher;

import Jasonparsing.ItemData;
import Jasonparsing.JsonParser;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class getStores {

	Context con;
	ArrayList<ItemData> data;
	int i,sz ;
	int trial = 0;
	public float lng = 32.423000335693F, lat = 20.463399887085F;
	
	ItemData curr;
	public getStores(Context c) {
		MainActivity.location.updateLocation();
		Location current = MainActivity.location.getLastLocation();
		lng = (float) current.getLongitude();
		lat = (float) current.getLatitude();
		System.out.println(lng+" "+lat);
		con = c;
		Log.i("getStores", "new getStores");
		DBDispatcher d = new DBDispatcher(c);
		JsonParser jp = new JsonParser();
		try {
			Log.i("getStores", "Before stores locations");
			JSONObject ob = d.storesLocations();
			Log.i("getStores", "After stores locations");

			data = jp.parse(ob);
			i = 0;
			sz = data.size();
			Log.i("getStores", data.toString());

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}


	}
	@JavascriptInterface	
	public int getNumberOfStores()
	{
		Log.i("getStores", "Num Of Stores "+sz);
		return sz;
	}
	@JavascriptInterface	
	public int inc() {
		curr = data.get(i++);
		Log.i("getStores", "inc");
		lat = (float) (lat+0.09);
		lng = (float) (lng-0.09);
		return 0;
	}
	@JavascriptInterface	
	public String getNextStoreName()
	{
		String s = curr.getValue(con.getResources().getString(R.string.DB_store_name));
		Log.i("getStores", "returning Name "+s);
		return s;
	}
	@JavascriptInterface	
	public float getNextStoreLng()
	{
		float f1 = Float.parseFloat(curr.getValue(con.getResources().getString(R.string.DB_store_longitude)));
		Log.i("getStores", "returning Lng "+f1);
		return f1;
	}
	@JavascriptInterface
	public float getNextStoreLat()
	{
		float f2 = Float.parseFloat(curr.getValue(con.getResources().getString(R.string.DB_store_latitude)));
		Log.i("getStores", "returning Lat "+f2);
		return f2;
	}
	
	
	@JavascriptInterface
	public float getLatCenter(){
		 Log.i("getStores", "returning lat");
		  return lat;
	  }
	
	 @JavascriptInterface
	  public float getLngCenter(){
		  Log.i("getStores", "returning lng");
		  return lng;
	  }

}
