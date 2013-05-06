package com.example.shd1;
import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "Offline Database";

	// table of history
	private static final String table_history = "History";
	// table of type
	private static final String table_type = "Type";
	// table of store
	private static final String table_stores = "stores";
	// table of favourites
	private static final String table_favourites = "favourites";

	//  Columns names
	private static final String bar_code = "bar_code";
	private static final String h_id = "h_id";
	private static final String name = "name";
	private static final String type_name = "type_name";
	private static final String type_id = "type_id";
	private static final String type = "type";
	private static final String store = "store";
	private static final String store_id = "store_id";
	private static final String store_name = "store_name";
	private static final String store_address = "store_address";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String Create_types = "CREATE TABLE " + table_type + "(" + type_id
				+ " INTEGER PRIMARY KEY," + type_name + " varchar(45)" + ")";
		db.execSQL(Create_types);
		String Create_stores = "CREATE TABLE " + table_stores + "(" + store_id
				+ " INTEGER PRIMARY KEY," + store_name + " varchar(45),"
				+ store_address + " varchar(100)" + ")";
		db.execSQL(Create_stores);
		String create_history = "CREATE TABLE " + table_history + "(" + h_id
				+ " INTEGER PRIMARY KEY autoincrement ," + bar_code
				+ " INTEGER UNIQUE, " + name + " varchar(45), " + type
				+ " INTEGER NOT NULL ," + store + " INTEGER NOT NULL ,"
				+ "FOREIGN KEY (" + type + ") REFERENCES " + table_type + " ( "
				+ type_id + " ), " + "FOREIGN KEY (" + store + ") REFERENCES "
				+ table_stores + " ( " + store_id + " ) " + ")";
		db.execSQL(create_history);
		String create_favourites = "CREATE TABLE " + table_favourites + "("

		+ bar_code + " INTEGER PRIMARY KEY, " + name + " varchar(45), " + type
				+ " INTEGER NOT NULL ," + store + " INTEGER NOT NULL ,"
				+ "FOREIGN KEY (" + type + ") REFERENCES " + table_type + " ( "
				+ type_id + " ), " + "FOREIGN KEY (" + store + ") REFERENCES "
				+ table_stores + " ( " + store_id + " ) " + ")";
		db.execSQL(create_favourites);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + table_type);
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
		Cursor mCount = Rdb.rawQuery("select count(*) from "+table_history, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		if (count >= 2){
			deleteOldest();}
			Rdb.close();

			SQLiteDatabase db = this.getWritableDatabase();
			SQLiteDatabase Rdb2 = this.getReadableDatabase();
		ContentValues values = new ContentValues();

		Cursor t = Rdb2.query(table_type, new String[] { type_name }, type_id
				+ "=?", new String[] { String.valueOf(product.getType_id()) },
				null, null, null, null);
		if (t.getCount() == 0) {
			values = new ContentValues();
			values.put(type_id, product.getType_id()); // type_id
			values.put(type_name, product.getType_name()); // type_name

			// Inserting Row
			db.insert(table_type, null, values);
		}
		t.close();
		Cursor s = Rdb2.query(table_stores, new String[] { store_name },
				store_id + "=?",
				new String[] { String.valueOf(product.getStore_id()) }, null,
				null, null, null);
		if (s.getCount() == 0) {
			values = new ContentValues();
			values.put(store_id, product.getStore_id());
			values.put(store_name, product.getStore_name());
			values.put(store_address, product.getStore_adress());

			// Inserting Row
			db.insert(table_stores, null, values);
		}
		s.close();

		values = new ContentValues();
		values.put(bar_code, product.getBar_code()); // barcode
		values.put(name, product.getName()); // name
		values.put(type, product.getType_id());
		values.put(store, product.getStore_id());

		// Inserting Row
		db.insert(table_history, null, values);
		db.close(); // Closing database connection
		Rdb2.close();
	}

	public void addFavourites(Product product) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values = new ContentValues();
		values.put(bar_code, product.getBar_code()); // barcode
		values.put(name, product.getName()); // name
		values.put(type, product.getType_id());
		values.put(store, product.getStore_id());
		db.insert(table_favourites, null, values);
		db.close(); // Closing database connection
	}

	
	// Getting All Contacts
	public List<Product> retreive() {
		List<Product> products = new ArrayList<Product>();
		// Select All Query
		String selectQuery = "select a.*,b." + type_name + " , c." + store_name
				+ " ,c." + store_address + " from " + table_history + " a, "
				+ table_type + "" + " b, " + table_stores + " c  where a."
				+ type + "=b." + type_id + " AND a." + store + " = c."
				+ store_id;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Product p = new Product();
				p.setBar_code(Integer.parseInt(cursor.getString(1)));
				p.setName(cursor.getString(2));
				p.setType_id(Integer.parseInt(cursor.getString(3)));
				p.setStore_id(Integer.parseInt(cursor.getString(4)));
				p.setType_name(cursor.getString(5));
				p.setStore_name(cursor.getString(6));
				p.setStore_adress(cursor.getString(7));
				// Adding contact to list
				// p=getProduct_store(p);
				products.add(p);
			} while (cursor.moveToNext());
		}

		// return contact list
		return products;
	}

	public List<Product> retreiveFavourites() {
		List<Product> products = new ArrayList<Product>();
		// Select All Query
		String selectQuery = "select a.*,b." + type_name + " , c." + store_name
				+ " ,c." + store_address + " from " + table_favourites + " a, "
				+ table_type + "" + " b, " + table_stores + " c  where a."
				+ type + "=b." + type_id + " AND a." + store + " = c."
				+ store_id;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Product p = new Product();
				p.setBar_code(Integer.parseInt(cursor.getString(0)));
				p.setName(cursor.getString(1));
				p.setType_id(Integer.parseInt(cursor.getString(2)));
				p.setStore_id(Integer.parseInt(cursor.getString(3)));
				p.setType_name(cursor.getString(4));
				p.setStore_name(cursor.getString(5));
				p.setStore_adress(cursor.getString(6));
				// Adding contact to list
				// p=getProduct_store(p);
				products.add(p);
			} while (cursor.moveToNext());
		}

		// return contact list
		return products;
	}


	// Deleting single contact
	public void deleteOldest() {Log.d("//////////", "DELETE1");
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

    public void deleteFavourite(int code) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_favourites, bar_code + " = ?",
                new String[] { String.valueOf(code) });
        db.close();
    }
 

}
