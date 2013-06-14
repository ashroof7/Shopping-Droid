package com.shoppingDroid.main;

public class Store {
	private int storeId;
	private String storeName;
	private double longitude;
	private double latitude;
	
	public Store(int storeId, String storeName, double longitude, double latitude) {
		this.storeId = storeId;
		this.storeName = storeName;
		this.longitude = longitude;	
		this.latitude = latitude;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "Store [" + storeId + ", " + storeName
				+ ", lng:" + longitude + ", lat:" + latitude + "]";
	}
	
	
}
