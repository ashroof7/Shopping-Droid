package com.shoppingDroid.main;

import java.util.BitSet;
import java.util.List;

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
		adapter = new ListAdapter(this, history);
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
	public void onDestroy() {
		super.onDestroy();
		// reflect changed products in Database
		List<? extends ViewItem> data = adapter.getData();
		BitSet curFav = adapter.getCurrentFav();
		Product p ;
		for (int i = 0; i < data.size(); i++) {
			p = (Product) data.get(i);
			if (!p.isFav() && curFav.get(i)) {
				MainActivity.db.addFavourites(p);
			} else if (p.isFav() && !curFav.get(i)) {
				MainActivity.db.deleteFavourite(p.getBarcode());
			}
		}
	}


	

}
