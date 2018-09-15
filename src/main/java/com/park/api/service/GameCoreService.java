package com.park.api.service;

import java.util.Arrays;

import org.aspectj.weaver.ast.Var;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.stereotype.Service;

@Service
public class GameCoreService {
	
	
	 static final int COUNT_VLEN = 11;
	
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
	 * @param count 为abc模式+原始模式8位一组合 abc模式加前加一位是否提供（n：未提供，y：提供）
	 * @param row
	 * @return
	 */
	public static String[] reckonIsProvide2(int row,String count,Integer val) {
		
		/*int vlen = COUNT_VLEN;
		
		int summaryNum = row/6;
		if(row%6==0)summaryNum = row/6-1;
		String valStr = getValStr(val);
		if(summaryNum<2)return new String[]{"-1"+valStr+"n","-1"+valStr+"n","-1"+valStr+"n"};
		
		
		
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
		ret[2]+=getValStr((ret[2].equals("01")&&p1c>0)?-val:val);*/
		
		
		String[] ret = new String[3];
		String[] ps = provideSymbol(count);
		
		ret[0] = (ps[0]!=null?"01":"-1")+getValStr((ps[0]!=null&&ps[0].equals("+"))?-val:val);
		ret[1] = (ps[1]!=null?"01":"-1")+getValStr((ps[1]!=null&&ps[1].equals("+"))?-val:val);
		ret[2] = (ps[2]!=null?"01":"-1")+getValStr((ps[2]!=null&&ps[2].equals("+"))?-val:val);
		return ret;
	}
	
	
	public static String getValStr(Integer val) {
		return (val>=0&&val<10)?("0"+val):val.toString();

	}
	
	/**
	 * 返回abc下一小结提供的是否取反 +:取反，-不取反，null：不提供
	 * 例如{y,n,null}
	 * @return
	 */
	public static String[] provideSymbol(String count) {
		
		String[] ret = new String[] {null,null,null};
		
		int countNum = count.length()/COUNT_VLEN;
		
		
		if(countNum<2)return ret;
		
		
		Integer pa1 = Integer.parseInt(count.substring((countNum-1)*COUNT_VLEN+0, (countNum-1)*COUNT_VLEN+0+2));
		Integer pb1 = Integer.parseInt(count.substring((countNum-1)*COUNT_VLEN+3, (countNum-1)*COUNT_VLEN+3+2));
		Integer pc1 = Integer.parseInt(count.substring((countNum-1)*COUNT_VLEN+6, (countNum-1)*COUNT_VLEN+6+2));
		
		
		int offset = 0;
		if(pa1!=0) {
			String aStr1 = count.substring((countNum-1)*COUNT_VLEN+offset, (countNum-1)*COUNT_VLEN+offset+3);
			String aStr2 = count.substring((countNum-2)*COUNT_VLEN+offset, (countNum-2)*COUNT_VLEN+offset+3);
			
			//提供：上一个提供，上上不提供
			if(aStr1.endsWith("y")&&aStr2.endsWith("n")) {
				
				ret[0] = (aStr2.startsWith("-"))?"-":"+";
			}
			//提供：上一个不提供，上上不提供
			if(aStr1.endsWith("n")&&aStr2.endsWith("n")&&aStr1.substring(0,1).equals(aStr2.substring(0,1))) {
				ret[0] = (aStr1.startsWith("-"))?"-":"+";
			}
			
		}
		
		offset = 3;
		if(pb1!=0&&countNum>=3) {
			String aStr1 = count.substring((countNum-1)*COUNT_VLEN+offset, (countNum-1)*COUNT_VLEN+offset+3);
			String aStr2 = count.substring((countNum-2)*COUNT_VLEN+offset, (countNum-2)*COUNT_VLEN+offset+3);
			String aStr3 = count.substring((countNum-3)*COUNT_VLEN+offset, (countNum-3)*COUNT_VLEN+offset+3);
			
			//提供：上一个提供，上上不提供
			if(aStr1.endsWith("y")&&aStr2.endsWith("n")) {
				
				ret[1] = (aStr2.startsWith("-"))?"-":"+";
			}
			//提供：上一个不提供，上上不提供,上上上不提供
			if(aStr1.endsWith("n")&&aStr2.endsWith("n")&&aStr3.endsWith("n")
					&&aStr1.substring(0,1).equals(aStr2.substring(0,1))&&aStr1.substring(0,1).equals(aStr3.substring(0,1))) {
				ret[1] = (aStr1.startsWith("-"))?"-":"+";
			}
			
		}
		
		offset = 6;
		if(pb1!=0&&countNum>=4) {
			String aStr1 = count.substring((countNum-1)*COUNT_VLEN+offset, (countNum-1)*COUNT_VLEN+offset+3);
			String aStr2 = count.substring((countNum-2)*COUNT_VLEN+offset, (countNum-2)*COUNT_VLEN+offset+3);
			String aStr3 = count.substring((countNum-3)*COUNT_VLEN+offset, (countNum-3)*COUNT_VLEN+offset+3);
			String aStr4 = count.substring((countNum-4)*COUNT_VLEN+offset, (countNum-4)*COUNT_VLEN+offset+3);
			
			//提供：上一个提供，上上不提供
			if(aStr1.endsWith("y")&&aStr2.endsWith("n")) {
				
				ret[2] = (aStr2.startsWith("-"))?"-":"+";
			}
			//提供：上一个不提供，上上不提供,上上上不提供
			if(aStr1.endsWith("n")&&aStr2.endsWith("n")&&aStr3.endsWith("n")&&aStr4.endsWith("n")
					&&aStr1.substring(0,1).equals(aStr2.substring(0,1))&&aStr1.substring(0,1).equals(aStr3.substring(0,1))&&aStr1.substring(0,1).equals(aStr4.substring(0,1))) {
				ret[2] = (aStr1.startsWith("-"))?"-":"+";
			}
			
		}
		
		
		
		return ret;

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
		
		
		
		/*String ret = "";
		//计算a模式
		Integer m = mark(count, 2, 0);
		ret+=getValStr((m!=null&&m>0)?-sum:sum);
		//计算b模式
		m = mark(count, 3, 1);
		ret+=getValStr((m!=null&&m>0)?-sum:sum);
		//计算c模式
		m = mark(count, 4, 2);
		ret+=getValStr((m!=null&&m>0)?-sum:sum);
		
		return ret+getValStr(sum);*/
		
		String[] ps = provideSymbol(count);
		String ret = "";
		ret+=getValStr((ps[0]!=null&&ps[0].equals("+"))?-sum:sum)+(ps[0]!=null?"y":"n");
		ret+=getValStr((ps[1]!=null&&ps[1].equals("+"))?-sum:sum)+(ps[1]!=null?"y":"n");
		ret+=getValStr((ps[2]!=null&&ps[2].equals("+"))?-sum:sum)+(ps[2]!=null?"y":"n");
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
		
		System.out.println(Arrays.toString(provideSymbol("01n01n01n01,-1n01n01n01,-1y01n01n01".replaceAll(",", ""))));
		
		
	}
	
}
