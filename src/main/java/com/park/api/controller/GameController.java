package com.park.api.controller;


import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxr.commons.exception.ApplicationException;
import com.lxr.framework.long1.JsonResult;
import com.park.api.ServiceManage;
import com.park.api.common.BaseController;
import com.park.api.service.GameCoreService;
import com.park.api.service.GameService;
import com.park.api.service.TurnService;

@RequestMapping("user/game")
@Controller
public class GameController extends BaseController{
	
	@Autowired
	GameService gameService;
	
	
	@Autowired
	TurnService turnService;
	
	                                                                                                      
	@RequestMapping("input")
	@ResponseBody
	public Object input(String pei) {
		
		String uid = ServiceManage.securityService.getSessionSubject().getId().toString();
		Integer isSu = ServiceManage.jdbcTemplate.queryForObject("select is_su from djt_user where djt_u_id = ?", Integer.class,uid);
		if(isSu!=1)throw new ApplicationException("不是超级会员无法操作");
		
		turnService.doInputTurn(pei);
		System.gc();
		Object ret = turnService.getMainModel(uid);
		if(ret==null)throw new ApplicationException("本輪已結束");
		return JsonResult.getSuccessResult(ret);
	}
	
	@RequestMapping("data")
	@ResponseBody
	public Object data() {
		String uid = ServiceManage.securityService.getSubjectId().toString();
		Map<String, Object> ret = turnService.getMainModel(uid);
		Integer isSu = ServiceManage.jdbcTemplate.queryForObject("select is_su from djt_user where djt_u_id = ?", Integer.class,uid);
		if(isSu==1&&ret==null) {
			turnService.doRenewTurn();
			ret = turnService.getMainModel(uid);
			
		}
		
		if(ret == null) throw new ApplicationException("没有正在进行的表格");
		return JsonResult.getSuccessResult(ret);
	}
	
	
	@RequestMapping("renew")
	@ResponseBody
	public Object renew() {
		
		String uid = ServiceManage.securityService.getSessionSubject().getId().toString();
		Integer isSu = ServiceManage.jdbcTemplate.queryForObject("select is_su from djt_user where djt_u_id = ?", Integer.class,uid);
		if(isSu!=1)throw new ApplicationException("不是超级会员无法操作");
		turnService.doFinishTurn();
		turnService.doRenewTurn();
		
		Map<String, Object> ret = turnService.getMainModel(uid);
		
		return JsonResult.getSuccessResult(ret);
	}
	
	
	
	@RequestMapping("removeAll")
	@ResponseBody
	public Object removeAll() {
		
		ServiceManage.jdbcTemplate.batchUpdate(
				"DELETE FROM `game_turn` WHERE state=1" ,
				"TRUNCATE `game_history`" ,
				"TRUNCATE `game_runing`" ,
				"TRUNCATE `game_runing_count`");
		
		return JsonResult.getSuccessResult();
	}
	
	@RequestMapping("setLock")
	@ResponseBody
	public Object setLock(Integer mod,String uid,String tid,String val) {
		
		if(StringUtils.isEmpty(uid)||StringUtils.isEmpty(tid)||StringUtils.isEmpty(val)||
				!(val.equals("0")||val.equals("1")))
			throw new ApplicationException("参数错误");
		
		String fname = null;
		switch (mod) {
		case 1:
			fname = "user_lock";
			break;
		case 2:
			fname = "hbbg_lock";
			break;
		case 3:
			fname = "xzbg_lock";
			break;

		default:
			break;
		}
		
		
		String lockStr = ServiceManage.jdbcTemplate.queryForObject("select "+fname+" from game_turn where id = ?", String.class,tid);
		
		String[] locks = lockStr.split(",");
		locks[Integer.valueOf(uid)-1] = val;
		String newLockStr = StringUtils.join(locks, ",");
		ServiceManage.jdbcTemplate.update("UPDATE game_turn SET "+fname+"=? WHERE id=?",newLockStr,tid);
		
		return JsonResult.getSuccessResult();
	}
	
	
	
	
	@RequestMapping("setConfig")
	@ResponseBody
	public Object setXzbgConfig(Integer mod,String uid,String tid,String val) {
		
		if(StringUtils.isEmpty(uid)||StringUtils.isEmpty(tid)||StringUtils.isEmpty(val))
			throw new ApplicationException("参数错误");
		
		String fname = null;
		switch (mod) {
		case 1:
			fname = "hbbg_config";
			break;
		case 2:
			fname = "xzbg_config";
			break;

		default:
			break;
		}
		
		
		String lockStr = ServiceManage.jdbcTemplate.queryForObject("select "+fname+" from game_turn where id = ?", String.class,tid);
		
		String[] locks = lockStr.split(",");
		locks[Integer.valueOf(uid)-1] = val;
		String newLockStr = StringUtils.join(locks, ",");
		ServiceManage.jdbcTemplate.update("UPDATE game_turn SET "+fname+"=? WHERE id=?",newLockStr,tid);
		
		return JsonResult.getSuccessResult();
	}
	
	
}
