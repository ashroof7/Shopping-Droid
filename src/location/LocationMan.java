package location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import location.LocationFetcher.LocationResult;

public class LocationMan {
	LocationResult lr ;
	LocationFetcher myLocation ;
	Location lastLocation;
	Context context;
	
	public LocationMan(Context context) {
		this.context = context;
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		lastLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		myLocation = new LocationFetcher();
		lr = new LocationResult(){
    	    @Override
    	    public void gotLocation(Location location){
    	    	lastLocation = location;
    	    }
    	};	
	}
	
	public void updateLocation(){
		myLocation.getLocation(context, lr);
	}
	
	public Location getLastLocation() {
		
		return lastLocation;
	}
	
}
