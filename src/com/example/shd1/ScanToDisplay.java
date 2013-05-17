package com.example.shd1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import onlineDB.DBDispatcher;

import org.json.JSONException;
import org.json.JSONObject;

import Jasonparsing.ItemData;
import Jasonparsing.JsonParser;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScanToDisplay extends Activity {

	@SuppressLint("NewApi")
	
	
	private ItemData scaned_item;
	private Product p;
	private String store_id;
	private double longitude;
	private double latitude;
	private String barcode;
	private DBDispatcher d;
	private JSONObject jOb ;
	private JsonParser jp;
	public static ArrayList<ItemData> data;
	
	TextView txt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_to_display);
		setupActionBar();
				
		d = new DBDispatcher(this);
		
		jp = new JsonParser();
				Intent intent = getIntent();
				barcode = intent.getStringExtra(MainActivity.BarCode);
				txt =  (TextView) findViewById(R.id.textView_res);
				txt.setText("Product Barcode: "+barcode);
				Log.i("in scann ", "in scann");

				Query(barcode);
	}

	private void Query(String product_barcode){
		Log.i("in scann ", "in Query");

		ArrayList<ItemData> stores;
		ItemData store = null;
	
//		// TODO get location
		double range = 2;
		try {
			latitude = 20.463399887085;
			longitude = 32.423000335693;
			jOb = d.storesInRange(latitude,longitude ,range);
			try {
				Log.i(" json ret ", jOb.toString(4));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			stores = jp.parse(jOb);
			int sz = stores.size() ;
			Log.i("in scann ", "after stores");

			if(sz == 1)
			{
				store = stores.get(0);
				store_id = stores.get(0).getValue(getResources().getString(R.string.DB_store_id));
				Log.i("in scann ", "size = 1");

			}
			else if(sz > 1)
			{
				//TODO init activity and let user select his store from the provided list;
				// set the store_id
			}
			else
			{
				jOb = d.stores();
				data = jp.parse(jOb);
				// TODO init activity and let user select a store
				// set the store_id;
				Log.i("in scann ", "not handled yet");

			}
			
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		
		Log.i("in scann ", "after job");

		
		try {
			barcode = product_barcode;
			
			store_id = store.getValue(this.getResources().getString(R.string.DB_store_id));
			
			jOb = d.product(product_barcode,Integer.parseInt(store_id));
			data = jp.parse(jOb);

			scaned_item = data.get(0);
//------------------------ Assuming ItemData is retrieved --------------------------------------------------
			Log.i("in scann ", "creating product");

			// display the returned item;

			txt.setText("Product Barcode: "+product_barcode+" Price "+scaned_item.getValue(getResources().getString(
			R.string.DB_product_price)));
			
			p = new Product(product_barcode,
					scaned_item.getValue(getResources().getString(
							R.string.DB_product_name)),
					scaned_item.getValue("product_type"),					
					Integer.parseInt(store_id),
					store.getValue(getResources().getString(R.string.DB_store_name))
					);
			
			MainActivity.db.addHistory(p);
			List<Product> s = MainActivity.db.retreive();

			Log.i("in scann ", "will print history");

			for (Product cn : s) {
				String log = "Id: " + cn.getBar_code() + " ,Name: " + cn.getName()
						+ "type name "
						+ cn.getType_name()+" store "+cn.getStore_name()+" store addr "
						+" STORE ID "+cn.getStore_id();
				System.out.println("History Retrieved !!!!!!!!!-->" + log);
				// Writing Contacts to log
				Log.d("Name: ", log);
			}
//----------------------------------------------------------------------------------------------------------			
						 						
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}
	
	public void add_to_favorites(View view) {
		//insert the tuple in offline DB
		MainActivity.db.addFavourites(p);
		Toast.makeText(this, "Product added to favorites", Toast.LENGTH_LONG).show();
		Log.d("in add favo", "favoraite added");
	
	}

	public void otherStores(View view) {
		//query other stores with the same product
		try {
			jOb = d.productGlobal(barcode);
			try {
				Log.i("other stores", jOb.toString(4));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			data = jp.parse(jOb);
			Log.i("other stores", data.toString());
	
			Intent intent = new Intent(this, ListActivity.class);
			intent.putParcelableArrayListExtra("data", data);
			startActivity(intent);		
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		
	}

	public void globalRecommender(View view) {
		//query stores with products of the same type
		
		double diff_amount = 20;
		
		
		try {
			jOb = d.productRangeGlobal(barcode, Integer.parseInt(store_id), diff_amount);
			try {
				Log.i("global recommender", jOb.toString(4));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			data = jp.parse(jOb);
			Log.d("global recommender ", data.toString());
			Intent intent = new Intent(this, ListActivity.class);
			intent.putParcelableArrayListExtra("data", data);
			startActivity(intent);		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	
	}

	public void similarProductsInStore(View view) {
		//query products of the same type in store
		double diff_amount = 20;
		
		
		try {
			jOb = d.productRange(barcode, Integer.parseInt(store_id), diff_amount);
			try {
				Log.i("similar products", jOb.toString(4));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			data = jp.parse(jOb);
			Log.d("similar prod", data.toString());
			Intent intent = new Intent(this, ListActivity.class);
			intent.putParcelableArrayListExtra("data", data);
			startActivity(intent);		
		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan_to_display, menu);
		
		return true;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
}
