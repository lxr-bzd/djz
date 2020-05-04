package com.park.api.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.park.api.entity.TurnGroup;
import com.park.api.utils.ArrayUtils;
import com.park.api.utils.JsonUtils;
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

		String[] inverseLockArr = bigTurn.getInverse_lock().split(",");
		 
		Long[][] newbBg = handelBg(results);

		 newbBg = inverseNewBg(newbBg,inverseLockArr);
		List<Long> gjList = JSONArray.parseArray(bigTurn.getGj(), Long.class);
		Long[] newGj = handelGj(gjList.toArray(new Long[gjList.size()]),newbBg);


		if(bigTurn.getFrow()<3){

			ServiceManage.jdbcTemplate.update("UPDATE `game_big_turn` SET `frow` = ?, `bg` = ?,  `gj` = ?  "+
							" WHERE `id` = ?"
					,bigTurn.getFrow()+1,
					JSONObject.toJSONString(newbBg),
					JSONObject.toJSONString(newGj),

					bigTurn.getId()
			);

		}else{

			//小板汇总
			List<TurnGroup> turnGroups = TurnGroupCountService.countGroup(bigTurn.getId(),results,bigTurn.getXb_lock(),bigTurn.getXb_inv_lock());
			TurnGroupCountService.updateCountGroup(turnGroups);

			String[] bkbgInvLocks = bigTurn.getBkbg_inv_lock().split(",");
			//板块报告
			Integer[] upBkbg = JsonUtils.toIntArray(bigTurn.getBkbg());
			Integer[] bkbg = CountCoreAlgorithm.inverseBg(BigCoreService.countBkbg(turnGroups)
                    ,bkbgInvLocks[0]);
			Integer[] bkbgJg = upBkbg==null?null:BigCoreService.countJg(pei.substring(0, 1),upBkbg);
			Long bkbg_jg_sum = bkbgJg==null?0:Long.valueOf(bkbgJg[0]+bkbgJg[1]);
			Integer bkbgJgType = upBkbg==null?null:BigCoreService.countJgDetail(bkbgJg,upBkbg);
			Integer newBkbgTrend = CountCoreAlgorithm.reckonTrend(bkbg_jg_sum,new Integer(bigTurn.getBkbg_trend()));
			//板块终端
			Double[] upBkzd = JsonUtils.toIntDouble(bigTurn.getBkzd());
			BigDecimal[] bkzd = CountCoreAlgorithm.inverseBg(BigCoreService.countBkzd(bkbg,bigTurn.getBigTurnConfig().getRule_bkbgs(),bigTurn.getBkzd_lock(),newBkbgTrend+"")
                    ,bkbgInvLocks[1]);
			Double[] bkzdJg = upBkzd==null?null:BigCoreService.countJg(pei.substring(0, 1),upBkzd);
			BigDecimal bkzd_jg_sum = bkzdJg==null?BigDecimal.ZERO:new BigDecimal(bkzdJg[0].toString()).add(new BigDecimal(bkzdJg[1].toString()));
			Integer bkzdJgType = bkzdJg==null?null:BigCoreService.countJgDetail(bkzdJg,upBkzd);


			//计算连续rule_A
			int[] oldTgTrends = ArrayUtils.str2int(bigTurn.getTg_trends().split(","));
			int[] newTgTrends = BigCoreService.reckonTgTrends(results,oldTgTrends);
			//AB报告
			long[][] bgAB = BigCoreService.reckonBgAB(newbBg,newTgTrends,bigTurn.getBigTurnConfig());
			bgAB = inverseBgAB(bgAB,inverseLockArr);
			long[] bgA = bgAB[0];
			long[] bgB = bgAB[1];

			//求和终端报告
			List<Object> upZdList = StringUtils.isEmpty(bigTurn.getZd())?null:JSONArray.parseArray(bigTurn.getZd());
			Integer[] upZdBg = bigTurn.getZd()!=null?upZdList.toArray(new Integer[upZdList.size()]):null;
			Integer[] zdBg = BigCoreService.countZdBg(newbBg,bigTurn.getZd_lock(),0,19,bgAB);
				zdBg = inverseQhzd(zdBg,inverseLockArr);
			Integer[] zdJg = upZdBg==null?null:BigCoreService.countJg(pei.substring(0, 1),upZdBg);
			Long zd_jg_sum = zdJg==null?0:Long.valueOf(zdJg[0]+zdJg[1]);


			//A报告
			List<Object> upBgaList = StringUtils.isEmpty(bigTurn.getBga())?null:JSONArray.parseArray(bigTurn.getBga());
			Integer[] upBgaBg = StringUtils.isNotEmpty(bigTurn.getBga())?upBgaList.toArray(new Integer[upBgaList.size()]):null;
			Integer[] bgaJg = upBgaBg==null?null:BigCoreService.countJg(pei.substring(0, 1),upBgaBg);
			Long bga_jg_sum = bgaJg==null?0:Long.valueOf(bgaJg[0]+bgaJg[1]);


			//B报告
			List<Object> upBgbList = StringUtils.isEmpty(bigTurn.getBgb())?null:JSONArray.parseArray(bigTurn.getBgb());
			Integer[] upBgbBg = StringUtils.isNotEmpty(bigTurn.getBgb())?upBgbList.toArray(new Integer[upBgbList.size()]):null;
			Integer[] bgbJg = upBgaBg==null?null:BigCoreService.countJg(pei.substring(0, 1),upBgbBg);
			Long bgb_jg_sum = bgbJg==null?0:Long.valueOf(bgbJg[0]+bgbJg[1]);


			Long[][] jgzdInputBg = new Long[][]{ArrayUtils.int2Long(zdBg),newbBg[17],newbBg[18],ArrayUtils.toObject(bgA),ArrayUtils.toObject(bgB)};

			//结果终端报告
			List<Long> upJgzdList = StringUtils.isEmpty(bigTurn.getJgzd())?null:JSONArray.parseArray(bigTurn.getJgzd(),Long.class);
			Long[] upJgzdBg = StringUtils.isNotEmpty(bigTurn.getJgzd())?upJgzdList.toArray(new Long[upJgzdList.size()]):null;
			Long[] jgzdBg = BigCoreService.countJgzdBg(jgzdInputBg,bigTurn.getJgzd_lock());
			Long[] jgzdJg = upJgzdBg==null?null:BigCoreService.countJg(pei.substring(0, 1),upJgzdBg);
			Long jgzd_jg_sum = jgzdJg==null?0:Long.valueOf(jgzdJg[0]+jgzdJg[1]);


			ServiceManage.jdbcTemplate.update("UPDATE `game_big_turn` SET `frow` = ?, `bg` = ?,  `gj` = ?,  "+
							" tg_trends=?,bkbg_trend=?,"+
							" zd=?, zd_sum=zd_sum+?, zd_jg=CONCAT(zd_jg,?), zd_jg_sum = zd_jg_sum+?," +
							" bga=?, bga_sum=bga_sum+?, bga_jg=CONCAT(bga_jg,?), bga_jg_sum = bga_jg_sum+?," +
							" bgb=?, bgb_sum=bgb_sum+?, bgb_jg=CONCAT(bgb_jg,?), bgb_jg_sum = bgb_jg_sum+?, " +
							" jgzd=?, jgzd_sum=jgzd_sum+?, jgzd_jg=CONCAT(jgzd_jg,?), jgzd_jg_sum = jgzd_jg_sum+?, " +
							" bkbg=?, bkbg_sum=bkbg_sum+?, bkbg_jg=CONCAT(bkbg_jg,?), bkbg_jg_sum = bkbg_jg_sum+?, " +
							" bkzd=?, bkzd_sum=bkzd_sum+?, bkzd_jg=CONCAT(bkzd_jg,?), bkzd_jg_sum = bkzd_jg_sum+? " +
							" WHERE `id` = ?"
					,bigTurn.getFrow()+1,
					JSONObject.toJSONString(newbBg),
					JSONObject.toJSONString(newGj),

					StringUtils.join(ArrayUtils.toObject(newTgTrends),","),newBkbgTrend,

					JSONArray.toJSONString(zdBg),
					Math.abs(zdBg[0])+Math.abs(zdBg[1]),
					zdJg==null?"":(zdJg[0]+zdJg[1]+","),
					zd_jg_sum,

					JSONArray.toJSONString(bgA),
					Math.abs(bgA[0])+Math.abs(bgA[1]),
					bgaJg==null?"":(bgaJg[0]+bgaJg[1]+","),
					bga_jg_sum,

					JSONArray.toJSONString(bgB),
					Math.abs(bgB[0])+Math.abs(bgB[1]),
					bgbJg==null?"":(bgbJg[0]+bgbJg[1]+","),
					bgb_jg_sum,



					JSONArray.toJSONString(jgzdBg),
					Math.abs(jgzdBg[0])+Math.abs(jgzdBg[1]),
					jgzdJg==null?"":(jgzdJg[0]+jgzdJg[1]+","),
					jgzd_jg_sum,

					JSONArray.toJSONString(bkbg),
					Math.abs(bkbg[0])+Math.abs(bkbg[1]),
					bkbgJg==null?"":bkbgJgType+"_"+(bkbgJg[0]+bkbgJg[1]+","),
					bkbg_jg_sum,

					JSONArray.toJSONString(bkzd),
					bkzd[0].abs().add(bkzd[1].abs()),
					bkzdJg==null?"":(bkzdJgType+"_"+new BigDecimal(bkzdJg[0].toString()).add(new BigDecimal(bkzdJg[1].toString()))+","),
					bkzd_jg_sum,

					bigTurn.getId()
			);
		}

	 }



	public long[][] inverseBgAB(long[][] bgAB,String[] lockArr){

		for (int i = 0; i < 2; i++) {
			int li = i+10;
			if(lockArr[li].equals("1")){
				bgAB[i][0] = -bgAB[i][0];
				bgAB[i][1] = -bgAB[i][1];
			}


		}
		return bgAB;
	}

	public Integer[] inverseQhzd(Integer[] qhzd,String[] lockArr){

		if(lockArr[7].equals("1")){
			qhzd[0] = -qhzd[0];
			qhzd[1] = -qhzd[1];
		}
	 	return qhzd;
	}
	public Long[][] inverseNewBg(Long[][] newbBg,String[] lockArr){

		for (int i = 10; i < 19; i++) {
			int li = i-10;
			if(i>16)li+=1;
			if(lockArr[li].equals("1")){
				newbBg[i][0] = -newbBg[i][0];
				newbBg[i][1] = -newbBg[i][1];
			}


		}


		return newbBg;
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
		TurnGroupCountService.initData(bigTurn);
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
		
		config.setRule3(sysService.getSysConfig("rule3", String.class));
		
		final BigTurn bigTurn = new BigTurn();
		bigTurn.setFrow(1);
		bigTurn.setGj("[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]");
		bigTurn.setConfig_json(JSONObject.toJSONString(config));
		bigTurn.setBigTurnConfig(config);
		bigTurn.setZd_sum(0l);
		bigTurn.setZd_jg("");
		bigTurn.setZd_jg_sum(0l);
		bigTurn.setZd_lock("1,1,1,1,1"+",1,1,1,1,1"+",1,0,0,0,0"+",0,0,1,1,1,1");
		bigTurn.setTg_trends("0,0,0,0,0,0,0,0,0,0");

		bigTurn.setJgzd_lock("1,1,1,1,1");
		bigTurn.setInverse_lock("0,0,0,0,0,"+"0,0,0,0,0,"+"0,0");
		bigTurn.setXb_inv_lock("0,0,0,0,0,"+"0,0,0,0,0"+",0");
		bigTurn.setXb_lock("1,1,1,1,1,"+"1,1,1,1,1"+",1");
		bigTurn.setBkzd_lock("1,1,1,1,1");
		bigTurn.setBkbg_inv_lock("0,0");
		KeyHolder keyHolder = new GeneratedKeyHolder();


	    ServiceManage.jdbcTemplate.update(
	            new PreparedStatementCreator() {
	                public PreparedStatement createPreparedStatement(Connection con) throws SQLException
	                {
	                	PreparedStatement ps = con.prepareStatement("INSERT INTO `game_big_turn`( `frow`, `bg`, `gj`,`config_json`, `state`,   zd_sum,zd_jg,zd_jg_sum,zd_lock,tg_trends," +
										"bga_jg,bgb_jg,jgzd_jg,jgzd_lock,inverse_lock,xb_inv_lock,xb_lock,bkbg_jg,bkzd_jg,bkzd_lock,bkbg_inv_lock) "
	                			+ "VALUES ( ?, '', ?,?, 1,  ?,?,?,?, ?" +
										",'','','',?,?,?,?,'','',?,?)"
	                			,Statement.RETURN_GENERATED_KEYS); 
	                	
	                	ps.setInt(1, bigTurn.getFrow());
	                	ps.setString(2, bigTurn.getGj());
	                	ps.setString(3, bigTurn.getConfig_json());

						ps.setLong(4, bigTurn.getZd_sum());
						ps.setString(5, bigTurn.getZd_jg());
						ps.setLong(6, bigTurn.getZd_jg_sum());
						ps.setString(7, bigTurn.getZd_lock());

						ps.setString(8, bigTurn.getTg_trends());
						ps.setString(9, bigTurn.getJgzd_lock());
						ps.setString(10, bigTurn.getInverse_lock());
						ps.setString(11, bigTurn.getXb_inv_lock());
						ps.setString(12, bigTurn.getXb_lock());
						ps.setString(13, bigTurn.getBkzd_lock());
						ps.setString(14, bigTurn.getBkbg_inv_lock());

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
			config.setRule_A(sysService.getSysConfig("rule_A", String.class));
			config.setRule_B(sysService.getSysConfig("rule_B", String.class));
			config.setRule_jg_A(sysService.getSysConfig("rule_jg_A", String.class));
			config.setRule_jg_B(sysService.getSysConfig("rule_jg_B", String.class));
			config.setRule_bkbgs(new String[]{
					sysService.getSysConfig("rule_bkbg1", String.class),
					sysService.getSysConfig("rule_bkbg2", String.class),
					sysService.getSysConfig("rule_bkbg3", String.class),
					sysService.getSysConfig("rule_bkbg4", String.class),
					sysService.getSysConfig("rule_bkbg5", String.class),
			});
			return bigTurn;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	
	private Long[][] handelBg( List<BigInputResult> results) {
		Long[][] ret= new Long[][] {new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L}
		,new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L}
		,new Long[] {0L,0L},new Long[] {0L,0L},new Long[] {0L,0L}
		,new Long[] {0L,0L}
		,new Long[] {0L,0L},new Long[] {0L,0L}};
		
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
			i++;
			if(bigInputResult.getJgbgBg()!=null) {
				ret[i][0]+=(long)bigInputResult.getJgbgBg()[0];
				ret[i][1]+=(long)bigInputResult.getJgbgBg()[1];
				
			}

			i++;
			if(bigInputResult.getJgABg()!=null) {
				ret[i][0]+=(long)bigInputResult.getJgABg()[0];
				ret[i][1]+=(long)bigInputResult.getJgABg()[1];

			}
			i++;
			if(bigInputResult.getJgBBg()!=null) {
				ret[i][0]+=(long)bigInputResult.getJgBBg()[0];
				ret[i][1]+=(long)bigInputResult.getJgBBg()[1];

			}
		}
		
		return ret;

	}
	
	private Long[] handelGj(Long[] oldGj,Long[][] newbBg) {
		Long[] gj = new Long[newbBg.length];
		
		for (int i = 0; i < gj.length; i++) {
			gj[i] = oldGj[i]+Math.abs(newbBg[i][0])+Math.abs(newbBg[i][1]);
		}
		
		return gj;
		
	}

}
