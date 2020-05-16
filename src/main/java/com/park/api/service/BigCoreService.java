package com.park.api.service;


import com.park.api.entity.BigInputResult;
import com.park.api.entity.InputResult;
import com.park.api.entity.TurnGroup;
import com.park.api.service.bean.BigTurnConfig;
import com.park.api.utils.ArrayUtils;
import com.park.api.utils.DoubleUtils;
import com.park.api.utils.JsonUtils;

import java.math.BigDecimal;
import java.util.List;

public class BigCoreService {


    /**
     * 结果终端报告
     * @param newbBg
     * @param lockStr
     * @return
     */
    public static Long[] countJgzdBg(Long[][] newbBg,String lockStr){
        Long[] ret = new Long[]{0l,0l};

        String[] lock = lockStr.split(",");

        for (int i = 0; i < newbBg.length;i++) {
            if("0".equals(lock[i]))continue;

            Long[] item = newbBg[i];

                    ret[0]+= item[0];
                    ret[1]+= item[1];


        }


        return ret;

    }

    /**
     * 终端报告
     * @param newbBg
     * @param lockStr
     * @param start
     * @param end
     * @return
     */
    public static Integer[] countZdBg(Long[][] newbBg,String lockStr,int start,int end,long[][] bgAB){
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

        //报告AB
        int li = 19;
        for (int i = 0; i < 2;i++) {

            if("0".equals(lock[i+li]))continue;

            long[] item = bgAB[i];
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


    public static Double[] countJg(String pei,Double[] upBg) {

        Double[] bg = upBg;

        Integer pv = Integer.valueOf(pei);
        Double jg1 =(pv>2?bg[0]:-bg[0]);
        Double	jg2 =(pv%2!=0?bg[1]:-bg[1]);
        return new Double[] {jg1,jg2};

    }


    public static Integer[] countJg(String pei,Integer[] upBg) {

        Integer[] bg = upBg;

        Integer pv = Integer.valueOf(pei);
        int jg1 =(pv>2?bg[0]:-bg[0]);
        int	jg2 =(pv%2!=0?bg[1]:-bg[1]);
        return new Integer[] {jg1,jg2};

    }

    /**
     * @param upBg
     * @return 颜色值
     * 颜色值:1：两个都对（红色）
     * ，2：对一个大的（橙色）
     * ，3 错一个大的（绿色）
     * ，4 两个错（黑色）
     * ，5 两个为0（淡灰色）
     */
    public static Integer countJgDetail(Integer[] jg,Integer[] upBg) {

        Integer type = null;
        if(jg[0]+jg[1]==0)
            type = 5;
        else if(jg[0]>0&&jg[1]>0)
            type = 1;
        else if(jg[0]<0&&jg[1]<0)
            type = 4;
        else {
            if(Math.abs(jg[0])>Math.abs(jg[1])){
                type = jg[0]>0?2:3;
            }else{
                type = jg[1]>0?2:3;
            }
        }
        return type;

    }

    public static Integer countJgDetail(Long[] jg) {
        Integer type = null;
        if(jg[0]+jg[1]==0)
            type = 5;
        else if(jg[0]>0&&jg[1]>0)
            type = 1;
        else if(jg[0]<0&&jg[1]<0)
            type = 4;
        else {
            if(Math.abs(jg[0])>Math.abs(jg[1])){
                type = jg[0]>0?2:3;
            }else{
                type = jg[1]>0?2:3;
            }
        }
        return type;
    }

    public static Integer countJgDetail(Double[] jg) {
        Integer type = null;
        if(jg[0]+jg[1]==0)
            type = 5;
        else if(jg[0]>0&&jg[1]>0)
            type = 1;
        else if(jg[0]<0&&jg[1]<0)
            type = 4;
        else {
            if(Math.abs(jg[0])>Math.abs(jg[1])){
                type = jg[0]>0?2:3;
            }else{
                type = jg[1]>0?2:3;
            }
        }
        return type;

    }

    /**
     * @param upBg
     * @return 颜色值
     * 颜色值:1：两个都对（红色）
     * ，2：对一个大的（橙色）
     * ，3 错一个大的（绿色）
     * ，4 两个错（黑色）
     * ，5 两个为0（淡灰色）
     */
    public static Integer countJgDetail(Double[] jg,Double[] upBg) {

        Integer type = null;
        if(jg[0]+jg[1]==0)
            type = 5;
        else if(jg[0]>0&&jg[1]>0)
            type = 1;
        else if(jg[0]<0&&jg[1]<0)
            type = 4;
        else {
            if(Math.abs(jg[0])>Math.abs(jg[1])){
                type = jg[0]>0?2:3;
            }else{
                type = jg[1]>0?2:3;
            }
        }
        return type;

    }


    public static Long[] countJg(String pei,Long[] upBg) {

        Long[] bg = upBg;

        Integer pv = Integer.valueOf(pei);
        long jg1 =(pv>2?bg[0]:-bg[0]);
        long jg2 =(pv%2!=0?bg[1]:-bg[1]);
        return new Long[] {jg1,jg2};

    }



    /**
     * 获取报告AB
     */
    public static long[][] reckonBgAB(Long[][] newbBg,int[] tgTrends, BigTurnConfig bigTurnConfig ) {


        int startA = Integer.parseInt(bigTurnConfig.getRule_A().split(",")[0]);
        int endA = Integer.parseInt(bigTurnConfig.getRule_A().split(",")[1]);

        int startB = Integer.parseInt(bigTurnConfig.getRule_B().split(",")[0]);
        int endB = Integer.parseInt(bigTurnConfig.getRule_B().split(",")[1]);

        long[] dataA = new long[]{0,0};
        long[] dataB = new long[]{0,0};
        for (int i = 0; i < 10; i++) {

            Long[] ret = newbBg[i];


            if(tgTrends[i]>0){
                int length = Math.abs(tgTrends[i]);
                if(!(length>=startA&&length<=endA))continue;
                long f = length<1?0:(long) (Math.pow(2,length-startA));
                //黑色的取反
                if(tgTrends[i]<0)f = f*-1L;
                long ls = ret[0];
                long nv = ret[1];
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
                int length = Math.abs(tgTrends[i]);
                if(!(length>=startB&&length<=endB))continue;
                long f = length<1?0:(long) (Math.pow(2,length-startB));
                //黑色的取反
                if(tgTrends[i]<0)f = f*-1L;
                long ls = ret[0];
                long nv = ret[1];
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



    public static BigDecimal[] countBkhz(List<BigInputResult> results,String lockStr,String[] rules){

        String[] locks = lockStr.split(",");
        BigDecimal[] bg = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
        for (int i = 0; i < locks.length; i++) {
            if("0".equals(locks[i]))continue;

            String[] rs = rules[i].split(",");
            int[] rule = ArrayUtils.str2int(rs);
            for (BigInputResult result :results) {
                Integer[] hu = result.getBkhzHu();
                if(hu==null)continue;

                int b = hu[4];
                if(!(b>=rule[0]&&b<=rule[1]))continue;

                long n = (long)Math.pow(2,hu[2]==0?1:(hu[2]-rule[0]+1));

                BigDecimal[] d = result.getBkhz();
                bg[0] = bg[0].add(d[0].multiply(new BigDecimal(n)));
                bg[1] = bg[1].add(d[1].multiply(new BigDecimal(n)));
            }

        }
        bg[0] = bg[0].setScale(0,BigDecimal.ROUND_HALF_UP);
        bg[1] = bg[1].setScale(0,BigDecimal.ROUND_HALF_UP);
        return bg;
    }


    public static BigDecimal[] countBkqh(List<BigInputResult> results,String lockStr,String[] rules){

        String[] locks = lockStr.split(",");
        BigDecimal[] bg = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
        for (BigInputResult result :results) {

            boolean flag = true;
            for (int i = 0; i < locks.length; i++) {
                if ("0".equals(locks[i])) continue;

                String[] rs = rules[i].split(",");
                int[] rule = ArrayUtils.str2int(rs);
                Integer[] hu = result.getBkhzHu();
                if(hu==null)continue;

                int b = hu[4];
                if(!(b>=rule[0]&&b<=rule[1]))continue;

                flag = false;
                break;

            }

            if(flag)continue;
            BigDecimal[] d = result.getBkhz();
            CountCoreAlgorithm.bgCount2(d,bg,1);
        }
        return bg;
    }
    public static BigDecimal[] countBkbg(List<BigInputResult> results){

        BigDecimal[] bg = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
        for (BigInputResult result :results) {
            BigDecimal[] d = result.getBkzd();
            CountCoreAlgorithm.bgSum(d,bg);
        }
        bg[0] = bg[0].setScale(0,BigDecimal.ROUND_HALF_UP);
        bg[1] = bg[1].setScale(0,BigDecimal.ROUND_HALF_UP);
        return bg;
    }



    public static BigDecimal[] countBkzd(Integer[] bkbg,String[] rules,String lockStr,String trend){

        String[] locks = lockStr.split(",");

        long [] d = ArrayUtils.toBasic(ArrayUtils.int2Long(bkbg));
        Integer t = Integer.valueOf(trend);
        BigDecimal[] bg = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
        for (int i = 0; i < rules.length; i++) {
            String ruleStr = rules[i];
            if(locks[i].equals("0"))continue;

            String[] rs = ruleStr.split(",");
            int[] rule = ArrayUtils.str2int(rs);
            CountCoreAlgorithm.bgCount2(d,bg,t,rule,CountCoreAlgorithm.COEFFICIENT_P);
        }
        /*if(isOdd(bg[0].intValue())||isOdd(bg[1].intValue())){
            System.out.println(1);
        }*/


        return bg;
    }

    private static boolean isOdd(int a) {
        if (a%2!=0) {
            return true;
        }
        return false;
    }


    public static long[] countBkzd2(List<BigInputResult> results,String[] ruleStr){

        long[] bg = new long[]{0l,0l};
        for (BigInputResult result :results) {
            /*boolean flag = true;
            for (int i = 0; i < ruleStr.length; i++) {
                String[] rs = ruleStr[i].split(",");
                int[] rule = ArrayUtils.str2int(rs);
                int length = Math.abs(result.getNewBkbgTrend());
                if((length>=rule[0]&&length<=rule[1])){
                    flag = false;
                    break;
                }
            }

            if(flag)continue;*/
            //long[] d = ArrayUtils.int2Long(ArrayUtils.toBasic(result.getBkbg()));
            BigDecimal[] d = result.getBkzd();
            CountCoreAlgorithm.bgCount(d,bg);
        }

        return bg;
    }


    public static BigDecimal[] sumZdbg(String lockStr,long[] bkzd,BigDecimal[] bkbg,BigDecimal[] bkqh,BigDecimal[] bkhz){

        String[] locks = lockStr.split(",");
        BigDecimal[] bg = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
        if("1".equals(locks[0]))bg[0] = bg[0].add(BigDecimal.valueOf(bkzd[0]));
        if("1".equals(locks[1]))bg[0] = bg[0].add(bkbg[0]);
        if("1".equals(locks[2]))bg[0] = bg[0].add(bkqh[0]);
        if("1".equals(locks[3]))bg[0] = bg[0].add(bkhz[0]);

        if("1".equals(locks[0]))bg[1] = bg[1].add(BigDecimal.valueOf(bkzd[1]));
        if("1".equals(locks[1]))bg[1] = bg[1].add(bkbg[1]);
        if("1".equals(locks[2]))bg[1] = bg[1].add(bkqh[1]);
        if("1".equals(locks[3]))bg[1] = bg[1].add(bkhz[1]);

        bg[0] = bg[0].setScale(0,BigDecimal.ROUND_HALF_UP);
        bg[1] = bg[1].setScale(0,BigDecimal.ROUND_HALF_UP);
        return bg;
    }

}
