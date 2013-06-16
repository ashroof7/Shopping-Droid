package com.shoppingDroid.main;

import com.shoppingDriod.main.R;
import com.shoppingDroid.location.LocationMan;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	public final static String BarCode = "com.example.MainActivity.MESSAGE";
	public static Database db ;
	public static LocationMan location;  
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new Database(this, "The Offline Database1");
        location = new LocationMan(this);
        location.updateLocation();
	}
		
	public void launchFavorites(View view){
		intent  = new Intent(this, FavoriteActivity.class);
    	startActivity(intent);
	}
	
	public void launchHistory(View view){
		intent = new Intent(this, HistoryActivity.class);
    	startActivity(intent);
	}
	
	public void launchMap(View view){
		intent = new Intent(this, MapsActivity.class);
		startActivity(intent);
	}
	
	public void launchScanner(View view){
//		  FIXME just a shortcut to jump to tabs activity
		 Intent intent2 = new Intent(this, TabsActivity.class);
        intent2.putExtra(BarCode, "6221053001809");
    	 startActivity(intent2);
	
//      Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//      intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
//  	  Log.i("xZing", "Start Scan");
//      startActivityForResult(intent, 0);
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
	                Intent intent2 = new Intent(this, TabsActivity.class);
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
	public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	
	
	
}
