package com.park.api.service;

import java.util.HashMap;
import java.util.Map;

import com.park.api.service.GameService.GameEach;

/**
 * 统计服务
 * @author Administrator
 *
 */
public class CountService {

	//每组的户数
	static int HU_NUM = 10;
	
	
	//默认正负
	static String DEF_ZF = "0";
	
	static int grp_itemSize = 3;
	
	static int queueItemSize = 2;
	//队列记录 偏移值
	static int QUEUE_VAL_OFFSET = 500;
	
	
	
	public static CountQueueResult countQueue2(int row,String queue,String queueCounts,String dui,String gong,String gongCol,String nextGong,GameEach[] es) {

		
		int group = GameCoreService2.SHENG_GROUP;
		int groupNum = GameCoreService2.SHENG_GROUP_NUM;
		int queueGroupSize = queueItemSize*20;
		int colGroupSize = 4;
		
		Integer[] qcs = parseQueueCounts(queueCounts);
		StringBuilder newQueue = new StringBuilder();
		
		
		
		int ys = 0;
		
		//循环组
		for (int i = 0; i < group; i++) {
			
			//循环户，户下两列
			int colIndex = i*(colGroupSize+1);
			boolean[] gong1 = GameCoreService2.gongMap[Long.valueOf(gong.substring(colIndex, colIndex+2),36).intValue()];
			boolean[] gong2 = GameCoreService2.gongMap[Long.valueOf(gong.substring(colIndex+2, colIndex+4),36).intValue()];
			
			boolean[] nGong1 = null;
			boolean[] nGong2 = null;
			
			if(nextGong!=null) {
				nGong1 = GameCoreService2.gongMap[Long.valueOf(nextGong.substring(colIndex, colIndex+2),36).intValue()];
				nGong2 = GameCoreService2.gongMap[Long.valueOf(nextGong.substring(colIndex+2, colIndex+4),36).intValue()];
				
			}
			
			boolean[] col1 = GameCoreService2.gongColMap[Long.valueOf(gongCol.substring(colIndex, colIndex+2),36).intValue()];
			boolean[] col2 = GameCoreService2.gongColMap[Long.valueOf(gongCol.substring(colIndex+2, colIndex+4),36).intValue()];
			
			
			
			
			for (int j = 0; j < groupNum*2; j++) {
				int queueIndex = i*(queueGroupSize+1)+(j*queueItemSize);
				
				
				Integer queueVal = null;
				if(queue!=null) {
					queueVal = Long.valueOf(queue.substring(queueIndex, queueIndex+queueItemSize),36).intValue();
				}
				
				boolean gongItem = false;
				Boolean nextGongItem = null;
				boolean col = false;
				if(j<groupNum) {
					col = col1[j];
					gongItem = gong1[j];
					nextGongItem = nGong1[j];
				}else {
					col = col2[j-groupNum];
					gongItem = gong2[j-groupNum];
					nextGongItem = nGong2[j-groupNum];
				}
				//计算原始值
				ys=ys+(col?1:-1);
				//上一次的列队值
				Integer newQueueVal = null;
				if(queueVal==null||((queueVal>QUEUE_VAL_OFFSET?false:true)!=col)) {
					newQueueVal = col?1:QUEUE_VAL_OFFSET+1;
					if(queueVal!=null)qcs[0] +=1;
				}else {
					newQueueVal = queueVal+1;
					int qm = queueVal>QUEUE_VAL_OFFSET?queueVal-QUEUE_VAL_OFFSET:queueVal;
					if(qm<40) qcs[qm] +=1;
										
				}
				
				newQueue.append(createItem(newQueueVal));
				
				
				
				for (GameEach ge : es) {
					if(ge!=null)
					ge.exe(i, j, queueVal, newQueueVal,gongItem,col,nextGongItem);
				}
				
				
			}
			
			newQueue.append(",");
			
		}
		
		CountQueueResult ret = new CountQueueResult();
		ret.setQueue(newQueue.substring(0, newQueue.length()-1));
		ret.setQueueCount(buildQueueCounts(qcs));
		ret.setYs(ys);
		return ret;
		
		

	}
	
	
	/**
	 * 
	 * @param queue
	 * @param gongCol
	 * @param queueCounts [队列数据，队列统计数据]
	 * @return [队列扫描，总队列统计，组队列统计，老少-男女结果值]
	 */
	public static CountQueueResult countQueue(int row,String queue,String queueCounts,String grpQueue
			,String gong,String gongCol,int rule_type,int start,int end,int mod2) {
		
		
		int group = GameCoreService2.SHENG_GROUP;
		int groupNum = GameCoreService2.SHENG_GROUP_NUM;
		int queueGroupSize = queueItemSize*20;
		int colGroupSize = 4;
		
		Integer[] qcs = parseQueueCounts(queueCounts);
		StringBuilder newQueue = new StringBuilder();
		
		int lsJg = 0;
		int nvJg = 0;
		
		int ys = 0;
		
		//循环组
		for (int i = 0; i < group; i++) {
			
			//循环户，户下两列
			int colIndex = i*(colGroupSize+1);
			boolean[] col1 = GameCoreService2.gongColMap[Long.valueOf(gongCol.substring(colIndex, colIndex+2),36).intValue()];
			boolean[] col2 = GameCoreService2.gongColMap[Long.valueOf(gongCol.substring(colIndex+2, colIndex+4),36).intValue()];
			
			
			for (int j = 0; j < groupNum*2; j++) {
				int queueIndex = i*(queueGroupSize+1)+(j*queueItemSize);
				
				
				Integer queueVal = null;
				if(queue!=null) {
					queueVal = Long.valueOf(queue.substring(queueIndex, queueIndex+queueItemSize),36).intValue();
				}
				
				
				boolean col = false;
				if(j<groupNum) {
					col = col1[j];
				}else {
					col = col2[j-groupNum];
				}
				//计算原始值
				ys=ys+(col?1:-1);
				//上一次的列队值
				Integer newQueueVal = null;
				if(queueVal==null||(queueVal>0?true:false==col)) {
					newQueueVal = col?0:QUEUE_VAL_OFFSET+1;
					newQueue.append(createItem(newQueueVal));
					if(queueVal!=null)qcs[0] +=1;
				}else {
					
					newQueueVal = queueVal+1;
					newQueue.append(createItem(newQueueVal));
					if(queueVal<40) {
								qcs[queueVal] +=1;
						}
					
				}
				
				if(row>3) {
					if((j+1)%2==0)
						nvJg+=getJg(rule_type,queueVal, start, end, newQueueVal*queueVal>0);
					else 
						lsJg+=getJg(rule_type,queueVal, start, end, newQueueVal*queueVal>0);
					
				}
				
			}
			newQueue.append(",");
			
		}
		
		
		if(mod2==2) {
			lsJg=-lsJg;
			nvJg=-nvJg;
		}
		
		CountQueueResult ret = new CountQueueResult();
		ret.setQueue(newQueue.substring(0, newQueue.length()-1));
		ret.setQueueCount(buildQueueCounts(qcs));
		ret.setJg(row>=3?(lsJg+"_"+nvJg):null);
		ret.setYs(ys);
		return ret;

	}
	
	
	
	
	
	/**
	 * 结果值转换
	 * @param length
	 * @param start
	 * @param end
	 * @param isMinus
	 * @return
	 */
	public static int getJg(int type,int length,int start,int end,boolean isMinus) {
		
		if(length>=start&&length<end) {
			int l = (int) (Math.pow(2,length-start));
			
			if(type==2)return isMinus?-1:1;
			else
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
	
	
	
	
	
	private static String createItem(int num) {
		String str = null;
		if(num<36) {
			str="0"+Long.toString(num, 36).toUpperCase();
		}else str=Long.toString(num, 36).toUpperCase();
		
		return str;

	}
	
	
	public static Integer[] countTgA(String queue,String gong,int rule_type,int start,int end) {
		
		int group = GameCoreService2.SHENG_GROUP;
		int groupNum = GameCoreService2.SHENG_GROUP_NUM;
		int queueGroupSize = queueItemSize*20;
		int colGroupSize = 4;
		Integer[] info = new Integer[]{
				0,0,
				0,0,
				0,0,
				0,0,
		};
		
		String bmap = "老+少+老-少-男+女+男-女-";
		
		/*'老+':{v:0},'少+':{v:0},
		'老-':{v:0},'少-':{v:0},
		'男+':{v:0},'女+':{v:0},
		'男-':{v:0},'女-':{v:0},*/
		//String[] gs = gong.split(",");
		for (int i = 0; i < group; i++) {
			int colIndex = i*(colGroupSize+1);
			boolean[] gong1 = GameCoreService2.gongMap[Long.valueOf(gong.substring(colIndex, colIndex+2),36).intValue()];
			boolean[] gong2 = GameCoreService2.gongMap[Long.valueOf(gong.substring(colIndex+2, colIndex+4),36).intValue()];
			
			
			for (int j = 0; j < groupNum*2; j++) {
				int queueIndex = i*(queueGroupSize+1)+(j*queueItemSize);
				Integer queueVal = null;
				if(queue!=null) {
					queueVal = Long.valueOf(queue.substring(queueIndex, queueIndex+queueItemSize),36).intValue();
				}
				int index = (j+1)%2==0?4:0;//确定老少/男女区域
				if(queueVal<0)index+=2;//确定 负正区域
				if(j<groupNum) {
					if(gong1[j])index+=1;
					
				}else {
					if(gong2[j-groupNum])index+=1;
				}
				
				info[index]+=getJg(rule_type,Math.abs(queueVal),start,end,false);
			}
			
		}
		
		return info;
		

	}
	
	public static Integer[] countTgB(String queue,String gong,int rule_type,int start,int end) {
		Integer[] info = new Integer[]{
				0,0,
				0,0
		};
		/*
		 * {男，女，老，少}
		 */
		String bmap = "男女老少";
		String[] gs = gong.split(",");
		for (int i = 0; i < gs.length; i++) {
			
			for (int j = 0; j < HU_NUM*2; j++) {
				String g1 = gs[i].substring(j,j+1);
				info[bmap.indexOf(g1)]+=1;
			}
			
		}
		
		return info;
		

	}
	
	/**
	 * 报告求和
	 * @param qh
	 * @return
	 */
	public static Integer[] countQh(Integer[] qh,int mod2) {
		Integer[] qhbg = new Integer[] {qh[0]-qh[1],qh[2]-qh[3]};
		
		return qhbg;
		
	}
	
	/**
	 * 计算求和结果
	 * @param pei
	 * @param qh
	 * @return
	 */
	public static Integer[] countQhJg(String pei,Integer[] upQh) {
		
		Integer[] qhbg = upQh;
		
		Integer pv = Integer.valueOf(pei);
		int jg1 =(pv>2?qhbg[0]:-qhbg[0]);
		int	jg2 =(pv%2!=0?qhbg[1]:-qhbg[1]);
		return new Integer[] {jg1,jg2};
		
	}
	
	
	/**
	 * 提供报告汇总表A模式
	 * @param allts
	 * @return "[[大的下标值,和值],[大的下标值,和值],[大的下标值,和值],[大的下标值,和值],（老男为正数，少女为负数表示报告）,（同上）,原allts]"
	 */
	public static Object[] countAllTgA(Integer[] allts,Integer mod2) {
		
		Integer[] lss = getHz(0,allts[0],allts[1]);
		Integer[] lsj = getHz(2,allts[2],allts[3]);
		Integer[] nvs = getHz(4,allts[4],allts[5]);
		Integer[] nvj = getHz(6,allts[6],allts[7]);
		
		int ls1 = (lss==null?0:((lss[0]+1)%2==0?lss[1]:-lss[1]));
		int ls2 = (lsj==null?0:((lsj[0]+1)%2==0?-lsj[1]:lsj[1]));
		
		int nv1 = (nvs==null?0:((nvs[0]+1)%2==0?nvs[1]:-nvs[1]));
		int nv2 = (nvj==null?0:((nvj[0]+1)%2==0?-nvj[1]:nvj[1]));
		
		if(mod2==1)
			return new Object[] {lss,lsj,nvs,nvj
					,ls1+ls2,nv1+nv2,allts
					
			};
		else 
			return new Object[] {lss,lsj,nvs,nvj
					,-(ls1+ls2),-(nv1+nv2),allts
					
			};
		
		

	}
	/**
	 * 提供报告汇总表B模式
	 * @param allts
	 * @return
	 */
	public static Object[] countAllTgB(Integer[] allts) {
		//{老少男女}
		Integer ls = null;
		Integer nv = null;
		
		ls = allts[0]-allts[1];
		nv = allts[2]-allts[3];
		
		return new Object[] {ls,nv
				,allts
				
		};
		
		

	}
	
	
	/**
	 * 
	 * @param start
	 * @param q1
	 * @param q2
	 * @return [下标，和值]
	 */
	public static Integer[] getHz(int start,int q1,int q2){
		int v = q1-q2;
		
		if(v>0)return new Integer[] {start,v};
		if(v<0)return new Integer[] {start+1,-v};
		
		return null;
		
	}
	
	
	
	
	public static void main(String[] args) {
		
		int row = 1;
		String queue = null;
		String queueCounts="0,200000,200000,200000,200000,200000,200000,200000,200000,200000,200000,200000,200000,200000,200000,200000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
		String grpQueue = null;
		String gong = null;
		String gongCol = null;
		int rule_type = 1;
		int start = 1;
		int end = 40;
		int mod2 = 2;
		for (int i = 0; i < 10000; i++) {
			if(queue==null)
				queue="+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016";
			else queue+=",+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016+016";
			if(gong==null)
				gong="3333333333";
			else gong+=",3333333333";
			if(gongCol==null)
				gongCol="4444444444";
			else gongCol+=",4444444444";
		}
		
		Long s1 = System.currentTimeMillis();
		countQueue(row, queue, queueCounts, grpQueue, gong, gongCol, rule_type, start, end, mod2);
		System.out.println("finish:"+(System.currentTimeMillis()-s1)+"ms");
	}
	
	public static class CountQueueResult{
		
		String queue;
		String queueCount;
		String jg;//lsJg+"_"+nvJg
		//原始值
		int ys;
		public String getQueue() {
			return queue;
		}
		public void setQueue(String queue) {
			this.queue = queue;
		}
		public String getQueueCount() {
			return queueCount;
		}
		public void setQueueCount(String queueCount) {
			this.queueCount = queueCount;
		}
		public String getJg() {
			return jg;
		}
		public void setJg(String jg) {
			this.jg = jg;
		}
		public int getYs() {
			return ys;
		}
		public void setYs(int ys) {
			this.ys = ys;
		}
		
		
		
		
	}
	
	
	
	
	
}
