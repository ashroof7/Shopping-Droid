package com.shoppingDroid.jsonParsing;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.*;

import android.util.Log;

public class JsonParser {
	private ArrayList<ItemData> data;
	private int i, szm, szs, j;
	private Iterator<?> it;
	private JSONObject temp, temp2;
	private ItemData curr;
	private JSONArray mainElement;
	private JSONArray subElement;
	private String currKeyTag;

	public JsonParser() {

	}

	public ArrayList<ItemData> parse(JSONObject in) {
		data = new ArrayList<ItemData>();
		try {

			String mainTag;
			mainTag = "";
			if (in.has("product")) {
				mainTag = "product";
			} else if (in.has("products")) {
				mainTag = "products";
			} else if (in.has("stores")) {
				mainTag = "stores";
			} else {
				throw new IllegalStateException("invalid jason object");
			}

			mainElement = in.getJSONArray(mainTag);

			// TODO refactor eh el esmaa2 di amousa :D :D
			szm = mainElement.length();
			for (i = 0; i < szm; ++i) {
				temp = mainElement.getJSONObject(i);
				if (temp.has("stores")) {
					subElement = temp.getJSONArray("stores");
					szs = subElement.length();
					for (j = 0; j < szs; ++j) {
						curr = new ItemData();
						temp2 = subElement.getJSONObject(j);
						it = temp2.keys();
						while (it.hasNext()) {
							currKeyTag = (String) it.next();
							curr.put(currKeyTag, temp2.getString(currKeyTag));
						}

						it = temp.keys();
						while (it.hasNext()) {
							currKeyTag = (String) it.next();
							if (!currKeyTag.equals("stores"))
								curr.put(currKeyTag, temp.getString(currKeyTag));
						}
						data.add(curr);

					}
				} else {
					curr = new ItemData();
					it = temp.keys();
					while (it.hasNext()) {
						currKeyTag = (String) it.next();
						curr.put(currKeyTag, temp.getString(currKeyTag));
					}
					data.add(curr);
				}
			}

		} catch (JSONException e) {
			Log.e("JSONparsing", "product tag notfound");
			e.printStackTrace();
		}

		return data;
	}

}
