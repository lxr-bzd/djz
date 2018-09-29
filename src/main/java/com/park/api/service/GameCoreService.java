package com.park.api.service;

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
	 * @param count 为abc模式+原始模式8位一组合 abc模式加前加一位是否提供（n：未提供，y：提供）(a值+开启提供+是否提供)
	 * @param row
	 * @return
	 */
	public static String[] reckonIsProvide2(int row,String count,Integer val,String upPile) {
		
		String[] ret = new String[3];
		String[] ps = provideSymbol2(count,upPile);
		
		ret[0] = (ps[0]!=null?"01":"-1")+getValStr((ps[0]!=null&&ps[0].equals("+"))?-val:val);
		ret[1] = (ps[1]!=null?"01":"-1")+getValStr((ps[1]!=null&&ps[1].equals("+"))?-val:val);
		ret[2] = (ps[2]!=null?"01":"-1")+getValStr((ps[2]!=null&&ps[2].equals("+"))?-val:val);
		return ret;
	}
	
	
	public static String getValStr(Integer val) {
		return (val>=0&&val<10)?("0"+val):val.toString();
		
	}
	
	/**
	 * 只记录正负域，0域不记录
	 * @param upPile a'+-'+数量,b
	 * @param abcVal
	 * @return
	 */
	public static String reckonPile(String upPile,Integer[] abcVal) {
		
		String[] nf = new String[3];
		nf[0] = (abcVal[0]>0?"+":(abcVal[0]<0?"-":"0"));
		nf[1] = (abcVal[1]>0?"+":(abcVal[1]<0?"-":"0"));
		nf[2] = (abcVal[2]>0?"+":(abcVal[2]<0?"-":"0"));
		
		if(upPile==null) return nf[0]+"1,"+nf[1]+"1,"+nf[2]+"1";
		
		String[] upPiles =  upPile.split(",");
		String ret = "";
		
		ret+=nf[0]+(nf[0].equals(upPiles[0].substring(0, 1))?(1+new Integer(upPiles[0].substring(1, upPiles[0].length()))):"1")+",";
		ret+=nf[1]+(nf[1].equals(upPiles[1].substring(0, 1))?(1+new Integer(upPiles[1].substring(1, upPiles[1].length()))):"1")+",";
		ret+=nf[2]+(nf[2].equals(upPiles[2].substring(0, 1))?(1+new Integer(upPiles[2].substring(1, upPiles[2].length()))):"1");
		
		return ret;

	}
	
	public static String reckonPile2(String upPile,Integer[] abcVal) {
		
		String nf = (abcVal[3]>0?"+":(abcVal[3]<0?"-":"0"));
		
		if(upPile==null)return nf+"1";
		
		String ret = "";
		
		ret+=nf+(nf.equals(upPile.substring(0, 1))?(1+new Integer(upPile.substring(1, upPile.length()))):"1");
		
		return ret;
	}
	
	/**
	 * 返回abc下一小结提供的是否取反 +:取反，-不取反，null：不提供
	 * 例如{y,n,null}
	 * @return
	 */
	public static String[] provideSymbol2(String count,String upPile) {
		
		String[] ret = new String[] {null,null,null};
		
		int countNum = count.length()/COUNT_VLEN;
		
		if(countNum<2)return ret;
		
		Integer p1 = Integer.parseInt(count.substring((countNum-1)*COUNT_VLEN+9, (countNum-1)*COUNT_VLEN+9+2));
		Integer p2 = Integer.parseInt(count.substring((countNum-2)*COUNT_VLEN+9, (countNum-2)*COUNT_VLEN+9+2));
		Integer p3 = countNum>=3?Integer.parseInt(count.substring((countNum-3)*COUNT_VLEN+9, (countNum-3)*COUNT_VLEN+9+2)):null;
		Integer p4 = countNum>=4?Integer.parseInt(count.substring((countNum-4)*COUNT_VLEN+9, (countNum-4)*COUNT_VLEN+9+2)):null;
		
		int offset = 0;
		if(true) {
			String aStr1 = count.substring((countNum-1)*COUNT_VLEN+offset, (countNum-1)*COUNT_VLEN+offset+3);
			String aStr2 = count.substring((countNum-2)*COUNT_VLEN+offset, (countNum-2)*COUNT_VLEN+offset+3);
			String aStr3 = countNum>=3?count.substring((countNum-3)*COUNT_VLEN+offset, (countNum-3)*COUNT_VLEN+offset+3):null;
			//提供：上一个提供，上上不提供
			if(aStr1.endsWith("y")&&(aStr2.endsWith("n")||(new Integer(upPile.substring(1, upPile.length()))<4&&aStr3!=null&&aStr3.endsWith("y")))) {
				ret[0] = (p2<0)?"-":"+";
				
			}
			//提供：提供者不超过2个，本身两个，不会超过
			else if(new Integer(upPile.substring(1, upPile.length()))<4&&
					( p1 * p2 >0)) {
				ret[0] = (p1<0)?"-":"+";
			}
		}
		
		offset = 3;
		if(countNum>=3) {
			String aStr1 = count.substring((countNum-1)*COUNT_VLEN+offset, (countNum-1)*COUNT_VLEN+offset+3);
			String aStr2 = count.substring((countNum-2)*COUNT_VLEN+offset, (countNum-2)*COUNT_VLEN+offset+3);
			String aStr3 = count.substring((countNum-3)*COUNT_VLEN+offset, (countNum-3)*COUNT_VLEN+offset+3);
			
			//提供：上一个提供，上上不提供
			if(aStr1.endsWith("y")&&aStr2.endsWith("n")) {
				ret[1] = (p2<0)?"-":"+";
			}
			//提供：举出反例子
			else if(new Integer(upPile.substring(1, upPile.length()))<6
					&&( p1 * p2 >0)
					&&( p1 * p3 >0)
					&&!(aStr1.endsWith("y")&&aStr3.endsWith("n"))) {
				ret[1] = (p1<0)?"-":"+";
			}
			
		}
		
		offset = 6;
		if(countNum>=4) {
			String aStr1 = count.substring((countNum-1)*COUNT_VLEN+offset, (countNum-1)*COUNT_VLEN+offset+3);
			String aStr2 = count.substring((countNum-2)*COUNT_VLEN+offset, (countNum-2)*COUNT_VLEN+offset+3);
			String aStr3 = count.substring((countNum-3)*COUNT_VLEN+offset, (countNum-3)*COUNT_VLEN+offset+3);
			String aStr4 = count.substring((countNum-4)*COUNT_VLEN+offset, (countNum-4)*COUNT_VLEN+offset+3);
			
			//提供：上一个提供，上上不提供
			if(aStr1.endsWith("y")&&aStr2.endsWith("n")) {
				
				ret[2] = (p2<0)?"-":"+";
			}
			//提供：上一个不提供，上上不提供,上上上不提供
			else if(new Integer(upPile.substring(1, upPile.length()))<8
					&&( p1 * p2 >0)
					&&( p1 * p3 >0)
					&&( p1 * p4 >0)
					&&!(aStr4.endsWith("n")&&((aStr1.endsWith("y")?1:0)+(aStr2.endsWith("y")?1:0)+(aStr3.endsWith("y")?1:0))>=2)) {
				ret[2] = (p1<0)?"-":"+";
			}
			
		}
		
		
		
		return ret;

	}
	
	/**
	 * 返回abc下一小结提供的是否取反 +:取反，-不取反，null：不提供
	 * 例如{y,n,null}
	 * @return
	 */
	public static String[] provideSymbol3(String count,String upPile) {
		
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
			//提供：提供者不超过2个，本身两个，不会超过
			if(new Integer(upPile.substring(1, upPile.length()))<4&&
					( Integer.parseInt(aStr1.substring(0,2)) * Integer.parseInt(aStr2.substring(0,2)) >0)) {
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
			//提供：举出反例子
			if(new Integer(upPile.substring(1, upPile.length()))<6
					&&( Integer.parseInt(aStr1.substring(0,2)) * Integer.parseInt(aStr2.substring(0,2)) >0)
					&&( Integer.parseInt(aStr1.substring(0,2)) * Integer.parseInt(aStr3.substring(0,2)) >0)
					&&!(aStr1.endsWith("y")&&aStr3.endsWith("n"))) {
				ret[1] = (aStr1.startsWith("-"))?"-":"+";
			}
			
		}
		
		offset = 6;
		if(pc1!=0&&countNum>=4) {
			String aStr1 = count.substring((countNum-1)*COUNT_VLEN+offset, (countNum-1)*COUNT_VLEN+offset+3);
			String aStr2 = count.substring((countNum-2)*COUNT_VLEN+offset, (countNum-2)*COUNT_VLEN+offset+3);
			String aStr3 = count.substring((countNum-3)*COUNT_VLEN+offset, (countNum-3)*COUNT_VLEN+offset+3);
			String aStr4 = count.substring((countNum-4)*COUNT_VLEN+offset, (countNum-4)*COUNT_VLEN+offset+3);
			
			//提供：上一个提供，上上不提供
			if(aStr1.endsWith("y")&&aStr2.endsWith("n")) {
				
				ret[2] = (aStr2.startsWith("-"))?"-":"+";
			}
			//提供：上一个不提供，上上不提供,上上上不提供
			if(new Integer(upPile.substring(1, upPile.length()))<8
					&&( Integer.parseInt(aStr1.substring(0,2)) * Integer.parseInt(aStr2.substring(0,2)) >0)
					&&( Integer.parseInt(aStr1.substring(0,2)) * Integer.parseInt(aStr3.substring(0,2)) >0)
					&&( Integer.parseInt(aStr1.substring(0,2)) * Integer.parseInt(aStr4.substring(0,2)) >0)
					&&!(aStr4.endsWith("n")&&((aStr1.endsWith("y")?1:0)+(aStr2.endsWith("y")?1:0)+(aStr3.endsWith("y")?1:0))>=2)) {
				ret[2] = (aStr1.startsWith("-"))?"-":"+";
			}
			
		}
		
		
		
		return ret;

	}
	
	
	
	
	/**
	 * abc模式组合(模式值+yn是否提供)+原始值
	 * @param tg
	 * @param count
	 * @return 【count值,abc模式值】
	 * 
	 */ 
	public static Object[] reckon6Count2(String tg,String count,String upPile) {
		
		Integer sum= 0;
		for (int i = 0; i < 6; i++) {
			sum+=Integer.parseInt(tg.substring(i*2,i*2+2));
		}
		
		String[] ps = provideSymbol2(count,upPile);
		Integer[] abcVal = new Integer[] {
				((ps[0]!=null&&ps[0].equals("+"))?-sum:sum),
				((ps[1]!=null&&ps[1].equals("+"))?-sum:sum),
				((ps[2]!=null&&ps[2].equals("+"))?-sum:sum),
				sum
		};
		String ret = "";
		ret+=getValStr(abcVal[0])+(ps[0]!=null?"y":"n");
		ret+=getValStr(abcVal[1])+(ps[1]!=null?"y":"n");
		ret+=getValStr(abcVal[2])+(ps[2]!=null?"y":"n");
		
		
		
		return new Object[] {ret+getValStr(sum),abcVal};
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
		
		//System.out.println(Arrays.toString(provideSymbol("01n01n01n01,-1n01n01n01,-1y01n01n01".replaceAll(",", ""))));
		System.out.println(reckonPile("+1,+1,-1",new Integer[] {1,1,0}));
		
	}
	
}
