package com.park.api.service;

import com.alibaba.fastjson.JSONObject;
import com.park.api.ServiceManage;
import com.park.api.entity.*;
import com.park.api.utils.ArrayUtils;
import com.park.api.utils.JsonUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import javax.json.JsonObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By lxr on 2020/6/21
 **/
@Service
public class TurnGroupService {


    public List<TurnGroupResult>[] handlGroup(BigTurn bigTurn,String pei,List<BigInputResult> results, String lockStr,String invLockStr){

        List<TurnGroup>[] groups = getTurnGroups(bigTurn.getId());
        List<TurnGroupResult>[] turnResults = build1(results,lockStr,invLockStr);
        String[] bkbgRules = bigTurn.getBigTurnConfig().getRule_bkbgs2();
        for (int i = 0; i < groups.length; i++) {
            List<TurnGroup> groupList = groups[i];
            List<TurnGroupResult> resultList = turnResults[i];
            int ii = i*3;
            int[] ruleA = ArrayUtils.str2int(bkbgRules[ii].split(","));
            int[] ruleB = ArrayUtils.str2int(bkbgRules[ii+1].split(","));
            int[] ruleC = ArrayUtils.str2int(bkbgRules[ii+2].split(","));
            for (int j = 0; j < groupList.size(); j++) {
                TurnGroup group = groupList.get(j);
                TurnGroupResult result = resultList.get(j);

                Object[] bkA = inputTurnGroup(pei,result.getXbbgA(),group.getXbhzA(),group.getXbhzA_Trend(),ruleA);
                Object[] bkB = inputTurnGroup(pei,result.getXbbgB(),group.getXbhzB(),group.getXbhzB_Trend(),ruleB);
                Object[] bkC = inputTurnGroup(pei,result.getXbbgC(),group.getXbhzC(),group.getXbhzC_Trend(),ruleC);

                result.setBgbgA((BigDecimal[]) bkA[1]);
                result.setBgbgB((BigDecimal[]) bkB[1]);
                result.setBgbgC((BigDecimal[]) bkC[1]);

                Integer bgATrend = (Integer) bkA[0];
                Integer bgBTrend = (Integer) bkB[0];
                Integer bgCTrend = (Integer) bkC[0];
                group.setXbhzA(JSONObject.toJSONString(result.getXbbgA()));
                group.setXbhzB(JSONObject.toJSONString(result.getXbbgB()));
                group.setXbhzC(JSONObject.toJSONString(result.getXbbgC()));
                group.setXbhzA_Trend(bgATrend);
                group.setXbhzB_Trend(bgBTrend);
                group.setXbhzC_Trend(bgCTrend);
            }
        }

        updateTurnGroups(groups);
        return turnResults;
    }

    private Object[] inputTurnGroup(String pei,Long[] xbBg,String upBg,Integer upTrend,int[] rule){
        Integer[] upBgA = JsonUtils.toIntArray(upBg);
        Integer[] bkbgJg = upBgA==null?null:BigCoreService.countJg(pei.substring(0, 1),upBgA);
        Long jg_sum = bkbgJg==null?0:Long.valueOf(bkbgJg[0]+bkbgJg[1]);
        Integer newBgTrend = CountCoreAlgorithm.computeTrend(jg_sum,new Integer(upTrend));

        //BigCoreService.countBkzd(cuBg,bigTurn.getBigTurnConfig().getRule_bkbgs(),bigTurn.getBkzd_lock(),newBkbgTrend+"");
        long [] d = ArrayUtils.toBasic(xbBg);
        BigDecimal[] bkbg = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
        CountCoreAlgorithm.bgCount3(d,bkbg,newBgTrend,rule,false);
        return new Object[]{newBgTrend,bkbg};
    }



    //计算小板报告
    public static List<TurnGroupResult>[] build1(List<BigInputResult> results, String lockStr,String invLockStr ){

        List<TurnGroupResult> list1 = new ArrayList<>();
        List<TurnGroupResult> list2 = new ArrayList<>();
        List<TurnGroupResult> list3 = new ArrayList<>();
        List<TurnGroupResult> list4 = new ArrayList<>();
        List<TurnGroupResult> list5 = new ArrayList<>();
        List<TurnGroupResult>[] lists = new List[]{list1,list2,list3,list4,list5};

        int[] locks = ArrayUtils.str2int(lockStr.split(","));
        int[] invLocks = ArrayUtils.str2int(invLockStr.split(","));
        /*for (int j = 0; j < results.size(); j++) {
            TurnGroupResult r = scanner(results,locks,invLocks,j,j+1);
            list1.add(r);
        }*/
        for (int i = 0; i < 5; i++) {
            int n = i+1;
            int t = results.size()/n+(results.size()%n==0?0:1);
            for (int j = 0; j < t; j++) {
                int start = j*n;
                int end = start+n;
                if(end>results.size())end=results.size();
                TurnGroupResult r = scanner(results,locks,invLocks,start,end);
                lists[i].add(r);
            }
        }

        return lists;
    }

    public static TurnGroupResult scanner(List<BigInputResult> results,int[] locks,int[] invLocks,int start,int end){

        TurnGroupResult r = new TurnGroupResult();
        long[] xbbgA = new long[]{0,0};//取大的
        long[] xbbgB = new long[]{0,0};//取小的
        long[] xbbgC = new long[]{0,0};//各取
        for (int j = start; j < end; j++) {
            CountCoreAlgorithm.bgSum(results.get(j).getXbbg(),xbbgA);
            CountCoreAlgorithm.bgSum(results.get(j).getXbbgB(),xbbgB);
            CountCoreAlgorithm.bgSum(results.get(j).getXbbgC(),xbbgC);
        }

        r.setXbbgA(ArrayUtils.toObject(xbbgA));
        r.setXbbgB(ArrayUtils.toObject(xbbgB));
        r.setXbbgC(ArrayUtils.toObject(xbbgC));

        return r;

    }



    public List<TurnGroup>[] getTurnGroups(Integer bigTurnId){


        List<TurnGroup> list1 = ServiceManage.jdbcTemplate.query("select * from game_turn_group where  big_turn_id=? AND group_num = 1 ORDER BY turnStart ASC",new BeanPropertyRowMapper<TurnGroup>(TurnGroup.class),bigTurnId);
        List<TurnGroup> list2 = ServiceManage.jdbcTemplate.query("select * from game_turn_group where  big_turn_id=? AND group_num = 2 ORDER BY turnStart ASC",new BeanPropertyRowMapper<TurnGroup>(TurnGroup.class),bigTurnId);
        List<TurnGroup> list3 = ServiceManage.jdbcTemplate.query("select * from game_turn_group where  big_turn_id=? AND group_num = 3 ORDER BY turnStart ASC",new BeanPropertyRowMapper<TurnGroup>(TurnGroup.class),bigTurnId);
        List<TurnGroup> list4 = ServiceManage.jdbcTemplate.query("select * from game_turn_group where  big_turn_id=? AND group_num = 4 ORDER BY turnStart ASC",new BeanPropertyRowMapper<TurnGroup>(TurnGroup.class),bigTurnId);
        List<TurnGroup> list5 = ServiceManage.jdbcTemplate.query("select * from game_turn_group where  big_turn_id=? AND group_num = 5 ORDER BY turnStart ASC",new BeanPropertyRowMapper<TurnGroup>(TurnGroup.class),bigTurnId);
        return new List[]{list1,list2,list3,list4,list5};
    }

    public void updateTurnGroups(List<TurnGroup>[]  turngroups){
        for (int i = 0; i < 5; i++) {
            List<TurnGroup> groups = turngroups[i];
            for (int j = 0; j < groups.size(); j++) {
                TurnGroup group = groups.get(j);
                ServiceManage.jdbcTemplate.update("UPDATE game_turn_group SET `xbhzA` = ?, `xbhzA_Trend` = ?,`xbhzB` = ?, `xbhzB_Trend` = ?,`xbhzC` = ?, `xbhzC_Trend` = ? WHERE `id` = ?",
                        group.getXbhzA(),group.getXbhzA_Trend(),group.getXbhzB(),group.getXbhzB_Trend(),group.getXbhzC(),group.getXbhzC_Trend(),group.getId());
            }
        }


    }


    public void initGroup(Integer bigTurnId,Integer turnNum){

        for (int i = 0; i < 5; i++) {
            int n = i+1;
            int t = turnNum/n+(turnNum%n==0?0:1);
            for (int j = 0; j < t; j++) {
                int start = j*n;
                int end = start+n;
                if(end>turnNum)end=turnNum;
                ServiceManage.jdbcTemplate.update("INSERT INTO game_turn_group( `big_turn_id`, `group_num`, `turnStart`, `turnEnd`, `xbhzA`, `xbhzA_Trend`, `xbhzB`, `xbhzB_Trend`, `xbhzC`, `xbhzC_Trend`) " +
                        "VALUES ( ?, ?, ?, ?, '', 0, '', 0, '', 0)",bigTurnId,n,start,end);
            }
        }


    }

}
