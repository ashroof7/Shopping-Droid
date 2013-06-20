package com.shoppingDroid.main;

import com.shoppingDriod.main.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TabsActivity extends Activity implements LoaderCallbacks<Boolean> {

	public static Context appContext;
	public static DataFetcher df;
	private static String scannedBarcode;
	ScannedFragment curFrag;
	private ListFragment simHereFrag, sameEFrag, simEFrag;
	MyTabsListener tabsListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tabs);
		appContext = getApplicationContext();
		scannedBarcode = getIntent().getStringExtra(MainActivity.BarCode);

		MainActivity.location.updateLocation();
		System.out.println(MainActivity.location);
		Location loc = MainActivity.location.getLastLocation();
		df = new DataFetcher(this, loc.getLatitude(), loc.getLongitude(),
				scannedBarcode);

		Log.wtf("tabs Activity", "OnCreate -- before call");
		getLoaderManager().initLoader(0, null, this).forceLoad();
		Log.wtf("tabs Activity", "OnCreate -- after call");
		
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
		
//		df.locateStore();
		
	
		
	}

	@Override
	public Loader<Boolean> onCreateLoader(int id, Bundle args) {
		return new TabsLoader(this, df);
	}

	@Override
	public void onLoadFinished(Loader<Boolean> arg0, Boolean res) {

		if (!res) {
			// error happened
			Toast.makeText(this, getString(R.string.diag_no_stores),
					Toast.LENGTH_LONG).show();
			finish();
		}
		
		
		Log.w("tabs activity", "set invisible");
		
		getFragmentManager().beginTransaction().detach(curFrag).commitAllowingStateLoss();
		getFragmentManager().beginTransaction().attach(curFrag).commitAllowingStateLoss();
		
		findViewById(R.id.frag_containter).setVisibility(View.VISIBLE);
		findViewById(R.id.tabs_progress).setVisibility(View.INVISIBLE);
		Log.wtf("Tabs Activity", "On Load Finished");
	}

	@Override
	public void onLoaderReset(Loader<Boolean> arg0) {
	}

	class MyTabsListener implements ActionBar.TabListener {

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO you may need to refetch from server here
			Toast.makeText(TabsActivity.appContext, "Reselected!",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			switch (tab.getPosition()) {
			case 0:
				if (curFrag == null) {
					curFrag = new ScannedFragment();
					curFrag.setDataFetcher(df);
					ft.add(R.id.frag_containter, curFrag);
				} else {
					Log.wtf("attach", "attaching ya teet");
					ft.attach(curFrag);
				}
				Log.wtf("cur", curFrag + "");
				break;

			case 1:
				if (simHereFrag == null) {
					simHereFrag = new ListFragment();
					simHereFrag.setDataFetcher(df, ListLoader.SIMILAR_HERE);
					ft.add(R.id.frag_containter, simHereFrag);
				} else {
					ft.attach(simHereFrag);
				}
				Log.wtf("sim", simHereFrag + "");
				break;
			case 2:
				if (sameEFrag == null) {
					sameEFrag = new ListFragment();
					sameEFrag.setDataFetcher(df, ListLoader.SAME_HERE);
					ft.add(R.id.frag_containter, sameEFrag);
				} else {

					ft.attach(sameEFrag);
				}
				Log.wtf("same", sameEFrag + "");
				break;
			case 3:
				if (simEFrag == null) {
					simEFrag = new ListFragment();
					simEFrag.setDataFetcher(df, ListLoader.SIMILAR_EVERYWHERE);
					ft.add(R.id.frag_containter, simEFrag);
				} else {
					ft.attach(simEFrag);
				}
				Log.wtf("simE", simEFrag + "");
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
				Log.wtf("cur", "detach");
				break;
			case 1:
				ft.detach(simHereFrag);
				Log.wtf("sim", "detach");
				break;
			case 2:
				ft.detach(sameEFrag);
				Log.wtf("same", "detach");
				break;
			case 3:
				ft.detach(simEFrag);
				Log.wtf("simE", "detach");
				break;
			default:
				break;
			}
		}

	}

	}

class TabsLoader extends AsyncTaskLoader<Boolean> {
	DataFetcher df ;
	
	public TabsLoader(Context context, DataFetcher df) {
		super(context);
		this.df = df ;
	}

	@Override
	public Boolean loadInBackground() {
		Log.wtf("Tabs Loader", "Load in background");
		return df.locateStore();
	}
}
