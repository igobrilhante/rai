package com.rai.context;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author igobrilhante
 *
 */
public class Context {
	
	public enum ContextEnum {
		LATITUDE("longitude"),
		LONGITUDE("longitude"),
		HOUR_OF_DAY("hour_of_day"),
		WEATHER("weather");
		
		private String context;
		
		private ContextEnum(String context){
			this.context = context;
		}
		public String getName(){
			return this.context;
		}
				
	}
	
	private Map<ContextEnum, Object> map;
	
	public Context(){
		this.map = new HashMap<ContextEnum, Object>();
	}
	
	public void addKeyValye(ContextEnum key, Object value){
		map.put(key, value);
	}
	
	
	
	
}
