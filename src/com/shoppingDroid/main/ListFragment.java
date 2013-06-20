package com.shoppingDroid.main;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import com.shoppingDriod.main.R;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;



public class ListFragment extends Fragment implements LoaderCallbacks<ArrayList<Product>>{
	
	private ListAdapter adapter;
	public static final String ARG_SECTION_NUMBER = "section_number";
	private DataFetcher df;
	private int choice = 1;
	private View listView ;
	
	
	public void setDataFetcher(DataFetcher df, int choice){
		this.df = df;
		this.choice = choice;
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getLoaderManager().initLoader(0, null, this).forceLoad();
		setRetainInstance(true);
	 	return listView =  inflater.inflate(R.layout.activity_display_list, container, false);
	}

	@Override
	public void onDestroy() {
		// reflect changed products in Database
		List<? extends ViewItem> data = adapter.getData();
		BitSet curFav = adapter.getCurrentFav();
		Product p ;
		for (int i = 0; i < data.size(); i++) {
			p = (Product) data.get(i);
			if (!p.isFav() && curFav.get(i)) {
				MainActivity.db.addFavourites(p);
			} else if (p.isFav() && !curFav.get(i)) {
				MainActivity.db.deleteFavourite(p.getBarcode());
			}
		}
		super.onDestroy();
	}


	
	@Override
	public Loader<ArrayList<Product>> onCreateLoader(int id, Bundle args) {
		return new ListLoader(getActivity(), df, choice);
	}
	


	@Override
	public void onLoadFinished(Loader<ArrayList<Product>> loader,
			ArrayList<Product> data) {
	        
	        if (data==null){
	        	Log.i("ListFragment", "No data returned");
	        	return ;
	        }
			adapter = new ListAdapter(this.getActivity(), data);
			((ListView) listView.findViewById(R.id.list_content)).setAdapter(adapter);
			
			listView.findViewById(R.id.list_progress).setVisibility(View.INVISIBLE);
			listView.findViewById(R.id.list_content).setVisibility(View.VISIBLE);
	}
	


	@Override
	public void onLoaderReset(Loader<ArrayList<Product>> arg0) {
	}

}



class ListLoader extends AsyncTaskLoader<ArrayList<Product>> {
	DataFetcher df;
	public static final int SIMILAR_HERE = 1;
	public static final int SAME_HERE = 2;
	public static final int SIMILAR_EVERYWHERE = 3;
	
	int choice ;
	
	public ListLoader(Context context, DataFetcher df, int choice) {
		super(context);
		this.df = df ;
		this.choice = choice;
	}

	@Override
	public ArrayList<Product> loadInBackground() {
		switch (choice) {
		case SIMILAR_HERE:
			return df.similarHere();
		case SAME_HERE:
			return df.sameEverywhere();
		case SIMILAR_EVERYWHERE:
			return df.similarEverywhere();
		default:
			return null;
		}
	}
}

