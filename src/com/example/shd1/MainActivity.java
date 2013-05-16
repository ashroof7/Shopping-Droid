package com.example.shd1;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import location.LocationMan;
import location.LocationFetcher;
import location.LocationTracker;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.client.android.Intents.Scan;

import onlineDB.DBDispatcher;
import Jasonparsing.ItemData;
import Jasonparsing.JsonParser;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView scanState;
	public final static String BarCode = "com.example.MainActivity.MESSAGE";
	
	public static Database db ;
	public static LocationMan locTest; //FIXME rename 
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
		db = new Database(this, "The Offline Database");
        scanState =  (TextView) findViewById(R.id.textView1);
        
        locTest = new LocationMan(this);
        locTest.updateLocation();
        scanState.setText("Press on the button to start scan");
        try{
            Product p1 = new Product("11", "ay7aga2", "biscuits", 5, "carfore");
    		
    		Log.d("Insert: ", "Inserting in History..");
    		db.addHistory(p1);
    		Log.d("Insert: ", "Inserting in Favorites..");
    		db.addFavourites(p1);
    }catch(android.database.sqlite.SQLiteConstraintException ex){
    }
        
        
        
//        Product p1 = new Product("10", "ay7aga", "biscuits", 1, 5, "carfore",		"somoha");	
//		db.addFavourites(p1);
//		List<Product> s2 = db.retreiveFavourites();
        
        
//        Product p1 = new Product("1barcode", "product name", "type name", 1, 1, "store_1", "ay 7etta");
        Product p1 = new Product("11", "ay7aga2", "biscuits", 5, "carfore");
		db.addFavourites(p1);
		List<Product> favs = db.retreiveFavourites();
		Log.wtf("main Act", "favlist size = "+favs.size());
//		Iterator<Product> it = favs.iterator();
        System.out.println("db main foo2 "+db);
    }
	
	public void getLocation(View view){
//		LocationTracker lt = new LocationTracker(this);
//        ((TextView)( findViewById(R.id.locationNowText))).setText(
//		"enta makanak feen now ?? \n"+lt.getLatitude()+" \n "+lt.getLongitude());
		locTest.updateLocation();
		((TextView)( findViewById(R.id.locationNowText))).setText(locTest.getLastLocation()+"");
	}
	
		
	public void testDB(View view){
		DBDispatcher d = new DBDispatcher(this);
		JsonParser jp = new JsonParser();
		JSONObject ob =null;
		try {	
			ob = d.productGlobal("6");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		ListView listView = (ListView) findViewById(R.id.listView);
		ArrayList<ItemData> data = jp.parse(ob);
		BitSet fav = new BitSet();
//		ListAdapter adapter = new ListAdapter(this, data, fav);
//		listView.setAdapter(adapter);
		Intent intent = new Intent(this, ListActivity.class);
		startActivity(intent);
		
	} 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void scanNow(View view)
    {
    	 Intent intent2 = new Intent(this, Scan_to_display.class);
         intent2.putExtra(BarCode, "1");
     	startActivity(intent2);

//    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//    intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
//	Log.i("xZing", "Start Scan");
//    startActivityForResult(intent, 0);
    }
    
    public void launchMap(View view){
    	Intent i = new Intent(this, MapsActivity.class);
		startActivity(i);
    }
    
    public void viewFav(View view){
    	Intent i = new Intent(this, FavoriteActivity.class);
    	startActivity(i);
    }
    
    public void viewHistory(View view){
    	Intent i = new Intent(this, HistoryActivity.class);
    	System.out.println("Db in main ta7t"+db);
    	startActivity(i);
    }
    
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 0)
        {
        	Log.i("xZing", "Back From Scanning");
            if (resultCode == RESULT_OK)
            {
            	Log.i("xZing", "Scan Successful");
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
             //   Log.i("xZing", "contents: "+contents+" format: "+format);
                Intent intent2 = new Intent(this, Scan_to_display.class);
                intent2.putExtra(BarCode, contents);
            	startActivity(intent2);

                // Handle successful scan
            }
            else if (resultCode == RESULT_CANCELED)
            {
                // Handle cancel
               // Log.i("xZing", "Cancelled");
            }
        }
    }
    
    
    @Override
    protected void onDestroy() {
    	Log.wtf("Main", "ana etmarmant ya rady");
    	super.onDestroy();
    }
}
