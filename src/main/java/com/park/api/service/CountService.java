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
	
	/**
	 * 
	 * @param queue
	 * @param gongCol
	 * @param queueCounts [队列数据，队列统计数据]
	 * @return
	 */
	public static String[] countQueue(String queue,String gongCol,String queueCounts) {
		
		Integer[] qcs = parseQueueCounts(queueCounts);
		
		String[] queues = queue.split(",");
		String[] gongCols = gongCol.split(",");
		String queueRet = "";
		for (int i = 0; i < queues.length; i++) {
			String q1 = queues[i];
			String col1 = gongCols[i];
			String ret = "";
			for (int j = 0; j < HU_NUM*2; j++) {
				String zf = q1.substring(j*itemSize, j*itemSize+1);
				String col = col1.substring(j, j+1);
				if(zf.equals(DEF_ZF)) {
					ret+=createItem(col.equals("1")?"-":"+",1);
					continue;
				}
				//上一次的列队值
				int pNum = Integer.parseInt(q1.substring(j*itemSize+1, j*itemSize+itemSize));
				
				if(zf.equals(col.equals("1")?"-":"+")) {
					ret+=createItem(col.equals("1")?"-":"+",pNum+1);
					if(pNum+1>=6)qcs[pNum+1-6] += 1;
				}else {
					ret+=createItem(col.equals("1")?"-":"+",1);
				}
				
			}
			queueRet+=ret+",";
			
		}
		
		
		return new String[] {queueRet.substring(0, queueRet.length()-1),buildQueueCounts(qcs)};

	}
	
	
	private static Integer[] parseQueueCounts(String qcs) {
		
		
		String[] q = qcs.split(",");
		Integer[] ret = new Integer[q.length];
		for (int i = 0; i < q.length; i++) {
			ret[i] = Integer.parseInt(q[i]);
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
