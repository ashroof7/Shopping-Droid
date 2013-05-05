package onlineDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

class FetchFromServer extends Thread {
	
	
	private final String serverURL = "http://localhost/run";
	
	
	private InputStream is ;

	HttpClient httpClient;
	HttpGet httpPost ;
	HttpResponse response;
	String result;
	HttpEntity entity;
	BufferedReader reader;
	StringBuilder sb;
	JSONObject jObject;
	
	public FetchFromServer(){
		super();
		httpClient = new DefaultHttpClient();
		httpPost = new HttpGet(serverURL);
		
	}
	

	
	public void connect() throws ClientProtocolException, IOException, JSONException{
		
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			is = entity.getContent();

			reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");

			result = sb.toString();
			jObject = new JSONObject(result);
			
			for (int i = 0 ; i < jObject.length(); i++){
				String store_name = null;
				double longitude, latitude;
				
				store_name = jObject.getString("store_name");
				latitude = jObject.getDouble("latitude");
				longitude = jObject.getDouble("longitude");
				
				System.out.println(store_name +" "+ longitude+" "+latitude);
				
			} 
			
		
	} 
	
	@Override
	public void run() {
		super.run();
		
		
	}
}