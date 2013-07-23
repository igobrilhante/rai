package com.rai.sensor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Service;
import android.content.Intent;
import android.location.*;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.rai.context.ContextManager;


import android.os.Bundle;

/**
 * Location Sensor to collect positional data from GooglePlayService
 * 
 * @author igobrilhante
 *
 */
public class LocationSensor extends Service
                            implements
							LocationListener,
							GooglePlayServicesClient.ConnectionCallbacks,
							GooglePlayServicesClient.OnConnectionFailedListener  {


    private static final String TAG = "LocationSensor";
    public  String locationProvider = LocationManager.GPS_PROVIDER;

    private LocationBinder binder = new LocationBinder();

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;
    
    // Manager the updates
    private ContextManager contextManager;

    private Criteria criteria = new Criteria();


    private LocationManager locationManager;

    
    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     *
     */
    boolean mUpdatesRequested = false;

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public String getLocationProvider() {
        return locationProvider;
    }

    public LocationSensor(){
    	// Create a new global location parameters object
        mLocationRequest        = LocationRequest.create();
        this.contextManager     = ContextManager.instance();
        try{
            Log.i(TAG,"LocationSensor");
            this.locationManager    = (LocationManager) this.contextManager.getContext().getSystemService(LOCATION_SERVICE);

            this.locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
            onLocationUpdate(this.locationManager.getLastKnownLocation(locationProvider));

        }
        catch (Exception e){
            Log.e(TAG,"Exception ",e);
        }

    }
    
	/*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG,"onConnected");

        if (mUpdatesRequested) {
        }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        Log.i(TAG,"onDisconnected");
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */

    }

    /**
     * Report location updates to the UI.
     *
     * @param location The updated location.
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG,"onLocationChanged");

        onLocationUpdate(location);
    }

    private void onLocationUpdate(Location location){
        Double latitude 	= location.getLatitude();
        Double longitude	= location.getLongitude();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        this.contextManager.getPreferences().edit().putString("latitude", latitude.toString()).commit();
        this.contextManager.getPreferences().edit().putString("longitude", longitude.toString()).commit();
        this.contextManager.getPreferences().edit().putString("days_week", Integer.toString(gregorianCalendar.get(Calendar.DAY_OF_WEEK))).commit();
    }

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

//    private String convert(int date){
//        switch (date){
//            case 0: return "sunday";
//            case 1: return "monday";
//            case 2: return  "tuesdat";
//            case3: return
//        }
//        return "";
//    }

	@Override
	public void onProviderEnabled(String provider) {
        Log.i(TAG,"onProviderEnabled");
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG,"onStatusChanged");
		// TODO Auto-generated method stub
		
	}

    @Override
    public IBinder onBind(Intent intent) {
        return this.binder;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public class LocationBinder extends Binder{
        public LocationSensor getService()
        {
            return LocationSensor.this;
        }
    }
}
