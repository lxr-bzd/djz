package com.park.api.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxr.commons.exception.ApplicationException;
import com.park.api.ServiceManage;
import com.park.api.dao.CrowDao;
import com.park.api.entity.BigInputResult;
import com.park.api.entity.BigTurn;
import com.park.api.entity.Crow;
import com.park.api.entity.Game;
import com.park.api.entity.InputResult;
import com.park.api.entity.Turn;
import com.park.api.entity.YzDto;
import com.park.api.service.bean.GameConfig;
import com.park.api.utils.JsonUtils;

@Service
public class TurnService {
	@Autowired
	CrowDao crowDao;
	
	@Autowired
	GameService gameService;
	@Autowired
	SysService sysService;
	
	public ThreadLocal<Turn> turn = new ThreadLocal<>();
	public ThreadLocal<Turn> inputTurn = new ThreadLocal<>();
	
	public ThreadLocal<String> countTurnId = new ThreadLocal<>();
	public ThreadLocal<Map<String, Object>> turnCount = new ThreadLocal<>();
	
	public Map<String, Object> getMainModel(BigTurn bigTurn,Integer turnNo) {
		try {
			Map<String, Object> turn = ServiceManage.jdbcTemplate.queryForMap(
					"select * from game_turn where big_turn_id=? AND turn_no=? limit 1",bigTurn.getId(),turnNo);
			
			Map<String, Object> ret = new HashMap<>();
			
			//ret.put("lastRow", crowDao.getInputRow(game.getId()));
			List<Map<String, Object>> maps = ServiceManage.jdbcTemplate.queryForList(
					"select b.id,b.tid,b.hid,b.tg,b.tg_sum,b.rule,b.rule_type,b.ys,b.g,b.g_sum,a.uid from game_history a left join game_runing_count b on a.id=b.hid  where a.state=1 AND b.tid=?",turn.get("id"));
			ret.put("counts", maps);
			ret.put("turn", turn);
			ret.put("tip_open", ServiceManage.jdbcTemplate.queryForObject("select val from djt_sys where ckey = 'tip_open'",Integer.class));
			return ret;
			
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		
	}
	
	
	public BigInputResult doInputTurn(String pei,BigTurn bigTurn,int turnNo) {
		
		
		
		turnCount.set(null);
		countTurnId.set(null);
		inputTurn.set(null);
		
		Turn turn = getCurrentTurn(bigTurn,turnNo);
		if(turn==null)throw new ApplicationException("請刷新到下壹輪");
		
		inputTurn.set(turn);
		List<Game> games = crowDao.getGames(turn.getId());
		
		List<InputResult> results = new ArrayList<>();
		for (Game game : games) {
			InputResult result = gameService.doInput(game,pei);
			if(result.getRets()!=null)
			results.add(result);
		}
		
		
		ServiceManage.jdbcTemplate.update("UPDATE game_turn SET frow=frow+1 WHERE id=?",turn.getId());
		BigInputResult bigInputResult = new BigInputResult();
		bigInputResult.setResults(results);
		if(countTurnId.get()==null)return bigInputResult;
		
		
		Map<String, Object> map = ServiceManage.jdbcTemplate.queryForMap(
				"select id,rule,frow, `mod`,user_lock,qh,yz,yz_jg_sum,hb,hb_jg_sum,hbqh,hbqh_sum,xz,xz_jg_sum,xzbg_lock,xzbg_config,xzbg_trend,hbbg_lock,hbbg_config,hbbg_trend from game_turn where id=? limit 1",turn.getId());
		String[] uLock = map.get("user_lock").toString().split(",");
		
		String[] rule = map.get("rule").toString().split(",");
		int start = Integer.parseInt(rule[0]);
		int end = Integer.parseInt(rule[1]);
		int frow = Integer.parseInt( map.get("frow").toString())-1;
		
		
		List<Object> upQhList = StringUtils.isEmpty(map.get("qh").toString())?
				null:JSONArray.parseArray(map.get("qh").toString()).getJSONArray(1);
		
		
		Integer[] upQh = upQhList!=null?upQhList.toArray(new Integer[upQhList.size()]):null;
		
		Long[] upYz = JsonUtils.toLongArray(StringUtils.isNotBlank(map.get("yz").toString())?JSONArray.parseArray(map.get("yz").toString()).getString(1):null);
		
		Long[] upHb = JsonUtils.toLongArray(map.get("hb")!=null?map.get("hb").toString():null);
		
		Long[] upXz = JsonUtils.toLongArray(map.get("xz")!=null?map.get("xz").toString():null);
		
		
		Integer[] upHbqh = JsonUtils.toIntArray(map.get("hbqh")!=null?map.get("hbqh").toString():null);
		
		
		int mod = Integer.parseInt(map.get("mod").toString());
		Integer[] qh = new Integer[]{0,0,0,0};
		if(mod==1) {
			long[] allts = new long[]{0,0,0,0 ,0,0,0,0};
			//汇总原值
			long[] allYz = new long[] {0,0,0,0};
			long hzSum = 0;
			
			int queueCount = 0;
			for (InputResult result : results) {
				queueCount+=result.getQueueCount();
				
				if(uLock[Integer.valueOf(result.getUid())-1].equals("0"))
					continue;
				Object[] objs = result.getRets();
				long[] ts = (long[])objs[6];
				allts[0]+=ts[0];
				allts[1]+=ts[1];
				allts[2]+=ts[2];
				allts[3]+=ts[3];
				allts[4]+=ts[4];
				allts[5]+=ts[5];
				allts[6]+=ts[6];
				allts[7]+=ts[7];
				
				long[] yz = result.getYz();
				//汇总原值
				allYz[0]+=yz[0];
				allYz[1]+=yz[1];
				allYz[2]+=yz[2];
				allYz[3]+=yz[3];
				//求和计算
				
					if((long)objs[4]>0)qh[0]++;
					if((long)objs[4]<0)qh[1]++;
					
					if((long)objs[5]>0)qh[2]++;
					if((long)objs[5]<0)qh[3]++;
				hzSum+=result.getJg_sum();
			}
			
			int mod2 = getInputTurn().get().getMod2();
			Object[] hzBg = CountService.countAllTgA(allts,mod2);
			
			//计算求和报告
			Integer[] qhBg = CountService.countQh(qh,mod2);
			Integer[] qhJg = upQh==null?null:CountService.countQhJg(pei.substring(0, 1), upQh);
			
			//计算原值报告
			YzDto yzDto = CountService.reckonYz(allYz, upYz, Long.valueOf(map.get("yz_jg_sum").toString()), pei, mod2);
			
			
			//计算合并报告
			
			String[] newHbbgConfigAndTrend = CountService.reckonHbbgTrend(results, map.get("hbbg_trend").toString(), map.get("hbbg_config").toString(),map.get("xzbg_config").toString(),bigTurn.getBigTurnConfig());
			String newHbbg_config = newHbbgConfigAndTrend[0];
			String newHbbg_trend = newHbbgConfigAndTrend[2];
			long[] hbBg = CountService.reckonHbBg(results,map.get("hbbg_lock").toString(),newHbbg_trend,bigTurn.getBigTurnConfig());
			Long[] hbJg = upHb==null?null:CountService.reckonHbJg(pei.substring(0, 1), upHb);
			long hbJgSum = hbJg==null?Long.valueOf(map.get("hb_jg_sum").toString()):Long.valueOf(map.get("hb_jg_sum").toString())+hbJg[0]+hbJg[1];

			//计算合并求和
			Integer[] hbqhBg = CountService.countHbQh(results, turn.getConfig());
			Integer[] hbqhJg = upHbqh==null?null:CountService.countHbQhJg(pei.substring(0, 1), upHbqh);
			
			//计算选择报告
			String newXzbg_config = newHbbgConfigAndTrend[1];
			String[] newXzbgConfigAndTrend = CountService.reckonXzbgTrend(results, map.get("xzbg_trend").toString(), newXzbg_config,bigTurn.getBigTurnConfig());
			
			//String newXzbg_config = newXzbgConfigAndTrend[0];
			String newXzbg_trend = newXzbgConfigAndTrend[1];
			long[] xzBg = CountService.reckonXzBg(results,map.get("xzbg_lock").toString(),newXzbg_trend,bigTurn.getBigTurnConfig());
			Long[] xzJg = upXz==null?null:CountService.reckonXzJg(pei.substring(0, 1), upXz);
			long xzJgSum = xzJg==null?Long.valueOf(map.get("xz_jg_sum").toString()):Long.valueOf(map.get("xz_jg_sum").toString())+xzJg[0]+xzJg[1];

			//构建返回参数
			bigInputResult.setHzBg(new long[] {(long)hzBg[4],(long)hzBg[5]});
			bigInputResult.setHbBg(hbBg);
			bigInputResult.setXzBg(xzBg);
			ServiceManage.jdbcTemplate.update("update game_turn set info=?,lj=lj+?,jg_sum=?"
					+ ",qh=?,qh_sum=qh_sum+?,qh_jg=CONCAT(qh_jg,?),qh_last_jg=? "
					+ ",yz=?,yz_sum=yz_sum+?,yz_jg=CONCAT(yz_jg,?),yz_jg_sum=?,yz_last_jg=?"
					+ ",hb=?,hb_sum=hb_sum+?,hb_jg=CONCAT(hb_jg,?),hb_jg_sum=?,hb_last_jg=?,hbbg_trend=?,hbbg_config=?"
							+ ",xz=?,xz_sum=xz_sum+?,xz_jg=CONCAT(xz_jg,?),xz_jg_sum=?,xz_last_jg=?,xzbg_trend=?,xzbg_config=?"
					+ ",hbqh=?,hbqh_sum=hbqh_sum+?,hbqh_jg=CONCAT(hbqh_jg,?),hbqh_last_jg=? "
					+ ",queue_count = ?"
					+ " where id=?",
					JSONObject.toJSONString(hzBg),
					Math.abs((long)hzBg[4])+Math.abs((long)hzBg[5]),
					hzSum,
					
					JSONObject.toJSONString(new Object[] {qh,qhBg}),
					Math.abs(qhBg[0])+Math.abs(qhBg[1]),
					qhJg==null?"":(qhJg[0]+qhJg[1]+","),
					qhJg==null?"":qhJg[0]+"_"+qhJg[1],
							
					JSONObject.toJSONString(new Object[] {allYz,yzDto.getYzBg()}),
					Math.abs(yzDto.getYzBg()[0])+Math.abs(yzDto.getYzBg()[1]),
					yzDto.getYzJg()==null?"":(yzDto.getYzJg()[0]+yzDto.getYzJg()[1]+","),
					yzDto.getYzJgSum(),
					yzDto.getYzJg()==null?"":yzDto.getYzJg()[0]+"_"+yzDto.getYzJg()[1],
					
					JSONObject.toJSONString(hbBg),
					Math.abs(hbBg[0])+Math.abs(hbBg[1]),
					hbJg==null?"":(hbJg[0]+hbJg[1]+","),
					hbJgSum,
					hbJg==null?"":hbJg[0]+"_"+hbJg[1],
					newHbbg_trend,newHbbg_config,
							
							JSONObject.toJSONString(xzBg),
							Math.abs(xzBg[0])+Math.abs(xzBg[1]),
							xzJg==null?"":(xzJg[0]+xzJg[1]+","),
							xzJgSum,
							xzJg==null?"":xzJg[0]+"_"+xzJg[1],
									newXzbg_trend,newXzbg_config,
							
					JSONObject.toJSONString(hbqhBg),
					Math.abs(hbqhBg[0])+Math.abs(hbqhBg[1]),
					hbqhJg==null?"":(hbqhJg[0]+hbqhJg[1]+","),
					hbqhJg==null?"":hbqhJg[0]+"_"+hbqhJg[1],
							
					queueCount,
					countTurnId.get());
		
		}else {//B模式
			
		}
		
		
		getTurn().set(null);
		
		
		
		return bigInputResult;
	}
	
	
	public void doFinishTurn(BigTurn bigTurn,Integer turnNo) {
		Turn turn = getCurrentTurn(bigTurn, turnNo);
		List<String> games = ServiceManage.jdbcTemplate.queryForList("select id from game_history where state=1 AND tid=?", String.class,turn.getId());
		for (int i = 0; i < games.size(); i++) {
			gameService.finishGame(null, games.get(i));
		}
		
		ServiceManage.jdbcTemplate.update("UPDATE game_turn SET state=2 WHERE id =?",turn.getId());
		

	}
	
	public void doRenewTurn(BigTurn bigTurn,Integer turnNo) {
		
		Turn turn = createTurn(bigTurn, turnNo);
		
		List<String> uids = getUids();
		for (String uid : uids) {
			gameService.doNewly(turn,uid);
		}
		//getTurn().set(null);
	 }
	
	
	public Turn createTurn(final BigTurn bigTurn,final Integer turnNo) {
		
		final Turn turn = new Turn();
		Integer rule_type = ServiceManage.jdbcTemplate.queryForObject("select val from djt_sys where ckey='use_rule'", Integer.class);
		String rule = null;
		if(rule_type==1)
			rule = ServiceManage.jdbcTemplate.queryForObject("select val from djt_sys where ckey='rule'",String.class);
		else 
			rule = ServiceManage.jdbcTemplate.queryForObject("select val from djt_sys where ckey='rule2'", String.class);
		Integer mod2 =  ServiceManage.jdbcTemplate.queryForObject("select val from djt_sys where ckey='mod2'", Integer.class);
		
		
		turn.setMod2(mod2);
		turn.setMod(1);
		turn.setRule_type(rule_type);
		turn.setRule(rule);
		GameConfig config = new GameConfig();
		config.setGameGroupNum(sysService.getSysConfig("gameGroupNum", Integer.class));
		config.setGameRowNum(sysService.getSysConfig("gameRowNum", Integer.class));
		config.setGameGroupLen(10);
		config.setIsTemporary(1);
		config.setHbBg(sysService.getSysConfig("hbBg", Integer.class));
		config.setHbQh(sysService.getSysConfig("hbQh", Integer.class));
		turn.setConfig(config);
		turn.setConfig_json(JSONObject.toJSONString(config));
		Integer conf_len = sysService.getSysConfig("conf_len", Integer.class);
		
		String s1 = "";
		String s2 = "";
		for (int i = 0; i < 10; i++) {
			
			s1+=(conf_len+"-1")+(i==9?"":",");
			s2+=("1-"+conf_len)+(i==9?"":",");
		}
		final String xzbgConfig = s1;
		final String hbbgConfig = s2;
		
		
		    KeyHolder keyHolder = new GeneratedKeyHolder();
		    ServiceManage.jdbcTemplate.update(
		            new PreparedStatementCreator() {
		                public PreparedStatement createPreparedStatement(Connection con) throws SQLException
		                {
		                	PreparedStatement ps = con.prepareStatement("INSERT INTO game_turn (big_turn_id,turn_no,`mod`,`mod2`,frow,info,lj  ,qh,qh_sum,qh_jg,qh_last_jg,  yz_jg, hb_jg,hbqh_jg,`rule`,rule_type,user_lock,state,config_json"
		                			+ ",xz_jg,xzbg_lock,xzbg_config,xzbg_trend,hbbg_lock,hbbg_config,hbbg_trend)"
		                			+ " VALUES (?,?,?,?,1,'',0  ,'',0,'',''   ,'','',''  ,?,?,'1,1,1,1,1,1,1,1,1,1',1,?,"
		                			+ "'','1,1,1,1,1,1,1,1,1,1',?,'0,0,0,0,0,0,0,0,0,0','1,1,1,1,1,1,1,1,1,1',?,'0,0,0,0,0,0,0,0,0,0')",Statement.RETURN_GENERATED_KEYS); 
		                	
		                	ps.setInt(1, bigTurn.getId());
		                	ps.setInt(2, turnNo);
		                	
		                	
		                	ps.setInt(3, turn.getMod());
		                	ps.setInt(4, turn.getMod2());
		                	ps.setString(5, turn.getRule());
		                    ps.setInt(6, turn.getRule_type());
		                    ps.setString(7, turn.getConfig_json());
		                    ps.setString(8, xzbgConfig);
		                    ps.setString(9, hbbgConfig);
		                	return ps;
		                }
		            }, keyHolder);
		    turn.setId(keyHolder.getKey().intValue()+"");
		    return turn;

	}
		

	public List<String> getUids() {
		return ServiceManage.jdbcTemplate.queryForList("select djt_u_id from djt_user WHERE djt_islock=1", String.class);
		
	}
	

	private Turn getCurrentTurn(BigTurn bigTurn,int turnNo) {
		
		try {
			return ServiceManage.jdbcTemplate.queryForObject("select * from game_turn where big_turn_id=? AND turn_no=? AND state=1 limit 1", 
					new BeanPropertyRowMapper<Turn>(Turn.class),bigTurn.getId(),turnNo);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		

	}
	

	public ThreadLocal<Turn> getTurn() {
		return turn;
	}

	public void setTurn(ThreadLocal<Turn> turn) {
		this.turn = turn;
	}

	public ThreadLocal<String> getCountTurnId() {
		return countTurnId;
	}

	public void setCountTurnId(ThreadLocal<String> countTurnId) {
		this.countTurnId = countTurnId;
	}


	public ThreadLocal<Map<String, Object>> getTurnCount() {
		return turnCount;
	}


	public void setTurnCount(ThreadLocal<Map<String, Object>> turnCount) {
		this.turnCount = turnCount;
	}


	public ThreadLocal<Turn> getInputTurn() {
		return inputTurn;
	}


	public void setInputTurn(ThreadLocal<Turn> inputTurn) {
		this.inputTurn = inputTurn;
	}

	
	public Turn getInputTurn2() {
		return inputTurn.get();
	}

}
