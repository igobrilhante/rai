package com.rai.context;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import com.rai.sensor.LocationSensor;
import com.rai.services.AbstractService;
import com.rai.services.ServiceController;
import com.rai.services.Weather;
import com.rai.services.WeatherService;
import com.rai.utils.Utils;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author igobrilhante
 *
 */
public class ContextManager {

    private static final String TAG = "ContextManager";

	private static ContextManager instance;
    private LocationSensor locationSensor;
    private Context context;
    private SharedPreferences   preferences;
//    private Map<String,TextView> textViewMap;
	
	private ContextManager(){

    }


	public static ContextManager instance(){
        Log.i(TAG,"instance");
		
		if(instance == null){
            Log.d(TAG,"new contextmanager");
			instance = new ContextManager();

		}
		
		return instance;
	}

    public void requestUpdateLocation() {

        this.locationSensor.getLocationManager().requestLocationUpdates(this.locationSensor.getLocationProvider(), 0, 0, this.locationSensor);
    }
    public void removeUpdateLocation(){
        this.locationSensor.getLocationManager().removeUpdates(this.locationSensor);
    }

    public void setContext(Context context){
        Log.d(TAG,"setContext");
        this.context = context;
        this.preferences = this.context.getSharedPreferences("RAI",Context.MODE_PRIVATE);
        this.locationSensor = new LocationSensor();
    }

    public Context getContext(){
        return this.context;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public String getString(String key){
        return this.preferences.getString(key,"");
    }

    public void clean(){
        this.preferences.edit().clear().commit();
    }
}
