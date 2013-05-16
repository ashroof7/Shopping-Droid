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

public class HistoryActivity extends Activity {

	private ListAdapter adapter;

	private void initList() {
		ListView listView = (ListView) findViewById(R.id.history_list);
		ArrayList<ItemData> data = new ArrayList<ItemData>();
		
		System.out.println("db in history " + MainActivity.db);
		List<Product> history = MainActivity.db.retreive();
		Log.wtf("history", "history_list = "+history.size());
		 
		Iterator<Product> it = history.iterator();
		Product p;
		BitSet isFav = new BitSet(history.size());
		System.out.println(isFav);
		TreeSet<String> favInHis = MainActivity.db.favoritesInHistory();
		
		int i =0 ;
		while (it.hasNext()) {
			p = it.next();
			ItemData d = new ItemData();
			d.put("barcode", p.getBar_code());
			d.put("product_name", p.getName());
			d.put("product_type", p.getType_name());
			d.put("product_price", "");
			data.add(d);
			if (favInHis.contains(p.getBar_code()))
				isFav.set(i);
			i++;
		}

		adapter = new ListAdapter(this, data, isFav);
		System.out.println(adapter);
		listView.setAdapter(adapter);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		initList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
