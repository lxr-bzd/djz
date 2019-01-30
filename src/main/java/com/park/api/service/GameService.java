package com.park.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.lxr.commons.exception.ApplicationException;
import com.park.api.ServiceManage;
import com.park.api.common.ZipUtils;
import com.park.api.dao.CountDao;
import com.park.api.dao.CrowDao;
import com.park.api.dao.TmplDao;
import com.park.api.entity.Count;
import com.park.api.entity.Crow;
import com.park.api.entity.Game;
import com.park.api.entity.Tmpl;
import com.park.api.entity.Turn;
import com.park.api.service.CountService.CountQueueResult;

@Service
public class GameService {
	
	static Logger logger = LoggerFactory.getLogger(GameService.class);
	
	
	int GAME_COL = 10;
	int GAME_ROW = 182;
	
	@Autowired
	CrowDao crowDao;
	
	@Autowired
	CountDao countDao;
	
	@Autowired
	TmplDao tmplDao;
	
	@Autowired
	GameCoreService2 gameCoreService;
	
	@Autowired
	TurnService turnService;
	
	
	
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
		game.setTid(turn.getId());
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
		
		int groupNum = GameCoreService2.SHENG_GROUP;
		
		int rowNum = 182;
		
		if(group==1) {
			ret = new ArrayList<>();
			 Random random = new Random();
			 
			 for (int i = 0; i <rowNum; i++) {
				 StringBuilder sheng = new StringBuilder();
				 for (int j = 0; j < groupNum; j++) {
					 String item = GameCoreService2.createShengStr(random.nextInt(1024), random.nextInt(1024));
					 sheng.append(item+",");
				}
				Crow crow = new Crow();
				crow.setRow(i+1);
				crow.setSheng(sheng.substring(0, sheng.length()-1));
				ret.add(crow);
				 
				
			}
		}
		else if(group==2||group==3||group==4||group==5){
			
			String u = null;
			
			switch (group) {
			case 2:
				u = "0000,0000,0000,0000,0000,0000,0000,0000,0000,0000,";
				break;
			case 3:
				u = "J0J0,J0J0,J0J0,J0J0,J0J0,J0J0,J0J0,J0J0,J0J0,J0J0,";
				break;
			case 4:
				u = "IYIY,IYIY,IYIY,IYIY,IYIY,IYIY,IYIY,IYIY,IYIY,IYIY,";
				break;
			case 5:
				u = "SFSF,SFSF,SFSF,SFSF,SFSF,SFSF,SFSF,SFSF,SFSF,SFSF,";
				break;
			}
			
			//100组
			String ten0 = "";
			
			for (int i = 0; i < 10; i++) {
				ten0+=u;
			}
			
			ret = new ArrayList<>();
			
			for (int i = 0; i < rowNum; i++) {
				StringBuilder sheng = new StringBuilder("");
				for (int j = 0; j < groupNum/100; j++) {
					sheng.append(ten0);
					
				}
				
				Crow crow = new Crow();
				crow.setRow(i+1);
				crow.setSheng(sheng.substring(0,sheng.length()-1));
				ret.add(crow);
				
				
			}
			
			
		}else {
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
			crow.setUid(uid);
			crowDao.save(hid,crow);
		}
		
	}
	
	
	
	public void doInput(Game game,String pei) {
		
		if(game == null)throw new ApplicationException("游戏未开始！");
		
		Crow icrow =  getInputRow(game.getId(),game.getFocus_row());
		
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
		
		updateCrow(icrow);
		// insulateCleanGame(game.getId(),icrow.getRow());
		
		Crow nCrow = getNextRow(game.getId(), icrow.getRow());
		//System.out.println(uid+"输入行："+((System.currentTimeMillis()-start))+"ms");
		//start = System.currentTimeMillis();
		
		
		//System.out.println(uid+"统计行："+((System.currentTimeMillis()-start))+"ms");
		//start = System.currentTimeMillis();
		//处理下一行开始
		if(nCrow!=null) {
			String gong = gameCoreService.reckonGong(nCrow.getSheng(), dui);
			nCrow.setGong(gong);
			updateCrow(nCrow);
		}else {
			finishGame(game.getUid(),game.getId());
		}
		
		//String queue = doCount(game.getId(),icrow.getRow(),icrow.getGong(),icrow.getGong_col(),nCrow);
		//更新统计,row>=3 queue才会有返回，否则返回null
		String queue = doCount2(game.getId(),game.getUid(),icrow.getRow(),icrow.getGong(),icrow.getGong_col(),nCrow);
		
	}
	
	
	private String doCount2(String hid,String uid,int row,String gong,String gongCol,Crow nCrow) {
		
		if(nCrow!=null) {
			Game g1 = new Game();
			g1.setId(hid);
			g1.setFocus_row(row+1);
			crowDao.updateGame(g1);
		}
		if(row<3) return null;
		
		Long start1 = System.currentTimeMillis();
		Map<String, Object> map = ServiceManage.jdbcTemplate.queryForMap("select `id`,tid,   `queue`,  `queue_count`,  `grp_queue`,   `rule`,  `rule_type`,`mod` from game_runing_count where hid=?",hid);
		System.out.println(hid+"查询："+((System.currentTimeMillis()-start1))+"ms");
		start1 = System.currentTimeMillis();
		String[] rule = map.get("rule").toString().split(",");
		int start = Integer.parseInt(rule[0]);
		int end = Integer.parseInt(rule[1]);
		int rule_type = Integer.parseInt(map.get("rule_type").toString());
		int mod =Integer.parseInt(map.get("mod").toString());
		int mod2 = turnService.getInputTurn().get().getMod2();
		String tid = map.get("tid").toString();
		
		
		JgHandel jgHandel = createJgHandel(row, rule_type, start, end, mod2);
		
		CountTurn countTurn = null;
		if(row>=3&&nCrow!=null) 
			countTurn = createCountTurnHandel(mod,rule_type,start,end,mod2);
		
		
		CountQueueResult rets = CountService.countQueue2(row,map.get("queue")==null?null:map.get("queue").toString(), map.get("queue_count").toString()
				,"", gong,gongCol,nCrow!=null?nCrow.getGong():null,new GameEach[] {countTurn,jgHandel});
		System.out.println(hid+"计算："+((System.currentTimeMillis()-start1))+"ms");
		start1 = System.currentTimeMillis();
		
		/*Object[] countTurnResult = null;
		if(countTurn!=null) 
			countTurnResult = countTurn.getResult();*/
		
		ServiceManage.jdbcTemplate.update("UPDATE game_runing_count SET queue=?,queue_count=?,tg=CONCAT(tg,?),ys=ys+? WHERE id=?"
				,rets.getQueue(),rets.getQueueCount(),jgHandel.getResult()+",",rets.getYs(),map.get("id"));
		System.out.println(hid+"update："+((System.currentTimeMillis()-start1))+"ms");
		start1 = System.currentTimeMillis();
		
		if(countTurn!=null) {
			turnService.getCountTurnId().set(tid);
			Object[] objs = countTurn.getResult();
			ServiceManage.jdbcTemplate.update("update game_runing_count set g=?,g_sum=g_sum+? where id=?",
					JSONObject.toJSONString(objs),
					Math.abs((Integer)objs[4])+Math.abs((Integer)objs[5]),
					map.get("id"));
			
			if(turnService.getTurnCount().get()==null)turnService.getTurnCount().set(new HashMap());
			turnService.getTurnCount().get().put(uid, objs);
		}
		return rets.getQueue();
	}
	private String doCount(String hid,int row,String gong,String gongCol,Crow nCrow) {
		
		if(nCrow!=null) {
			Game g1 = new Game();
			g1.setId(hid);
			g1.setFocus_row(row+1);
			crowDao.updateGame(g1);
		}
		
		if(row<3) {
			
			return null;
		}
		
		Long start1 = System.currentTimeMillis();
		Map<String, Object> map = ServiceManage.jdbcTemplate.queryForMap("select `id`,   `queue`,  `queue_count`,  `grp_queue`,   `rule`,  `rule_type` from game_runing_count where hid=?",hid);
		System.out.println(hid+"查询："+((System.currentTimeMillis()-start1))+"ms");
		start1 = System.currentTimeMillis();
		String[] rule = map.get("rule").toString().split(",");
		int start = Integer.parseInt(rule[0]);
		int end = Integer.parseInt(rule[1]);
		int rule_type = Integer.parseInt(map.get("rule_type").toString());
		
		int mod2 = turnService.getInputTurn().get().getMod2();
		
		CountQueueResult rets = CountService.countQueue(row,map.get("queue")==null?null:map.get("queue").toString(), map.get("queue_count")==null?null:map.get("queue_count").toString(),map.get("grp_queue").toString()
				, gong,gongCol,rule_type,start,end,mod2);
		System.out.println(hid+"计算："+((System.currentTimeMillis()-start1))+"ms");
		start1 = System.currentTimeMillis();
		ServiceManage.jdbcTemplate.update("UPDATE game_runing_count SET queue=?,queue_count=?,tg=CONCAT(tg,?),ys=ys+? WHERE id=?"
				,rets.getQueue(),rets.getQueueCount(),rets.getJg()!=null?rets.getJg()+",":"",rets.getYs(),map.get("id"));
		System.out.println(hid+"update："+((System.currentTimeMillis()-start1))+"ms");
		start1 = System.currentTimeMillis();
		
		if(row>=3&&nCrow!=null) {
			
			start1 = System.currentTimeMillis();
			doCountTurn(hid,rets.getQueue(), nCrow.getGong());
			System.out.println(hid+"doCountTurn："+((System.currentTimeMillis()-start1))+"ms");
			start1 = System.currentTimeMillis();
		}
		return rets.getQueue();
	}
	
	
	private CountTurn createCountTurnHandel(int mod,int ruleType,int start,int end,int mod2) {

		CountTurn countTurn = new CountTurn();
		countTurn.setMod2(mod2);
		countTurn.setStart(start);
		countTurn.setEnd(end);
		countTurn.setRuleType(ruleType);
		return countTurn;

	}
	
	
	private JgHandel createJgHandel(int row,int ruleType,int start,int end,int mod2) {
		JgHandel handel = new JgHandel();
		handel.setRow(row);
		handel.setRule_type(ruleType);
		handel.setStart(start);
		handel.setEnd(end);
		handel.setMod2(mod2);
		return handel;

	}
	
	/**
	 * 关闭游戏
	 * @param hid
	 */
	 public void finishGame(String uid,String hid) {
		
		 deleteGame(hid);
		
	}
	
	 
	 public void  insulateCleanGame(String hid,int frow) {
		if(frow<=5||frow>GAME_ROW+5)return;
		ServiceManage.jdbcTemplate.update("UPDATE game_runing SET sheng='',pei=null,gong=null,gong_col=null WHERE hid=? AND row=?"
				,hid,frow-5);

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
			 		"		 select a.tid,hid,b.uid,tbNum,focus_row,queue_count,tg,rule,ys,g_sum from game_runing_count a left join game_history b  on a.hid= b.id " + 
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
	 * 
	 * @param uid
	 * @return
	 */
	public int getGroup(String uid) {
		return crowDao.getGroup(uid);
	}
	
	private StringBuilder buildShengByRow(int row,Map<Integer, StringBuilder> map) {
		StringBuilder crow = map.get(row);
		if(crow==null) {
			crow = new StringBuilder("");
			map.put(row,crow);
		}
		
		return crow;

	}
	
	public Crow getNextRow(String hid,int frow) {
		if(frow>=GAME_ROW)return null;
		
		return crowDao.getRow(hid,frow+1);
		
	}
	
	public Crow getInputRow(String hid,int frow) {
		Crow crow = getRow(hid,frow);
		
		return crow;
	}
	
	
	public void updateCrow(Crow crow) {
		//String dui = crow.getDui();
		//String gong = crow.getGong();
		//String gongCol = crow.getGong_col();
		//if(dui!=null)crow.setDui(ZipUtils.gzip(dui));
		//if(gong!=null)crow.setGong(ZipUtils.gzip(gong));
		//if(gongCol!=null)crow.setGong_col(ZipUtils.gzip(gongCol));
		
		crowDao.update(crow);
		//if(dui!=null)crow.setDui(dui);
		//if(gong!=null)crow.setGong(gong);
		//if(gongCol!=null)crow.setGong_col(gongCol);

	}
	
	private Crow getRow(String hid,int frow) {
		Crow crow = crowDao.getRow(hid,frow);
		//if(crow.getDui()!=null)crow.setDui(ZipUtils.gunzip(crow.getDui()));
		//if(crow.getGong()!=null)crow.setGong(ZipUtils.gunzip(crow.getGong()));
		//if(crow.getGong_col()!=null)crow.setGong_col(ZipUtils.gunzip(crow.getGong_col()));
		return crow;
	}
	
	
	
	
	
	public Game getRuningGame(String uid) {
		Game game = crowDao.getRuningGame(uid);
		
		return game;

	}
	
	
	
	
	
	public static void main(String[] args) {
		for (int i = 161; i < 341; i++) {
			System.out.println((char)i+","+i);
		}
	}
	
public static class GameEach{
		
		
		public void exe(int groupIndex,int itemIndex,Integer queueVal,int newQueueVal
				,boolean gong,boolean gongCol,Boolean nextGong) {
			

		}
		
	}

	class JgHandel extends GameEach{
		int groupNum = GameCoreService2.SHENG_GROUP_NUM;
		int row;
		int rule_type;
		int mod2;
		int start,end;
		int lsJg = 0;
		int nvJg = 0;
		
		boolean isResult = false;
		@Override
		public void exe(int groupIndex, int itemIndex, Integer queueVal, int newQueueVal, boolean gong, boolean gongCol,Boolean nextGong) {
			if(row>3) {
				if((itemIndex+1)%2==0)
					nvJg+=CountService.getJg(rule_type,queueVal>CountService.QUEUE_VAL_OFFSET?queueVal-CountService.QUEUE_VAL_OFFSET:queueVal, start, end, (newQueueVal-CountService.QUEUE_VAL_OFFSET)*(queueVal-CountService.QUEUE_VAL_OFFSET)>0);
				else 
					lsJg+=CountService.getJg(rule_type,queueVal>CountService.QUEUE_VAL_OFFSET?queueVal-CountService.QUEUE_VAL_OFFSET:queueVal, start, end, (newQueueVal-CountService.QUEUE_VAL_OFFSET)*(queueVal-CountService.QUEUE_VAL_OFFSET)>0);
				
			}
		}
		
		
		
		public int getRow() {
			return row;
		}



		public void setRow(int row) {
			this.row = row;
		}



		public int getRule_type() {
			return rule_type;
		}



		public void setRule_type(int rule_type) {
			this.rule_type = rule_type;
		}



		public int getMod2() {
			return mod2;
		}



		public void setMod2(int mod2) {
			this.mod2 = mod2;
		}



		public int getStart() {
			return start;
		}
		
		public void setStart(int start) {
			this.start = start;
		}
		
		public int getEnd() {
			return end;
		}



		public void setEnd(int end) {
			this.end = end;
		}



		public String getResult() {
			if(isResult)throw new ApplicationException("重复获取");
			isResult = true;
			if(mod2==2) {
				lsJg=-lsJg;
				nvJg=-nvJg;
			}
			
			return row>=3?(lsJg+"_"+nvJg):null;

		}
		
	}
	
	class CountTurn extends GameEach{
		int groupNum = GameCoreService2.SHENG_GROUP_NUM;
		Integer[] info = new Integer[]{
				0,0,
				0,0,
				0,0,
				0,0,
		};
		
		int ruleType;
		int start,end;
		int mod2;
		
		//String bmap = "老+少+老-少-男+女+男-女-";
		@Override
		public void exe(int groupIndex, int itemIndex, Integer queueVal, int newQueueVal
				,boolean gong,boolean gongCol,Boolean nextGong) {
			
				int index = ((itemIndex+1)%2)==0?4:0;//确定老少/男女区域
				if(newQueueVal>CountService.QUEUE_VAL_OFFSET)index+=2;//确定 负正区域
				
				if(nextGong)index+=1;
				
				info[index]+=CountService.getJg(ruleType,newQueueVal>CountService.QUEUE_VAL_OFFSET?newQueueVal-CountService.QUEUE_VAL_OFFSET:newQueueVal,start,end,false);
		}
		
		
		private Object[] getResult() {
			Integer[] tgs = info;
			Object[] objs = CountService.countAllTgA(tgs,mod2);

			return objs;
		}
		
		public int getRuleType() {
			return ruleType;
		}
		public void setRuleType(int ruleType) {
			this.ruleType = ruleType;
		}
		public int getStart() {
			return start;
		}
		public void setStart(int start) {
			this.start = start;
		}
		public int getEnd() {
			return end;
		}
		public void setEnd(int end) {
			this.end = end;
		}


		public int getMod2() {
			return mod2;
		}


		public void setMod2(int mod2) {
			this.mod2 = mod2;
		}
		
		
		
	}
	
	
	
}
