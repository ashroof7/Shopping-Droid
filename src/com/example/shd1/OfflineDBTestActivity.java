package com.example.shd1;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

public class OfflineDBTestActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_db);

		Database db = new Database(this, "Offline Database");
		Product p1 = new Product("8", "lambada", "biscuits", 5, "carfore");
		Product p2 = new Product("9", "moro", "choclete",  6, "carfore");
		Product p3 = new Product("28", "kranchy", "chips",  15, "carfore");
		Product p4 = new Product("18", "vaio", "laptop", 5, "carfore");
		Product p5 = new Product("38", "pmw", "car", 15, "carfore");
		Product p6 = new Product("88", "borio", "biscuits", 15, "carfore");
		/**
		 * CRUD Operations
		 * */
		// Inserting Contacts
		Log.d("Insert: ", "Inserting ..");
		db.addHistory(p1);
		db.addHistory(p2);
		db.addHistory(p3);
		db.addHistory(p4);
		db.addHistory(p5);
		Log.d("Reading: ", "FAVOURITESSSS..");
		db.addFavourites(p5);
		// Reading all contacts;
		Log.d("Reading: ", "Reading all contacts..");
		List<Product> s = db.retreive();

		for (Product cn : s) {
			String log = "Id: " + cn.getBar_code() + " ,Name: " + cn.getName()
					+  ",type name "
					+ cn.getType_name()+" storen "+cn.getStore_name()+" SOREID "+cn.getStore_id();
			System.out.println("//////////////" + log);
			// Writing Contacts to log
			Log.d("Name: ", log);
		}
		List<Product> s2 = db.retreiveFavourites();

		for (Product cn : s2) {
			String log = "Id: " + cn.getBar_code() + " ,Name: " + cn.getName()
					+  ",type name "
					+ cn.getType_name()+" storen "+cn.getStore_name()+" SOREID "+cn.getStore_id();
			System.out.println("//////////////" + log);
			// Writing Contacts to log
			Log.d("Name: ", log);
		}
	}
}
