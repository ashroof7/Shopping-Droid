package com.shoppingDroid.main;

import com.shoppingDriod.main.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


public class TabsActivity  extends Activity {

	public static Context appContext;
	DataFetcher df ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        appContext = getApplicationContext();
        String barcode = getIntent().getStringExtra(MainActivity.BarCode);
        
        //get location
        MainActivity.location.updateLocation();
		Location loc = MainActivity.location.getLastLocation();
		double lat = loc.getLatitude();
		double lng = loc.getLongitude();
        
        // datafetcher
        df = new DataFetcher(this, lat, lng, barcode);
        
        if (!df.locateStore()){
        	// error happend
        	Toast.makeText(this, "Could not resolve location", Toast.LENGTH_LONG).show();
        	finish();
        }
        
       //ActionBar
        ActionBar actionbar = getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab currentTab = actionbar.newTab().setText(R.string.sec_current);
        ActionBar.Tab simHereTab = actionbar.newTab().setText(R.string.sec_sim_here);
        ActionBar.Tab sameETab = actionbar.newTab().setText(R.string.sec_same_everyw);
        ActionBar.Tab simETab = actionbar.newTab().setText(R.string.sec_sim_everyw);
        
        currentTab.setTabListener(new MyTabsListener(new ListFragment()));
        simHereTab.setTabListener(new MyTabsListener(new ListFragment()));
        sameETab.setTabListener(new MyTabsListener(new ListFragment()));
        simETab.setTabListener(new MyTabsListener(new ListFragment()));

        actionbar.addTab(currentTab);
        actionbar.addTab(simHereTab);
        actionbar.addTab(sameETab);
        actionbar.addTab(simETab);
    }
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }
    
}



class MyTabsListener implements ActionBar.TabListener {
	public Fragment fragment;

	public MyTabsListener(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Toast.makeText(TabsActivity.appContext, "Reselected!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
//		ft.replace(R.id.fragment_container, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}

}
	