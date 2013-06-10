package com.shoppingDroid.main;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.content.Context;
import com.shoppingDriod.main.R;
import com.shoppingDroid.jsonParsing.ItemData;
import com.shoppingDroid.jsonParsing.JsonParser;
import com.shoppingDroid.onlineDB.DBDispatcher;

public class DataFetcher {

	private String store_id;
	private String barcode;
	private DBDispatcher d;
	private JSONObject jOb;
	private JsonParser jp;
	private ItemData scannedItem;
	private  ArrayList<ItemData> data;
	private double curLat, curLng;
	private ItemData store = null;
	private Context context;
	

	public DataFetcher(Context context, double lat, double lng,
			String productBarcode) {
		this.context = context;
		curLat = lat;
		curLng = lng;
		barcode = productBarcode;
		d = new DBDispatcher(context);
		jp = new JsonParser();
	}

	public boolean locateStore() {
		double range = 0.2;// distance range raduis
		ArrayList<ItemData> stores;
		
		try {
			jOb = d.storesInRange(curLat, curLng, range);
			if (jOb == null) {
				return false;
			}
			stores = jp.parse(jOb);
			int sz = stores.size();

			if (sz == 1) {
				store = stores.get(0);
				store_id = stores.get(0).getValue(
						context.getResources().getString(R.string.DB_store_id));

			} else if (sz > 1) {
				// TODO initialize activity and let user select his store from
				// the provided list;
				
				// set the store_id
				sz = stores.size();
				store = getNearestStore(stores);
				store_id = store.getValue("store_id");
			} else {
				jOb = d.stores();
				if (jOb == null) {
					return false;
				}
				stores = jp.parse(jOb);
				store = getNearestStore(stores);
				store_id = store.getValue("store_id");
				// TODO init activity and let user select a store
			}
			return true ;
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		return false;

	}

	private ItemData getNearestStore(ArrayList<ItemData> stores) {
		double nearestDis = Double.MAX_VALUE;
		double deltaLat, deltaLng, delta;
		int index = 0;
		int i = 0;
		for (ItemData st : stores) {
			deltaLat = curLat - Double.parseDouble(st.getValue("latitude"));
			deltaLng = curLng - Double.parseDouble(st.getValue("longitude"));
			delta = Math.sqrt(deltaLat * deltaLat + deltaLng * deltaLng);
			if (delta < nearestDis) {
				nearestDis = delta;
				index = i;
			}
			i++;
		}
		return stores.get(index);
	}
	
	public ItemData getStore(){
		return store;
	}
	
	//TODO malhach lazma 5ales 
	public ItemData getItem(){
		return scannedItem;
	}
	
	public ArrayList<ItemData> getData(){
		return data;
	} 
	
	public boolean here() {
		try {
			jOb = d.product(barcode, Integer.parseInt(store_id));
			if (jOb == null)
				return false;

			data = jp.parse(jOb);
			if (data.size() == 0)
				return false;
			
			scannedItem = data.get(0);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean sameEverywhere() {
		// query other stores with the same product
		try {
			jOb = d.productGlobal(barcode);
			if (jOb == null)
				return false;
			data = jp.parse(jOb);
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean similarHere() {
		double diff_amount = 20;
		try {
			jOb = d.productRange(barcode, Integer.parseInt(store_id),
					diff_amount);
			if (jOb == null)
				return false;

			data = jp.parse(jOb);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean similarEverywhere() {
		// query stores with products of the same type

		double diff_amount = 20;

		try {
			jOb = d.productRangeGlobal(barcode, Integer.parseInt(store_id),
					diff_amount);
			if (jOb == null)
				return false;
			data = jp.parse(jOb);
		} catch (NumberFormatException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}
}
