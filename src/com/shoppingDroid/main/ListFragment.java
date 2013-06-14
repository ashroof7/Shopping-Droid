package com.shoppingDroid.main;

import java.util.ArrayList;
import java.util.BitSet;

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
			BitSet isFav = new BitSet(data.size());
			adapter = new ListAdapter(this.getActivity(), data, isFav);
			listView.setAdapter(adapter);
			return v;
	    }
}
