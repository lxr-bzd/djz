package com.park.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lxr.framework.Subject;
import com.lxr.framework.long1.JsonResult;
import com.park.api.ServiceManage;
import com.park.api.common.BaseController;

/**
 * 登录授权
 * @author Administrator
 */
@RequestMapping
@Controller
public class AuthenticationController extends BaseController{

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("login")
	@ResponseBody
	public Object login(String account,String pwd) {
		Subject subject = new Subject();
		subject.setAccount(account);
		subject.setPwd(pwd);
		ServiceManage.securityService.checkUser(subject);
		ServiceManage.securityService.login(subject);
		return JsonResult.getSuccessResult("登录成功");
	}
	
	@RequestMapping("user/checkIn")
	@ResponseBody
	public Object test(HttpServletRequest request) {
		return JsonResult.getSuccessResult();
	}
	@RequestMapping("test2") 
	@ResponseBody
	public Object test2(HttpServletRequest request) {
		return JsonResult.getSuccessResult();
	}
	
	@RequestMapping("logout")
	@ResponseBody
	public Object logout() {
		ServiceManage.securityService.logout();
		return JsonResult.getSuccessResult("登出成功");
	}
	

	public static void main(String[] args) throws Exception {


        String s = "少女少女老男老女少男老男,老男少女少女少男少女老男,老女少男少女少男少男老男,老男老女少男老男老女老女,老女少女老男老女少女老男,少男少男老男老女少男老男,少女少男少女少女老女少男,少女老女少女老女老男少女,老男少女老男老女老女老男,少女老女少男老男少男老男,少男老女老男老男老男老男,少女少男少男老男少女少男,少男老女老女少男老男少女,少男老男少男老女老男少男,少男少男老男少男少女少女,老女少女老女少女少女老女,老女老女少男少男老男老男,少男少男少男老男老女少男,少女老女少女老女少女少女,少男少女老男老女老男老女,老女老男少女老男少男老女,老女老男少女少女少女老女,老女少女老男老男少女少女,少男少男老男少男少女少男,少男老男少男少男少男老男,少女老女老男老男老男少男,少女少女老男少女老男老女,少女少男老男老女老女少男,老男少男少女少女老女老男,老女少男少男少女少男老男,少女少女老男老女少女少女,少男老男老男少女少男老女,少男少男少男少女少女老女,少男少女少男老男少女少女,老女少女老男少女老女少女,少女少女老女老女少女少男,少男老女少男少女老女少女,少男老女老男少男少女少女,少女少男老女老女少女少男,老女少女少男少男少女老男,少男少女少男少女少男老女,老男少男老女老男少男老女,少女老女少女老男少女少男,少男老女少女少男老男老女,少男少男少男少男少女少男,老女少男少女老男少女老女,少男老女少女老女老男少男,老女老女老男少男少男老女,老男少女少女老女少男少女,少男老男老男少女少女老女,少女少女老男老女老男少男,少男少女老女老女老男老女,老女少男老男少男少女少女,老男少女老男老女老女老女,老男老男少男少男老男老女,少女少男老男老女少女少男,少女老女老男少男少女老男,少男少女少女少男少男少男,老男老男老女少男老男少男,老男少女少男老女少女老女,老男少女老男老男老男少女,少女老女少女少男少男少男,老女少男少女老男少女少女,老女老男少女少女少男老男,少女老女少女老女少男少男,老女老男老男少男老女老女,老女老男老男老女老男老男,少女少男老男少男少女少女,少女少男老男老女少女老男,少女老男少男少男少女少男,老男老女少女少女老女老女,老男老男少女老女少女少女,老女少男少女老男少女少女,老女老男老女少女少男少男,老女少男少男少女老男老男,老男老女老女少女少女少男,少男少男少女老男老男老男,老男老男老男老女老男老男,少女老女老男老男少男老女,老男老男老女少女老女少女,少男老男少男老男老女老男,少女老女少男少男少女老男,老男少男老女少女少男少男,老女老女老男少女少女少女,老男老男少男少女少男老男,少男老女老女少男少女老女,老女老女老女老女少男少男,老男少男老女老女少男老男,老男少女老女少女少女少女,老男老男少女老女少女少女,少男少女老男少男少男老男,老女老女少男少女老女少男,老男老女少男老男老女少男,老男少女少女老女少男少女,少男老女少女老女老男老男,老男少男老男老男少男老女,少男少女少男老男少女少男,少女老男少男老女老女老女,少男老女老男少男老男老女,老男老女老男少男少男老女,少女老女老女少男少女老女,少女老女老女少男老男少男";

       

        //创建一个Map 用于存储数据

        Map<String,Integer>map = new HashMap<String,Integer>();

         

        //将字符与出现次数联系

        for(int i = 0; i < s.length(); i++){

             String perStr = s.substring(i, i+1);

             if(map.containsKey(perStr)){

                  Integer num = map.get(perStr);

                  map.put(perStr, num + 1);

             }else{

                  map.put(perStr, 1);

             }

        }

        //用map输出的两种格式

        Set<Map.Entry<String,Integer>> set= map.entrySet();

        for(Map.Entry<String,Integer> entry: set){

             System.out.println(entry.getKey()+ ":" + entry.getValue());

        }

        System.out.println("=======================");

        Set<String>keys = map.keySet();

        for(String key : keys){

             System.out.println(key + "--->"+ map.get(key));

        }

    }


	
	
}
