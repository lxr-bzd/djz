package com.park.api.service;

import com.park.api.entity.BigInputResult;
import com.park.api.entity.InputResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created By lxr on 2020/4/18
 **/
public class CountCoreAlgorithm {


    // L  X  D  S
    // 老 少 男 女

    //倍数2
    public static final int MULTIPLE_2 = 2;

    //取正系数
    public static final int COEFFICIENT_P = 1;

    //取反系数
    public static final int COEFFICIENT_N = -1;



    /**
     * @param recBg
     * @return
     */
    public static void bgCount(long[] bg, BigDecimal[] recBg,int jgTrend,int[] range,int coefficient){

        int length = Math.abs(jgTrend);
        if(!(length>=range[0]&&length<=range[1]))return;

        long f = length<1?0:(long) (Math.pow(MULTIPLE_2,length-range[0]));
        //黑色的取反
        if(jgTrend<0)f = f*-1L;
        f = f*coefficient;

        BigDecimal[] dt = recBg;
        long xl = bg[0];
        long sd = bg[1];
        if(Math.abs(xl)>Math.abs(sd)){
            dt[0] = dt[0].add(BigDecimal.valueOf((xl>0?1:-1)*f));
        }else if(Math.abs(sd)>Math.abs(xl)){
            dt[1] = dt[1].add(BigDecimal.valueOf((sd>0?1:-1)*f));
        }else if(xl!=0){
            dt[0] = dt[0].add(BigDecimal.valueOf((xl>0?1:-1)*f).multiply(new BigDecimal("0.5")));
            dt[1] = dt[1].add(BigDecimal.valueOf((sd>0?1:-1)*f).multiply(new BigDecimal("0.5")));
        }


    }


    /**
     * 统计报告,取一个大的
     * @param bg
     * @param recBg
     * @return
     */
    public static void bgCount(long[] bg,long[] recBg){
        bgCount(bg,recBg,false);
    }

    public static void bgCount(long[] bg,long[] recBg,boolean inverse){
        long[] dt = recBg;
        long xl = bg[0];
        long sd = bg[1];
        if(Math.abs(xl)>Math.abs(sd)){
            dt[0]+=xl>0?1:-1;
        }else if(Math.abs(sd)>Math.abs(xl)){
            dt[1]+=sd>0?1:-1;
        }else if(xl!=0){
            dt[0]+=xl>0?1:-1;
            dt[1]+=sd>0?1:-1;
        }
    }


    public static int reckonTrend(long jg, int oldTrend){

        int newTgTrend = 0;

        if(oldTrend!=0&&oldTrend*jg>0)//同号
            newTgTrend = oldTrend+(jg>0?1:-1);
        else //不同号或者为0
            newTgTrend = (jg>0?1:-1);

        return newTgTrend;
    }

}
