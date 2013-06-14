package com.shoppingDroid.main;

import com.shoppingDriod.main.R;

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

	boolean addToFav = false;
	boolean isFav = false;
	Product product;
	View v;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		v = inflater.inflate(R.layout.activity_scanned, container, false);
		

		((TextView) v.findViewById(R.id.scanned_name)).setText(product.getName());
		((TextView) v.findViewById(R.id.scanned_price)).setText(product.getPrice()+"");
		((TextView) v.findViewById(R.id.scanned_type))
				.setText(product.getTypeName());
		// TODO change string here
		((TextView) v.findViewById(R.id.scanned_barcode)).setText("Barcode : "
				+ product.getBarcode());
		((TextView) v.findViewById(R.id.scanned_store)).setText(product
				.getStoreName());

		CheckBox box = ((CheckBox) v.findViewById(R.id.scanned_fav));
		box.setChecked(isFav);

		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					addToFav = true;
				} else {
					addToFav = false;
				}
			}
		});

		return v;
	}

	public void displayData(Product p, boolean isFav, Context c) {
		this.isFav = isFav;
		this.product = p;
		MainActivity.db.addHistory(p);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (addToFav && !isFav) {
			MainActivity.db.addFavourites(product);
		} else if (isFav && !addToFav) {
			MainActivity.db.deleteFavourite(product.getBarcode());
		}
	}

}
