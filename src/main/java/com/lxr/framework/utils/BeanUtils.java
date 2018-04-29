package com.lxr.framework.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.portable.ApplicationException;


public class BeanUtils {
	
	public static List attrsConvertBeans(Object[] attrs,String attrName,Class cls) {
		
		List list = new ArrayList();
		try {
			for (int i = 0; i < attrs.length; i++) {
				Object bean = cls.newInstance();
				Method method = cls.getMethod("set"+captureName(attrName),attrs[i].getClass());
				method.invoke(bean, attrs[i]);
				
			list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		
		
		return list;

	}
	
	 public static String captureName(String name) {
	        name = name.substring(0, 1).toUpperCase() + name.substring(1);
	       return  name;
	      
	    }
	
	

}
