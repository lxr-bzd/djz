package com.park.api.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.park.api.entity.*;
import com.park.api.utils.ArrayUtils;
import com.park.api.utils.DoubleUtils;
import com.park.api.utils.JsonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxr.commons.exception.ApplicationException;
import com.park.api.ServiceManage;
import com.park.api.service.bean.BigTurnConfig;

import javax.json.JsonObject;

@Service
public class BigTurnService {

	@Autowired
	TurnService turnService;

	@Autowired
	TurnGroupService turnGroupService;

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
			//List<TurnGroup> turnGroups = TurnGroupCountService.countGroup(bigTurn.getId(),results,bigTurn.getXb_lock(),bigTurn.getXb_inv_lock());
			//TurnGroupCountService.updateCountGroup(turnGroups);

			String[] bkbgInvLocks = bigTurn.getBkbg_inv_lock().split(",");
			//板块报告
            /*Double[] upBkbg = JsonUtils.toDoubleArray(bigTurn.getBkbg());
			BigDecimal[] bkbg = CountCoreAlgorithm.inverseBg(BigCoreService.countBkbg(results),bkbgInvLocks[1]);
            Double[] bkbgJg = upBkbg==null?null:CountCoreAlgorithm.computeJg(pei.substring(0, 1),upBkbg);
			Long bkbg_jg_sum = bkbgJg==null?0: DoubleUtils.add(bkbgJg[0],bkbgJg[1]).longValue();
			Integer bkbgJgType = upBkbg==null?null:BigCoreService.countJgDetail(bkbgJg);
			Integer newBkbgTrend = CountCoreAlgorithm.computeTrend(bkbg_jg_sum,new Integer(bigTurn.getBkbg_trend()));*/

			//板塊報告求和
			Long[] upBkzd = JsonUtils.toLongArray(bigTurn.getBkzd());
			long[] bkzd =  CountCoreAlgorithm.inverseBg(BigCoreService.countBkzd2(results,bigTurn.getBigTurnConfig().getRule_bkbgs()),bkbgInvLocks[0]);
            Long[] bkzdJg = upBkzd==null?null:CountCoreAlgorithm.computeJg(pei.substring(0, 1),upBkzd);
			long bkzd_jg_sum = bkzdJg==null?0:bkzdJg[0]+bkzdJg[1];
			Integer bkzdJgType = bkzdJg==null?null:BigCoreService.countJgDetail(bkzdJg);


            String[] bkhzInvLocks = bigTurn.getBkhz_inv_lock().split(",");
			//板块汇总
			Long[] upBkhz = JsonUtils.toLongArray(bigTurn.getBkhz());
			BigDecimal[] bkhz = CountCoreAlgorithm.inverseBg(BigCoreService.countBkhz(results,bigTurn.getBkhz_lock(),bigTurn.getBigTurnConfig().getRuleBkhzs())
                ,bkhzInvLocks[1]);
			Long[] bkhzJg = upBkhz==null?null:BigCoreService.countJg(pei.substring(0, 1),upBkhz);
			Long bkhz_jg_sum = bkhzJg==null?0:Long.valueOf(bkhzJg[0]+bkhzJg[1]);
			Integer bkhzJgType = upBkhz==null?null:BigCoreService.countJgDetail(bkhzJg);


			//板塊匯總求和
			Long[] upBkqh = JsonUtils.toLongArray(bigTurn.getBkqh());
			BigDecimal[] bkqh = CountCoreAlgorithm.inverseBg(BigCoreService.countBkqh(results,bigTurn.getBkhz_lock(),bigTurn.getBigTurnConfig().getRuleBkhzs())
                    ,bkhzInvLocks[0]);
			Long[] bkqhJg = upBkqh==null?null:BigCoreService.countJg(pei.substring(0, 1),upBkqh);
			Long bkqh_jg_sum = bkhzJg==null?0:Long.valueOf(bkqhJg[0]+bkqhJg[1]);
			Integer bkqhJgType = upBkqh==null?null:BigCoreService.countJgDetail(bkqhJg);


			//计算连续rule_A
			int[] oldTgTrends = ArrayUtils.str2int(bigTurn.getTg_trends().split(","));
			int[] newTgTrends = BigCoreService.reckonTgTrends(results,oldTgTrends);
			//AB报告
			/*long[][] bgAB = BigCoreService.reckonBgAB(newbBg,newTgTrends,bigTurn.getBigTurnConfig());
			bgAB = inverseBgAB(bgAB,inverseLockArr);
			long[] bgA = bgAB[0];
			long[] bgB = bgAB[1];*/

			//求和终端报告
			/*List<Object> upZdList = StringUtils.isEmpty(bigTurn.getZd())?null:JSONArray.parseArray(bigTurn.getZd());
			Integer[] upZdBg = bigTurn.getZd()!=null?upZdList.toArray(new Integer[upZdList.size()]):null;
			Integer[] zdBg = BigCoreService.countZdBg(newbBg,bigTurn.getZd_lock(),0,19,bgAB);
				zdBg = inverseQhzd(zdBg,inverseLockArr);
			Integer[] zdJg = upZdBg==null?null:BigCoreService.countJg(pei.substring(0, 1),upZdBg);
			Long zd_jg_sum = zdJg==null?0:Long.valueOf(zdJg[0]+zdJg[1]);
*/

			//A报告
			/*List<Object> upBgaList = StringUtils.isEmpty(bigTurn.getBga())?null:JSONArray.parseArray(bigTurn.getBga());
			Integer[] upBgaBg = StringUtils.isNotEmpty(bigTurn.getBga())?upBgaList.toArray(new Integer[upBgaList.size()]):null;
			Integer[] bgaJg = upBgaBg==null?null:BigCoreService.countJg(pei.substring(0, 1),upBgaBg);
			Long bga_jg_sum = bgaJg==null?0:Long.valueOf(bgaJg[0]+bgaJg[1]);*/


			//B报告
			/*List<Object> upBgbList = StringUtils.isEmpty(bigTurn.getBgb())?null:JSONArray.parseArray(bigTurn.getBgb());
			Integer[] upBgbBg = StringUtils.isNotEmpty(bigTurn.getBgb())?upBgbList.toArray(new Integer[upBgbList.size()]):null;
			Integer[] bgbJg = upBgaBg==null?null:BigCoreService.countJg(pei.substring(0, 1),upBgbBg);
			Long bgb_jg_sum = bgbJg==null?0:Long.valueOf(bgbJg[0]+bgbJg[1]);*/


			//Long[][] jgzdInputBg = new Long[][]{ArrayUtils.int2Long(zdBg),newbBg[17],newbBg[18],ArrayUtils.toObject(bgA),ArrayUtils.toObject(bgB)};

			//结果终端报告
			/*List<Long> upJgzdList = StringUtils.isEmpty(bigTurn.getJgzd())?null:JSONArray.parseArray(bigTurn.getJgzd(),Long.class);
			Long[] upJgzdBg = StringUtils.isNotEmpty(bigTurn.getJgzd())?upJgzdList.toArray(new Long[upJgzdList.size()]):null;
			Long[] jgzdBg = BigCoreService.countJgzdBg(jgzdInputBg,bigTurn.getJgzd_lock());
			Long[] jgzdJg = upJgzdBg==null?null:BigCoreService.countJg(pei.substring(0, 1),upJgzdBg);
			Long jgzd_jg_sum = jgzdJg==null?0:Long.valueOf(jgzdJg[0]+jgzdJg[1]);*/

			//
			List<TurnGroupResult>[] groupResults = turnGroupService.handlGroup(bigTurn,pei,results,bigTurn.getXb_lock(),bigTurn.getXb_inv_lock());

			//15个板块报告
			List<BgItem> bkbgs = BigCoreService.handelBkbg(groupResults,pei,bigTurn);

			String[] bkbgSqls = joinBkbgs(bkbgs);
			//终端报告
			/*Long[] upZdbg = JsonUtils.toLongArray(bigTurn.getZdbg());
			BigDecimal[] zdbg = BigCoreService.sumZdbg(bigTurn.getZdbg_lock(),bkzd,bkbg,bkqh,bkhz);
			Long[] zdbgJg = upZdbg==null?null:BigCoreService.countJg(pei.substring(0, 1),upZdbg);
			Long zdbg_jg_sum = zdbgJg==null?0:Long.valueOf(zdbgJg[0]+zdbgJg[1]);
			Integer zdbgJgType = zdbgJg==null?null:BigCoreService.countJgDetail(zdbgJg);*/

			BgItem zdbg = buildZdbg(JsonUtils.toLongArray(bigTurn.getZdbg()),pei,bkbgs,bigTurn.getBkbgs_zd_lock(),bkbgInvLocks[0]);
			//终端求和
			BgItem zdqh = buildZdqh(JsonUtils.toLongArray(bigTurn.getZdqh()),pei,bkbgs,bigTurn.getBkbgs_qh_lock(),bkbgInvLocks[1]);

			ServiceManage.jdbcTemplate.update("UPDATE `game_big_turn` SET `frow` = ?, `bg` = ?,  `gj` = ?,  "+
							" tg_trends=?," +
							//"bkbg_trend=?,"+
							//" zd=?, zd_sum=zd_sum+?, zd_jg=CONCAT(zd_jg,?), zd_jg_sum = zd_jg_sum+?," +
							//" bga=?, bga_sum=bga_sum+?, bga_jg=CONCAT(bga_jg,?), bga_jg_sum = bga_jg_sum+?," +
							//" bgb=?, bgb_sum=bgb_sum+?, bgb_jg=CONCAT(bgb_jg,?), bgb_jg_sum = bgb_jg_sum+?, " +
							//" jgzd=?, jgzd_sum=jgzd_sum+?, jgzd_jg=CONCAT(jgzd_jg,?), jgzd_jg_sum = jgzd_jg_sum+?, " +
							//" bkbg=?, bkbg_sum=bkbg_sum+?, bkbg_jg=CONCAT(bkbg_jg,?), bkbg_jg_sum = bkbg_jg_sum+?, " +
							" bkzd=?, bkzd_sum=bkzd_sum+?, bkzd_jg=CONCAT(bkzd_jg,?), bkzd_jg_sum = bkzd_jg_sum+?, " +

							" bkhz=?, bkhz_sum=bkhz_sum+?, bkhz_jg=CONCAT(bkhz_jg,?), bkhz_jg_sum = bkhz_jg_sum+?, " +
                            " bkqh=?, bkqh_sum=bkqh_sum+?, bkqh_jg=CONCAT(bkqh_jg,?), bkqh_jg_sum = bkqh_jg_sum+?, " +

                           " bkbgs=?, bkbgs_sum=?, bkbgs_jg_sum = ?, " +
							" bkbg1_jg=CONCAT(bkbg1_jg,?),bkbg2_jg=CONCAT(bkbg2_jg,?),bkbg3_jg=CONCAT(bkbg3_jg,?),bkbg4_jg=CONCAT(bkbg4_jg,?),bkbg5_jg=CONCAT(bkbg5_jg,?)," +
							" bkbg6_jg=CONCAT(bkbg6_jg,?),bkbg7_jg=CONCAT(bkbg7_jg,?),bkbg8_jg=CONCAT(bkbg8_jg,?),bkbg9_jg=CONCAT(bkbg9_jg,?),bkbg10_jg=CONCAT(bkbg10_jg,?)," +
							" bkbg11_jg=CONCAT(bkbg11_jg,?),bkbg12_jg=CONCAT(bkbg12_jg,?),bkbg13_jg=CONCAT(bkbg13_jg,?),bkbg14_jg=CONCAT(bkbg14_jg,?),bkbg15_jg=CONCAT(bkbg15_jg,?),"+

							" zdbg=?, zdbg_sum=zdbg_sum+?, zdbg_jg=CONCAT(zdbg_jg,?), zdbg_jg_sum = zdbg_jg_sum+?, " +
							" zdqh=?, zdqh_sum=zdqh_sum+?, zdqh_jg=CONCAT(zdqh_jg,?), zdqh_jg_sum = zdqh_jg_sum+? " +

							" WHERE `id` = ?"
					,buildUpdate(
							new Object[]{
									bigTurn.getFrow()+1,
									JSONObject.toJSONString(newbBg),
									JSONObject.toJSONString(newGj),

									StringUtils.join(ArrayUtils.toObject(newTgTrends),",")/*,newBkbgTrend*/}
							,new Object[]{
									/*JSONArray.toJSONString(bkbg),
									bkbg[0].abs().add(bkbg[1].abs()),
									bkbgJg==null?"":bkbgJgType+"_"+(bkbgJg[0]+bkbgJg[1]+","),
									bkbg_jg_sum,*/

									JSONArray.toJSONString(bkzd),
									Math.abs(bkzd[0])+Math.abs(bkzd[1]),
									bkzdJg==null?"":(bkzdJgType+"_"+new BigDecimal(bkzdJg[0].toString()).add(new BigDecimal(bkzdJg[1].toString()))+","),
									bkzd_jg_sum,

									JSONArray.toJSONString(bkhz),
									bkhz[0].abs().add(bkhz[1].abs()),
									bkhzJg==null?"":bkhzJgType+"_"+(bkhzJg[0]+bkhzJg[1]+","),
									bkhz_jg_sum,

									JSONArray.toJSONString(bkqh),
									bkqh[0].abs().add(bkqh[1].abs()),
									bkqhJg==null?"":bkqhJgType+"_"+(bkqhJg[0]+bkqhJg[1]+","),
									bkqh_jg_sum/*,

									JSONArray.toJSONString(zdbg),
									zdbg[0].abs().add(zdbg[1].abs()),
									zdbgJg==null?"":zdbgJgType+"_"+(zdbgJg[0]+zdbgJg[1]+","),
									zdbg_jg_sum*/}
							,new Object[]{bkbgSqls[0],bkbgSqls[1], bkbgSqls[2]}
							,buildBkbgJgs(bkbgs)
							,buildBgItemArray(zdbg)
							,buildBgItemArray(zdqh)
							,bigTurn.getId())


					/*JSONArray.toJSONString(zdBg),
					Math.abs(zdBg[0])+Math.abs(zdBg[1]),
					zdJg==null?"":(zdJg[0]+zdJg[1]+","),
					zd_jg_sum,*/

					/*JSONArray.toJSONString(bgA),
					Math.abs(bgA[0])+Math.abs(bgA[1]),
					bgaJg==null?"":(bgaJg[0]+bgaJg[1]+","),
					bga_jg_sum,*/

					/*JSONArray.toJSONString(bgB),
					Math.abs(bgB[0])+Math.abs(bgB[1]),
					bgbJg==null?"":(bgbJg[0]+bgbJg[1]+","),
					bgb_jg_sum,*/



					/*JSONArray.toJSONString(jgzdBg),
					Math.abs(jgzdBg[0])+Math.abs(jgzdBg[1]),
					jgzdJg==null?"":(jgzdJg[0]+jgzdJg[1]+","),
					jgzd_jg_sum,*/
			);


		}

	 }



	 private Object[] buildUpdate(Object[] p1,Object[] p2,Object[] bkbgs,Object[] bkgbJgs,Object[] zdbg,Object[] zdqh,Integer bigTurnId){

		 return ArrayUtils.concatAll(p1,p2,bkbgs,bkgbJgs,zdbg,zdqh,new Object[]{bigTurnId});
	 }


	/**
	 * " zdqh=?, zdqh_sum=zdqh_sum+?, zdqh_jg=CONCAT(zdqh_jg,?), zdqh_jg_sum = zdqh_jg_sum+? " +
	 * @param item
	 * @return
	 */
	private Object[] buildBgItemArray(BgItem item){
		Object[] objs = new Object[]{
				JSONArray.toJSONString(item.getBg())
				,item.getBgSum()
				,item.getJg()==null?"":(item.getJgType()+"_"+(item.getJg()[0]+item.getJg()[1])+",")
				,item.getJgSum()};

		return objs;
	}

	 private Object[] buildBkbgJgs(List<BgItem> items){
	 	Object[] objs = new Object[items.size()];
		 for (int i = 0; i < items.size(); i++) {
			 BgItem item = items.get(i);
			 objs[i] = item.getJg()==null?"":item.getJgType()+"_"+(item.getJg()[0]+item.getJg()[1]+",");
		 }
	 	return objs;
	 }


	 private String[]  joinBkbgs(List<BgItem> items){
		 StringBuffer bg = new StringBuffer();
		 StringBuffer bgSum = new StringBuffer();
		 StringBuffer jgSum = new StringBuffer();
		 for (int i = 0; i < items.size(); i++) {
		 	BgItem item = items.get(i);
		 	bg.append(JSONArray.toJSONString(item.getBg())+"_");
		 	bgSum.append(item.getBgSum()+",");
			jgSum.append(item.getJgSum()+",");
		 }
		 bg.delete(bg.length()-1,bg.length());
		 bgSum.delete(bgSum.length()-1,bgSum.length());
		 jgSum.delete(jgSum.length()-1,jgSum.length());
	 	return new String[]{bg.toString(),bgSum.toString(),jgSum.toString()};
	 }

	 private BgItem buildZdbg(Long[] upBg,String pei,List<BgItem> bkbgs,String lock,String inv){
		String[] locks = lock.split(",");
		 BgItem ret = new BgItem();
		 long[] bg = new long[]{0l,0l};
		 for (int i = 0; i < bkbgs.size(); i++) {
			 BgItem item = bkbgs.get(i);
			 if("1".equals(locks[i]))
			 CountCoreAlgorithm.bgSum(ArrayUtils.toBasic(item.getBg()),bg,false);

		 }
		 Long[] bgLong = ArrayUtils.toObject(bg);
		 if(inv.equals("1")){
			 bgLong[0] = -bgLong[0];
			 bgLong[1] = -bgLong[1];
		 }
		 ret.setBg(bgLong);
		 ret.setJg(upBg==null?null:CountCoreAlgorithm.computeJg(pei.substring(0, 1),upBg));
		 ret.setBgSum(Math.abs(ret.getBg()[0])+Math.abs(ret.getBg()[1]));
		 ret.setJgSum(ret.getJg()==null?0:ret.getJg()[0]+ret.getJg()[1]);
		 ret.setJgType(upBg==null?null:BigCoreService.countJgDetail(ret.getJg()));
		 return ret;
	 }

	 private BgItem buildZdqh(Long[] upBg,String pei,List<BgItem> bkbgs,String lock,String inv){
		 String[] locks = lock.split(",");
		 BgItem ret = new BgItem();
		 BigDecimal[] bg = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
		 for (int i = 0; i < bkbgs.size(); i++) {
			 BgItem item = bkbgs.get(i);
			 if("1".equals(locks[i]))
			 	CountCoreAlgorithm.bgCount2(ArrayUtils.toBasic(item.getBg()),bg,1);
		 }

		 Long[] bgLong = new Long[]{bg[0].setScale(0,BigDecimal.ROUND_HALF_UP).longValue(),bg[1].setScale(0,BigDecimal.ROUND_HALF_UP).longValue()};
		 if(inv.equals("1")){
			 bgLong[0] = -bgLong[0];
			 bgLong[1] = -bgLong[1];
		 }

		 ret.setBg(bgLong);
		 ret.setJg(upBg==null?null:CountCoreAlgorithm.computeJg(pei.substring(0, 1),upBg));
		 ret.setBgSum(Math.abs(ret.getBg()[0])+Math.abs(ret.getBg()[1]));
		 ret.setJgSum(ret.getJg()==null?0:ret.getJg()[0]+ret.getJg()[1]);
		 ret.setJgType(upBg==null?null:BigCoreService.countJgDetail(ret.getJg()));

		return ret;
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
		 BigTurn bigTurn = getCurrentTurn(true);
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
		//TurnGroupCountService.initData(bigTurn);
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

		bigTurn.setTg_trends("0,0,0,0,0,0,0,0,0,0");

		bigTurn.setJgzd_lock("1,1,1,1,1");
		bigTurn.setInverse_lock("0,0,0,0,0,"+"0,0,0,0,0,"+"0,0");
		bigTurn.setXb_inv_lock("0,0,0,0,0,"+"0,0,0,0,0"+",0");
		bigTurn.setXb_lock("1,1,1,1,1,"+"1,1,1,1,1"+",1");
		bigTurn.setBkzd_lock("1,0,0,0,0,"+"0,,0,0,0,0");

		bigTurn.setBkbg_inv_lock("0,0");
		bigTurn.setBkhz_lock("1,1,1,1,1,"+"1,1,1,1,1");

        bigTurn.setTurn_bkhz_lock("1,1,1,1,1,"+"1,1,1,1,1");
        bigTurn.setBkhz_inv_lock("0,0");

        bigTurn.setZdbg_jg("");
        bigTurn.setZdbg_lock("1,1,1,1");

        bigTurn.setBkbgs_sum("0,0,0,0,0,"+"0,0,0,0,0,"+"0,0,0,0,0");
        bigTurn.setBkbgs_jg_sum("0,0,0,0,0,"+"0,0,0,0,0,"+"0,0,0,0,0");
		bigTurn.setBkbgs_inv_lock("0,0,0,0,0,"+"0,0,0,0,0,"+"0,0,0,0,0");

		bigTurn.setBkbgs_lock("1,1,1,1,1,"+"1,1,1,1,1,"+"1,1,1,1,1");
		bigTurn.setBkbgs_zd_lock("1,1,1,1,1,"+"1,1,1,1,1,"+"1,1,1,1,1");
		bigTurn.setBkbgs_qh_lock("1,1,1,1,1,"+"1,1,1,1,1,"+"1,1,1,1,1");

        Map<String, Object> param = null;
        try {
            param = BeanUtils.describe(bigTurn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

		String sql = "INSERT INTO `game_big_turn`( `frow`, `bg`, `gj`,`config_json`, `state`  ,tg_trends" +
                ",jgzd_jg,jgzd_lock,inverse_lock,xb_inv_lock,xb_lock,bkbg_jg,bkzd_jg,bkzd_lock,bkbg_inv_lock" +
                ",bkhz_jg,bkhz_lock,bkqh_jg,turn_bkhz_lock,bkhz_inv_lock,zdbg_jg,zdbg_lock,zdqh_jg,bkbgs_sum,bkbgs_jg_sum,bkbgs_inv_lock,bkbgs_lock,bkbgs_zd_lock,bkbgs_qh_lock" +
				",bkbg1_jg,bkbg2_jg,bkbg3_jg,bkbg4_jg,bkbg5_jg,bkbg6_jg,bkbg7_jg,bkbg8_jg,bkbg9_jg,bkbg10_jg,bkbg11_jg,bkbg12_jg,bkbg13_jg,bkbg14_jg,bkbg15_jg) "
                + "VALUES ( :frow, '', :gj,:config_json, 1,  :tg_trends" +
                ",'',:jgzd_lock,:inverse_lock,:xb_inv_lock,:xb_lock,'','',:bkzd_lock,:bkbg_inv_lock" +
                ",'',:bkhz_lock,'',:turn_bkhz_lock,:bkhz_inv_lock,:zdbg_jg,:zdbg_lock,'',:bkbgs_sum,:bkbgs_jg_sum,:bkbgs_inv_lock,:bkbgs_lock,:bkbgs_zd_lock,:bkbgs_qh_lock" +
				",'','','','','','','','','','','','','','','')";
        ServiceManage.namedJdbcTemplate.update(sql,new MapSqlParameterSource(param),keyHolder);

        bigTurn.setId(keyHolder.getKey().intValue());


		turnGroupService.initGroup(bigTurn.getId(),bigTurn.getBigTurnConfig().getTurnNum());

        return bigTurn;

        /*ServiceManage.namedJdbcTemplate.update(
	            new PreparedStatementCreator() {
	                public PreparedStatement createPreparedStatement(Connection con) throws SQLException
	                {
	                	PreparedStatement ps = con.prepareStatement("INSERT INTO `game_big_turn`( `frow`, `bg`, `gj`,`config_json`, `state`,   zd_sum,zd_jg,zd_jg_sum,zd_lock,tg_trends" +
										",jgzd_jg,jgzd_lock,inverse_lock,xb_inv_lock,xb_lock,bkbg_jg,bkzd_jg,bkzd_lock,bkbg_inv_lock" +
										",bkhz_jg,bkhz_lock,bkqh_jg,turn_bkhz_lock,bkhz_inv_lock) "
	                			+ "VALUES ( ?, '', ?,?, 1,  ?,?,?,?, ?" +
										",'',?,?,?,?,'','',?,?" +
										",'',?,'',?,?)"
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
						ps.setString(15, bigTurn.getBkhz_lock());
						ps.setString(16, bigTurn.getTurn_bkhz_lock());
						ps.setString(17, bigTurn.getBkhz_inv_lock());

	                	return ps;
	                }
	            }, keyHolder);*/

	}

	public BigTurn getCurrentTurn() {

	 	return getCurrentTurn(false);

	}

	public BigTurn getCurrentTurn(boolean isFull) {
		BigTurn bigTurn = null;
		try {

			 if(isFull){
				 bigTurn = ServiceManage.jdbcTemplate.queryForObject("select * from game_big_turn where  state=1 limit 1",
						 new BeanPropertyRowMapper<BigTurnFull>(BigTurnFull.class));
			 }else {
				 bigTurn = ServiceManage.jdbcTemplate.queryForObject("select * from game_big_turn where  state=1 limit 1",
						 new BeanPropertyRowMapper<BigTurn>(BigTurn.class));
			 }

		} catch (EmptyResultDataAccessException e) {
			return null;
		}
			if(StringUtils.isNotBlank(bigTurn.getConfig_json()))
				bigTurn.setBigTurnConfig(JSONObject.parseObject(bigTurn.getConfig_json(), BigTurnConfig.class));
			BigTurnConfig config = bigTurn.getBigTurnConfig();
			config.setConfLen(sysService.getSysConfig("conf_len", Integer.class));
			config.setTgThre(sysService.getSysConfig("tg_thre", Integer.class));
			config.setRule_A(sysService.getSysConfig("rule_A", String.class));
			config.setRule_B(sysService.getSysConfig("rule_B", String.class));
			config.setRule_jg_A(sysService.getSysConfig("rule_jg_A", String.class));
			config.setRule_jg_B(sysService.getSysConfig("rule_jg_B", String.class));
			/*config.setRule_bkbgs(new String[]{
					sysService.getSysConfig("rule_bkbg1", String.class),
					sysService.getSysConfig("rule_bkbg2", String.class),
					sysService.getSysConfig("rule_bkbg3", String.class),
					sysService.getSysConfig("rule_bkbg4", String.class),
					sysService.getSysConfig("rule_bkbg5", String.class),
			});*/
            config.setRule_bkbgs(sysService.getSysConfig("rule_bkbg", String.class).split("_"));
            config.setRule_bkbgs2(sysService.getSysConfig("rule_bkbg2", String.class).split("_"));
			config.setRuleBkhzs(sysService.getSysConfig("rule_bkhz", String.class).split("_"));
			return bigTurn;

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

			/*i++;
			if(bigInputResult.getJgABg()!=null) {
				ret[i][0]+=(long)bigInputResult.getJgABg()[0];
				ret[i][1]+=(long)bigInputResult.getJgABg()[1];

			}
			i++;
			if(bigInputResult.getJgBBg()!=null) {
				ret[i][0]+=(long)bigInputResult.getJgBBg()[0];
				ret[i][1]+=(long)bigInputResult.getJgBBg()[1];

			}*/
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
