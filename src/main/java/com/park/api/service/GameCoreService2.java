package com.park.api.service;

import java.util.Random;

import org.springframework.stereotype.Service;
@Service
public class GameCoreService2 {
	
	static int[][] shengMap = new int[1024][];
	
	static boolean[][] duiMap = new boolean[1024][];
	
	static boolean[][] gongMap = new boolean[1024][];
	
	static boolean[][] gongColMap = new boolean[1024][];
	
	static {
		for (int i = 0; i < shengMap.length; i++) {
			
			shengMap[i] = new int[] {1,1,1,1,1};
			String iBinary = Integer.toBinaryString(i);
			for (int j = 0; j < 5; j++) {
				int index = j*2;
				if(index>=iBinary.length())break;
				if("1".equals(iBinary.substring(iBinary.length()-index-1, iBinary.length()-index)))
					shengMap[i][4-j]++;
				if(index+1>=iBinary.length())break;
				if("1".equals(iBinary.substring(iBinary.length()-(index+1)-1, iBinary.length()-(index+1))))
					shengMap[i][4-j]+=2;
				
			}
			
		}
		
		for (int i = 0; i < duiMap.length; i++) {
			
			duiMap[i] = new boolean[] {false,false,false,false,false,false,false,false,false,false};
			String iBinary = Integer.toBinaryString(i);
			for (int j = 0; j < 10; j++) {
				int index = j;
				if(index>=iBinary.length())break;
				if("1".equals(iBinary.substring(iBinary.length()-index-1, iBinary.length()-index)))
					duiMap[i][9-index] = true;
			}
			
		}
		
		gongMap = duiMap;
		gongColMap = duiMap;
		
	}
	
	static final int COUNT_VLEN = 11;
	 
	static final int SHENG_GROUP = 100000;//总组数
	/*户数*/
	static final int SHENG_GROUP_NUM = 10;
	static final int SHENG_ITEM_SIZE = 4;//生行一组大小
	 
	
		
	/**
	 * 
	 * @param sheng 36进制 四位一组,ag:AB0P,09OP...
	 * @param pei
	 * @return 1:对 ，2：错
	 */
	public static String reckonDui(String sheng,String pei) {
		
		int group = SHENG_GROUP;
		int groupLength = SHENG_ITEM_SIZE;
		int peis1 = Integer.parseInt(pei.substring(0, 1));
		
		
		StringBuilder duis = new StringBuilder();
		
		for (int i = 0; i < group; i++) {
			int st = i*(groupLength+1);
			int[] sheng1 = shengMap[Long.valueOf(sheng.substring(st,st+2), 36).intValue()];
			int[] sheng2 = shengMap[Long.valueOf(sheng.substring(st+2,st+4), 36).intValue()];
			
			int ret1 = 0;
			int ret2 = 0;
			
			for (int j = 0; j < 10; j++) {
				int a = 0;
				if(j<5) {
					a = sheng1[j];
				}else {
					
					a = sheng2[j-5];
				}
				
				int b = peis1;
				boolean jls = a>2?b>2:b<3;
				boolean jnn = (a%2==0)?(b%2==0):b%2==1;
				
				
				if(j<5) {
					int e = (4-j)*2;
					if(jls)ret1= ret1+(int)Math.pow(2, e+1);
					if(jnn)ret1= ret1+(int)Math.pow(2, e);
				}else {
					int e = (4-(j-5))*2;
					if(jls)ret2= ret2+(int)Math.pow(2, e+1);
					if(jnn)ret2= ret2+(int)Math.pow(2, e);
				}
				
				
			}
			
			
			duis.append(createDuiStr(ret1,ret2)+",");
			
		}
		
		return duis.deleteCharAt(duis.length()-1).toString();
	}
		
		
	/**
	 * 
	 * @param sheng
	 * @param dui 
	 * @return 
	 * 	老=0
	 * 	少=1
	 * 	男=0
	 * 	女=1
	 * 由以上构成二进制组合，两位一组五组一大组转换36进制返回
	 * ag： 老男老男老男老男老男老男老男老男老男老男
	 * 二进制： 00000000000000000000
	 * 36进制：00
	 */
	public String reckonGong(String sheng,String dui) {
		
		int group = SHENG_GROUP;
		int groupLength = SHENG_ITEM_SIZE;
		
		
		StringBuilder gong = new StringBuilder();
			
		for (int i = 0; i < group; i++) {
			int st = i*(groupLength+1);
			
			int[] mapv1 = shengMap[Long.valueOf(sheng.substring(st,st+2), 36).intValue()];
			int[] mapv2 = shengMap[Long.valueOf(sheng.substring(st+2,st+4), 36).intValue()];
			boolean[] dui1 = duiMap[Long.valueOf(dui.substring(st,st+2), 36).intValue()];
			boolean[] dui2 = duiMap[Long.valueOf(dui.substring(st+2,st+4), 36).intValue()];
			
			int ret1 = 0;
			int ret2 = 0;
			for (int j = 0; j < 5; j++) {
				//老少列对错
				boolean jd1 =  dui1[j*2];
				//男女列对错
				boolean jd2 =  dui1[j*2+1];
				//取对应的生
				Integer js =  mapv1[j];
				int e = (4-j)*2;
				if(js>2?!jd1:jd1)ret1= ret1+(int)Math.pow(2, e+1);
				if(js%2==0?jd2:!jd2)ret1= ret1+(int)Math.pow(2, e);
				
			}
			
			for (int j = 0; j < 5; j++) {
				//老少列对错
				boolean jd1 =  dui2[j*2];
				//男女列对错
				boolean jd2 =  dui2[j*2+1];
				//取对应的生
				Integer js =  mapv2[j];
				int e = (4-j)*2;
				if(js>2?!jd1:jd1)ret2= ret2+(int)Math.pow(2, e+1);
				if(js%2==0?jd2:!jd2)ret2= ret2+(int)Math.pow(2, e);
				
			}
			
			gong.append(createDuiStr(ret1,ret2)+",");
		}
			
			
			return gong.deleteCharAt(gong.length()-1).toString();
	}
		
	/**
	 * 
	 * @param pei 只有一组
	 * @param gong 多组
	 * @return
	 * 
	 * 1：红色对
	 * 0：白色错
	 * 
	 */
	public String reckonGongCol(String pei,String gong) {
		int group = SHENG_GROUP;
		int groupLength = SHENG_ITEM_SIZE;
		
		StringBuilder gongCol = new StringBuilder();
		int[] peis = toIntArray(pei);
		for (int i = 0; i < group; i++) {
			int st = i*(groupLength+1);
			boolean[] gong1 = gongMap[Long.valueOf(gong.substring(st,st+2), 36).intValue()];
			boolean[] gong2 = gongMap[Long.valueOf(gong.substring(st+2,st+4), 36).intValue()];
			
			int ret1 = 0;
			int ret2 = 0;
			
			for (int j = 0; j < 5; j++) {
				//老少供
				boolean jd1 =  gong1[j*2];
				//男女列供
				boolean jd2 =  gong1[j*2+1];
				//取对应的配
				int jp =  peis[j];
				int e = (4-j)*2;
				if(jp>2?!jd1:jd1)ret1= ret1+(int)Math.pow(2, e+1);
				if(jp%2==0?jd2:!jd2)ret1= ret1+(int)Math.pow(2, e);
			}
			
			for (int j = 0; j < 5; j++) {
				//取对应的配
				int jp =  peis[j+5];
				//老少供
				boolean jd1 =  gong2[j*2];
				//男女列供
				boolean jd2 =  gong2[j*2+1];
				
				int e = (4-j)*2;
				if(jp>2?!jd1:jd1)ret2= ret2+(int)Math.pow(2, e+1);
				if(jp%2==0?jd2:!jd2)ret2= ret2+(int)Math.pow(2, e);
			}
			
			gongCol.append(createGongColStr(ret1,ret2)+",");
			
		}
		
		return gongCol.deleteCharAt(gongCol.length()-1).toString();
		

	}
		
		
	/**
	 * 
	 * @param ten1 前10位十进制
	 * @param ten2 后10位十进制
	 * @return
	 */
	public static String createShengStr(int ten1,int ten2) {
		String str = null;
		if(ten1<36) {
			str="0"+Long.toString(ten1, 36).toUpperCase();
		}else str=Long.toString(ten1, 36).toUpperCase();
			
		if(ten2<36) {
			str+="0"+Long.toString(ten2, 36).toUpperCase();
		}else str+=Long.toString(ten2, 36).toUpperCase();
		
		return str;
	}
		
		
	/**
	 * 
	 * @param ten1 前10位十进制
	 * @param ten2 后10位十进制
	 * @return
	 */
	private static String createDuiStr(int ten1,int ten2) {
		String str = null;
		if(ten1<36) {
			str="0"+Long.toString(ten1, 36).toUpperCase();
		}else str=Long.toString(ten1, 36).toUpperCase();
			
		if(ten2<36) {
			str+="0"+Long.toString(ten2, 36).toUpperCase();
		}else str+=Long.toString(ten2, 36).toUpperCase();
		
		return str;
	}

	private static String createGongStr(int ten1,int ten2) {
			
		return createDuiStr(ten1,ten2);
	}

	private static String createGongColStr(int ten1,int ten2) {
			
		return createDuiStr(ten1,ten2);
	}

	private int[] toIntArray(String str) {
			
		char[] is = str.toCharArray();
		int[] ret = new int[is.length];
		for (int i = 0; i < is.length; i++) {
			ret[i] = Integer.valueOf(String.valueOf(is[i]));
		}
			return ret;
			
	}
	
	
	

	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder("123,");
			System.out.println(Long.toBinaryString(Long.valueOf("SF",36)));
			System.out.println(Long.valueOf("DX", 36));
			System.out.println(Long.toString(682,36));
			
			Random random = new Random();
			for (int i = 0; i < 100; i++) {
				System.out.println(random.nextInt(1024));
			}
			
	}
		

}
