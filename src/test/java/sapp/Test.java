package sapp;

import sapp.DllTest.Dll;

public class Test {
	
	static int[][] map = {{1,1,1,1,1},{1,1,1,1,2}};
	
	

	/**
	 * 
	 * @param sheng 36进制 四位一组,ag:AB0P,09OP...
	 * @param pei
	 * @return 1:对 ，2：错
	 */
	public static String reckonDui2(String sheng,String pei) {
		//String[] shengs = sheng.split(",");
		int group = 10000;
		int groupLength = 4;
		int peis1 = Integer.parseInt(pei.substring(0, 1));
		
		
		StringBuilder duis = new StringBuilder();
		
		for (int i = 0; i < group; i++) {
			int st = i*(groupLength+1);
			int[] mapv1 = map[Long.valueOf(sheng.substring(st,st+2), 36).intValue()];
			int[] mapv2 = map[Long.valueOf(sheng.substring(st+2,st+4), 36).intValue()];
			
			int ret1 = 0;
			int ret2 = 0;
			
			for (int j = 0; j < 10; j++) {
				int a = 0;
				if(j<5) {
					a = mapv1[j];
				}else {
					
					a = mapv2[j-5];
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
			
			
			duis.append(Long.toString(ret1, 36).toUpperCase()
					+Long.toString(ret2, 36).toUpperCase()+",");
			
			
		}
		
		return duis.deleteCharAt(duis.length()-1).toString();
	}
	
	/**
	 * 
	 * @param sheng 36进制 四位一组,ag:AB0P,09OP...
	 * @param pei
	 * @return 1:对 ，2：错
	 */
	public static String reckonDui(String sheng,String pei) {
		String[] shengs = sheng.split(",");
		
		StringBuilder duis = new StringBuilder();
		
		for (int i = 0; i < shengs.length; i++) {
			String ts = shengs[i];
			for (int j = 0; j < ts.length(); j++) {
				Integer a = Integer.parseInt(ts.substring(j, j+1));
				Integer b = Integer.parseInt(pei.substring(j, j+1));
				boolean jls = a>2?b>2:b<3;
				boolean jnn = (a%2==0)?(b%2==0):b%2==1;
				
				duis.append(jls?"1":"2");
				duis.append(jnn?"1":"2");
				
			}
			duis.append(",");
			
			
		}
		
		return duis.deleteCharAt(duis.length()-1).toString();
	}
	
	public static void main(String[] args) {
		//System.out.println(System.getProperty("sun.arch.data.model"));
		
		String sheng = null;
		long start = 0;
		
		
		String a = null;
		
		
		
		
		sheng = "";
		for (int i = 0; i < 10000; i++) {
			if(i+1<10000)
			sheng+="1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,";
			
			else sheng+="1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111";
		}
		
		start = System.currentTimeMillis();
		a = reckonDui(sheng,"1111111111");
		System.out.println(System.currentTimeMillis()-start);
		
		
		sheng = "";
		
		for (int i = 0; i < 10000; i++) {
			if(i+1<10000)
			sheng+="0000,0000,0000,0000,0000,0000,0000,0000,0000,0000,";
			
			else sheng+="0000,0000,0000,0000,0000,0000,0000,0000,0000,0000";
		}
		
		//String sum = Dll.instance.countQueue();
		
				
		start = System.currentTimeMillis();
		a = reckonDui2(sheng,"1111111111");
		System.out.println(System.currentTimeMillis()-start);
		
		start = System.currentTimeMillis();
		
		System.out.println(a.length());
	}



}
