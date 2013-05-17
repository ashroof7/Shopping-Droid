package com.example.shd1;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapsActivity extends Activity {

	private WebView webview;
	ProgressDialog pgDiagWebView;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		pgDiagWebView = ProgressDialog.show(this, "Loading", "Wait", true);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		webview = (WebView) findViewById(R.id.webview);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pgDiagWebView.dismiss();
			}
		});
		webview.getSettings().setJavaScriptEnabled(true);
		webview.addJavascriptInterface(new getStores(this), "storesObject");
		webview.loadUrl("file:///android_asset/jscript.html");
		
	}


}
