package com.park.api.service;

/**
 * 统计服务
 * @author Administrator
 *
 */
public class CountService {

	//每组的户数
	static int HU_NUM = 10;
	
	static int itemSize = 4;
	//默认正负
	static String DEF_ZF = "0";
	
	static int grp_itemSize = 3;
	
	
	
	
	/**
	 * 
	 * @param queue
	 * @param gongCol
	 * @param queueCounts [队列数据，队列统计数据]
	 * @return [队列扫描，总队列统计，组队列统计，老少-男女结果值]
	 */
	public static String[] countQueue(int row,String queue,String queueCounts,String grpQueue
			,String gong,String gongCol,int start,int end) {
		
		Integer[] qcs = parseQueueCounts(queueCounts);
		
		String[] queues = queue.split(",");
		String[] gongCols = gongCol.split(",");
		StringBuilder queueRet = new StringBuilder();
		String[] grpQueues = grpQueue.split(",");
		StringBuilder newGrpQueue = new StringBuilder();
		
		int lsJg = 0;
		int nvJg = 0;
		String[] gongs = gong.split(",");
		
		//循环组
		for (int i = 0; i < queues.length; i++) {
			String q1 = queues[i];
			String col1 = gongCols[i];
			StringBuilder qr = new StringBuilder();
			Integer[] gqs = parseGrpQueue(grpQueues[i]);
			//循环户，户下两列
			for (int j = 0; j < HU_NUM*2; j++) {
				String zf = q1.substring(j*itemSize, j*itemSize+1);
				String col = col1.substring(j, j+1);
				//上一次的列队值
				int pNum = Integer.parseInt(q1.substring(j*itemSize+1, j*itemSize+itemSize));
				
				if(zf.equals(DEF_ZF)) {
					qr.append(createItem(col.equals("1")?"-":"+",1));
					continue;
				}
				
				if(zf.equals(col.equals("1")?"-":"+")) {
					qr.append(createItem(col.equals("1")?"-":"+",pNum+1));
					if(pNum<40) {
						gqs[pNum] +=1;
						qcs[pNum] +=1;}
				}else {
					qr.append(createItem(col.equals("1")?"-":"+",1));
					gqs[0] +=1;
					qcs[0] +=1;
				}
				
				if(row>3) {
					if((j+1)%2==0)
						nvJg+=getJg(pNum, start, end, zf.equals("+"));
					else 
						lsJg+=getJg(pNum, start, end, zf.equals("+"));
					
				}
				
				
			}
			newGrpQueue.append(buildGrpQueue(gqs)+",");
			queueRet.append(qr+",");
			
		}
		
		
		return new String[] {queueRet.substring(0, queueRet.length()-1)
				,buildQueueCounts(qcs)
				,newGrpQueue.substring(0, newGrpQueue.length()-1)
				,row>3?(lsJg+"_"+nvJg):null};

	}
	
	
	/**
	 * 结果值转换
	 * @param length
	 * @param start
	 * @param end
	 * @param isMinus
	 * @return
	 */
	private static int getJg(int length,int start,int end,boolean isMinus) {
		
		if(length>=start&&length<end) {
			int l = (int) (length*Math.pow(2,length-start));
			
			return isMinus?-l:l;
		}
			
		else return 0;
		
		

	}
	
	
	
	private static Integer[] parseQueueCounts(String qcs) {
		
		
		String[] q = qcs.split(",");
		Integer[] ret = new Integer[q.length];
		for (int i = 0; i < q.length; i++) {
			ret[i] = Integer.parseInt(q[i]);
		}
		
		return ret;
	}
	
	private static Integer[] parseGrpQueue(String gq) {
		
		Integer[] ret = new Integer[gq.length()/grp_itemSize];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = Integer.parseInt(gq.substring(i*grp_itemSize, (i+1)*grp_itemSize));
		}
		
		return ret;
	}
	
	private static String buildGrpQueue(Integer[] gqs) {
		
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < gqs.length; i++) {
			if(gqs[i]>=100)ret.append(gqs[i]);
			else if(gqs[i]>=10)ret.append("0"+gqs[i]);
			else ret.append("00"+gqs[i]);
		}
		
		return ret.toString();
	}
	
	
	private static String buildQueueCounts(Integer[] qcs) {
		String ret = "";
		for (int i = 0; i < qcs.length; i++) {
			ret+=qcs[i]+",";
		}
		
		return ret.substring(0,ret.length()-1);

	}
	
	
	
	
	
	private static String createItem(String zf,int num) {
		if(num>=100)return zf+num;
		
		if(num>=10)return zf+"0"+num;
		
		return zf+"00"+num;

	}
	
	
	
}
