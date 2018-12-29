package com.park.api.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.park.api.ServiceManage;
import com.park.api.entity.Game;
import com.park.api.entity.Turn;

@Service
public class TurnService {
	
	@Autowired
	GameService gameService;
	
	public ThreadLocal<Turn> turn = new ThreadLocal<>();
	public ThreadLocal<Turn> inputTurn = new ThreadLocal<>();
	
	public ThreadLocal<String> countTurnId = new ThreadLocal<>();
	public ThreadLocal<Map<String, Object>> turnCount = new ThreadLocal<>();
	
	public Map<String, Object> getMainModel(String uid) {
		try {
			Map<String, Object> turn = ServiceManage.jdbcTemplate.queryForMap(
					"select * from game_turn where state=1 limit 1");
			
			Map<String, Object> ret = new HashMap<>();
			
			//ret.put("lastRow", crowDao.getInputRow(game.getId()));
			List<Map<String, Object>> maps = ServiceManage.jdbcTemplate.queryForList(
					"select b.id,b.tid,b.hid,b.tg,b.rule,b.rule_type,b.ys,b.g,b.g_sum,a.uid from game_history a left join game_runing_count b on a.id=b.hid  where a.state=1 AND b.tid=?",turn.get("id"));
			ret.put("counts", maps);
			ret.put("turn", turn);
			return ret;
			
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		
	}
	
	
	public void doInputTurn(String pei) {
		turnCount.set(null);
		countTurnId.set(null);
		inputTurn.set(null);
		
		Turn turn = getCurrentTurn();
		if(turn==null)doRenewTurn();
		turn = getCurrentTurn();
		inputTurn.set(turn);
		
		List<String> uids = getUids();
		for (int i = 0; i < uids.size(); i++) {
			gameService.doInput(pei, uids.get(i));
		}
		
		ServiceManage.jdbcTemplate.update("UPDATE game_turn SET frow=frow+1 WHERE id=?",turn.getId());
		
		if(countTurnId.get()==null)return;
		
		
		Map<String, Object> map = ServiceManage.jdbcTemplate.queryForMap(
				"select id, `mod`,user_lock,qh from game_turn where state=1 limit 1");
		String[] uLock = map.get("user_lock").toString().split(",");
		
		List<Object> upQhList = StringUtils.isEmpty(map.get("qh").toString())?
				null:JSONArray.parseArray(map.get("qh").toString()).getJSONArray(1);
		Integer[] upQh = upQhList!=null?upQhList.toArray(new Integer[upQhList.size()]):null;
		int mod = Integer.parseInt(map.get("mod").toString());
		Integer[] qh = new Integer[]{0,0,0,0};
		if(mod==1) {
			Integer[] allts = new Integer[]{0,0,0,0 ,0,0,0,0};
			
			for (Entry<String, Object> entry : turnCount.get().entrySet()) {
				if(uLock[Integer.valueOf(entry.getKey())-1].equals("0"))
					continue;
				Object[] objs = (Object[])entry.getValue();
				Integer[] ts = (Integer[])objs[6];
				allts[0]+=ts[0];
				allts[1]+=ts[1];
				allts[2]+=ts[2];
				allts[3]+=ts[3];
				allts[4]+=ts[4];
				allts[5]+=ts[5];
				allts[6]+=ts[6];
				allts[7]+=ts[7];
				//求和计算
				
					if((Integer)objs[4]>0)qh[0]++;
					if((Integer)objs[4]<0)qh[1]++;
					
					if((Integer)objs[5]>0)qh[2]++;
					if((Integer)objs[5]<0)qh[3]++;
				
				
			}
			int mod2 = getInputTurn().get().getMod2();
			Object[] objs = CountService.countAllTgA(allts,mod2);
			Integer[] qhBg = CountService.countQh(qh,mod2);
			Integer[] qhJg = upQh==null?null:CountService.countQhJg(pei.substring(0, 1), upQh);
			
			
			ServiceManage.jdbcTemplate.update("update game_turn set info=?,lj=lj+?"
					+ ",qh=?,qh_sum=qh_sum+?,qh_jg=CONCAT(qh_jg,?),qh_last_jg=? where id=?",
					JSONObject.toJSONString(objs),
					Math.abs((Integer)objs[4])+Math.abs((Integer)objs[5]),
					
					JSONObject.toJSONString(new Object[] {qh,qhBg}),
					Math.abs(qhBg[0])+Math.abs(qhBg[1]),
					qhJg==null?"":(qhJg[0]+qhJg[1]+","),
					qhJg==null?"":qhJg[0]+"_"+qhJg[1],
					countTurnId.get());
			
		}else {//B模式
			
		}
		

		getTurn().set(null);
	}
	
	public void doRenewTurn() {
		List<String> uids = getUids();
		List<String> games = ServiceManage.jdbcTemplate.queryForList("select id from game_history where state=1", String.class);
		for (int i = 0; i < games.size(); i++) {
			gameService.finishGame(null, games.get(i));
		}
		for (String uid : uids) {
			gameService.doNewly(uid);
		}
		getTurn().set(null);
	 }
	
	
	public Turn createTurn() {
		
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
		
		
		    KeyHolder keyHolder = new GeneratedKeyHolder();
		    ServiceManage.jdbcTemplate.update(
		            new PreparedStatementCreator() {
		                public PreparedStatement createPreparedStatement(Connection con) throws SQLException
		                {
		                	PreparedStatement ps = con.prepareStatement("INSERT INTO game_turn (`mod`,`mod2`,frow,info,lj  ,qh,qh_sum,qh_jg,qh_last_jg  ,`rule`,rule_type,user_lock,state)"
		                			+ " VALUES (?,?,1,'',0  ,'',0,'',''  ,?,?,'1,1,1,1,1,1,1,1,1,1',1)",Statement.RETURN_GENERATED_KEYS); 
		                	ps.setInt(1, turn.getMod());
		                	ps.setInt(2, turn.getMod2());
		                	ps.setString(3, turn.getRule());
		                    ps.setInt(4, turn.getRule_type());
		                	return ps;
		                }
		            }, keyHolder);
		    turn.setId(keyHolder.getKey().intValue()+"");
		    return turn;

	}
		

	public List<String> getUids() {
		return ServiceManage.jdbcTemplate.queryForList("select djt_u_id from djt_user WHERE djt_islock=1", String.class);
		
	}
	

	private Turn getCurrentTurn() {

		//ServiceManage.jdbcTemplate.queryFor
		return ServiceManage.jdbcTemplate.queryForObject("select * from game_turn where state=1 limit 1", 
				new BeanPropertyRowMapper<Turn>(Turn.class));

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

	
	

}
