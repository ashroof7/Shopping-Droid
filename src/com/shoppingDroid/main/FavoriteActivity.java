package com.shoppingDroid.main;

import java.util.BitSet;
import java.util.List;

import com.shoppingDriod.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class FavoriteActivity extends Activity {

	private ListAdapter adapter;

	private void initList() {
		ListView listView = (ListView) findViewById(R.id.favorite_list);
		List<? extends ViewItem> favs = MainActivity.db.retreiveFavourites();
		adapter = new ListAdapter(this, favs);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
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
