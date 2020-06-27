package com.park.api.utils;

import java.util.Arrays;

public class ArrayUtils {

    public static Long[] int2Long(Integer[] ints){

        Long[] data = new Long[ints.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = new Long(ints[i]);
        }

        return data;
    }

    public static long[] int2Long(int[] ints){

        long[] data = new long[ints.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = (long)ints[i];
        }

        return data;
    }

    public static int[] str2int(String[] strs){

        int[] data = new int[strs.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Integer.parseInt(strs[i]);
        }

        return data;
    }
    public static long[] str2long(String[] strs){

        long[] data = new long[strs.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Long.parseLong(strs[i]);
        }

        return data;
    }



    public static Integer[] toObject(int[] ints){

        Integer[] data = new Integer[ints.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Integer.valueOf(ints[i]);
        }

        return data;

    }

    public static Long[] toObject(long[] ints){

        Long[] data = new Long[ints.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Long.valueOf(ints[i]);
        }

        return data;

    }

    public static long[] toBasic(Long[] arr){

        long[] data = new long[arr.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = arr[i].longValue();
        }

        return data;

    }

    public static int[] toBasic(Integer[] arr){

        int[] data = new int[arr.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = arr[i].intValue();
        }

        return data;

    }


    public static Object[] concatAll(Object[]... arr) {

        int n = 0;
        for (int i = 0; i < arr.length; i++) {
            n+=arr[i].length;
        }
        Object[] c= new Object[n];

        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            Object[] a = arr[i];
            System.arraycopy(a, 0, c, index, a.length);
            index+=a.length;
        }

        return c;
    }

    public static void main(String[] args) {
        Object[] a = concatAll(new Object[]{1,2,3},new Object[]{2,3,4},new Object[]{5,6,7},new Object[]{1,2,3});
        System.out.println(Arrays.toString(a));
    }

}
