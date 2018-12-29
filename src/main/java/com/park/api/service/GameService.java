package com.park.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.lxr.commons.exception.ApplicationException;
import com.park.api.ServiceManage;
import com.park.api.dao.CountDao;
import com.park.api.dao.CrowDao;
import com.park.api.dao.TmplDao;
import com.park.api.entity.Count;
import com.park.api.entity.Crow;
import com.park.api.entity.Game;
import com.park.api.entity.Tmpl;
import com.park.api.entity.Turn;

@Service
public class GameService {
	
	
	int GAME_COL = 10;
	int GAME_ROW = 182;
	
	int GAME_GROUP = 1000;
	
	@Autowired
	CrowDao crowDao;
	
	@Autowired
	CountDao countDao;
	
	@Autowired
	TmplDao tmplDao;
	
	@Autowired
	GameCoreService gameCoreService;
	
	@Autowired
	TurnService turnService;
	
	public Map<String, Object> getMainModel(String uid) {
		if(true)
		throw new ApplicationException("这个方法不能被调用");
		Map<String, Object> ret = new HashMap<>();
		
		Game game = crowDao.getRuningGame(uid);
		if(game==null) return null;
		
		ret.put("game", game);
		//ret.put("lastRow", crowDao.getInputRow(game.getId()));
		List<Map<String, Object>> maps = ServiceManage.jdbcTemplate.queryForList(
				"select b.id,b.tid,b.hid,b.tg,b.rule,b.rule_type,b.ys,b.g,b.g_sum,a.uid from game_history a left join game_runing_count b on a.id=b.hid  where a.state=1");
		ret.put("counts", maps);
		if(maps.size()>0)
		ret.put("turn",ServiceManage.jdbcTemplate.queryForMap(
				"select * from game_turn where id=?",maps.get(0).get("tid")));
		return ret;
	}
	
	
	
	
	
	public void doNewly(String uid) {
		Game game1 = getRuningGame(uid);
		if(game1!=null)finishGame(game1.getUid(), game1.getId());
		
		//读取使用的组
		int group = getGroup(uid);
		
		if(turnService.getTurn().get()==null) {
			turnService.getTurn().set(turnService.createTurn());
		 }
		
		Turn turn = turnService.getTurn().get();
		
		Game game = new Game();
		game.setUid(uid);
		game.setTbNum(group);
		game.setFocus_row(1);
		game.setCreatetime(System.currentTimeMillis());
		crowDao.createGame(game);
		createRunGame(uid,game.getId(), group);
		countDao.save(game.getId(),turn.getMod(),turn.getRule_type(),turn.getRule(),turn.getId());
		
		
		
	}
	
	/**
	 * 生成运行数据
	 * @param group
	 */
	public void createRunGame(String uid,String hid,int group){
		List<Crow> ret = null;
		
		if(group==1) {
			ret = new ArrayList<>();
			 Random random = new Random();
			 
			 for (int i = 0; i <182; i++) {
				 StringBuilder sheng = new StringBuilder();
				 for (int j = 0; j < GAME_GROUP*10; j++) {
					 if(j%10==0)sheng.append(",");
					 sheng.append(random.nextInt(4)+1);
				}
				Crow crow = new Crow();
				crow.setRow(i+1);
				crow.setSheng(sheng.toString());
				ret.add(crow);
				 
				
			}
		}
		else {
			/*Map<Integer, Crow> crows = new HashMap<>();
			List<Tmpl> tmpls = tmplDao.findTmpl(uid,group);
			
			for (Tmpl tmpl : tmpls) {
				String[] rs = tmpl.getSheng().trim().split(",");
				for (int i = 0; i < rs.length/10; i++) {
					Crow crow = buildCrowByRow(i+1, crows);
					for (int j = 0; j < 10; j++) {
						crow.setSheng(crow.getSheng()+","+rs[j*182+i]);
					}
					
				}
			}*/
			Map<Integer, StringBuilder> shnegs = new HashMap<>();
			List<Tmpl> tmpls = tmplDao.findTmpl(uid,group);
			
			for (Tmpl tmpl : tmpls) {
				String[] rs = tmpl.getSheng().trim().split(",");
				for (int i = 0; i < rs.length/10; i++) {
					StringBuilder shneg = buildShengByRow(i+1, shnegs);
					for (int j = 0; j < 10; j++) {
						shneg.append(",");
						shneg.append(rs[j*182+i]);
					}
					
				}
			}
			ret = new ArrayList<>();
			for (Map.Entry<Integer, StringBuilder> entry : shnegs.entrySet()) { 
				Crow crow = new Crow();
				crow.setRow(entry.getKey());
				crow.setSheng(entry.getValue().toString());
				ret.add(crow);
			} 
			
			
		}
		
		for (Crow crow : ret) {
			String str1 = crow.getSheng();
			crow.setSheng( str1.substring(1, str1.length()));
			crow.setUid(uid);
			crowDao.save(hid,crow);
		}
		
	}
	
	
	
	public void doInput(String pei,String uid) {
		
		Game game = crowDao.getRuningGame(uid);
		if(game == null)throw new ApplicationException("游戏未开始！");
		
		Crow icrow =  crowDao.getInputRow(game.getId(),game.getFocus_row());
		
		if(icrow==null)throw new ApplicationException("本轮游戏已结束！");
		
		//基础计算开始
		String dui = gameCoreService.reckonDui(icrow.getSheng(),pei);
		icrow.setDui(dui);
		icrow.setPei(pei);
		if(icrow.getRow()>1) {
			String gongCol = gameCoreService.reckonGongCol(pei, icrow.getGong());
			icrow.setGong_col(gongCol);
		}
		//基础计算结束
		
		crowDao.update(icrow);
		
		
		
		Crow nCrow = getNextRow(game.getId(), icrow.getRow());
		
		//更新统计,row>=3 queue才会有返回，否则返回null
		String queue = doCount(icrow.getRow(),nCrow!=null,game.getId(),icrow.getGong(),icrow.getGong_col());
		
		
		//处理下一行开始
		if(nCrow!=null) {
			String gong = gameCoreService.reckonGong(nCrow.getSheng(), dui);
			nCrow.setGong(gong);
			crowDao.update(nCrow);
		}else {
			finishGame(game.getUid(),game.getId());
		}
		//处理下一行结束
		
		
		
		if(icrow.getRow()>=3&&nCrow!=null) {
			doCountTurn(game.getId(),queue, nCrow.getGong());
		}
		
		
	}
	
	
	private String doCount(int row,boolean hasNext,String hid,String gong,String gongCol) {
		
		
		if(hasNext) {
			Game g1 = new Game();
			g1.setId(hid);
			g1.setFocus_row(row+1);
			crowDao.updateGame(g1);
		}
		
		if(row<3) {
			
			return null;
		}
		
		
		Map<String, Object> map = ServiceManage.jdbcTemplate.queryForMap("select * from game_runing_count where hid=?",hid);
		
		String[] rule = map.get("rule").toString().split(",");
		int start = Integer.parseInt(rule[0]);
		int end = Integer.parseInt(rule[1]);
		int rule_type = Integer.parseInt(map.get("rule_type").toString());
		
		int mod2 = turnService.getInputTurn().get().getMod2();
		
		String[] rets = CountService.countQueue(row,map.get("queue").toString(), map.get("queue_count").toString(),map.get("grp_queue").toString()
				, gong,gongCol,rule_type,start,end,mod2);
		
		ServiceManage.jdbcTemplate.update("UPDATE game_runing_count SET queue=?,queue_count=?,grp_queue=?,tg=CONCAT(tg,?),ys=ys+? WHERE id=?"
				,rets[0],rets[1],rets[2],rets[3]!=null?rets[3]+",":"",rets[4],map.get("id"));
		
		
		
		return rets[0];
	}
	
	
	/**
	 * 关闭游戏
	 * @param hid
	 */
	 public void finishGame(String uid,String hid) {
		
		 deleteGame(hid);
		
	}
	
	 
	
	
	/**
	 * 
	 * @param hid
	 * @param queue 
	 * @param gong 最后一个无颜色的供
	 */
	private void doCountTurn(String hid,String queue,String gong) {
		Map<String, Object> map = ServiceManage.jdbcTemplate.queryForMap("select b.id, b.tid,a.uid,b.mod,b.rule,b.rule_type from game_history a left join game_runing_count b on a.id = b.hid where a.id=? limit 1", hid);
		turnService.getCountTurnId().set(map.get("tid").toString());
		String[] rule = map.get("rule").toString().split(",");
		int start = Integer.parseInt(rule[0]);
		int end = Integer.parseInt(rule[1]);
		int rule_type =Integer.parseInt(map.get("rule_type").toString());
		//1:A模式，2:B模式
		int mod2 = turnService.getInputTurn().get().getMod2();
		int mod =Integer.parseInt(map.get("mod").toString());
		if(mod==1) {
			//A模式
			Integer[] tgs = CountService.countTgA(queue, gong,rule_type, start, end);
			Object[] objs = CountService.countAllTgA(tgs,mod2);
			ServiceManage.jdbcTemplate.update("update game_runing_count set g=?,g_sum=g_sum+? where id=?",
					JSONObject.toJSONString(objs),
					Math.abs((Integer)objs[4])+Math.abs((Integer)objs[5]),
					map.get("id"));
			
			if(turnService.getTurnCount().get()==null)turnService.getTurnCount().set(new HashMap());
			turnService.getTurnCount().get().put(map.get("uid").toString(), objs);
			
		}else {
			//B模式
			Integer[] tgs = CountService.countTgB(queue, gong,rule_type, start, end);
			Object[] objs = CountService.countAllTgB(tgs);
			ServiceManage.jdbcTemplate.update("update game_runing_count set g=?,g_sum=g_sum+? where id=?",
					JSONObject.toJSONString(objs),
					Math.abs((Integer)objs[0])+Math.abs((Integer)objs[1]),
					map.get("id"));
			if(turnService.getTurnCount().get()==null)turnService.getTurnCount().set(new HashMap());
			turnService.getTurnCount().get().put(map.get("uid").toString(), objs);
			
		}
		
		
		
		
	}
	

		 
	public void deleteGame(String hid) {
		
	   
			 
			 //拷贝统计数据
		ServiceManage.jdbcTemplate.update("INSERT INTO djt_history (tid,`hid`, `uid`, `tbNum`, `focus_row`, `queue_count`, `tg`, `rule`,ys,g_sum) " + 
			 		"		 select tid,hid,b.uid,tbNum,focus_row,queue_count,tg,rule,ys,g_sum from game_runing_count a left join game_history b  on a.hid= b.id " + 
			 		"		  where hid = ? limit 1",hid);
		ServiceManage.jdbcTemplate.update("UPDATE game_turn SET state=2 WHERE id =(select tid from game_runing_count where hid = ? limit 1)",hid);
			
			 String[] sqls = new String[] {
						"delete from game_history where id="+hid,
						"delete from game_runing where hid="+hid,
						"delete from game_runing_count where hid="+hid
				};
			 ServiceManage.jdbcTemplate.batchUpdate(sqls);
			
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
	
	
	
	/**
	 * 
	 * @param uid
	 * @return
	 */
	public int getGroup(String uid) {
		return crowDao.getGroup(uid);
	}
	
	
	
	
	private Crow buildCrowByRow(int row,Map<Integer, Crow> map) {
		Crow crow = map.get(row);
		if(crow==null) {
			crow = new Crow();
			crow.setRow(row);
			map.put(row, crow);
			crow.setSheng("");
		}
		
		return crow;

	}
	
	private StringBuilder buildShengByRow(int row,Map<Integer, StringBuilder> map) {
		StringBuilder crow = map.get(row);
		if(crow==null) {
			crow = new StringBuilder("");
			map.put(row,crow);
		}
		
		return crow;

	}
	
	public Crow getNextRow(String hid,int crow) {
		if(crow>=GAME_ROW)return null;
		
		return crowDao.getRow(hid,crow+1);
		
	}
	
	
	public Crow getAboveRow(String uid,int crow) {
		if(crow<=1)return null;
		return crowDao.getRow(uid, crow-1);
	}
	
	
	public void newGame(String uid){
		crowDao.delete(uid);
	}
	
	
	
	
	
	
	
	
	public Game getRuningGame(String uid) {
		Game game = crowDao.getRuningGame(uid);
		
		return game;

	}
	
	
}
