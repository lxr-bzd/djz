package com.park.api.utils;

public class ArrayUtils {


    public static int[] str2int(String[] strs){

        int[] data = new int[strs.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Integer.parseInt(strs[i]);
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

    public static long[] toBasic(Long[] arr){

        long[] data = new long[arr.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = arr[i].longValue();
        }

        return data;

    }

}
