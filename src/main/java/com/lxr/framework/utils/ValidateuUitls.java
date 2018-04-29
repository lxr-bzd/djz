package com.lxr.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ValidateuUitls {

	 /** 
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 
     * 此方法中前三位格式有： 
     * 13+任意数 
     * 15+除4的任意数 
     * 18+除1和4的任意数 
     * 17+除9的任意数 
     * 147 
     */  
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException { 
    	if(str==null)return false;
    	
        String regExp = "^((13[0-9])|(15[^4])|(18[1,0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";  
        Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(str);  
        return m.matches();  
    }  
  
    /** 
     * 香港手机号码8位数，5|6|8|9开头+7位任意数 
     */  
    public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {  
        String regExp = "^(5|6|8|9)\\d{7}$";  
        Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(str);  
        return m.matches();  
    } 
	
    
    /**
     *验证车牌
     *包括新能源车牌
     * @param vehicleNumber
     * @return
     */
    public static boolean isVehicleNumber(String vehicleNumber)

    {
    	
    	if(vehicleNumber==null)
    		return false;

        
        String regExp = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1,2}$";
  
        Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(vehicleNumber);  
        return m.matches();  

    }
    
    
    /**
     *判断是不是32位md5
     * @param vehicleNumber
     * @return
     */
    public static boolean has32Md5(String md5)
    {

        
        if(md5==null||md5.length()!=32)return false;
        
        return true;  

    }
    
    public static void main(String[] args) {
    	int[] vars = new int[]{134,135,136,137,138,139,150,151,152,158,159,157,182,187,188,147,130,131,132,155,156,185,186
    			,180,189,133,153};
    	
    	for (int i = 0; i < vars.length; i++) {
    		if(!isChinaPhoneLegal(vars[i]+"70824615"))
    			System.out.println(vars[i]);
    			
		}
    	
    	
		
	}
    
    
    
}
