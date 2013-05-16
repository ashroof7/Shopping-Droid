package com.example.shd1;



import com.google.android.maps.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapsActivity extends Activity {
	
	
	private WebView webview;
	ProgressDialog pgDiagWebView;
	
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        System.out.println("gowa el maps ");
        setContentView(R.layout.activity_map);
        pgDiagWebView = ProgressDialog.show(this, "Loading", "Wait", true);
        setupWebView();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        webview.setWebViewClient(new WebViewClient(){
    	    @Override
    	    public void onPageFinished(WebView view, String url){
    	    	 super.onPageFinished(view, url);
    	         pgDiagWebView.dismiss();

    	    }
    	  });
    }
    
    private void setupWebView(){
    	Log.i("Mapviewer", "Map Using Javascript");
    	 webview = (WebView) findViewById(R.id.webview);
   	  webview.addJavascriptInterface(new getStores(this), "AndroidFunction");
   	  webview.getSettings().setJavaScriptEnabled(true);   	 
   	  webview.loadUrl("file:///android_asset/jscript.html");
    	}
      
  

}
