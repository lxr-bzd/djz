package com.park.api.service;

import org.springframework.stereotype.Service;

@Service
public class GameCoreService {
	
	/**
	 * 计算统计和实计
	 * @param gongCol
	 * @return
	 */
	public int[] reckonAllCount(String count,String upCount) {
		int allcount = 0;
		int allrealA = 0;
		int allrealB = 0;
		String[] counts = count.split(",");
		String[] upCounts = upCount.split(",");
		int l = counts.length;
		for (int i = 0; i < l; i++) {
			Integer jc = Integer.parseInt(counts[i]);
			Integer juc = Integer.parseInt(upCounts[i]);
			allcount+=jc;
			if(juc>0)allrealA+=jc;
			if(juc<0)allrealB+=jc;
			
		}
		
		return new int[]{allcount,allrealA,allrealB};
	}
	
	public String reckonDui(String sheng,String pei) {
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
	
	public String reckonGong(String sheng,String dui) {
		String[] duis = dui.split(",");
		String[] shengs = sheng.split(",");
		StringBuilder gong = new StringBuilder();
		
		for (int i = 0; i < duis.length; i++) {
			String d = duis[i];
			String s = shengs[i];
			for (int j = 0; j < s.length(); j++) {
				Integer jd1 =  Integer.parseInt(d.substring(j*2, j*2+1));
				Integer jd2 =  Integer.parseInt(d.substring(j*2+1, j*2+2));
				//取对应的生
				Integer js =  Integer.parseInt(s.substring(j, j+1));
				
				gong.append(js>2?(jd1==1?"老":"少"):(jd1==1?"少":"老"));
				gong.append(js%2==0?(jd2==1?"女":"男"):(jd2==1?"男":"女"));			
			}
			gong.append(",");
		}
		
		return gong.deleteCharAt(gong.length()-1).toString();
	}
	
	/**
	 * 
	 * @param pei 只有一组
	 * @param gong 多组
	 * @return
	 */
	public String reckonGongCol(String pei,String gong) {
		String[] gongs = gong.split(",");
		
		StringBuilder gongCol = new StringBuilder();
		
		for (int i = 0; i < gongs.length; i++) {
			String g = gongs[i];
			
			for (int j = 0; j < pei.length(); j++) {
				String jd1 =  g.substring(j*2, j*2+1);
				String jd2 =  g.substring(j*2+1, j*2+2);
				//取对应的生
				Integer jp =  Integer.parseInt(pei.substring(j, j+1));
				
				gongCol.append((jp>2?jd1.equals("老"):jd1.equals("少"))?"2":"1");
				gongCol.append((jp%2==0?jd2.equals("女"):jd2.equals("男"))?"2":"1");			
			}
			gongCol.append(",");
			
			
		}
		
		return gongCol.deleteCharAt(gongCol.length()-1).toString();
		

	}
	
	
	/**
	 * 计算102组统计
	 * @param pCount 一组占两位
	 * @param gongCol 
	 * @return
	 */
	public String reckonCount(String gongCol) {
		
		
		String[] gongCols = gongCol.split(",");
		
		StringBuilder count = new StringBuilder();
		
		for (int i = 0; i < gongCols.length; i++) {
			String gc = gongCols[i];
			int redNum = 0;
			
			for (int j = 0; j < gc.length(); j++) {
				
				Integer jgc =  Integer.parseInt(gc.substring(j, j+1));
				if (jgc==2) redNum++;		
			}
			int jc = redNum*2-gc.length();
			
			count.append(jc+",");
			
			
		}
		
		return count.substring(0, count.length()-1);
		

	}
	
	
	
	public boolean checkPei(String pei) {
		
		if(pei==null||pei.length()!=6)
		return false;
		return true;
	}
	
	
	
	
	
	public static void main(String[] args) {
		/*String ss = "111131,122431";
		String ps = "141141";
		System.out.println(ss);
		System.out.println(ps);
		System.out.println(new GameCoreService().reckonDui(ss, ps));
		
		String sheng = "112431,112431";
		String dui = "211211122121,211211122121";
		System.out.println(new GameCoreService().reckonGong(sheng, dui));*/
		
		/*String gong = "老男少女少女老男少男老男";
		String pei = "411411";
		System.out.println(new GameCoreService().reckonGongCol(pei, gong));
		
		String gongCol = "212111112211,222121222112";
		
		System.out.println(Arrays.toString(new GameCoreService().reckonAllCount("02-2","-404")));*/
		
		
	}
	
}
