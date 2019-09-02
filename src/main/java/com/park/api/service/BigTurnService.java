package com.park.api.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.lxr.commons.exception.ApplicationException;
import com.park.api.ServiceManage;
import com.park.api.entity.BigTurn;
import com.park.api.entity.Turn;

import sun.org.mozilla.javascript.internal.ast.ForInLoop;

@Service
public class BigTurnService {
	
	@Autowired
	TurnService turnService;
	
	 public void doInputBigTurn(String pei) {
		 BigTurn bigTurn = getCreateTurn();
		 if(bigTurn==null)throw new ApplicationException("请刷新本轮");
		 
		 for (int i = 0; i < 10; i++) {
			 
			 turnService.doInputTurn(pei, i);
			
		}
		 
		 
	 }
	 
	 
	 public Map<Integer, Object> getMainModel() {
		 if(getCreateTurn()==null)return null;
		 
		 Map<Integer, Object> map = new HashMap<Integer, Object>();
		 
		 for (int i = 0; i < 10; i++) {
			 map.put(i, turnService.getMainModel());
		}
		 
		 return map;
	 }
	 
	public void doRenewTurn(){
		
		 for (int i = 0; i < 10; i++) {
			 turnService.doRenewTurn();
		}
		 
	}
	
	public void doFinishTurn(){
		
		 for (int i = 0; i < 10; i++) {
			 turnService.doFinishTurn();
		}
		
		
	}
	
	
	BigTurn getCreateTurn() {
		try {
			return ServiceManage.jdbcTemplate.queryForObject("select * from game_big_turn where turn_no=? AND state=1 limit 1", 
					new BeanPropertyRowMapper<BigTurn>(BigTurn.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	

}
