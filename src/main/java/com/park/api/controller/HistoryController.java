package com.park.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxr.commons.exception.ApplicationException;
import com.lxr.framework.long1.JsonResult;
import com.park.api.ServiceManage;
import com.park.api.common.BaseController;

@RequestMapping("user/his")
@Controller
public class HistoryController extends BaseController{
	
	@RequestMapping()
	@ResponseBody
	public Object his() {
		Map<String, Object> map = new HashMap<>();
		map.put("turns", ServiceManage.jdbcTemplate.queryForList("SELECT a.* FROM game_turn a WHERE a.state=2"));
		map.put("his", ServiceManage.jdbcTemplate.queryForList("SELECT a.* FROM djt_history a "));
		
		
		return JsonResult.getSuccessResult(map);

	}
	
	@RequestMapping("del")
	@ResponseBody
	public Object del(String mod,String tid) {
		if(mod.equals("1")) {
			if(StringUtils.isEmpty(tid))
				throw new ApplicationException("参数错误");
			ServiceManage.jdbcTemplate.batchUpdate(
					 "delete from game_turn WHERE id="+tid
					,"delete from djt_history WHERE tid="+tid);
			
		} 
		if(mod.equals("2")) {
			ServiceManage.jdbcTemplate.batchUpdate(
					 "delete from game_turn WHERE state=2"
					,"delete from djt_history ");
		}
		
		
		return JsonResult.getSuccessResult();

	}

}