package com.park.api.utils;

import java.math.BigDecimal;

/**
 * Created By lxr on 2020/5/10
 **/
public class DoubleUtils {

    private static final int DEF_DIV_SCALE = 10;

    /**
     * @param d1
     * @param d2
     * @return Double
     * @Description 两个Double数相加
     * @author liangbao.huang
     * @date Jul 7, 2014 10:06:56 AM
     */
    public static Double add(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * @param d1
     * @param d2
     * @return Double
     * @Description 两个Double数相减
     * @author liangbao.huang
     * @date Jul 7, 2014 10:06:56 AM
     */
    public static Double sub(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.subtract(b2).doubleValue();
    }

    /**
     * @param d1
     * @param d2
     * @return Double
     * @Description 两个Double数相乘
     * @author liangbao.huang
     * @date Jul 7, 2014 10:06:56 AM
     */
    public static Double mul(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.multiply(b2).doubleValue();
    }

    /**
     * @param v1
     * @param v2
     * @return Double
     * @Description 两个Double数相除
     * @author liangbao.huang
     * @date Jul 7, 2014 10:06:56 AM
     */
    public static Double div(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @param d1
     * @param d2
     * @param scale
     * @return Double
     * @Description 两个Double数相除，并保留scale位小数
     * @author liangbao.huang
     * @date Jul 7, 2014 10:06:56 AM
     */
    public static Double div(Double d1, Double d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
