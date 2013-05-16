package com.example.shd1;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import Jasonparsing.ItemData;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class ListActivity extends Activity {
	
	private ListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		
		ListView listView = (ListView) findViewById(R.id.favorite_list);
		ArrayList<ItemData> data = new ArrayList<ItemData>();
				
		Product temp = new Product("1", "product name", "type name", 1, "store_1");
		MainActivity.db.addFavourites(temp);
		List<Product> favs = MainActivity.db.retreiveFavourites();
		System.out.println("favourite  = " +favs.size());
		Iterator<Product> it = favs.iterator();
		Product p;
		BitSet isFav = new BitSet(favs.size());
		isFav.set(0, favs.size());
		
		while(it.hasNext()){
			p = it.next();
			ItemData d= new ItemData();
			d.put("product_name", p.getName());
			d.put("product_type", p.getType_name());
			d.put("product_price", "");
			data.add(d);
		}
		
//		adapter = new ListAdapter(this, data, isFav);
		System.out.println(listView);
		listView.setAdapter(adapter);
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
		Iterator<String> it = changed.iterator();
		while(it.hasNext())
			MainActivity.db.deleteFavourite(it.next());
		
	}
	
}
