package com.park.api.controller;

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
	
	
	@RequestMapping("new")
	@ResponseBody
	public Object newly() {
		
		String uid = ServiceManage.securityService.getSessionSubject().getId().toString();
		gameService.donewly(uid);
		return JsonResult.getSuccessResult();
	}
	
	
	
}
