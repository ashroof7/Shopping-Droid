package com.shoppingDroid.main;

import com.shoppingDriod.main.R;
import com.shoppingDroid.jsonParsing.ItemData;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ScannedFragment extends Fragment {

	String barcode;
	String price;
	boolean addToFav = false;
	boolean isFav = false;
	Product p;
	View v;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		v = inflater.inflate(R.layout.activity_scanned, container, false);
		

		((TextView) v.findViewById(R.id.scanned_name)).setText(p.getName());
		((TextView) v.findViewById(R.id.scanned_price)).setText(price);
		((TextView) v.findViewById(R.id.scanned_type))
				.setText(p.getType_name());
		// TODO change string here
		((TextView) v.findViewById(R.id.scanned_barcode)).setText("Barcode : "
				+ barcode);
		((TextView) v.findViewById(R.id.scanned_store)).setText(p
				.getStore_name());

		CheckBox box = ((CheckBox) v.findViewById(R.id.scanned_fav));
		box.setChecked(isFav);

		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (barcode == null)
					return;
				if (isChecked) {
					addToFav = true;
				} else {
					addToFav = false;
				}
			}
		});

		
		return v;
	}

	public void displayData(String barcode, ItemData item, boolean isFav, ItemData store, Context c) {
		this.barcode = barcode;
		this.isFav = isFav;

		p = new Product(
				barcode,
				item.getValue(c.getResources()
						.getString(R.string.DB_product_name)),
				item.getValue(c.getResources()
						.getString(R.string.DB_product_type)),
				Integer.parseInt(store.getValue(c.getResources().getString(
						R.string.DB_store_id))),
				store.getValue(c.getResources().getString(R.string.DB_store_name)));
		
		price = item.getRightText();
		
		MainActivity.db.addHistory(p);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (addToFav && !isFav) {
			MainActivity.db.addFavourites(p);
		} else if (isFav && !addToFav) {
			MainActivity.db.deleteFavourite(barcode);
		}
	}

}
