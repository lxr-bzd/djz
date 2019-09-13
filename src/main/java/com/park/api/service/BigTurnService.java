package com.park.api.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxr.commons.exception.ApplicationException;
import com.park.api.ServiceManage;
import com.park.api.entity.BigInputResult;
import com.park.api.entity.BigTurn;
import com.park.api.entity.InputResult;
import com.park.api.entity.Turn;
import com.park.api.service.bean.BigTurnConfig;

@Service
public class BigTurnService {
	
	@Autowired
	TurnService turnService;
	
	@Autowired
	SysService sysService;
	
	 public void doInputBigTurn(String pei) {
		 BigTurn bigTurn = getCurrentTurn();
		 if(bigTurn==null)throw new ApplicationException("请刷新本轮");
		 
		 List<BigInputResult> results = new ArrayList<>();
		 
		 for (int i = 0; i < bigTurn.getBigTurnConfig().getTurnNum(); i++) {
			 
			 BigInputResult bigInputResult = turnService.doInputTurn(pei,bigTurn, i);
			 results.add(bigInputResult);
			
		}
		 
		Long[][] newbBg = handelBg(results);
		List<Long> gjList = JSONArray.parseArray(bigTurn.getGj(), Long.class);
		Long[] newGj = handelGj(gjList.toArray(new Long[gjList.size()]),newbBg);
		
		ServiceManage.jdbcTemplate.update("UPDATE `game_big_turn` SET `frow` = ?, `bg` = ?,  `gj` = ?  WHERE `id` = ?" 
				,bigTurn.getFrow()+1,
				JSONObject.toJSONString(newbBg),
				JSONObject.toJSONString(newGj),
				bigTurn.getId()
				);
		
		 
	 }
	 
	 
	 public Map<String, Object> getMainModel() {
		 BigTurn bigTurn = getCurrentTurn();
		 if(bigTurn==null)return null;
		 
		 Map<String, Object> map = new HashMap<String, Object>();
		 
		 for (int i = 0; i < bigTurn.getBigTurnConfig().getTurnNum(); i++) {
			 map.put(i+"", turnService.getMainModel(bigTurn,i));
		}
		 map.put("bigTurn", bigTurn);
		 return map;
	 }
	 
	public void doRenewTurn(){
		
		BigTurn bigTurn = createBigTurn();
		
		 for (int i = 0; i < bigTurn.getBigTurnConfig().getTurnNum(); i++) {
			 turnService.doRenewTurn(bigTurn,i);
		}
		 
	}
	
	public void doFinishTurn(){
		
		BigTurn bigTurn = getCurrentTurn();
		if(bigTurn==null)return;
		
		 for (int i = 0; i < bigTurn.getBigTurnConfig().getTurnNum(); i++) {
			 turnService.doFinishTurn(bigTurn,i);
		}
		
		 ServiceManage.jdbcTemplate.update("UPDATE game_big_turn SET state=2 WHERE id =?",bigTurn.getId());
			
		
	}
	
	
	public BigTurn createBigTurn() {
		
		BigTurnConfig config = new BigTurnConfig();
		config.setTurnNum(sysService.getSysConfig("turn_num", Integer.class));
		config.setConfLen(sysService.getSysConfig("conf_len", Integer.class));
		config.setTgMod(sysService.getSysConfig("tg_mod", Integer.class));
		config.setTgThre(sysService.getSysConfig("tg_thre", Integer.class));
		
		final BigTurn bigTurn = new BigTurn();
		bigTurn.setFrow(1);
		bigTurn.setGj("[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]");
		bigTurn.setConfig_json(JSONObject.toJSONString(config));
		bigTurn.setBigTurnConfig(config);
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    ServiceManage.jdbcTemplate.update(
	            new PreparedStatementCreator() {
	                public PreparedStatement createPreparedStatement(Connection con) throws SQLException
	                {
	                	PreparedStatement ps = con.prepareStatement("INSERT INTO `game_big_turn`( `frow`, `bg`, `gj`,`config_json`, `state`) "
	                			+ "VALUES ( ?, '', ?,?, 1)" 
	                			,Statement.RETURN_GENERATED_KEYS); 
	                	
	                	ps.setInt(1, bigTurn.getFrow());
	                	ps.setString(2, bigTurn.getGj());
	                	ps.setString(3, bigTurn.getConfig_json());
	                	return ps;
	                }
	            }, keyHolder);
	    bigTurn.setId(keyHolder.getKey().intValue());
	    return bigTurn;
	}
	
	public BigTurn getCurrentTurn() {
		try {
			
			BigTurn bigTurn = ServiceManage.jdbcTemplate.queryForObject("select * from game_big_turn where  state=1 limit 1", 
					new BeanPropertyRowMapper<BigTurn>(BigTurn.class));
			if(StringUtils.isNotBlank(bigTurn.getConfig_json()))
				bigTurn.setBigTurnConfig(JSONObject.parseObject(bigTurn.getConfig_json(), BigTurnConfig.class));
			BigTurnConfig config = bigTurn.getBigTurnConfig();
			config.setConfLen(sysService.getSysConfig("conf_len", Integer.class));
			config.setTgThre(sysService.getSysConfig("tg_thre", Integer.class));
			
			
			return bigTurn;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	
	private Long[][] handelBg( List<BigInputResult> results) {
		Long[][] ret= new Long[][] {new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L}
		,new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L}
		,new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L}};
		
		for (BigInputResult bigInputResult : results) {
			
			for (InputResult inputResult : bigInputResult.getResults()) {
				int i = Integer.valueOf(inputResult.getUid())-1;
				ret[i][0]+=(long)inputResult.getRets()[4];
				ret[i][1]+=(long)inputResult.getRets()[5];
			}
			int i = 10;
			if(bigInputResult.getHzBg()!=null) {//汇总报告
				ret[i][0]+=(long)bigInputResult.getHzBg()[0];
				ret[i][1]+=(long)bigInputResult.getHzBg()[1];
			}
			
			i++;
			if(bigInputResult.getHzqhBg()!=null) {//汇总求和
				ret[i][0]+=(long)bigInputResult.getHzqhBg()[0];
				ret[i][1]+=(long)bigInputResult.getHzqhBg()[1];
				
			}
			i++;
			if(bigInputResult.getHbBg()!=null) {
				ret[i][0]+=(long)bigInputResult.getHbBg()[0];
				ret[i][1]+=(long)bigInputResult.getHbBg()[1];
				
			}
			i++;
			if(bigInputResult.getHbqhBg()!=null) {
				ret[i][0]+=(long)bigInputResult.getHbqhBg()[0];
				ret[i][1]+=(long)bigInputResult.getHbqhBg()[1];
				
			}
			
			i++;
			if(bigInputResult.getXzBg()!=null) {
				ret[i][0]+=(long)bigInputResult.getXzBg()[0];
				ret[i][1]+=(long)bigInputResult.getXzBg()[1];
			}
			i++;
			if(bigInputResult.getXzqhBg()!=null) {
				ret[i][0]+=(long)bigInputResult.getXzqhBg()[0];
				ret[i][1]+=(long)bigInputResult.getXzqhBg()[1];
				
			}
		}
		
		return ret;

	}
	
	private Long[] handelGj(Long[] oldGj,Long[][] newbBg) {
		Long[] gj = new Long[16];
		
		for (int i = 0; i < gj.length; i++) {
			gj[i] = oldGj[i]+Math.abs(newbBg[i][0])+Math.abs(newbBg[i][1]);
		}
		
		return gj;
		
	}

}
