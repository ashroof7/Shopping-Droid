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

		Database db = new Database(this);
		Product p1 = new Product(8, "lambada", "biscuits", 1, 5, "carfore",
				"somoha");
		Product p2 = new Product(9, "moro", "choclete", 0, 6, "carfore",
				"mandara");
		Product p3 = new Product(28, "kranchy", "chips", 11, 15, "carfore",
				"ma3mora");
		Product p4 = new Product(18, "vaio", "laptop",55, 5, "carfore",
				"somoha");
		Product p5 = new Product(38, "pmw", "car", 7, 15, "carfore",
				"somoha");
		Product p6 = new Product(88, "borio", "biscuits", 1, 15, "carfore",
				"somoha");
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
					+ " ,type: " + cn.getType_id() + "type name "
					+ cn.getType_name()+" storen "+cn.getStore_name()+" storeadd "
					+cn.getStore_adress()+" SOREID "+cn.getStore_id();
			System.out.println("//////////////" + log);
			// Writing Contacts to log
			Log.d("Name: ", log);
		}
		List<Product> s2 = db.retreiveFavourites();

		for (Product cn : s2) {
			String log = "Id: " + cn.getBar_code() + " ,Name: " + cn.getName()
					+ " ,type: " + cn.getType_id() + "type name "
					+ cn.getType_name()+" storen "+cn.getStore_name()+" storeadd "
					+cn.getStore_adress()+" SOREID "+cn.getStore_id();
			System.out.println("//////////////" + log);
			// Writing Contacts to log
			Log.d("Name: ", log);
		}
	}
}
