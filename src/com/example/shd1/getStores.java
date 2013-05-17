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
	public float lng = 39.909330368042F, lat = 31.208690643311F;
	
	ItemData curr;
	public getStores(Context c) {
		MainActivity.location.updateLocation();
		Location current = MainActivity.location.getLastLocation();
		lng = (float) current.getLongitude();
		lat = (float) current.getLatitude();
		System.out.println(lng+" "+lat);
		con = c;
		Log.i("getStores", "new getStores");
//		DBDispatcher d = new DBDispatcher(c);
//		JsonParser jp = new JsonParser();
//		try {
//			Log.i("getStores", "Before stores locations");
//			JSONObject ob = d.storesLocations();
//			Log.i("getStores", "After stores locations");
//
//			data = jp.parse(ob);
//			i = 0;
//			sz = data.size();
//			Log.i("getStores", "After JSON");
//
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}


	}
	
	public int getNumberOfStores()
	{
		Log.i("getStores", "Num Of Stores");
		return sz;
	}
	
	public int inc() {
		curr = data.get(i++);
		Log.i("getStores", "inc");
		lat = (float) (lat+0.09);
		lng = (float) (lng-0.09);
		return 0;
	}
	public String getNextStoreName()
	{
		return curr.getValue(con.getResources().getString(R.string.DB_store_name));
	}
	
	public float getNextStoreLng()
	{
		return Float.parseFloat(curr.getValue(con.getResources().getString(R.string.DB_store_longitude)));
	}
	
	public float getNextStoreLat()
	{
		return Float.parseFloat(curr.getValue(con.getResources().getString(R.string.DB_store_latitude)));
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
