package com.park.api.service;

import com.alibaba.fastjson.JSONObject;
import com.park.api.ServiceManage;
import com.park.api.entity.BigInputResult;
import com.park.api.entity.BigTurn;
import com.park.api.entity.InputResult;
import com.park.api.entity.TurnGroup;
import com.park.api.utils.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created By lxr on 2020/4/17
 **/
@Service
public class TurnGroupCountService {


  /*  public static List<TurnGroup> countGroup(int big_turn_id,List<BigInputResult> results,String lockStr,String invLockStr){

        int[] locks = ArrayUtils.str2int(lockStr.split(","));
        int[] invLocks = ArrayUtils.str2int(invLockStr.split(","));

        int size = 10;
        int groupNum = results.size()/size+(results.size()%size!=0?1:0);

        List<TurnGroup> list = new ArrayList<>();
        for (int j = 0; j < groupNum; j++) {
            int s = j*size;
            int e = s+size>results.size()?results.size():s+size;

            long[][] datas = new long[][]{
                    new long[]{0,0},new long[]{0,0},new long[]{0,0},new long[]{0,0},new long[]{0,0}
                    ,new long[]{0,0},new long[]{0,0},new long[]{0,0},new long[]{0,0},new long[]{0,0}
                    ,new long[]{0,0}};

            for (int i = s; i < e; i++) {
                BigInputResult result = results.get(i);
                mergeTrun(datas,result,locks,invLocks);
            }
            long[] xbbg = countXbbg(datas);
            TurnGroup group = new TurnGroup();
            group.setBig_turn_id(big_turn_id);
            group.setData(JSONObject.toJSONString(datas));
            group.setOrder(j);
            group.setPosition(s+"_"+e);
            group.setXbbg(JSONObject.toJSONString(xbbg));
            list.add(group);

        }
        return list;
    }*/
/*
    public static long[] countXbbg(long[][] datas){
        long[] ret = new long[]{0,0};
        for (int i = 0; i < datas.length; i++) {
            CountCoreAlgorithm.bgCount(datas[i],ret);
        }
        return ret;
    }





    public static void updateCountGroup(List<TurnGroup> groups){

        for (TurnGroup group:groups) {
            ServiceManage.jdbcTemplate.update("UPDATE game_turn_group SET  `data` =?, `xbbg` = ? WHERE `big_turn_id` = ? AND `order` = ?",
                    group.getData(),group.getXbbg(),group.getBig_turn_id(),group.getOrder());
        }

    }

    public static void mergeTrun(long[][] datas,BigInputResult result,int[] lock,int[] invLock ){

        for (int i = 0; i < result.getResults().size(); i++) {
            if(lock[i]!=1)continue;
            InputResult inputResult = result.getResults().get(i);
            long[] dt = datas[i];
            long xl = (long)inputResult.getRets()[4];
            long sd =(long)inputResult.getRets()[5];
            CountCoreAlgorithm.bgCount(new long[]{xl,sd},dt,invLock[i]==1);
        }
        //汇总报告的
        if(lock[10]==1){
            long[] dt = datas[10];
            long xl = (long)result.getHzBg()[0];
            long sd =(long)result.getHzBg()[1];
            CountCoreAlgorithm.bgCount(new long[]{xl,sd},dt,invLock[10]==1);
        }

    }

    public static void initData(BigTurn bigTurn){

        int turnNum = bigTurn.getBigTurnConfig().getTurnNum();
        int size = 10;
        int groupNum = turnNum/size+(turnNum%size!=0?1:0);
        for (int j = 0; j < groupNum; j++) {
            int s = j*size;
            int e = s+size>turnNum?turnNum:s+size;

            TurnGroup group = new TurnGroup();
            group.setBig_turn_id(bigTurn.getId());
            group.setData("");
            group.setOrder(j);
            group.setPosition(s+"_"+e);
            ServiceManage.jdbcTemplate.update("INSERT INTO `game_turn_group`( `big_turn_id`, `order`, `data`, `position`, `xbbg`) VALUES ( ?, ?, '', ?, '')",
                    group.getBig_turn_id(),group.getOrder(),group.getPosition());

        }



    }




    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int groupIndex = i/10;
            if(list.size()<=groupIndex){
                list.add(i+"");}
            else {
                list.set(groupIndex, list.get(groupIndex)+(","+i));
            }
        }
        System.out.println(Arrays.toString(list.toArray()));
    }*/


}
