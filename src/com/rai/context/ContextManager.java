package com.rai.context;

import com.rai.context.Context.ContextEnum;

/**
 * 
 * @author igobrilhante
 *
 */
public class ContextManager {
	
	private ContextManager instance;
	private Context current;
	
	private ContextManager(){ this.current = new Context();}
	
	public ContextManager instance(){
		
		if(this.instance == null){
			this.instance = new ContextManager();
		}
		
		return this.instance;
	}
	
	public void putContextEntry(ContextEnum key,Object value){
		this.current.addKeyValye(key, value);
	}
	
	
}
