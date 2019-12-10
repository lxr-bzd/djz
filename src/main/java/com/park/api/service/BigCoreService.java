package com.park.api.service;


import com.park.api.entity.BigInputResult;
import com.park.api.entity.InputResult;
import com.park.api.service.bean.BigTurnConfig;

import java.util.List;

public class BigCoreService {



    public static Integer[] countZdBg(Long[][] newbBg,String lockStr){
        Integer[] ret = new Integer[]{0,0};

        String[] lock = lockStr.split(",");

        for (int i = 0; i < newbBg.length;i++) {
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
    public static long[] reckonBgAB(List<BigInputResult> results, BigTurnConfig bigTurnConfig ) {


        int start = Integer.parseInt(bigTurnConfig.getRule_A().split(",")[0]);
        int end = Integer.parseInt(bigTurnConfig.getRule_A().split(",")[1]);

        long[] data = new long[]{0,0};
        for (BigInputResult result : results) {

            int length = Math.abs(result.getTgTrend());
            if(!(length>=start&&length<=end))continue;
            long f = length<1?0:(long) (Math.pow(2,length-start));
            if(result.getTgTrend()<0)f = f*-1L;
            long ls = (long)result.getRets()[4];
            long nv = (long)result.getRets()[5];
            if(Math.abs(ls)>Math.abs(nv))
                data[0]+=(ls>0?1L:-1L)*f;
            else if(Math.abs(nv)>Math.abs(ls))
                data[1]+=(nv>0?1L:-1L)*f;
            else if(Math.abs(ls)>0&&Math.abs(nv)>0) {
                data[0]+=(ls>0?1L:-1L)*f;
                data[1]+=(nv>0?1L:-1L)*f;
            }

        }


        return data;
    }



}
