package com.shoppingDroid.main;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.TreeSet;

import com.shoppingDriod.main.R;
import com.shoppingDroid.jsonParsing.ItemData;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
 
public class ListAdapter extends BaseAdapter {
 
	private ArrayList<ItemData> data; //array of list data
	//FIXME change to private
    public BitSet isFavorite;
    
    private TreeSet<String> removedItems;
    
    private static LayoutInflater inflater = null;
    private int index ;
    
    public ListAdapter(Activity activity, ArrayList<ItemData> data, BitSet isFavorite) {
    	this.data = data;
        this.index = 0;
        this.isFavorite = isFavorite;
        removedItems = new TreeSet<String>();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public TreeSet<String> getRemovedItems(){
    	return removedItems;
    } 
    
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return data.get(index);
    }
 
    public long getItemId(int position) {
    	//TODO
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        
        if(convertView == null)
            vi = inflater.inflate(R.layout.row, null);
        // Setting all values in listview
        ((TextView)vi.findViewById(R.id.row_text_1)).setText(data.get(position).getTitle()); // title
        ((TextView)vi.findViewById(R.id.row_text_2)).setText(data.get(position).getSubText()); // subtext
        ((TextView)vi.findViewById(R.id.row_price)).setText(data.get(position).getRightText()); // r_text
        CheckBox box = ((CheckBox)vi.findViewById(R.id.row_fav));
        box.setChecked(isFavorite.get(position));
        final String barcode = data.get(position).getValue("barcode");
        
        box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (barcode == null )
					return;
				if (isChecked ){
					removedItems.remove(barcode);
				}else {
					removedItems.add(barcode);
				}
			}
		});
        //TODO change picture here
        index++;
        return vi;
    }
}