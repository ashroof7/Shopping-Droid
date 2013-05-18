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

public class MainActivity_test extends Activity {

	private TextView scanState;
	public final static String BarCode = "com.example.MainActivity.MESSAGE";
	
	public static Database db ;
	public static LocationMan locTest; //FIXME rename 
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main_test);
		db = new Database(this, "The Offline Database");
        scanState =  (TextView) findViewById(R.id.textView1);
        
        locTest = new LocationMan(this);
        locTest.updateLocation();
        scanState.setText("Press on the button to start scan");
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
		
		ListView listView = (ListView) findViewById(R.id.main_list_view);
		ArrayList<ItemData> data = jp.parse(ob);
		BitSet fav = new BitSet();
		ListAdapter adapter = new ListAdapter(this, data, fav);
		listView.setAdapter(adapter);
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
    	 Intent intent2 = new Intent(this, ScanToDisplay.class);
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
                Intent intent2 = new Intent(this, ScanToDisplay.class);
                intent2.putExtra(BarCode, contents);
            	startActivity(intent2);
            }
            else if (resultCode == RESULT_CANCELED)
            {
                // TODO Handle cancel
            }
        }
    }
    
    
    @Override
    protected void onDestroy() {
    	Log.wtf("Main", "ana etmarmant ya rady");
    	super.onDestroy();
    }
}
