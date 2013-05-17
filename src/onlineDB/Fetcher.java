package onlineDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

class Fetcher extends AsyncTask<String, Integer, JSONObject> {


	private InputStream is;

	HttpClient httpClient;
	HttpGet httpGet;
	HttpResponse response;
	String result;
	HttpEntity entity;
	BufferedReader reader;
	StringBuilder sb;
	Context con;
	ProgressDialog diag;
	
	
	public Fetcher(Context cont) {
		super();
		con = cont;
		httpClient = new DefaultHttpClient();
		diag = new ProgressDialog(con);
		
	}
	
	@Override
	protected void onPreExecute() {
//		super.onPreExecute();
		diag.setMessage("Waiting for Server Response");
		diag.show();
		Log.i("Show", "showing");
	}
	

	@Override
	protected JSONObject doInBackground(String...param) {
		JSONObject jObject = null;
		try {
			URL url= new URL(param[0]);
			try{
			url.openConnection();
			}catch(Exception e)
			{
				Log.e("url error",e.toString());
			}
			try {
				httpGet = new HttpGet(url.toURI());
			} catch (URISyntaxException e) {
				Log.e("uri conversion error",e.toString());
			}
			try{
			response = httpClient.execute(httpGet);
			}catch(Exception e)
			{
//				Toast.makeText(this, "There is no Connection", Toast.LENGTH_LONG).show();
			}
			Log.i("Status code",""+response.getStatusLine().getStatusCode());
			Log.i("Reason phrase",""+response.getStatusLine().getReasonPhrase());
			entity = response.getEntity();
			is = entity.getContent();

			reader = new BufferedReader(new InputStreamReader(is,"UTF-8" ), 8);
			sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");

			result = sb.toString();
			jObject = new JSONObject(result);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jObject;
		
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
//		super.onPostExecute(result);
		 diag.dismiss();
         if (result != null) {
             Toast.makeText(con, "Result retrived", Toast.LENGTH_LONG).show();
         } else {
             Toast.makeText(con, "Server down", Toast.LENGTH_LONG).show();
         }
	}
}