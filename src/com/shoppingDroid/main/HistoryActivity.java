package com.shoppingDroid.main;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.shoppingDriod.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class HistoryActivity extends Activity {

	private ListAdapter adapter;

	private void initList() {
		ListView listView = (ListView) findViewById(R.id.history_list);
		List<Product> history = MainActivity.db.retreive();
		Iterator<Product> it = history.iterator();
		BitSet isFav = new BitSet(history.size());
		TreeSet<String> favInHis = MainActivity.db.favoritesInHistory();
		
		int i =0 ;
		while (it.hasNext()) {
			if (favInHis.contains(it.next().getBarcode()))
				isFav.set(i);
			i++;
		}

		adapter = new ListAdapter(this, history, isFav);
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
		Iterator<String> it = changed.iterator();
		while (it.hasNext()){
			String p = it.next();
			MainActivity.db.deleteFavourite(p);
		}
		

		}

	

}
