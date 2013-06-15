package com.shoppingDroid.main;

public class Product extends ViewItem{
	private String barcode;
	private String name;
	private String typeName;
	private String storeName;
	private int storeId;
	private double price;
	private boolean isFav; 
	
	public Product(String barcode, String name, String type, int storeId, String store, double price, boolean isFav) {
		this.barcode = barcode;
		this.name = name;
		this.typeName = type;
		this.storeId = storeId;
		this.storeName = store;
		this.price = price;
		this.isFav = isFav;
	}
	
	public Product(String barcode, String name, String type, int storeId, String store, boolean isFav){
		this(barcode, name, type, storeId, store, 0, isFav);
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	

	public boolean isFav() {
		return isFav;
	}

	public void setFav(boolean isFav) {
		this.isFav = isFav;
	}

	@Override
	public String toString() {
		return "Product [" + barcode + ", name=" + name + ", "
				+ typeName + ", " + storeName + " : "
				+ storeId + "]";
	}

	@Override
	public String getTitle() {
		return name;
	}

	@Override
	public String getSubText() {
		return storeName +"  "+typeName;
	}

	@Override
	public String getRightText() {
		return price+"";
	}

	
}
