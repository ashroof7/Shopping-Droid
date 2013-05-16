package com.example.shd1;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import onlineDB.DBDispatcher;

import Jasonparsing.ItemData;
import Jasonparsing.JsonParser;
import android.content.Context;
import android.util.Log;

public class getStores {

	Context con;
	ArrayList<ItemData> data;
	int i,sz ;
	int trial = 0;
	float lng = 39.909330368042F, lat = 31.208690643311F;
	
	ItemData curr;
	public getStores(Context c) {
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
			Log.i("getStores", "After JSON");

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}


	}
	
	public int getNumberOfStores()
	{
		Log.i("getStores", "Num Of Stores");
		return sz;
//		return 3;
	}
	
	public int inc() {
		curr = data.get(i++);
//		lat = (float) (lat+0.2);
//		lng = (float) (lng-0.2);
		return 0;
	}
	
	public String getNextStoreName()
	{
		return curr.getValue(con.getResources().getString(R.string.DB_store_name));
//		return "Alex";
	}
	
	public float getNextStoreLng()
	{
		return Float.parseFloat(curr.getValue(con.getResources().getString(R.string.DB_store_longitude)));
//		 return lng;
	}
	
	public float getNextStoreLat()
	{
		return Float.parseFloat(curr.getValue(con.getResources().getString(R.string.DB_store_latitude)));
//		return lat;
	}
	
	
	/*TODO get the location of the center*/
	 public float getLatCenter(){
		  return lat;
		  
//		  return (float)31.208690643311;
	  }
	  public float getLngCenter(){
		  return lng;
//		  return (float)29.909330368042;
	  }
}
