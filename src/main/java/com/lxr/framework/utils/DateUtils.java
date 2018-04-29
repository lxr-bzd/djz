package com.lxr.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	
	public static String format(String pattern) {
		
		return format(new Date(),pattern);

	}
	
	public static String format(Date date,String pattern) {
		
		
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		
		return format.format(date);

	}
	
	
	public static String format(Long date) {
		
		return format(new Date(date),"yyyy-MM-dd HH:mm");

	}
	
	
}
