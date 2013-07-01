package com.rai.context;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author igobrilhante
 *
 */
public class ContextApp {
	
//	public enum ContextEnum {
//		LATITUDE("latitude"),
//		LONGITUDE("longitude"),
//		HOUR_OF_DAY("hour_of_day"),
//		WEATHER("weather");
//
//		private String context;
//
//		private ContextEnum(String context){
//			this.context = context;
//		}
//		public String getName(){
//			return this.context;
//		}
//
//	}
	
	private Map<String, String> map;
	
	public ContextApp(){
		this.map = new HashMap<String, String>();
	}
	
	public void addKeyValye(String key, String value){
		map.put(key, value);
	}

    public String getValue(String key){
        return this.map.get(key);
    }
	
	
	
	
}
