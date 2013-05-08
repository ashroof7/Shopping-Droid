package com.example.shd1;


import com.google.android.maps.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapsActivity extends Activity {
	
	
	private WebView webview;
	private double lattitude = -34.397;
	private double longitude = 150.644;
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        System.out.println("gowa el maps ");
        setContentView(R.layout.activity_map);
        setupWebView();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
    }
    
    private void setupWebView(){
    	Log.i("Mapviewer", "Map Using Javascript");
    	  final String centerURL = "javascript:centerAt(" +
    	  lattitude + "," +
    	  longitude+ ")";
    	  webview = (WebView) findViewById(R.id.webview);
    	  webview.addJavascriptInterface(new getStores(this), "AndroidFunction");
    	  webview.getSettings().setJavaScriptEnabled(true);
    	  //Wait for the page to load then send the location information
    	  webview.setWebViewClient(new WebViewClient(){
    	    @Override
    	    public void onPageFinished(WebView view, String url){
    	      webview.loadUrl(centerURL);
    	    }
    	  });
    	  webview.loadUrl("file:///android_asset/jscript.html");
    	}
      
  

}
