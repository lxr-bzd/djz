package com.park.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.eclipse.jdt.internal.compiler.env.IGenericField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lxr.commons.exception.ApplicationException;
import com.park.api.ServiceManage;
import com.park.api.dao.CountDao;
import com.park.api.dao.CrowDao;
import com.park.api.dao.TmplDao;
import com.park.api.entity.Count;
import com.park.api.entity.Crow;
import com.park.api.entity.Game;
import com.park.api.entity.Tmpl;

@Service
public class GameService {
	int GAME_COL = 10;
	int GAME_ROW = 182;
	int GAME_ROW_INDEX = 180;
	
	@Autowired
	CrowDao crowDao;
	
	@Autowired
	CountDao countDao;
	
	@Autowired
	TmplDao tmplDao;
	
	@Autowired
	GameCoreService gameCoreService;
	
	public Map<String, Object> getMainModel(String uid) {
		
		Map<String, Object> ret = new HashMap<>();
		
		Game game = crowDao.getRuningGame(uid);
		if(game==null) {doNewly(uid);
		game = crowDao.getRuningGame(uid);
		}
		ret.put("game", game);
		ret.put("mainTb", crowDao.getAllGong(game.getId()));
		ret.put("counts", countDao.findAll(game.getId()));
		ret.put("mod", getMod(game.getTbNum()));
		return ret;
	}
	
	
	
	
	
	public void doNewly(String uid) {
		Game game1 = getRuningGame(uid);
		if(game1!=null)deleteGame(game1.getId());
		
		//读取使用的组
		int group = getGroup();
		
		/*if(GAME_COL!=tmplDao.getTmplNum(group))
			throw new ApplicationException("后台模板配置错误");
		crowDao.delete(uid);*/
		Game game = new Game();
		game.setUid(uid);
		game.setTbNum(group);
		game.setInfo("");
		game.setCreatetime(System.currentTimeMillis());
		
		crowDao.createGame(game);
		crowDao.temp2ruing(uid,game.getId(), group);
		countDao.save(game.getId());
		
		//crowDao.setUserGroup(uid, group);
		
		
	}
	
	
	
	public void doinput(String pei,String uid) {
		
		Game game = crowDao.getRuningGame(uid);
		if(game == null) {
			doNewly(uid);
			game = crowDao.getRuningGame(uid);
		}
		
		Crow icrow =  crowDao.getInputRow2(game.getId());
		
		if(icrow==null)throw new ApplicationException("错误：本轮游戏已结束！");
		
		String dui = gameCoreService.reckonDui(icrow.getSheng(),pei);
		icrow.setDui(dui);
		icrow.setPei(pei);
		if(icrow.getRow()>-1) {
			String gongCol = gameCoreService.reckonGongCol(pei, icrow.getGong());
			icrow.setGong_col(gongCol);
		}
		String tgVal = null;
		
		//处理统计
		if(icrow.getRow()>0)
			tgVal = count2(game.getId(), icrow.getRow(), icrow.getGong(), icrow.getGong_col(),crowDao.getUpTgVal(game.getId()));
		
		icrow.setTg_val(tgVal);
		crowDao.update(icrow);
		
		//处理下一行
		Crow nCrow = getNextRow(game.getId(), icrow.getRow());
		if(nCrow!=null) {
		String gong = gameCoreService.reckonGong(nCrow.getSheng(), dui);
		nCrow.setGong(gong);
		crowDao.update(nCrow);
		}else {
			finishGame(game.getId());
			
		}
		
		
	}
	
	
	private Integer getMod(int tbNum) {
		return ServiceManage.jdbcTemplate.queryForObject("select model from djt_use_table where d_table_id=?"
				, Integer.class,tbNum);
		
	}
	
	/**
	 * 处理统计
	 */
	private String count2(String hid,int row,String gong,String gong_col,String upTgVal) {
	
		List<Count> counts = countDao.findAll(hid);
		
		Integer[] tgval = new Integer[] {0,0,0};
		
		for (int i = 0; i < counts.size(); i++) {
			Count count = counts.get(i);
			int sindex = (count.getIndex()-1)*2;
			String lval = gong_col.substring(sindex, sindex+1).equals("1")?"-1":"01";
			String rval = gong_col.substring(sindex+1, sindex+2).equals("1")?"-1":"01";
			String[] lProvide =  gameCoreService.reckonIsProvide2(row, count.getLcount(), Integer.parseInt(lval),count.getL_lj());
			String[] rProvide =  gameCoreService.reckonIsProvide2(row, count.getRcount(), Integer.parseInt(rval),count.getR_lj());
			
			count.setAl_tg(count.getAl_tg()+lProvide[0]);
			count.setBl_tg(count.getBl_tg()+lProvide[1]);
			count.setCl_tg(count.getCl_tg()+lProvide[2]);
			count.setL_info(count.getL_info()+lval);
			
			count.setAr_tg(count.getAr_tg()+rProvide[0]);
			count.setBr_tg(count.getBr_tg()+rProvide[1]);
			count.setCr_tg(count.getCr_tg()+rProvide[2]);
			count.setR_info(count.getR_info()+rval);
			
			
			if(lProvide[0].startsWith("01"))tgval[0]+=Integer.parseInt(lProvide[0].substring(2, 4));
			if(rProvide[0].startsWith("01"))tgval[0]+=Integer.parseInt(rProvide[0].substring(2, 4));
			
			if(lProvide[1].startsWith("01"))tgval[1]+=Integer.parseInt(lProvide[1].substring(2, 4));
			if(rProvide[1].startsWith("01"))tgval[1]+=Integer.parseInt(rProvide[1].substring(2, 4));
			
			if(lProvide[2].startsWith("01"))tgval[2]+=Integer.parseInt(lProvide[2].substring(2, 4));
			if(rProvide[2].startsWith("01"))tgval[2]+=Integer.parseInt(rProvide[2].substring(2, 4));
			
			if(row%6==0) {
				String lJie = count.getL_info().substring(count.getL_info().length()-12, count.getL_info().length());
				String rJie = count.getR_info().substring(count.getR_info().length()-12, count.getR_info().length());
				
				Object[] clcountABC = gameCoreService.reckon6Count2(lJie,count.getLcount(),count.getL_lj());
				Object[] crcountABC = gameCoreService.reckon6Count2(rJie,count.getRcount(),count.getR_lj());
				count.setLcount(count.getLcount()+clcountABC[0]);
				count.setRcount(count.getRcount()+crcountABC[0]);
				count.setL_lj(gameCoreService.reckonPile2(count.getL_lj(), (Integer[]) clcountABC[1]));
				count.setR_lj(gameCoreService.reckonPile2(count.getR_lj(), (Integer[]) crcountABC[1]));
			}
			
			countDao.update(count);
		}
		
		
		if(StringUtils.isEmpty(upTgVal))
			return tgval[0]+","+tgval[1]+","+tgval[2];
		String[] upvals = upTgVal.split(",");
		
		return (Integer.parseInt(upvals[0])+tgval[0])+","+(Integer.parseInt(upvals[1])+tgval[1])+","+(Integer.parseInt(upvals[2])+tgval[2]);
		
	}
	
	
	
	/*
	 * 获取第几组
	 */
	private int getGroup() {
		return crowDao.getGroup();
	}
	
	
	/**
	 * 模板转换
	 * @param tmpls
	 * @return
	 */
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
			crow.setRow(row);
			map.put(row, crow);
			crow.setSheng("");
		}
		
		return crow;

	}
	
	
	
	
	/**
	 * 获取最后一次填入的行
	 * @param uid
	 * @return
	 */
	public Crow getLastCrow(String uid) {
		return crowDao.getLastRow(uid);
	}
	
	
	public Crow getNextRow(String hid,int crow) {
		if(crow>=GAME_ROW_INDEX)return null;
		
		return crowDao.getRow(hid,crow+1);
		
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
	
	
	public Game getRuningGame(String uid) {
		Game game = crowDao.getRuningGame(uid);
		
		return game;

	}
	
	 public void finishGame(String hid) {
		
		 Game game = new Game();
		 game.setId(hid);
		 game.setState(2);
		 game.setEndtime(System.currentTimeMillis());
		 crowDao.updateGame(game);
	}
	
	 
	 public void deleteGame(String hid) {
		
		 ServiceManage.jdbcTemplate.update("delete from game_history where id=?", hid);
		 ServiceManage.jdbcTemplate.update("delete from game_runing where hid=?", hid);
		 ServiceManage.jdbcTemplate.update("delete from game_runing_count where hid=?", hid);

	}
	
	
	
}
