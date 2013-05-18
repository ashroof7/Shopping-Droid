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
		BitSet isFav = new BitSet(data.size());
		
		adapter = new ListAdapter(this, data, isFav);
		listView.setAdapter(adapter);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		data =  this.getIntent().getParcelableArrayListExtra("data");
		initList();
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// reflect changed products in Database
		TreeSet<String> changed = adapter.getRemovedItems();
		Iterator<String> it = changed.iterator();
		while (it.hasNext()){
			String p = it.next();
			if (p == null ) // in case the list is not used for favourite or history 
				continue;
			MainActivity.db.deleteFavourite(p);
		}

	}

}
