package com.shoppingDroid.main;

public class Store extends ViewItem{
	private int id;
	private String name;
	private double longitude;
	private double latitude;
	
	public Store(int storeId, String storeName, double longitude, double latitude) {
		this.id = storeId;
		this.name = storeName;
		this.longitude = longitude;	
		this.latitude = latitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int storeId) {
		this.id = storeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String storeName) {
		this.name = storeName;
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
		return "Store [" + id + ", " + name
				+ ", lng:" + longitude + ", lat:" + latitude + "]";
	}

	@Override
	public String getTitle() {
		return name;
	}

	@Override
	public String getSubText() {
		return "";
	}

	@Override
	public String getRightText() {
		return "";
	}
	
	
}
