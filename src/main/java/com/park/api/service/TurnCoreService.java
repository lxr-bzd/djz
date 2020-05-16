package com.park.api.service;


import com.park.api.entity.BigInputResult;
import com.park.api.entity.InputResult;
import com.park.api.utils.ArrayUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created By lxr on 2020/5/4
 **/
public class TurnCoreService {

        /**
         *
         * @param results
         * @param upBkhzHu
         * @return [[板块汇总][哪一户提供,提供的倍数,连续切换的次数,累计提供次数]]
         */
    public static Object[] countBkhz(List<InputResult> results,Integer[] upBkhzHu,String lockStr) {


        String[] locks = lockStr.split(",");

        Integer hu = null;
        Integer max = null;
        InputResult oldResult = null;
        InputResult curResult = null;
        for (InputResult result : results) {
            Integer uid = Integer.valueOf(result.getUid());
            if(upBkhzHu!=null&&uid.equals(upBkhzHu[0])){
                oldResult = result;
            }

            if(!locks[uid-1].equals("1"))continue;
            if(result.getTgTrend2()==0)continue;
            //最大并且，户数最小
            if(hu==null||Math.abs(result.getTgTrend2())>max||(Math.abs(result.getTgTrend2())==max&&uid<hu)){
                hu = uid;
                max = Math.abs(result.getTgTrend2());
                curResult = result;
            }
        }

        //选举不出来
        if(curResult==null)return null;


        //以下代码为 maxResult 不为空

        int f = 2;//当前倍数
        int n = 0;//连续切换的次数
        int l = 0;//连续提供次数
        int b  = 1;//2为底初始倍数的底数
        if(upBkhzHu==null){
            f = 2;
            n = 1;
            l = 1;
            b = 1;
        }else if(hu==upBkhzHu[0]){
            f = 1;
            n = 0;
            l = upBkhzHu[3]+1;
            b = upBkhzHu[4];
        }else {
            if(oldResult.getTgTrend2()==0&&upBkhzHu[3]==1){
                f = upBkhzHu[1];
                n = upBkhzHu[2];
                l = 1;
                b = upBkhzHu[4];
            }else{
                f = (upBkhzHu[3]>1)?2:upBkhzHu[1]*2;
                n = (upBkhzHu[3]>1)?1:upBkhzHu[2]+1;
                l = 1;
                b = (upBkhzHu[3]>1)?1:upBkhzHu[4]+1;
            }
        }


        //[哪一户提供,初始提供的倍数,累计提供次数]
        Integer[] bkhzHu = new Integer[]{hu,f,n,l,b};
        //板块汇总

        //黑色取反
        int ff = /*f**/(curResult.getTgTrend2()<0?-1:1);
        BigDecimal[] dt = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
        CountCoreAlgorithm.bgCount2(new long[]{(long)curResult.getRets()[4],(long)curResult.getRets()[5]},dt,ff);
        //黑色取反
        return new Object[]{dt,bkhzHu};
    }



        public static long[] countXbbg(List<InputResult> results,long[] hzbg, String lockStr,String invLockStr ){
        int[] locks = ArrayUtils.str2int(lockStr.split(","));
        int[] invLocks = ArrayUtils.str2int(invLockStr.split(","));
        long[][] datas = new long[][]{new long[]{0l,0l},new long[]{0l,0l},new long[]{0l,0l},new long[]{0l,0l},new long[]{0l,0l},new long[]{0l,0l},new long[]{0l,0l},new long[]{0l,0l},new long[]{0l,0l},new long[]{0l,0l}
                ,new long[]{0l,0l}};
        for (int i = 0; i < results.size(); i++) {
            if(locks[i]!=1)continue;
            InputResult inputResult = results.get(i);
            long[] dt = datas[i];
            long xl = (long)inputResult.getRets()[4];
            long sd =(long)inputResult.getRets()[5];
            CountCoreAlgorithm.bgCount(new long[]{xl,sd},dt,invLocks[i]==1);
        }
        //汇总报告的
        if(locks[10]==1){
            long[] dt = datas[10];
            long xl = (long)hzbg[0];
            long sd =(long)hzbg[1];
            CountCoreAlgorithm.bgCount(new long[]{xl,sd},dt,invLocks[10]==1);
        }


        long[] xbbg = countXbbg(datas);
        return xbbg;

    }

    public static long[] countXbbg(long[][] datas){
        long[] ret = new long[]{0,0};
        for (int i = 0; i < datas.length; i++) {
            CountCoreAlgorithm.bgCount(datas[i],ret);
        }
        return ret;
    }

    public static Integer[] countBkbg2(long[] xbbg){

        long[] bg = new long[]{0,0};
        long[] d = xbbg;
        CountCoreAlgorithm.bgCount(d,bg);

        return new Integer[]{(int)bg[0],(int)bg[1]};
    }



}
