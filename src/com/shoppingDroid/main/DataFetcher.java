package com.shoppingDroid.main;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.shoppingDriod.main.R;
import com.shoppingDroid.onlineDB.DBDispatcher;

/**
 * a class that calls the server (using DBDispatcher) and parses the result.
 * At first we locate the nearest store based on current user location.
 * all methods @returns either an arrayList of Products or stores.
 */
public class DataFetcher {
	
	private double curLat, curLng;
	private Store store;
	private double diffAmount = 20;
	private String barcode;
	private String type="";
	private DBDispatcher dbDispatcher;
	private JSONObject jOb;
	private Context context;
	private Database DB;
	
	public DataFetcher(Context context, double lat, double lng,
			String productBarcode) {
		this.context = context;
		curLat = lat;
		curLng = lng;
		barcode = productBarcode;
		dbDispatcher = new DBDispatcher(context);
		DB = MainActivity.db;
	}

	public boolean locateStore() {
		double range = 0.05;// distance range radius

		String mainTag = context.getString(R.string.DB_stores);
		ArrayList<Store> stores = new ArrayList<Store>();

		try {
			jOb = dbDispatcher.storesInRange(curLat, curLng, range);
			if (jOb == null || !jOb.has(mainTag))
				return false;

			JSONArray elements = jOb.getJSONArray(mainTag);

			JSONObject store;
			for (int i = 0; i < elements.length(); i++) {
				store = elements.getJSONObject(i);
				stores.add(new Store(
						store.getInt(	context.getString(R.string.DB_store_id)),
						store.getString(context.getString(R.string.DB_store_name)),
						store.getDouble(context.getString(R.string.DB_store_longitude)),
						store.getDouble(context.getString(R.string.DB_store_latitude))));
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		store = getNearestStore(stores);
		
		return !(store==null);

	}

	private Store getNearestStore(ArrayList<Store> stores) {
		double nearestDis = Double.MAX_VALUE;
		double deltaLat, deltaLng, delta;
		int index = 0;
		int i = 0;
		for (Store st : stores) {
			deltaLat = curLat - st.getLatitude();
			deltaLng = curLng - st.getLongitude();
			delta = Math.sqrt(deltaLat * deltaLat + deltaLng * deltaLng);
			if (delta < nearestDis) {
				nearestDis = delta;
				index = i;
			}
			i++;
		}
		return stores.get(index);
	}
	
	public ArrayList<Store>stores(){
		ArrayList<Store> stores = new ArrayList<Store>();

		String mainTag = context.getString(R.string.DB_stores);
		try {
			jOb = dbDispatcher.stores();
			if (jOb == null || !jOb.has(mainTag)){
//				Toast.makeText(context, context.getString(R.string.diag_no_connection), Toast.LENGTH_SHORT).show();
				return null;
			}

			JSONArray elements = jOb.getJSONArray(mainTag);
			JSONObject store;
			
			for (int i = 0; i < elements.length(); i++) {
				store = elements.getJSONObject(i);
				stores.add(new Store(
						store.getInt(	context.getString(R.string.DB_store_id)),
						store.getString(context.getString(R.string.DB_store_name)),
						store.getDouble(context.getString(R.string.DB_store_longitude)),
						store.getDouble(context.getString(R.string.DB_store_latitude))));
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stores;
	}
	
	public Product here() {
		Product ret = null;
		try {
			
			if (store==null){
//				Toast.makeText(context, context.getString(R.string.diag_no_data), Toast.LENGTH_SHORT).show();
				return null;
			}
			
			jOb = dbDispatcher.product(barcode, store.getId());
			String mainTag = context.getString(
					R.string.DB_product);

			if (jOb == null || !jOb.has(mainTag)){
//				Toast.makeText(context, context.getString(R.string.diag_no_data), Toast.LENGTH_SHORT).show();
				return null;
			}

			JSONArray elements = jOb.getJSONArray(mainTag);
			
			if (elements.isNull(0))
				return null;
			
			JSONObject product = elements.getJSONObject(0);
			
			ret = new Product(barcode, product.getString(context.getResources()
					.getString(R.string.DB_product_name)),
					product.getString(context.getString(
							R.string.DB_product_type)), store.getId(),
					store.getName(), product.getDouble(context.getString(
									R.string.DB_product_price)),
									DB.isFavorite(barcode));
		

			// set the type attribute to be used in next calls
			type = ret.getTypeName();
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public ArrayList<Product> sameEverywhere() {
		// query other stores with the same product
		ArrayList<Product> products = new ArrayList<Product>();

		String mainTag = context.getString(R.string.DB_product);
		String pName, ptype;

		try {

			jOb = dbDispatcher.productGlobal(barcode);
			if (jOb == null || !jOb.has(mainTag)){
//				Toast.makeText(context, context.getString(R.string.diag_no_data), Toast.LENGTH_SHORT).show();
				return null;
			}
			
			JSONArray elements = jOb.getJSONArray(mainTag);
			JSONObject product = elements.getJSONObject(0);

			pName = product.getString(context.getString(
					R.string.DB_product_name));
			ptype = product.getString(context.getString(
					R.string.DB_product_type));

			if (product == null
					|| !product.has(context.getString(
							R.string.DB_stores)))
				return null;

			elements = product.getJSONArray(context.getString(
					R.string.DB_stores));

			JSONObject store;
			for (int i = 0; i < elements.length(); i++) {
				store = elements.getJSONObject(i);
				products.add(new Product(barcode, pName, ptype, 
						store.getInt(context.getString(R.string.DB_store_id)),
						store.getString(context.getString(R.string.DB_store_name)),
						store.getDouble(context.getString(R.string.DB_product_price)),
						DB.isFavorite(barcode)));
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return products;
	}

	public ArrayList<Product> similarHere() {

		ArrayList<Product> products = new ArrayList<Product>();

		String mainTag = context.getString(R.string.DB_products);
		try {
			
			if (store==null){
//				Toast.makeText(context, context.getString(R.string.diag_no_data), Toast.LENGTH_SHORT).show();
				return null;
			}
			
			jOb = dbDispatcher.productRange(barcode, store.getId(),
					diffAmount);
			if (jOb == null || !jOb.has(mainTag)){
//				Toast.makeText(context, context.getString(R.string.diag_no_data), Toast.LENGTH_SHORT).show();
				return null;
			}

			JSONArray elements = jOb.getJSONArray(mainTag);
			JSONObject product;
			
			String tempBarcode;
			for (int i = 0; i < elements.length(); i++) {
				product = elements.getJSONObject(i);
				products.add(new Product(
						tempBarcode = product.getString(context.getString(R.string.DB_product_barcode)),
						product.getString(context.getString(	R.string.DB_product_name)),
						type, 
						store.getId(), store.getName(), product.getDouble(context.getString(R.string.DB_product_price)),
						DB.isFavorite(tempBarcode)));
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return products;
	}

	public ArrayList<Product> similarEverywhere() {
		// query stores with products of the same type
		ArrayList<Product> products = new ArrayList<Product>();

		String mainTag = context.getString(R.string.DB_products);
		try {
			
			if (store==null){
//				Toast.makeText(context, context.getString(R.string.diag_no_data), Toast.LENGTH_SHORT).show();
				return null;
			}
			
			jOb = dbDispatcher.productRangeGlobal(barcode, store.getId(),
					diffAmount);
			if (jOb == null || !jOb.has(mainTag)){
//				Toast.makeText(context, context.getString(R.string.diag_no_data), Toast.LENGTH_SHORT).show();
				return null;
			}
			JSONArray elements = jOb.getJSONArray(mainTag);
			JSONObject product;
			String tempBarcode;
			for (int i = 0; i < elements.length(); i++) {
				product = elements.getJSONObject(i);
				products.add(new Product(
						tempBarcode = product.getString(context.getString(R.string.DB_product_barcode)),
						product.getString(context.getString(R.string.DB_product_name)),
						type,
						product.getInt(context.getString(R.string.DB_store_id)),
						product.getString(context.getString(R.string.DB_store_name)), 
						product.getDouble(context.getString(R.string.DB_product_price)),
						DB.isFavorite(tempBarcode)));
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return products;
	}
}
