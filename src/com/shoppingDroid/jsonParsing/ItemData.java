package com.shoppingDroid.jsonParsing;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ItemData implements Parcelable {

	private HashMap<String, String> hm;

	public ItemData() {
		hm = new HashMap<String, String>();
	}

	@SuppressWarnings("unchecked")
	public ItemData(Parcel p) {
		Bundle b = p.readBundle();
		hm = (HashMap<String, String>) b.getSerializable("map");

	}

	/*
	 * the following method is used for creation of the ItemData and parsing the
	 * JSON
	 */

	public void put(String keyTag, String value) {
		hm.put(keyTag, value);
	}

	/*
	 * the following set of methods are used to get specific attributes of the
	 * Item
	 */

	public boolean containsTag(String keyTag) {
		return hm.containsKey(keyTag);
	}

	public String getValue(String keyTag) {
		return hm.get(keyTag);
	}

	/*
	 * the following set of methods are used for UI
	 */

	public String getTitle() {
		if (hm.containsKey("product_name"))
			return hm.get("product_name");
		else if (hm.containsKey("store_name"))
			return hm.get("store_name");
		throw new IllegalStateException("no title found");

	}

	public String getSubText() {
		String out;

		out = "";
		if (hm.containsKey("product_name"))
			if (hm.containsKey("product_type"))
				out = hm.get("product_type");
			else
				// TODO check server product range queries
				out = "";
		if (hm.containsKey("store_name")) {

			// if (hm.containsKey("store_address"))
			// return hm.get("store_address");
			// else
			// TODO check server product range queries
			if (out.equals(""))
				return hm.get("store_name");
			else
				return hm.get("store_name") + " - " + out;
		}
		return out;

		// throw new IllegalStateException("no sub-text found");

	}

	public String getRightText() {
		String out = "";
		if (hm.containsKey("product_name") && hm.containsKey("product_price")) {
			out = hm.get("product_price");
			// if (hm.containsKey("store_name"))
			// out = hm.get("store_name") + " - " + out;
			return out;
		} else if (hm.containsKey("store_name"))
			return "";

		throw new IllegalStateException("no right text found");

	}

	@Override
	public String toString() {

		return "title : " + this.getTitle() + " subText : " + this.getSubText()
				+ " rightText : " + this.getRightText();

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		Bundle b = new Bundle();
		b.putSerializable("map", hm);
		dest.writeBundle(b);

	}

	public static Creator<ItemData> CREATOR = new Creator<ItemData>() {
		public ItemData createFromParcel(Parcel parcel) {
			return new ItemData(parcel);
		}

		@Override
		public ItemData[] newArray(int arg0) {
			return new ItemData[arg0];
		}

	};
}