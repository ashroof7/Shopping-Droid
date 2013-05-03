package com.shopping_droid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class DBConnector {
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	String sql;
	
	PreparedStatement selectItemGlobal,selectItemInStore, selectStores, selectStoresLocations, selectRangeProducts;
	
	
	public void connect() throws ClassNotFoundException, SQLException{
		// database type and name are hard coded for now
		
		// loading MySQL driver
		Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		connection = DriverManager
					.getConnection("jdbc:mysql://localhost/shopping_droid?"
							+ "user=leo&password=leonardo");
		
		// Statements allow to issue SQL queries to the database
		statement = connection.createStatement();
	
		// generating prepared statements
		selectItemGlobal = connection.prepareStatement("select name, type_name, price, store_name " +
				"from product, product_type, product_store, stores " +
				"where product_code = barcode and type = type_id and product_store.store_id = stores.store_id  and barcode = ? ;") ;
		
		selectItemInStore = connection.prepareStatement("select name, type_name, price " +
				"from product, product_type, product_store " +
				"where product_code = barcode and type = type_id and product_store.store_id = ? and barcode = ?") ;
		
		selectStores = connection.prepareStatement("select * from stores ;");
		
		selectStores = connection.prepareStatement("select store_name, latitude, longitude from stores ;");
		
//		selectRangeProducts
	}
	
	public void getProductsInRangeGlobal(int productID, double range) throws SQLException{
		//FIXME
		PreparedStatement selectRangeProducts = connection.prepareStatement("select name, store_name, price " +
				"from product, stores, product_type, product_store " +
				"where barcode = product_code and type_id = type and product_store.store_id = stores.store_id " +
				"and @pivot = (select price from product_store where product_code = 4 ) " +
				"and price between @pivot-20 and @pivot +20; ");
		
		resultSet = selectRangeProducts.executeQuery();
		resultSet.last();
		System.out.println(resultSet.getRow());
		
		while (resultSet.next()) 
			System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) +  " " + resultSet.getDouble(3));
		
	} 
	
	public void getItemInAllStores(int productCode) throws SQLException{
		selectItemGlobal.setInt(1, productCode);
		resultSet = selectItemGlobal.executeQuery();
		
		while (resultSet.next()) 
			System.out.println(resultSet.getString(1) + " "+resultSet.getString(2) + " " + resultSet.getDouble(3)+" "+ resultSet.getString(4));
		
	}
	
	public void getItem(int storeId, int productCode) throws SQLException{
		selectItemInStore.setInt(1, storeId);
		selectItemInStore.setInt(2, productCode);
		resultSet = selectItemInStore.executeQuery();
		
		while (resultSet.next()) 
			System.out.println(resultSet.getString(1) + " "+resultSet.getString(2) + " " + resultSet.getDouble(3));
	}			
	
	public void getStores () throws SQLException{ 
		resultSet = selectStores.executeQuery();
		while (resultSet.next()) 
			System.out.println(resultSet.getInt(1) + " " +resultSet.getString(2) + " " + resultSet.getDouble(3) +  " " + resultSet.getDouble(4) +  " " + resultSet.getString(5));
	} 
	
	public void getStoresLocations() throws SQLException{ 
		resultSet = selectStoresLocations.executeQuery();
		while (resultSet.next()) 
			System.out.println(resultSet.getString(1) + " " + resultSet.getDouble(2) +  " " + resultSet.getDouble(3));
	}
	
	
	// You need to close the resultSet
	public void close() {
		try {
			if (resultSet != null) 
				resultSet.close();
			
			if (statement != null) 
				statement.close();

			if (connection != null) 
				connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}