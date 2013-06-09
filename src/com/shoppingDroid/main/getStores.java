package com.shoppingDroid.main;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.shoppingDriod.main.R;
import com.shoppingDroid.jsonParsing.ItemData;
import com.shoppingDroid.jsonParsing.JsonParser;
import com.shoppingDroid.onlineDB.DBDispatcher;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class getStores {

	Context con;
	ArrayList<ItemData> data;
	int i, sz;
	int trial = 0;
	public float lng = 32.423000335693F, lat = 20.463399887085F;

	ItemData curr;

	public getStores(Context c) {
		con = c;
		DBDispatcher d = new DBDispatcher(c);
		JsonParser jp = new JsonParser();
		try {
			JSONObject ob = d.storesLocations();

			data = jp.parse(ob);
			i = 0;
			sz = data.size();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	@JavascriptInterface
	public int getNumberOfStores() {
		return sz;
	}

	@JavascriptInterface
	public String getNextStoreName() {
		String s = curr.getValue(con.getResources().getString(
				R.string.DB_store_name));
		return s;
	}

	@JavascriptInterface
	public float getNextStoreLng() {
		float f1 = Float.parseFloat(curr.getValue(con.getResources().getString(
				R.string.DB_store_longitude)));
		return f1;
	}

	@JavascriptInterface
	public float getNextStoreLat() {
		float f2 = Float.parseFloat(curr.getValue(con.getResources().getString(
				R.string.DB_store_latitude)));
		return f2;
	}


}
