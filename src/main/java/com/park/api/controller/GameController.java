package com.park.api.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lxr.framework.long1.JsonResult;
import com.park.api.ServiceManage;
import com.park.api.common.BaseController;
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
		gameService.doinput(pei, uid);
		
		return JsonResult.getSuccessResult(gameService.getMainModel(uid));
	}
	
	@RequestMapping("data")
	@ResponseBody
	public Object data() {
		String uid = ServiceManage.securityService.getSubjectId().toString();
		
		Map<String, Object> ret = gameService.getMainModel(uid);
		
		return JsonResult.getSuccessResult(ret);
	}
	
	
	@RequestMapping("renew")
	@ResponseBody
	public Object renew() {
		
		String uid = ServiceManage.securityService.getSessionSubject().getId().toString();
		gameService.doNewly(uid);
		Map<String, Object> ret = gameService.getMainModel(uid);
		
		List<Map<String, Object>> list = ServiceManage.jdbcTemplate.queryForList("select id from game_history where uid=? AND state=2 order by createtime DESC limit 30,5", uid);
		if(list!=null&&list.size()>0) 
			for (int i = 0; i < list.size(); i++) {
				gameService.deleteGame(list.get(i).get("id").toString());
			}
			
		
		
		return JsonResult.getSuccessResult(ret);
	}
	
	
	
}
