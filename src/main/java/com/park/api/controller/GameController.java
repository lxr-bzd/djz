package com.park.api.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.park.api.service.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxr.commons.exception.ApplicationException;
import com.lxr.framework.long1.JsonResult;
import com.park.api.ServiceManage;
import com.park.api.common.BaseController;
import com.park.api.entity.BigTurn;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("user/game")
@Controller
public class GameController extends BaseController{
	
	@Autowired
	GameService gameService;

	@Autowired
	UserService userService;
	
	@Autowired
	BigTurnService bigTurnService;
	
	                                                                                                      
	@RequestMapping("input")
	@ResponseBody
	public Object input(String pei) {
		
		String uid = ServiceManage.securityService.getSessionSubject().getId().toString();
		Integer isSu = ServiceManage.jdbcTemplate.queryForObject("select is_su from djt_user where djt_u_id = ?", Integer.class,uid);
		if(isSu!=1)throw new ApplicationException("不是超级会员无法操作");
		
		bigTurnService.doInputBigTurn(pei);
		System.gc();
		Object ret = bigTurnService.getMainModel();
		if(ret==null)throw new ApplicationException("本輪已結束");
		return JsonResult.getSuccessResult(ret);
	}
	
	@RequestMapping("data")
	@ResponseBody
	public Object data() {
		String uid = ServiceManage.securityService.getSubjectId().toString();
		Map<String, Object> ret = bigTurnService.getMainModel();
		Integer isSu = ServiceManage.jdbcTemplate.queryForObject("select is_su from djt_user where djt_u_id = ?", Integer.class,uid);
		if(isSu==1&&ret==null) {
			bigTurnService.doRenewTurn();
			ret = bigTurnService.getMainModel();
			
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
		bigTurnService.doFinishTurn();
		bigTurnService.doRenewTurn();
		
		Map<String, Object> ret = bigTurnService.getMainModel();
		
		return JsonResult.getSuccessResult(ret);
	}
	
	
	
	@RequestMapping("removeAll")
	@ResponseBody
	public Object removeAll() {
		
		ServiceManage.jdbcTemplate.batchUpdate(
				"TRUNCATE `game_big_turn`" ,
				"TRUNCATE `game_turn`" ,
				"TRUNCATE `game_turn2`" ,
				"TRUNCATE `djt_history`" ,
				"TRUNCATE `game_history`" ,
				"TRUNCATE `game_runing`" ,
				"TRUNCATE `game_turn_group`" ,
				"TRUNCATE `game_runing_count`");
		
		return JsonResult.getSuccessResult();
	}
	
	@RequestMapping("clear")
	@ResponseBody
	public Object clear() {
		
		ServiceManage.jdbcTemplate.batchUpdate(
				"DELETE FROM `game_big_turn` WHERE state=1",
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
		case 4:
				fname = "zd_lock";
				break;
			case 5:
				fname = "jgzd_lock";
				break;
			case 6:
				fname = "inverse_lock";
				break;
			case 7:
				fname = "xb_inv_lock";
				break;
			case 8:
				fname = "xb_lock";
				break;
			case 9:
				fname = "bkzd_lock";
				break;
            case 10:
                fname = "bkbg_inv_lock";
                break;
			case 11:
				fname = "bkhz_lock";
				break;
			case 12:
				fname = "turn_bkhz_lock";
				break;
			case 13:
				fname = "zdbg_lock";
				break;
			case 14:
				fname = "bkhz_inv_lock";
				break;

		default:
			throw new ApplicationException("错误的类型");
		}
		
		
		BigTurn bigTurn = bigTurnService.getCurrentTurn();

		if(ArrayUtils.contains(new int[]{1,2,3},mod)){
            String lockStr = ServiceManage.jdbcTemplate.queryForObject("select "+fname+" from game_turn where turn_no = 0 AND big_turn_id=?", String.class,bigTurn.getId());

            String[] locks = lockStr.split(",");
            locks[Integer.valueOf(uid)-1] = val;
            String newLockStr = StringUtils.join(locks, ",");
            ServiceManage.jdbcTemplate.update("UPDATE game_turn SET "+fname+"=? WHERE  big_turn_id=?",newLockStr, bigTurn.getId());


		}else{
            String lockStr = ServiceManage.jdbcTemplate.queryForObject("select "+fname+" from game_big_turn where id=?", String.class,bigTurn.getId());

            String[] locks = lockStr.split(",");
            locks[Integer.valueOf(uid)-1] = val;
            String newLockStr = StringUtils.join(locks, ",");
            ServiceManage.jdbcTemplate.update("UPDATE game_big_turn SET "+fname+"=? WHERE  id=?",newLockStr, bigTurn.getId());


        }

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
		
		BigTurn bigTurn = bigTurnService.getCurrentTurn();
		
		String lockStr = ServiceManage.jdbcTemplate.queryForObject("select "+fname+" from game_turn where turn_no = 0 AND big_turn_id=?", String.class,bigTurn.getId());
		
		String[] locks = lockStr.split(",");
		locks[Integer.valueOf(uid)-1] = val;
		String newLockStr = StringUtils.join(locks, ",");
		ServiceManage.jdbcTemplate.update("UPDATE game_turn SET "+fname+"=? WHERE  big_turn_id=?",newLockStr,bigTurn.getId());
		
		return JsonResult.getSuccessResult();
	}

	@RequestMapping("validationSettingPwd")
	@ResponseBody
	public Object validationSettingPwd(HttpServletRequest request, String pwd) {

		String account = ServiceManage.securityService.getSessionSubject().getAccount();
		Map userMap = userService.getByAccount(account);
		Map map = new HashMap();
		if(pwd.equals(userMap.get("settingPwd"))){
			map.put("result","1");
			map.put("msg","成功");
		}else{
			map.put("result","0");
			map.put("msg","验证失败");
		}


		return JsonResult.getSuccessResult(map);
	}


	public static void main(String[] args) {
		Integer.parseInt("4294963100");
	}
	
}
