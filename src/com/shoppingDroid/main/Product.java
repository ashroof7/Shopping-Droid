package com.shoppingDroid.main;

public class Product {
	private String bar_code;
	private String name;
	private String type_name;
	private int store_id;
	private String store_name;

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}


	public Product(String code, String p_name, String t_name,int s_id,String s_name) {
		this.bar_code = code;
		this.name = p_name;
		this.type_name = t_name;
		this.store_id=s_id;
		this.store_name=s_name;
	}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public String getBar_code() {
		return bar_code;
	}

	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}


}
