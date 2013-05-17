package com.example.shd1;

import location.LocationMan;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
		db = new Database(this, "The Offline Database");
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
	    intent = new Intent(this, ScanToDisplay.class);
	    // TODO what is that one 
        intent.putExtra(BarCode, "1");
    	startActivity(intent);
	}
	
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	
	
	
}
