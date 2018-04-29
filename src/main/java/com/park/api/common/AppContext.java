package com.park.api.common;

import java.util.Map;
import java.util.UUID;

import org.dom4j.DocumentException;

import com.lxr.commons.utils.XmlUtils;

public class AppContext {
	
	public static boolean isDebug = false;
	
	public static String debugKey = "45c7b7f0-f662-499e-9545-7151eda7d2f3";

	public static Map<String, String> appConfig = null;
	
	
	//充值通知易车
	public static String ERROR_Recharge_Notify = "E100";
	//消息推送错误
	public static String ERROR_Messages = "E101";
	
	//消息推送错误
	public static String ERROR_Yc_Notify = "E102";
	
	static{
		
		
			try {
				
				appConfig = XmlUtils.xml2map(AppContext.class.getResourceAsStream("/appConfig.xml"));
			} catch (DocumentException e) {
				throw new RuntimeException(e);
			}
		
		
	}
	
	public static String getFileHost() {
		
		return appConfig.get("fileHost");

	}
	
	public static String getProperties(String key) {
		return appConfig.get(key);

	}
	
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
	}
	
	
}
