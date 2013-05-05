package com.example.shd1;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import onlineDB.DBDispatcher;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView scanState;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
        scanState =  (TextView) findViewById(R.id.textView1);
        //TODO swap withe R.string
        scanState.setText("Press on the button to start scan");
    }
	
	
	public void testDB(View view){
		DBDispatcher d = new DBDispatcher(this);
		JSONObject ob ;
		String ret ;
		
		try {
			ob = d.stores();
			ret = ob.toString(4);
			System.out.println(ret);
			((TextView)findViewById(R.id.textView2)).setText(ret);
			 						
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	} 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void scanNow(View view)
    {
    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
    intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
	Log.i("xZing", "Start Scan");
    startActivityForResult(intent, 0);
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
                scanState.setText("contents: "+contents+" format: "+format);
                // Handle successful scan
            }
            else if (resultCode == RESULT_CANCELED)
            {
                // Handle cancel
               // Log.i("xZing", "Cancelled");
            }
        }
    }
    
}
