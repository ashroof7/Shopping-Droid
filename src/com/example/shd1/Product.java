package com.example.shd1;

public class Product {
	private int bar_code;
	private String name;
	private String type_name;
	private int type_id;
	private int store_id;
	private String store_name;
	private String store_adress;

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

	public String getStore_adress() {
		return store_adress;
	}

	public void setStore_adress(String store_adress) {
		this.store_adress = store_adress;
	}

	public Product(int code, String p_name, String t_name, int t_id,int s_id,String s_name,String s_address) {
		this.bar_code = code;
		this.name = p_name;
		this.type_name = t_name;
		this.type_id = t_id;
		this.store_id=s_id;
		this.store_name=s_name;
		this.store_adress=s_address;
	}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public int getBar_code() {
		return bar_code;
	}

	public void setBar_code(int bar_code) {
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

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

}
