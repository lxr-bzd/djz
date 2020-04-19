package com.park.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxr.commons.exception.ApplicationException;
import com.lxr.framework.long1.JsonResult;
import com.park.api.ServiceManage;
import com.park.api.common.BaseController;
import com.park.api.service.SysService;

@RequestMapping("user/config")
@Controller
public class ConfigController extends BaseController{
	
	@Autowired
	SysService sysService;

	@RequestMapping("getConfig")
	@ResponseBody
	private Object getConfig() {
		
		Map<String, String> map = sysService.getAllConfig();
		
		return JsonResult.getSuccessResult(map);
	}
	
	
	@RequestMapping("setConfig")
	@ResponseBody
	private Object setConfig(String mod,String val) {
		
		Object valObj = val;
		switch (mod) {
		case "mod2":
			case "use_rule":
		case "tip_open":
			
			if(!ArrayUtils.contains(new String[] {"1","2"}, val)) throw new ApplicationException();
			
			break;
		case "gameGroupNum":
			Integer num2 = Integer.valueOf(val);
			if(!(num2>=1&&num2<5000)) throw new ApplicationException();
			
			break;
		case "gameRowNum":
			Integer num = Integer.valueOf(val);
			if(!(num>=2&&num<5000)) throw new ApplicationException();
			
			break;
		case "turn_num":
			valObj = Integer.valueOf(val);
			break;
		case "tg_thre":
			valObj = Integer.valueOf(val);
			break;
		case "conf_len":
			valObj = Integer.valueOf(val);
			break;
		case "rule":
		case "rule2":
		case "rule3":
			case "rule_jg_A":
			case "rule_jg_B":
			case "rule_A":
			case "rule_B":
			case "rule_bkbg1":
			case "rule_bkbg2":
			case "rule_bkbg3":
			case "rule_bkbg4":
			case "rule_bkbg5":
			Integer start = Integer.parseInt(val.split(",")[0]);
			Integer end = Integer.parseInt(val.split(",")[1]);
			if(!(start>=1&&end<=100))throw new ApplicationException("值範圍錯誤(在1~100)！！");
			break;
			

		default:throw new ApplicationException();
		}
		
		ServiceManage.jdbcTemplate.update("UPDATE djt_sys SET val=? WHERE  ckey=?",valObj,mod);
		
		return JsonResult.getSuccessResult();
	}
	
	
	
}
