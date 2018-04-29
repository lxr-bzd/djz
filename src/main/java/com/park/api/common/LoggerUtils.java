package com.park.api.common;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils {
	
	public static Map<Class, Logger>  logs = new HashMap<>();
	
	
	public static Logger getLog(Class cls) {
		
		Logger log = logs.get(cls);
		
		if(log==null){
			log = LoggerFactory.getLogger(cls);
			logs.put(cls, log);
		}
		
		return log;
	}
	
	public static Logger getLog(Object obj) {
		
		return getLog(obj.getClass());
	}
	
	
	

}
