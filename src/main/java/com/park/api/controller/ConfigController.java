package com.park.api.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxr.commons.exception.ApplicationException;
import com.lxr.framework.long1.JsonResult;
import com.park.api.ServiceManage;
import com.park.api.service.SysService;

@RequestMapping("user/config")
@Controller
public class ConfigController {
	
	@Autowired
	SysService sysService;

	@RequestMapping("getConfig")
	@ResponseBody
	private Object getConfig() {
		
		Map<String, Object> map = new HashMap();
		map.put("tg_thre", sysService.getSysConfig("tg_thre", Integer.class));
		map.put("conf_len", sysService.getSysConfig("conf_len", Integer.class));
		
		return JsonResult.getSuccessResult(map);
	}
	
	
	@RequestMapping("setConfig")
	@ResponseBody
	private Object setConfig(String mod,String val) {
		
		Object valObj = null;
		switch (mod) {
		case "tg_thre":
			valObj = Integer.valueOf(val);
			break;
		case "conf_len":
			valObj = Integer.valueOf(val);
			break;

		default:throw new ApplicationException();
		}
		
		ServiceManage.jdbcTemplate.update("UPDATE djt_sys SET val=? WHERE  ckey=?",valObj,mod);
		
		return JsonResult.getSuccessResult();
	}
	
	
	
}
