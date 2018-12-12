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
			,String gong,String gongCol,int rule_type,int start,int end) {
		
		Integer[] qcs = parseQueueCounts(queueCounts);
		
		String[] queues = queue.split(",");
		String[] gongCols = gongCol.split(",");
		StringBuilder queueRet = new StringBuilder();
		String[] grpQueues = grpQueue.split(",");
		StringBuilder newGrpQueue = new StringBuilder();
		
		int lsJg = 0;
		int nvJg = 0;
		String[] gongs = gong.split(",");
		
		int ys = 0;
		
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
				//计算原始值
				ys=ys+(col.equals("1")?-1:1);
				//上一次的列队值
				int pNum = Integer.parseInt(q1.substring(j*itemSize+1, j*itemSize+itemSize));
				
				String newItem = null;
				if(zf.equals(DEF_ZF)) {
					newItem = createItem(col.equals("1")?"-":"+",1);
					qr.append(newItem);
					
				}else {
					
						if(zf.equals(col.equals("1")?"-":"+")) {
							newItem = createItem(col.equals("1")?"-":"+",pNum+1);
							qr.append(newItem);
							if(pNum<40) {
								gqs[pNum] +=1;
								qcs[pNum] +=1;
								}
						}else {
							newItem =createItem(col.equals("1")?"-":"+",1);
							qr.append(newItem);
							gqs[0] +=1;
							qcs[0] +=1;
						}
					
				}
				
				
				
				if(row>3) {
					if((j+1)%2==0)
						nvJg+=getJg(rule_type,pNum, start, end, newItem.startsWith(zf));
					else 
						lsJg+=getJg(rule_type,pNum, start, end, newItem.startsWith(zf));
					
				}
				
				
			}
			newGrpQueue.append(buildGrpQueue(gqs)+",");
			queueRet.append(qr+",");
			
		}
		
		
		return new String[] {queueRet.substring(0, queueRet.length()-1)
				,buildQueueCounts(qcs)
				,newGrpQueue.substring(0, newGrpQueue.length()-1)
				,row>=3?(lsJg+"_"+nvJg):null
				,ys+""};

	}
	
	
	
	
	
	/**
	 * 结果值转换
	 * @param length
	 * @param start
	 * @param end
	 * @param isMinus
	 * @return
	 */
	private static int getJg(int type,int length,int start,int end,boolean isMinus) {
		
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
	
	
	
	
	
	private static String createItem(String zf,int num) {
		if(num>=100)return zf+num;
		
		if(num>=10)return zf+"0"+num;
		
		return zf+"00"+num;

	}
	
	
	public static Integer[] countTg(String queue,String gong,int rule_type,int start,int end) {
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
		String[] qs = queue.split(",");
		String[] gs = gong.split(",");
		for (int i = 0; i < qs.length; i++) {
			
			for (int j = 0; j < HU_NUM*2; j++) {
				String q1 = qs[i].substring(j*itemSize,j*itemSize+4);
				String g1 = gs[i].substring(j,j+1);
				info[bmap.indexOf(g1+q1.substring(0,1))/2]+=getJg(rule_type,new Integer(q1.substring(1,itemSize)),start,end,false);
			}
			
		}
		
		return info;
		

	}
	
	
	public static Object[] countAllTg(Integer[] allts) {
		
		Integer[] lss = getHz(0,allts[0],allts[1]);
		Integer[] lsj = getHz(2,allts[2],allts[3]);
		Integer[] nvs = getHz(4,allts[4],allts[5]);
		Integer[] nvj = getHz(6,allts[6],allts[7]);
		
		int ls1 = (lss==null?0:((lss[0]+1)%2==0?lss[1]:-lss[1]));
		int ls2 = (lsj==null?0:((lsj[0]+1)%2==0?-lsj[1]:lsj[1]));
		
		int nv1 = (nvs==null?0:((nvs[0]+1)%2==0?nvs[1]:-nvs[1]));
		int nv2 = (nvj==null?0:((nvj[0]+1)%2==0?-nvj[1]:nvj[1]));
		
		
		return new Object[] {lss,lsj,nvs,nvj
				,ls1+ls2,nv1+nv2,allts
				
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
	
	
}
