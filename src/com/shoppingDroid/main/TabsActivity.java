package com.shoppingDroid.main;

import java.util.ArrayList;

import com.shoppingDriod.main.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class TabsActivity extends Activity {

	public static Context appContext;
	public static DataFetcher df;

	private ListFragment curFrag, simHereFrag, sameEFrag, simEFrag;
	MyTabsListener tabsListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);
		appContext = getApplicationContext();
		String barcode = getIntent().getStringExtra(MainActivity.BarCode);

		// get location
		MainActivity.location.updateLocation();
		Location loc = MainActivity.location.getLastLocation();
		double lat = loc.getLatitude();
		double lng = loc.getLongitude();

		System.out.println(lat + "  " + lng);
		// datafetcher
		df = new DataFetcher(this, lat, lng, barcode);

		if (!df.locateStore()) {
			// error happend
			Toast.makeText(this, "Could not get current store",
					Toast.LENGTH_LONG).show();
			finish();
		}

		// ActionBar
		ActionBar actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab currentTab = actionbar.newTab().setText(
				R.string.sec_current);
		ActionBar.Tab simHereTab = actionbar.newTab().setText(
				R.string.sec_sim_here);
		ActionBar.Tab sameETab = actionbar.newTab().setText(
				R.string.sec_same_everyw);
		ActionBar.Tab simETab = actionbar.newTab().setText(
				R.string.sec_sim_everyw);

		tabsListener = new MyTabsListener();

		currentTab.setTabListener(tabsListener);
		simHereTab.setTabListener(tabsListener);
		sameETab.setTabListener(tabsListener);
		simETab.setTabListener(tabsListener);

		actionbar.addTab(currentTab);
		actionbar.addTab(simHereTab);
		actionbar.addTab(sameETab);
		actionbar.addTab(simETab);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab_index", getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		getActionBar().setSelectedNavigationItem(
				savedInstanceState.getInt("tab_index"));
		
	}
	
	
	class MyTabsListener implements ActionBar.TabListener {
		
		public MyTabsListener() {
			Log.wtf("TabL", "elbes dah bey3mel ob gedeed 3and el rotation");
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(TabsActivity.appContext, "Reselected!",
					Toast.LENGTH_LONG).show();
		}


		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// ft.replace(R.id.fragment_container, fragment);
			// FIXME search for a better code style
			switch (tab.getPosition()) {
			case 0:
				if (curFrag == null) {
					curFrag = new ListFragment();
					TabsActivity.df.here();
					curFrag.setData(df.getData());
					curFrag.setRetainInstance(true);
					ft.add(R.id.frag_containter, curFrag);
				} else {
					ft.attach(curFrag);
				}
				Log.wtf("cur", curFrag+"");
				break;
			case 1:
				if (simHereFrag == null) {
					simHereFrag = new ListFragment();
					TabsActivity.df.similarHere();
					simHereFrag.setData(df.getData());
					simHereFrag.setRetainInstance(true);
					ft.add(R.id.frag_containter, simHereFrag);
			} else {
					ft.attach(simHereFrag);
				}
				Log.wtf("sim", simHereFrag+"");
				break;
			case 2:
				if (sameEFrag == null) {
					sameEFrag = new ListFragment();
					TabsActivity.df.sameEverywhere();
					sameEFrag.setData(df.getData());
					sameEFrag.setRetainInstance(true);
					ft.add(R.id.frag_containter, sameEFrag);

				} else {
					ft.attach(sameEFrag);
				}
				Log.wtf("same", sameEFrag+"");
				break;
			case 3:
				if (simEFrag == null) {
					simEFrag = new ListFragment();
					TabsActivity.df.similarEverywhere();
					simEFrag.setData(df.getData());
					simEFrag.setRetainInstance(true);
					ft.add(R.id.frag_containter, simEFrag);

				} else {
					ft.attach(simEFrag);
				}
				Log.wtf("simE", simEFrag+"");
				break;
			default:
				break;
			}

		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			switch (tab.getPosition()) {
			case 0:
				ft.detach(curFrag);
				Log.wtf("cur","detach");
				break;
			case 1:
				ft.detach(simHereFrag);
				Log.wtf("sim","detach");
				break;
			case 2:
				ft.detach(sameEFrag);
				Log.wtf("same","detach");
				break;
			case 3:
				ft.detach(simEFrag);
				Log.wtf("simE","detach");
				break;
			default:
				break;
			}
		}

	}
}