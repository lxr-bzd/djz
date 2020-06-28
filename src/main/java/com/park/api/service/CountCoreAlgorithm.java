package com.park.api.service;

import com.park.api.entity.BigInputResult;
import com.park.api.entity.InputResult;

import java.math.BigDecimal;
import java.util.Arrays;
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
    public static void bgCount2(long[] bg, BigDecimal[] recBg,int jgTrend,int[] range,int coefficient){

        int length = Math.abs(jgTrend);
        if(!(length>=range[0]&&length<=range[1]))return;

        long f = length<1?0:(long) (Math.pow(MULTIPLE_2,length-range[0]));
        //黑色的取反
        if(jgTrend<0)f = f*-1L;
        f = f*coefficient;

        bgCount2(bg,recBg,f);


    }

    /**
     *  与bgCount相比bgCount2会对提供值一样的各取一半
     *
     * @param recBg
     * @return
     */
    public static void bgCount2(Double[] bg, BigDecimal[] recBg,long f){

        bgCount2(new long[]{bg[0].longValue(),bg[1].longValue()},recBg,f);
    }

    /**
     *  与bgCount相比bgCount2会对提供值一样的各取一半
     *
     * @param recBg
     * @return
     */
    public static void bgCount2(long[] bg, BigDecimal[] recBg,long f){

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
     *  与bgCount相比bgCount2会对提供值一样的各取一半
     *
     * @param recBg
     * @return
     */
    public static void bgCount2(BigDecimal[] bg, BigDecimal[] recBg,long f){

        BigDecimal[] dt = recBg;
        BigDecimal xl = bg[0];
        BigDecimal sd = bg[1];
        if(xl.abs().compareTo(sd.abs())==1){
            dt[0] = dt[0].add(BigDecimal.valueOf((xl.compareTo(BigDecimal.ZERO)==1?1:-1)*f));
        }else if(sd.abs().compareTo(xl.abs())==1){
            dt[1] = dt[1].add(BigDecimal.valueOf((sd.compareTo(BigDecimal.ZERO)==1?1:-1)*f));
        }else if(!xl.equals(BigDecimal.ZERO)){
            dt[0] = dt[0].add(BigDecimal.valueOf((xl.compareTo(BigDecimal.ZERO)==1?1:-1)*f).multiply(new BigDecimal("0.5")));
            dt[1] = dt[1].add(BigDecimal.valueOf((sd.compareTo(BigDecimal.ZERO)==1?1:-1)*f).multiply(new BigDecimal("0.5")));
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

        int f = inverse?-1:1;
        long[] dt = recBg;
        long xl = bg[0];
        long sd = bg[1];
        if(Math.abs(xl)>Math.abs(sd)){
            dt[0]+=(xl>0?1:-1)*f;
        }else if(Math.abs(sd)>Math.abs(xl)){
            dt[1]+=(sd>0?1:-1)*f;
        }else if(xl!=0){
            dt[0]+=(xl>0?1:-1)*f;
            dt[1]+=(sd>0?1:-1)*f;
        }
    }

    /**
     * 取 小的
     * @param bg
     * @param recBg
     * @param inverse
     */
    public static void bgCountB(long[] bg,long[] recBg,boolean inverse){

        int f = inverse?-1:1;
        long[] dt = recBg;
        long xl = bg[0];
        long sd = bg[1];
        if(Math.abs(xl)<Math.abs(sd)){
            dt[0]+=(xl>0?1:-1)*f;
        }else if(Math.abs(sd)<Math.abs(xl)){
            dt[1]+=(sd>0?1:-1)*f;
        }else if(xl!=0){
            dt[0]+=(xl>0?1:-1)*f;
            dt[1]+=(sd>0?1:-1)*f;
        }
    }

    /**
     * 取 小的
     * @param bg
     * @param recBg
     * @param inverse
     */
    public static void bgCountC(long[] bg,long[] recBg,boolean inverse){

        int f = inverse?-1:1;
        long[] dt = recBg;
        long xl = bg[0];
        long sd = bg[1];
        if(Math.abs(xl)!=0){
            dt[0]+=(xl>0?1:-1)*f;
        }
        if(Math.abs(sd)!=0){
            dt[1]+=(sd>0?1:-1)*f;
        }
    }

    public static void bgCount(BigDecimal[] bg,long[] recBg){

        int f = 1;
        long[] dt = recBg;
        BigDecimal xl = bg[0];
        BigDecimal sd = bg[1];
        if(xl.abs().compareTo(sd.abs())==1){
            dt[0]+=(xl.compareTo(BigDecimal.ZERO)==1?1:-1)*f;
        }else if(sd.abs().compareTo(xl.abs())==1){
            dt[1]+=(sd.compareTo(BigDecimal.ZERO)==1?1:-1)*f;
        }else if(xl.abs().compareTo(BigDecimal.ZERO)!=0){
            dt[0]+=(xl.compareTo(BigDecimal.ZERO)==1?1:-1)*f;
            dt[1]+=(sd.compareTo(BigDecimal.ZERO)==1?1:-1)*f;
        }
    }

    public static void bgSum(long[] bg,long[] recBg){
        bgSum(bg,recBg,false);
    }

    public static void bgSum(long[] bg,long[] recBg,boolean inverse){

        int f = inverse?-1:1;
        long[] dt = recBg;
        long xl = bg[0];
        long sd = bg[1];
        dt[0]+=(xl)*f;
        dt[1]+=(sd)*f;

    }


    public static void bgSum(BigDecimal[] bg,BigDecimal[] recBg){

        recBg[0] = recBg[0].add(bg[0]);
        recBg[1] = recBg[1].add(bg[1]);

    }

    public static void bgSum(Integer[] bg,Integer[] recBg){

        recBg[0] += bg[0];
        recBg[1] += bg[1];

    }

    public long[][] inverseBg(long[][] bg,String[] lockArr){

        for (int i = 0; i < 2; i++) {
            int li = i+10;
            if(lockArr[li].equals("1")){
                bg[i][0] = -bg[i][0];
                bg[i][1] = -bg[i][1];
            }


        }
        return bg;
    }
    public static long[] inverseBg(long[] bg,String lock){

        if(lock.equals("1")){
            bg[0] = -bg[0];
            bg[1] = -bg[1];
        }
        return bg;
    }


    public static Integer[] inverseBg(Integer[] bg,String lock){

            if(lock.equals("1")){
                bg[0] = -bg[0];
                bg[1] = -bg[1];
            }
        return bg;
    }

    public static BigDecimal[] inverseBg(BigDecimal[] bg,String lock){

        if(lock.equals("1")){
            bg[0] = bg[0].multiply(BigDecimal.valueOf(-1));
            bg[1] = bg[1].multiply(BigDecimal.valueOf(-1));
        }
        return bg;
    }


    public static int computeTrend(long jg, int oldTrend){

        int newTgTrend = oldTrend;

        if(jg!=0) {
            if (oldTrend != 0 && oldTrend * jg > 0)//同号
                newTgTrend = oldTrend + (jg > 0 ? 1 : -1);
            else //不同号或者为0
                newTgTrend = (jg > 0 ? 1 : -1);

        }
        return newTgTrend;
    }


    /**
     * 由提供和配值计算结果
     * @param pei
     * @param upBg
     * @return
     */
    public static Integer[] computeJg(String pei,Integer[] upBg) {

        Integer[] bg = upBg;

        Integer pv = Integer.valueOf(pei);
        int jg1 =(pv>2?bg[0]:-bg[0]);
        int	jg2 =(pv%2!=0?bg[1]:-bg[1]);
        return new Integer[] {jg1,jg2};

    }


    /**
     * 由提供和配值计算结果
     * @param pei
     * @param upBg
     * @return
     */
    public static Long[] computeJg(String pei,Long[] upBg) {

        Long[] bg = upBg;

        Integer pv = Integer.valueOf(pei);
        long jg1 =(pv>2?bg[0]:-bg[0]);
        long jg2 =(pv%2!=0?bg[1]:-bg[1]);
        return new Long[] {jg1,jg2};

    }

    /**
     * 由提供和配值计算结果
     * @param pei
     * @param upBg
     * @return
     */
    public static Double[] computeJg(String pei,Double[] upBg) {

        Double[] bg = upBg;

        Integer pv = Integer.valueOf(pei);
        Double jg1 =(pv>2?bg[0]:-bg[0]);
        Double jg2 =(pv%2!=0?bg[1]:-bg[1]);
        return new Double[] {jg1,jg2};

    }


    /**
     *
     * @param bg
     * @param recBg
     * @param inverse
     */
    public static void bgCountGroup1(long[] bg,Integer[] recBg,boolean inverse){

        int f = inverse?-1:1;
        Integer[] temp = new Integer[]{0,0};
        long max = 0;
        for (int i = 0; i < bg.length; i++) {
            long xl = bg[i];
            if(Math.abs(xl)>max){
                max = Math.abs(xl);
                temp = new Integer[]{0,0};
            }else if(Math.abs(xl)==max){

            }else if(Math.abs(xl)<max){
                continue;
            }

            int t = i%2==0?0:1;
            temp[t]+=(xl>0?1:-1)*f;
        }

        recBg[0]+=temp[0];
        recBg[1]+=temp[1];
    }

    /**
     *
     * @param bg
     * @param recBg
     * @param inverse
     */
    public static void bgCountGroup2(long[] bg,Integer[] recBg,boolean inverse){

        int f = inverse?-1:1;
        Integer[] temp = new Integer[]{0,0};
        long min = Long.MAX_VALUE;
        for (int i = 0; i < bg.length; i++) {
            long xl = bg[i];
            if(Math.abs(xl)<min){
                min = Math.abs(xl);
                temp = new Integer[]{0,0};
            }else if(Math.abs(xl)==min){

            }else if(Math.abs(xl)>min){
                continue;
            }

            int t = i%2==0?0:1;
            temp[t]+=(xl>0?1:-1)*f;
        }

        recBg[0]+=temp[0];
        recBg[1]+=temp[1];
    }

    /**
     *
     * @param bg
     * @param recBg
     * @param inverse
     */
    public static void bgCountGroup3(long[] bg,Integer[] recBg,boolean inverse){

        int f = inverse?-1:1;
        Integer[] temp = new Integer[]{0,0};
        long min = 0;
        for (int i = 0; i < bg.length; i++) {
            long xl = bg[i];

            int t = i%2==0?0:1;
            temp[t]+=(xl>0?1:-1)*f;
        }

        recBg[0]+=temp[0];
        recBg[1]+=temp[1];
    }

    public static void main(String[] args) {

        Integer[] rec = new Integer[]{0,0};
        bgCountGroup3(new long[]{1,2,1,2},rec,false);
        System.out.println(Arrays.toString(rec));
        Double b = 0.9d;

        System.out.println(new BigDecimal(1.5).setScale(0,BigDecimal.ROUND_HALF_UP));
        System.out.println(b-1);
        b = BigDecimal.valueOf(b).subtract(BigDecimal.valueOf(1)).doubleValue();
        System.out.println(b);
    }



}
