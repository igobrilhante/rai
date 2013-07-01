package com.rai.context;

import android.content.Context;
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
    private ServiceController serviceController;
    private LocationSensor locationSensor;
    private Context context;
    private Map<String,String> space;
//    private Map<String,TextView> textViewMap;
	
	private ContextManager(){
        this.serviceController = ServiceController.instance();
        this.space             = new Hashtable<String, String>();
    }

	public static ContextManager instance(){
        Log.i(TAG,"instance");
		
		if(instance == null){
			instance = new ContextManager();
		}
		
		return instance;
	}

    public void setContext(Context context){
        this.context = context;
        this.locationSensor = new LocationSensor();
    }

    public Context getContext(){
        return this.context;
    }

//    public void putTextView(String key, TextView textView){
//        if(this.textViewMap == null){
//            this.textViewMap = new Hashtable<String, TextView>();
//        }
//        this.textViewMap.put(key,textView);
//    }
//
//    public TextView getTextView(String key){
//        return this.textViewMap.remove(key);
//    }
//
//    public void cleanTextView(){
//        this.textViewMap = null;
//    }
	
	public void putEntry(String key,String value){
		this.space.put(key, value);
	}

    public String getValue(String key){
        return this.space.get(key);
    }

	
	
}
