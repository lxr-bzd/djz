package com.park.api.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxr.commons.exception.ApplicationException;
import com.lxr.framework.long1.JsonResult;
import com.park.api.ServiceManage;
import com.park.api.common.BaseController;
import com.park.api.entity.Game;
import com.park.api.service.GameCoreService;
import com.park.api.service.GameService;

@RequestMapping("user/game")
@Controller
public class GameController extends BaseController{
	
	@Autowired
	GameService gameService;
	
	@Autowired
	GameCoreService gameCoreService;
	
	                                                                                                      
	@RequestMapping("input")
	@ResponseBody
	public Object input(String pei) {
		
		String uid = ServiceManage.securityService.getSessionSubject().getId().toString();
		Integer isSu = ServiceManage.jdbcTemplate.queryForObject("select is_su from djt_user where djt_u_id = ?", Integer.class,uid);
		if(isSu!=1)throw new ApplicationException("不是超级会员无法操作");
		List<String> uids = ServiceManage.jdbcTemplate.queryForList("select djt_u_id from djt_user WHERE djt_islock=1", String.class);
		gameService.doInputTurn(pei,uids);
		System.gc();
		return JsonResult.getSuccessResult(gameService.getMainModel(uid));
	}
	
	@RequestMapping("data")
	@ResponseBody
	public Object data() {
		String uid = ServiceManage.securityService.getSubjectId().toString();
		Map<String, Object> ret = gameService.getMainModel(uid);
		Integer isSu = ServiceManage.jdbcTemplate.queryForObject("select is_su from djt_user where djt_u_id = ?", Integer.class,uid);
		if(isSu==1&&ret==null) {
			List<String> uids = ServiceManage.jdbcTemplate.queryForList("select djt_u_id from djt_user WHERE djt_islock=1", String.class);
			gameService.doRenewTurn(uids);
			ret = gameService.getMainModel(uid);
			
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
		List<String> uids = ServiceManage.jdbcTemplate.queryForList("select djt_u_id from djt_user WHERE djt_islock=1", String.class);
		gameService.doRenewTurn(uids);
		
		Map<String, Object> ret = gameService.getMainModel(uid);
		
		return JsonResult.getSuccessResult(ret);
	}
	
	
	
	@RequestMapping("removeAll")
	@ResponseBody
	public Object removeAll() {
		
		ServiceManage.jdbcTemplate.batchUpdate("TRUNCATE `game_turn`" ,
				"TRUNCATE `djt_history`" ,
				"TRUNCATE `game_history`" ,
				"TRUNCATE `game_runing`" ,
				"TRUNCATE `game_runing_count`");
		
		return JsonResult.getSuccessResult();
	}
	
	
}
