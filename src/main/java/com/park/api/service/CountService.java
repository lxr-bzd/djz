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
	 * @return
	 */
	public static String[] countQueue(String queue,String gongCol,String queueCounts,String grpQueue) {
		
		Integer[] qcs = parseQueueCounts(queueCounts);
		
		String[] queues = queue.split(",");
		String[] gongCols = gongCol.split(",");
		String queueRet = "";
		String[] grpQueues = grpQueue.split(",");
		String newGrpQueue = "";
		for (int i = 0; i < queues.length; i++) {
			String q1 = queues[i];
			String col1 = gongCols[i];
			String qr = "";
			Integer[] gqs = parseGrpQueue(grpQueues[i]);
			for (int j = 0; j < HU_NUM*2; j++) {
				String zf = q1.substring(j*itemSize, j*itemSize+1);
				String col = col1.substring(j, j+1);
				if(zf.equals(DEF_ZF)) {
					qr+=createItem(col.equals("1")?"-":"+",1);
					continue;
				}
				//上一次的列队值
				int pNum = Integer.parseInt(q1.substring(j*itemSize+1, j*itemSize+itemSize));
				
				if(zf.equals(col.equals("1")?"-":"+")) {
					qr+=createItem(col.equals("1")?"-":"+",pNum+1);
					if(pNum+1>=6) {
						gqs[pNum+1-6] +=1;
						qcs[pNum+1-6] += 1;}
				}else {
					qr+=createItem(col.equals("1")?"-":"+",1);
				}
				
			}
			newGrpQueue+=buildGrpQueue(gqs)+",";
			queueRet+=qr+",";
			
		}
		
		
		return new String[] {queueRet.substring(0, queueRet.length()-1)
				,buildQueueCounts(qcs)
				,newGrpQueue.substring(0, newGrpQueue.length()-1)};

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
		
		String ret = "";
		for (int i = 0; i < gqs.length; i++) {
			if(gqs[i]>=100)ret+=gqs[i];
			else if(gqs[i]>=10)ret+=("0"+gqs[i]);
			else ret+=("00"+gqs[i]);
		}
		
		return ret;
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
