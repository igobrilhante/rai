package com.rai.sensor;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.rai.context.Context.ContextEnum;
import com.rai.context.ContextManager;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Location Sensor to collect positional data from GooglePlayService
 * 
 * @author igobrilhante
 *
 */
public class LocationSensor implements
							LocationListener,
							GooglePlayServicesClient.ConnectionCallbacks,
							GooglePlayServicesClient.OnConnectionFailedListener  {

	
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;
    
    // Manager the updates
    private ContextManager contextManager;
    
    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     *
     */
    boolean mUpdatesRequested = false;
    
    private LocationSensor(ContextManager contextManager){
    	// Create a new global location parameters object
        mLocationRequest = LocationRequest.create();
        this.contextManager = contextManager;
    }
    
	/*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {

        if (mUpdatesRequested) {
        }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
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
//        if (connectionResult.hasResolution()) {
//            try {
//
////                 Start an Activity that tries to resolve the error
//                connectionResult.startResolutionForResult(
//                        this,
//                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
//
//                /*
//                * Thrown if Google Play services canceled the original
//                * PendingIntent
//                */
//
//            } catch (IntentSender.SendIntentException e) {
//
//                // Log the error
//                e.printStackTrace();
//            }
//        } else {
//
////             If no resolution is available, display a dialog to the user with the error.
//            showErrorDialog(connectionResult.getErrorCode());
//        }
    }

    /**
     * Report location updates to the UI.
     *
     * @param location The updated location.
     */
    @Override
    public void onLocationChanged(Location location) {

        // Report to the UI that the location was updated
//        mConnectionStatus.setText(R.string.location_updated);

        // In the UI, set the latitude and longitude to the value received
//        mLatLng.setText(LocationUtils.getLatLng(this, location));
    	
//    	float accuracy 		= location.getAccuracy();
    	double latitude 	= location.getLatitude();
    	double longitude	= location.getLongitude();
//    	long time 			= location.getElapsedRealtimeNanos();
    	Calendar c			= new GregorianCalendar();
//    	c.setTimeInMillis(time);
    	
    	this.contextManager.putContextEntry(ContextEnum.LATITUDE, latitude);
    	this.contextManager.putContextEntry(ContextEnum.LONGITUDE, longitude);
    	this.contextManager.putContextEntry(ContextEnum.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
    	
    }

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
