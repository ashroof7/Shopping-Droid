package com.example.shd1;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import Jasonparsing.ItemData;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class ListActivity extends Activity {

	private ListAdapter adapter;

	ArrayList<ItemData> data ;
	private void initList() {
		ListView listView = (ListView) findViewById(R.id.main_list_view);
		data = new ArrayList<ItemData>();
		
		List<Product> list = MainActivity.db.retreive();
		Iterator<Product> it = list.iterator();
		Product p;
		BitSet isFav = new BitSet(list.size());
		
		while (it.hasNext()) {
			p = it.next();
			ItemData d = new ItemData();
			d.put("barcode", p.getBar_code());
			d.put("store_name", p.getStore_name());
			d.put("product_name", p.getName());
			d.put("product_type", p.getType_name());
			d.put("product_price", "");
			data.add(d);
		}

		adapter = new ListAdapter(this, data, isFav);
		listView.setAdapter(adapter);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		data =  savedInstanceState.getParcelableArrayList("data");
		initList();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//FIXME mafeech menu aslun lel class dah we ba3deen sheel el menus men kol el activities
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// reflect changed products in Database
		TreeSet<String> changed = adapter.getRemovedItems();
		Log.wtf("list", "barcodes.size = "+changed.size());
		Iterator<String> it = changed.iterator();
		System.out.println(adapter.isFavorite);
		while (it.hasNext()){
			String p = it.next();
			System.out.println(p);
			MainActivity.db.deleteFavourite(p);
		}

	}

}
