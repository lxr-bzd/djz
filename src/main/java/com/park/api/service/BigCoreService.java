package com.park.api.service;



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



}
