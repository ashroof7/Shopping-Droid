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

	boolean initFav = false;
	boolean isFav = false;
	Product product;
	View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		v = inflater.inflate(R.layout.activity_scanned, container, false);

		if (product != null) {
			((TextView) v.findViewById(R.id.scanned_name)).setText(product
					.getName());
			((TextView) v.findViewById(R.id.scanned_price)).setText(product
					.getPrice() + "");
			((TextView) v.findViewById(R.id.scanned_type)).setText(product
					.getTypeName());
			((TextView) v.findViewById(R.id.scanned_barcode))
					.setText(getString(R.string.barcode)+" : " + product.getBarcode());
			((TextView) v.findViewById(R.id.scanned_store)).setText(product
					.getStoreName());
			isFav = initFav = product.isFav();
			
		}else {
			((TextView) v.findViewById(R.id.scanned_name)).setText(getString(R.string.diag_prod_not_found));
		}
		
		CheckBox box = ((CheckBox) v.findViewById(R.id.scanned_fav));
		box.setChecked(isFav);
		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					isFav = true;
				} else {
					isFav = false;
				}
			}
		});
		return v;
	}

	public void displayData(Product p, Context c) {
		this.product = p;
		if (p != null)
			MainActivity.db.addHistory(p);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (!initFav && isFav) {
			MainActivity.db.addFavourites(product);
		} else if (initFav && !isFav) {
			MainActivity.db.deleteFavourite(product.getBarcode());
		}
	}

}
