package com.park.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.internal.compiler.env.IGenericField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lxr.commons.exception.ApplicationException;
import com.park.api.dao.CrowDao;
import com.park.api.dao.TmplDao;
import com.park.api.entity.Crow;
import com.park.api.entity.Tmpl;

@Service
public class GameService {
	int GAME_COL = 102;
	int GAME_ROW = 162;
	
	@Autowired
	CrowDao crowDao;
	
	@Autowired
	TmplDao tmplDao;
	
	@Autowired
	GameCoreService gameCoreService;
	
	public Map<String, Object> getMainModel(String uid) {
		
		Map<String, Object> ret = new HashMap<>();
		ret.put("gameRow", GAME_ROW);
		
		Crow last = getLastCrow(uid);
		
		if(last==null)return ret;
		
			if(last.getCrow()>=1) {
			Crow upCrow = getAboveRow(uid, last.getCrow());
			
			if(last.getCrow()>=3)
			ret.put("upData", gameCoreService.reckonAllCount(last.getCount(), upCrow.getCount()));
			
			ret.put("upRow", last);
			
			}
			
			Crow cRow = getNextRow(uid, last.getCrow());
			if(cRow!=null) {
				ret.put("cRow", cRow);
			}

			
			return ret;
	}
	
	/*
	 * 获取第几组
	 */
	private int getGroup() {
		return crowDao.getGroup();
	}
	
	
	
	public void donewly(String uid) {
		int group = getGroup();
		
		if(GAME_COL!=tmplDao.getTmplNum(group))
			throw new ApplicationException("后台模板配置错误");
		crowDao.delete(uid);
		
		List<Tmpl> tmpls = tmplDao.findTmpl(group);
		List<Crow> crows = toCrows(tmpls);
		
		for (Crow crow : crows) {
			crow.setUid(uid);
			crowDao.save(crow);
		}
		
		crowDao.setUserGroup(uid, group);
		
		
	}
	
	public List<Crow> toCrows(List<Tmpl> tmpls) {
		Map<Integer, Crow> crows = new HashMap<>();
		for (Tmpl tmpl : tmpls) {
			String[] rs = tmpl.getTgroup().trim().split(",");
			for (int i = 0; i < GAME_ROW; i++) {
				Crow crow = getCrowByRow(i+1, crows);
				crow.setSheng(crow.getSheng()+","+rs[i]);
				
			}
		}
		
		List<Crow> ret = new ArrayList<>(crows.values());
		
		for (Crow crow : ret) {
			String str1 = crow.getSheng();
			crow.setSheng( str1.substring(1, str1.length()));
		}
		return ret;
	}
	
	private Crow getCrowByRow(int row,Map<Integer, Crow> map) {
		Crow crow = map.get(row);
		if(crow==null) {
			crow = new Crow();
			crow.setCrow(row);
			map.put(row, crow);
			crow.setSheng("");
		}
		
		return crow;

	}
	
	
	
	
	public void doinput(String pei,String uid) {
		if(crowDao.getGameNum(uid)<1)donewly(uid);
		
		Crow icrow =  getInputRow(uid);
		
		if(icrow==null)throw new ApplicationException("错误：本轮游戏已结束！");
		
		String dui = gameCoreService.reckonDui(icrow.getSheng(),pei);
		icrow.setDui(dui);
		icrow.setPei(pei);
		if(icrow.getCrow()>1) {
			String gongCol = gameCoreService.reckonGongCol(pei, icrow.getGong());
			String count = gameCoreService.reckonCount(gongCol);
			icrow.setGong_col(gongCol);
			icrow.setCount(count);
		}
		
		Crow nCrow = getNextRow(uid, icrow.getCrow());
		
		if(nCrow!=null) {
		String gong = gameCoreService.reckonGong(nCrow.getSheng(), dui);
		nCrow.setGong(gong);
		crowDao.update(nCrow);
		}
		
		crowDao.update(icrow);
		
		
	}
	
	
	/**
	 * 获取最后一次填入的行
	 * @param uid
	 * @return
	 */
	public Crow getLastCrow(String uid) {
		return crowDao.getLastRow(uid);
	}
	
	
	public Crow getNextRow(String uid,int crow) {
		if(crow>=GAME_ROW)return null;
		
		return crowDao.getNextRow(uid,crow);
		
	}
	
	
	public Crow getAboveRow(String uid,int crow) {
		if(crow<=1)return null;
		return crowDao.getRow(uid, crow-1);
	}
	
	
	public void newGame(String uid){
		crowDao.delete(uid);
	}
	
	
	
	
	
	public Crow getInputRow(String uid) {
		return crowDao.getInputRow(uid);

	}
	
	
	
	
	
}
