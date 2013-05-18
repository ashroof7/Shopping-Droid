package com.shoppingDroid.main;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


import org.json.JSONObject;

import com.shoppingDriod.main.R;
import com.shoppingDroid.jsonParsing.ItemData;
import com.shoppingDroid.jsonParsing.JsonParser;
import com.shoppingDroid.onlineDB.DBDispatcher;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScanToDisplay extends Activity {

	@SuppressLint("NewApi")
	private ItemData scaned_item;
	private Product p;
	private String store_id;
	private String barcode;
	private DBDispatcher d;
	private JSONObject jOb;
	private JsonParser jp;
	public static ArrayList<ItemData> data;
	private double curLat, curLng;

	TextView txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_to_display);
		setupActionBar();

		MainActivity.location.updateLocation();
		Location loc = MainActivity.location.getLastLocation();
		curLat = loc.getLatitude();
		curLng = loc.getLongitude();

		d = new DBDispatcher(this);

		jp = new JsonParser();
		Intent intent = getIntent();
		barcode = intent.getStringExtra(MainActivity.BarCode);
		txt = (TextView) findViewById(R.id.textView_res);
		txt.setText("Product Barcode: " + barcode);
		Query(barcode);
	}

	private void Query(String product_barcode) {
		ArrayList<ItemData> stores;
		ItemData store = null;

		double range = 0.2;// distance range raduis
		try {
			jOb = d.storesInRange(curLat, curLng, range);
			if (jOb == null) {
				finish();
				return;
			}
			stores = jp.parse(jOb);
			int sz = stores.size();

			if (sz == 1) {
				store = stores.get(0);
				store_id = stores.get(0).getValue(
						getResources().getString(R.string.DB_store_id));

			} else if (sz > 1) {
				// TODO initialize activity and let user select his store from the
				// provided list;
				// set the store_id
				sz = stores.size();
				store = getNearestStore(stores);
				store_id = store.getValue("store_id");
			} else {
				jOb = d.stores();
				if (jOb == null) {
					finish();
					return;
				}
				stores = jp.parse(jOb);
				store = getNearestStore(stores);
				store_id = store.getValue("store_id");
				// TODO init activity and let user select a store
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}


		try {
			barcode = product_barcode;
			jOb = d.product(product_barcode, Integer.parseInt(store_id));
			if (jOb == null) {
				finish();
				return;
			}
			data = jp.parse(jOb);
			if (data.size() == 0) {
				Toast.makeText(this, "product not found", Toast.LENGTH_LONG)
						.show();
				finish();
				return;
			}
			scaned_item = data.get(0);
// ------------------------ Assuming ItemData is retrieved ---------------------

			// display the returned item;

			txt.setText("Product Barcode\n"
					+ product_barcode
					+ "\nPrice "
					+ scaned_item.getValue(getResources().getString(
							R.string.DB_product_price)));

			p = new Product(product_barcode,
					scaned_item.getValue("product_name"),
					scaned_item.getValue("product_type"),
					Integer.parseInt(store_id), store.getValue(getResources()
							.getString(R.string.DB_store_name)));

			MainActivity.db.addHistory(p);

			// ----------------------------------------------------------------------------------------------------------

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	public ItemData getNearestStore(ArrayList<ItemData> stores) {
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

	public void add_to_favorites(View view) {
		// insert the tuple in offline DB
		MainActivity.db.addFavourites(p);
		Toast.makeText(this, "Product added to favorites", Toast.LENGTH_LONG)
				.show();

	}

	public void otherStores(View view) {
		// query other stores with the same product
		try {
			jOb = d.productGlobal(barcode);
			if (jOb == null)
				return;
			data = jp.parse(jOb);
			Intent intent = new Intent(this, DisplayList.class);
			startActivity(intent);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	public void globalRecommender(View view) {
		// query stores with products of the same type

		double diff_amount = 20;

		try {
			jOb = d.productRangeGlobal(barcode, Integer.parseInt(store_id),
					diff_amount);
			if (jOb == null)
				return;
			data = jp.parse(jOb);
			Intent intent = new Intent(this, DisplayList.class);
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
		// query products of the same type in store
		double diff_amount = 20;

		try {
			jOb = d.productRange(barcode, Integer.parseInt(store_id),
					diff_amount);
			if (jOb == null)
				return;

			data = jp.parse(jOb);
			Intent intent = new Intent(this, DisplayList.class);
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
