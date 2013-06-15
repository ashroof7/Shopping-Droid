package com.shoppingDroid.main;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import com.shoppingDriod.main.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;



public class ListFragment extends Fragment{
	
	private ListAdapter adapter;
	public static final String ARG_SECTION_NUMBER = "section_number";
	private ArrayList<? extends ViewItem> data;
	
	public void setData(ArrayList<? extends ViewItem> data){
		this.data = data ;
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
		 	View v =  inflater.inflate(R.layout.activity_display_list, container, false);
	        ListView listView = (ListView) v.findViewById(R.id.results_list);
			adapter = new ListAdapter(this.getActivity(), data);
			listView.setAdapter(adapter);
			return v;
	    }

	@Override
	public void onDestroy() {
		super.onDestroy();
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
	}

}
