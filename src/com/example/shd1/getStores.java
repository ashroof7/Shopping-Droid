package com.example.shd1;
import android.content.Context;
import android.util.Log;


public class getStores {

	Context con;
	
	public getStores(Context c)
	{
		con = c;
	}
	
	  public Object testCall(){
	    	Log.i("GetStores", "Test Succeeded");
	    	return (String)"Alex";
	    }
	    

}
