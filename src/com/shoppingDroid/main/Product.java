package com.shoppingDroid.main;

public class Product {
	private String barcode;
	private String name;
	private String typeName;
	private String storeName;
	private int storeId;
	
	public Product(String barcode, String name, String type, int storeId, String store) {
		this.barcode = barcode;
		this.name = name;
		this.typeName = type;
		this.storeId = storeId;
		this.storeName = store;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarCode(String barCode) {
		this.barcode = barCode;
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

	@Override
	public String toString() {
		return "Product [" + barcode + ", name=" + name + ", "
				+ typeName + ", " + storeName + " : "
				+ storeId + "]";
	}

	
}
