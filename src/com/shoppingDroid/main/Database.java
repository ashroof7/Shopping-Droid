package com.shoppingDroid.main;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	// FIXME copy this values to @+res/string
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// table of history
	private static final String table_history = "History";
	// table of store
	private static final String table_stores = "stores";
	// table of favourites
	private static final String table_favourites = "favourites";
	// Columns names
	private static final String bar_code = "bar_code";
	private static final String h_id = "h_id";
	private static final String name = "product_name";
	private static final String type_name = "type_name";
	private static final String store = "store";
	private static final String store_id = "store_id";
	private static final String store_name = "store_name";

	public Database(Context context, String DATABASE_NAME) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String Create_stores = "CREATE TABLE " + table_stores + "(" + store_id
				+ " INTEGER PRIMARY KEY," + store_name + " varchar(45)" + ")";
		db.execSQL(Create_stores);
		String create_history = "CREATE TABLE " + table_history + "(" + h_id
				+ " INTEGER PRIMARY KEY autoincrement ," + bar_code
				+ " varchar(45) UNIQUE, " + name + " varchar(45), " + type_name
				+ " varchar(45)," + store + " INTEGER NOT NULL ,"
				+ "FOREIGN KEY (" + store + ") REFERENCES " + table_stores
				+ " ( " + store_id + " ) " + ")";
		db.execSQL(create_history);
		String create_favourites = "CREATE TABLE " + table_favourites + "("

		+ bar_code + " varchar(45) PRIMARY KEY, " + name + " varchar(45), "
				+ type_name + " varchar(45)," + store + " INTEGER NOT NULL ,"
				+ "FOREIGN KEY (" + store + ") REFERENCES " + table_stores
				+ " ( " + store_id + " ) " + ")";
		db.execSQL(create_favourites);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + table_stores);
		db.execSQL("DROP TABLE IF EXISTS " + table_history);
		db.execSQL("DROP TABLE IF EXISTS " + table_favourites);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void addHistory(Product product) {
		SQLiteDatabase Rdb = this.getReadableDatabase();

		Cursor mCount = Rdb.rawQuery("select count(*) from " + table_history,
				null);

		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		if (count >= 20) {
			deleteOldest();
		}
		Rdb.close();

		SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase Rdb2 = this.getReadableDatabase();
		ContentValues values = new ContentValues();

		Cursor s = Rdb2.query(table_stores, new String[] { store_name },
				store_id + "=?",
				new String[] { String.valueOf(product.getStoreId()) }, null,
				null, null, null);
		if (s.getCount() == 0) {
			values = new ContentValues();
			values.put(store_id, product.getStoreId());
			values.put(store_name, product.getStoreName());

			// Inserting Row
			db.insert(table_stores, null, values);
		}
		s.close();

		values = new ContentValues();
		values.put(bar_code, product.getBarcode()); // barcode
		values.put(name, product.getName()); // name
		values.put(type_name, product.getTypeName());
		values.put(store, product.getStoreId());

		// Inserting Row
		db.insert(table_history, null, values);
		db.close(); // Closing database connection
		Rdb2.close();
		Log.i("Database", "Inserted in history");
	}

	public void addFavourites(Product product) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values = new ContentValues();
		values.put(bar_code, product.getBarcode()); // barcode
		values.put(name, product.getName()); // name
		values.put(type_name, product.getTypeName());
		values.put(store, product.getStoreId());
		db.insert(table_favourites, null, values);
		db.close(); // Closing database connection
		Log.i("Database", "Inserted in favorites");
	}

	// Getting All Contacts
	public List<Product> retreive() {
		List<Product> products = new ArrayList<Product>();
		// Select All Query
		String selectQuery = "select a.*, c." + store_name + " from "
				+ table_history + " a, " + table_stores + " c  where a."
				+ store + " = c." + store_id;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				products.add( new Product(cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						Integer.parseInt(cursor.getString(4)),
						cursor.getString(5)));
			} while (cursor.moveToNext());
		}

		return products;
	}

	public TreeSet<String> favoritesInHistory() {
		TreeSet<String> barcodes = new TreeSet<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "select a." + bar_code + " from " + table_history
				+ " a inner join " + table_favourites + " b where a."
				+ bar_code + " = b." + bar_code;

		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				barcodes.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		return barcodes;
	}

	public List<Product> retreiveFavourites() {
		List<Product> products = new ArrayList<Product>();
		// Select All Query
		Log.i("Database", "Retrieving Favorites");
		String selectQuery = "select a.*, c." + store_name + " from "
				+ table_favourites + " a, " + table_stores + " c  where a."
				+ store + " = c." + store_id;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				products.add( new Product(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						Integer.parseInt(cursor.getString(3)),
						cursor.getString(4)));
			} while (cursor.moveToNext());
		}
		Log.i("Database", "Retrieved Favorites");
		// return contact list
		return products;
	}

	// Deleting single contact
	public void deleteOldest() {
		SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase Rdb = this.getReadableDatabase();
		Cursor c = Rdb.query(table_history,
				new String[] { "min(" + h_id + ")" }, null, null, null, null,
				null);
		c.moveToFirst(); // ADD THIS!
		int rowID = c.getInt(0);
		db.delete(table_history, h_id + " = ?",
				new String[] { String.valueOf(rowID) });
		db.close();
		Rdb.close();
	}

	public void deleteFavourite(String code) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table_favourites, bar_code + " = ?", new String[] { code });
		db.close();
	}

}
