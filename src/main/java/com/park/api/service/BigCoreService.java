package com.park.api.service;


import com.park.api.entity.BigInputResult;
import com.park.api.entity.InputResult;
import com.park.api.service.bean.BigTurnConfig;

import java.util.List;

public class BigCoreService {



    public static Integer[] countZdBg(Long[][] newbBg,String lockStr,int start,int end){
        Integer[] ret = new Integer[]{0,0};

        String[] lock = lockStr.split(",");

        for (int i = start; i < end;i++) {
            if("0".equals(lock[i]))continue;

            Long[] item = newbBg[i];
            if(Math.abs(item[0])>Math.abs(item[1])){
                ret[0]+= item[0]>0?1:-1;
            }else if(Math.abs(item[1])>Math.abs(item[0])){
                ret[1]+= item[1]>0?1:-1;
            }else {
                //相等
                if(item[0]!=0){
                    ret[0]+= item[0]>0?1:-1;
                    ret[1]+= item[1]>0?1:-1;
                }

            }
        }


        return ret;

    }

    public static Integer[] countJg(String pei,Integer[] upBg) {

        Integer[] bg = upBg;

        Integer pv = Integer.valueOf(pei);
        int jg1 =(pv>2?bg[0]:-bg[0]);
        int	jg2 =(pv%2!=0?bg[1]:-bg[1]);
        return new Integer[] {jg1,jg2};

    }



    /**
     * 获取报告AB
     */
    public static long[][] reckonBgAB(Long[][] newbBg,int[] tgTrends, BigTurnConfig bigTurnConfig ) {


        int start = Integer.parseInt(bigTurnConfig.getRule_A().split(",")[0]);
        int end = Integer.parseInt(bigTurnConfig.getRule_A().split(",")[1]);

        long[] dataA = new long[]{0,0};
        long[] dataB = new long[]{0,0};
        for (int i = 0; i < 10; i++) {

            Long[] ret = newbBg[i];

            int length = Math.abs(tgTrends[i]);
            if(!(length>=start&&length<=end))continue;
            long f = length<1?0:(long) (Math.pow(2,length-start));
            //黑色的取反
            if(tgTrends[i]<0)f = f*-1L;
            long ls = ret[0];
            long nv = ret[1];
            if(tgTrends[i]>0){
                if(Math.abs(ls)>Math.abs(nv))
                    dataA[0]+=(ls>0?1L:-1L)*f;
                else if(Math.abs(nv)>Math.abs(ls))
                    dataA[1]+=(nv>0?1L:-1L)*f;
                else if(Math.abs(ls)>0&&Math.abs(nv)>0) {
                    dataA[0]+=(ls>0?1L:-1L)*f;
                    dataA[1]+=(nv>0?1L:-1L)*f;
                }
            }
            if(tgTrends[i]<0){
                if(Math.abs(ls)>Math.abs(nv))
                    dataB[0]+=(ls>0?1L:-1L)*f;
                else if(Math.abs(nv)>Math.abs(ls))
                    dataB[1]+=(nv>0?1L:-1L)*f;
                else if(Math.abs(ls)>0&&Math.abs(nv)>0) {
                    dataB[0]+=(ls>0?1L:-1L)*f;
                    dataB[1]+=(nv>0?1L:-1L)*f;
                }
            }


        }


        return new long[][]{dataA,dataB};
    }


    public static int[] reckonTgTrends(List<BigInputResult> bigResults,int[] oldTgTrends){

        int[] newTgTrends = new int[10];

        long[] jgs = new long[10];
        for (BigInputResult bigResult :bigResults) {
            List<InputResult> inputResults = bigResult.getResults();
            for (InputResult result: inputResults){

                jgs[Integer.parseInt(result.getUid())-1]+=result.getJg();

            }

        }

        for (int i = 0; i < newTgTrends.length; i++) {

            long jg = jgs[i];
            int oldTgTrend = oldTgTrends[i];
            Integer newTgTrend = oldTgTrend;
            if(jg!=0) {
                if(oldTgTrend!=0&&oldTgTrend*jg>0)//同号
                    newTgTrend = oldTgTrend+(jg>0?1:-1);
                else //不同号或者为0
                    newTgTrend = (jg>0?1:-1);

            }

            newTgTrends[i] = newTgTrend;
        }

        return newTgTrends;

    }



}
