package onlineDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

import com.example.shd1.R;

public class DBDispatcher {

	private final String host = "shoppingdroid.bugs3.com";
	private final String serverURL = "http://" + host + "/shoppingdroid.php/";
	private Context context;
	private String requestAddress;
	private List<NameValuePair> params;
	private String paramString;

	public DBDispatcher(Context context) {
		this.context = context;
		params = new ArrayList<NameValuePair>(5);
	}

	public JSONObject productGlobal(String barcode)
			throws InterruptedException, ExecutionException {
		params.clear();
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_product_barcode), barcode));
		paramString = URLEncodedUtils.format(params,
				context.getString(R.string.DB_return_encoding));

		requestAddress = serverURL
				+ context.getString(R.string.DB_product_global) + "?"
				+ paramString;

		return new Fetcher().execute(requestAddress).get();
	}

	public JSONObject product(String barcode, int storeId)
			throws InterruptedException, ExecutionException {
		params.clear();
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_product_barcode), barcode));
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_store_id), storeId + ""));
		paramString = URLEncodedUtils.format(params,
				context.getString(R.string.DB_return_encoding));
		requestAddress = serverURL + context.getString(R.string.DB_product)
				+ "?" + paramString;

		return new Fetcher().execute(requestAddress).get();

	}

	public JSONObject stores() throws InterruptedException, ExecutionException {
		requestAddress = serverURL + context.getString(R.string.DB_stores);
		return new Fetcher().execute(requestAddress).get();
	}

	public JSONObject storesLocations() throws InterruptedException,
			ExecutionException {
		requestAddress = serverURL
				+ context.getString(R.string.DB_stores_locations);
		return new Fetcher().execute(requestAddress).get();
	}

	public JSONObject storesInRange(double latitude, double longitude,
			double range) throws InterruptedException, ExecutionException {
		params.clear();
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_store_latitude), latitude + ""));
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_store_longitude), longitude + ""));
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_store_range), range + ""));
		requestAddress = serverURL
				+ context.getString(R.string.DB_stores_in_range) + "?"
				+ paramString;
		return new Fetcher().execute(requestAddress).get();
	}

	public JSONObject productRange(String barcode, int storeId,
			double diffAmount) throws InterruptedException, ExecutionException {
		params.clear();
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_product_barcode), barcode));
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_store_id), storeId + ""));
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_diff_amount), diffAmount + ""));
		paramString = URLEncodedUtils.format(params,
				context.getString(R.string.DB_return_encoding));
		requestAddress = serverURL
				+ context.getString(R.string.DB_product_range) + "?"
				+ paramString;
		return new Fetcher().execute(requestAddress).get();

	}

	public JSONObject productRangeGlobal(String barcode, int storeId,
			double diffAmount) throws InterruptedException, ExecutionException {
		params.clear();
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_product_barcode), barcode));
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_store_id), storeId + ""));
		params.add(new BasicNameValuePair(context
				.getString(R.string.DB_diff_amount), diffAmount + ""));
		paramString = URLEncodedUtils.format(params,
				context.getString(R.string.DB_return_encoding));
		requestAddress = serverURL
				+ context.getString(R.string.DB_product_range_global) + "?"
				+ paramString;
		return new Fetcher().execute(requestAddress).get();
	}
}