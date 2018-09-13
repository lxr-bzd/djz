package com.park.api.service;

import java.util.Arrays;

import org.aspectj.weaver.ast.Var;
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
	
	/**
	 * 
	 * @param sheng
	 * @param pei
	 * @return 1:对 ，2：错
	 */
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
	
	/**每一次出现提供报告无论+、-“只限提供2小结”如果第1小结结果是0系统将自动关闭提供第2小结（即本次提供完毕）。
	 * 计算是否提供
	 * @param row
	 * @return
	 */
	public static String[] reckonIsProvide(int row,String summary,String val) {
		
		int summaryNum = row/6;
		if(row%6==0)summaryNum = row/6-1;
		if(summaryNum<2)return new String[]{"-1"+val,"-1"+val,"-1"+val};
		Integer p1 = Integer.parseInt(summary.substring((summaryNum-1)*2, summaryNum*2));
		Integer p2 = Integer.parseInt(summary.substring((summaryNum-1)*2-2, summaryNum*2-2));
		Integer p3 = (summaryNum<3)?null:Integer.parseInt(summary.substring((summaryNum-1)*2-4, summaryNum*2-4));
		Integer p4 = (summaryNum<4)?null:Integer.parseInt(summary.substring((summaryNum-1)*2-6, summaryNum*2-6));
		
		String[] ret = new String[3];
		ret[0] = (p1*p2>0)?"01":"-1";
		ret[1] = (ret[0].equals("01")&&p3!=null&&p1*p3>0)?"01":"-1";
		ret[2] = (ret[1].equals("01")&&p4!=null&&p1*p4>0)?"01":"-1";
		
		ret[0]+=val;
		ret[1]+=val;
		ret[2]+=val;
		
		return ret;
	}
	
	/**每一次出现提供报告无论+、-“只限提供2小结”如果第1小结结果是0系统将自动关闭提供第2小结（即本次提供完毕）。
	 * 计算是否提供
	 * @param 为abc模式+原始模式8位一组合
	 * @param row
	 * @return
	 */
	public static String[] reckonIsProvide2(int row,String count,Integer val) {
		
		int vlen = 8;
		
		int summaryNum = row/6;
		if(row%6==0)summaryNum = row/6-1;
		String valStr = getValStr(val);
		if(summaryNum<2)return new String[]{"-1"+valStr,"-1"+valStr,"-1"+valStr};
		
		summaryNum--;
		int offset = 0;
		Integer p1a = Integer.parseInt(count.substring(summaryNum*vlen+offset, summaryNum*vlen+offset+2));
		Integer p2a = Integer.parseInt(count.substring((summaryNum-1)*vlen+offset, (summaryNum-1)*vlen+offset+2));
		offset = 2;
		Integer p1b = Integer.parseInt(count.substring(summaryNum*vlen+offset, summaryNum*vlen+offset+2));
		Integer p2b = Integer.parseInt(count.substring((summaryNum-1)*vlen+offset, (summaryNum-1)*vlen+offset+2));
		Integer p3b = summaryNum<2?null:Integer.parseInt(count.substring((summaryNum-2)*vlen+offset, (summaryNum-2)*vlen+offset+2));
		offset = 4;
		Integer p1c = Integer.parseInt(count.substring(summaryNum*vlen+offset, summaryNum*vlen+offset+2));
		Integer p2c = Integer.parseInt(count.substring((summaryNum-1)*vlen+offset, (summaryNum-1)*vlen+offset+2));
		Integer p3c = summaryNum<2?null:Integer.parseInt(count.substring((summaryNum-2)*vlen+offset, (summaryNum-2)*vlen+offset+2));
		Integer p4c = summaryNum<3?null:Integer.parseInt(count.substring((summaryNum-3)*vlen+offset, (summaryNum-3)*vlen+offset+2));
		
		String[] ret = new String[3];
		ret[0] = (p1a*p2a>0)?"01":"-1";
		ret[1] = (p3b!=null&&p1b*p2b>0&&p1b*p3b>0)?"01":"-1";
		ret[2] = (p4c!=null&&p1c*p2c>0&&p1c*p3c>0&&p1c*p4c>0)?"01":"-1";
		
		ret[0]+=getValStr((ret[0].equals("01")&&p1a>0)?-val:val);
		ret[1]+=getValStr((ret[1].equals("01")&&p1b>0)?-val:val);
		ret[2]+=getValStr((ret[2].equals("01")&&p1c>0)?-val:val);
		
		return ret;
	}
	
	
	public static String getValStr(Integer val) {
		return (val>=0&&val<10)?("0"+val):val.toString();

	}
	
	
	/**
	 * 计算6连合计
	 * @param gongCol
	 * @return
	 */
	public static Integer reckon6Count(String tg) {
		
		Integer count= 0;
		for (int i = 0; i < 6; i++) {
			
			count+=Integer.parseInt(tg.substring(i*4+2,i*4+4));
			
		}
		
		
		
		return count;
	}
	
	
	
	
	/**
	 * abc模式组合+原始值
	 * @param tg
	 * @param count
	 * @return
	 */
	public static String reckon6Count2(String tg,String count) {
		
		Integer sum= 0;
		for (int i = 0; i < 6; i++) {
			
			sum+=Integer.parseInt(tg.substring(i*2,i*2+2));
			
		}
		String ret = "";
		//计算a模式
		Integer m = mark(count, 2, 0);
		ret+=getValStr((m!=null&&m>0)?-sum:sum);
		//计算b模式
		m = mark(count, 3, 1);
		ret+=getValStr((m!=null&&m>0)?-sum:sum);
		//计算c模式
		m = mark(count, 4, 2);
		ret+=getValStr((m!=null&&m>0)?-sum:sum);
		
		return ret+getValStr(sum);

	}
	
	/**
	 * 从后往前计算相同符号，不相同返回null
	 * @param count
	 * @param mod 0:a模式，1：b:模式，2：c模式
	 * @return 
	 */
	 public static Integer mark(String count,int n,int mod) {
		 //单位count长度
		 int vlen = 8;
		 if(count.length()<n*vlen)
			 return null;
		 int ret = 0;
		 for (int i = 0; i < n; i++) {
			Integer nn = Integer.parseInt(count.substring(count.length()-((i+1)*vlen)+(mod*2), count.length()-((i+1)*vlen)+(mod*2)+2));
			if(i==0) {ret = nn>0?1:-1; continue;}
			if(nn*ret<=0)return null;
		 }
		 
		return ret;

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
		//System.out.println(mark("01-10101-101010101",3,1));
		
		System.out.println(Arrays.toString(reckonIsProvide2(25,"0606060604040404-606060604-40404",-1)));
		
		
	}
	
}
