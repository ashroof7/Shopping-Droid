package com.shoppingDroid.main;

import com.shoppingDriod.main.R;

import android.os.Bundle;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ScannedFragment extends Fragment implements LoaderCallbacks<Product> {

	boolean initFav = false;
	boolean isFav = false;
	private int callCnt = 0;
	View resultView;
	Product product;
	DataFetcher df;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getLoaderManager().initLoader(0, null, this).forceLoad();
		setRetainInstance(true);
		Log.wtf("Scanned Frag", "on Create View");
		return resultView = inflater.inflate(R.layout.activity_scanned, container, false);
	}

	public void setDataFetcher(DataFetcher df) {
		this.df = df ;
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

	
	public Loader<Product> onCreateLoader(int arg0, Bundle arg1) {
		return new ScanLoader(getActivity(), df);
	}

	
	@Override
	public void onLoadFinished(Loader<Product> loader,	Product product) {
		this.product = product;
		Log.wtf("Scanned Fragment", "Load finshed yaaaaaay!");
		
//		FIXME 
//		quick and dirty fix : if removed the frag shows "barcode not found" for a while then shows the
//		correct information
//		since there are 2 calls happen the first set the scan_content.visible = true 
		if (callCnt++ < 2)
			return;
		
		if (product != null) {
			MainActivity.db.addHistory(product);
			((TextView) resultView.findViewById(R.id.scanned_name)).setText(product
					.getName());
			((TextView) resultView.findViewById(R.id.scanned_price)).setText(product
					.getPrice() + "");
			((TextView) resultView.findViewById(R.id.scanned_type)).setText(product
					.getTypeName());
			((TextView) resultView.findViewById(R.id.scanned_barcode))
					.setText(getString(R.string.barcode) + " : "
							+ product.getBarcode());
			((TextView) resultView.findViewById(R.id.scanned_store)).setText(product
					.getStoreName());
			isFav = initFav = product.isFav();

		} else {
			((TextView) resultView.findViewById(R.id.scanned_name))
					.setText(getString(R.string.diag_prod_not_found));
		}

		CheckBox box = ((CheckBox) resultView.findViewById(R.id.scanned_fav));
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
		
		resultView.findViewById(R.id.scanned_progress).setVisibility(View.INVISIBLE);
		resultView.findViewById(R.id.scanned_content).setVisibility(View.VISIBLE);
		Log.w("scan frag", "set visible");
		Log.wtf("Scanned Frag", "on Load Finished");
	}

	@Override
	public void onLoaderReset(Loader<Product> product) {
		
	}
	
}


class ScanLoader extends AsyncTaskLoader<Product> {
		DataFetcher df;
		
		public ScanLoader(Context context, DataFetcher df) {
			super(context);
			this.df = df ;
		}

		@Override
		public Product loadInBackground() {
			Log.wtf("Scan Loader", "load in background");
			return df.here();
		}
	}

