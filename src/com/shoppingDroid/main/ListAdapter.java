package com.shoppingDroid.main;

import java.util.BitSet;
import java.util.List;
import com.shoppingDriod.main.R;
 
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
 
	private List<? extends ViewItem> data; //array of list data
    private static LayoutInflater inflater = null;
    private BitSet currentFav;
    private int index ;
    
    public ListAdapter(Activity activity, List<? extends ViewItem> data) {
    	this.data = data;
        this.index = 0;
       	currentFav = new BitSet(data.size());
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public BitSet getCurrentFav(){
    	return currentFav;
    } 
    
    public int getCount() {
        return data.size();
    }
    
    public List<? extends ViewItem> getData(){
    	return data;
    } 
    
    public Object getItem(int position) {
        return data.get(index);
    }
 
    public long getItemId(int position) {
    	//TODO
        return 0;
    }
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        
        if(convertView == null)
            vi = inflater.inflate(R.layout.row, null);
        // Setting all values in listview
        ((TextView)vi.findViewById(R.id.row_text_1)).setText(data.get(position).getTitle()); // title
        ((TextView)vi.findViewById(R.id.row_text_2)).setText(data.get(position).getSubText()); // subtext
        ((TextView)vi.findViewById(R.id.row_price)).setText(data.get(position).getRightText()); // r_text
        CheckBox box = ((CheckBox)vi.findViewById(R.id.row_fav));
        
        box.setChecked(((Product)data.get(position)).isFav());
        currentFav.set(position,box.isChecked());
        
        final String barcode = ((Product)data.get(position)).getBarcode();
        
        box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (barcode == null )
					return;
				if (isChecked){
					currentFav.set(position);
				}else {
					currentFav.clear(position);
				}
			}
		});
        //TODO change picture here
        index++;
        return vi;
    }
}