package com.lxr.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * @类名 DataUtil
 * @描述 参数常量
 * @创建者 wayne
 * @时间 2015-03-15
 */
public class DataUtil 
{
	public static final Object NULL = null;
	public static final Map<String, String> NULL_CONDITION = null;
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	public static final int SIX = 6;
	public static final int SEVEN = 7;
	public static final int EIGHT = 8;
	public static final int NINE = 9;
	public static final int TEN = 10;
	public static final int ELEVEN = 11;
	public static final int TWELVE = 12;
	public static final int DEFULT_PAGE_ROWS = 10; // 页面默认显示行数
	public static final long MAX_IMAGE_SIZE = 1048576; // 上传最大图片大小1M
	
	public static final String EMPTY = ""; // 空字符
	public static final String BLANK = " "; // 空格
	public static final String SEMICOLON = ";"; // 分号
	public static final String COMMA = ","; // 逗号
	public static final String COLON = ":"; // 冒号
	public static final String SINGLE_QUOTES = "'"; // 单引号
	public static final String SLASH = "/"; // 斜杠
	public static final String SEPARATOR = "-"; // 横杠
	public static final String UNDERLINE = "_"; // 下划线
	public static final String DOT = "."; // 点
	
	public static final String UTF8 = "UTF-8";
	public static final String PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	
	public static final String CURRENT_ROW = "currentRow";
	public static final String PAGE_ROWS = "pageRows";
	public static final String ORDER_FIELD = "orderField";
	
	public static final String USER = "sessionUser";
	public static final String STATUS = "state";
	public static final String MESSAGE = "message";
	public static final String TOKEN = "token";
    
    private static Map<Integer, String> accountTypeMap; // 钱包类型 1-金额 2-月卡 3-各种优惠券 4-赠送金额
    private static Map<Integer, String> bankTypeMap; // 充值银行 1-支付宝 2-微信支付 3-银行卡支付 4-洪城一卡通
    private static Map<Integer, String> applyTypeMap; // 停车订单支付类型 1-缴费 2-续费 3-后付费结算
    private static Map<Integer, String> orderStatusMap; // 停车订单状态 1-待付款  2-停车中 3-待补缴 4-已结算 5-已关闭
    
    /**
	 * 钱包类型
	 */
	public static Map<Integer, String> getAccountTypeMap()
	{
		if (NULL == accountTypeMap)
		{
			accountTypeMap = new HashMap<Integer, String>();
			accountTypeMap.put(ONE, "金额");
			accountTypeMap.put(TWO, "月卡");
			accountTypeMap.put(THREE, "优惠券");
			accountTypeMap.put(FOUR, "赠送金额");
		}
		return accountTypeMap;
	}
	
	/**
	 * 银行类型
	 */
	public static Map<Integer, String> getBankTypeMap()
	{
		if (NULL == bankTypeMap)
		{
			bankTypeMap = new HashMap<Integer, String>();
			bankTypeMap.put(ONE, "支付宝");
			bankTypeMap.put(TWO, "微信");
			bankTypeMap.put(THREE, "银行卡");
			bankTypeMap.put(FOUR, "洪城一卡通");
		}
		return bankTypeMap;
	}
	
	/**
	 * 停车订单状态
	 */
	public static Map<Integer, String> getOrderStatusMap()
	{
		if (NULL == orderStatusMap)
		{
			orderStatusMap = new HashMap<Integer, String>();
			orderStatusMap.put(ONE, "待付款");
			orderStatusMap.put(TWO, "停车中");
			orderStatusMap.put(THREE, "待补缴");
			orderStatusMap.put(FOUR, "已结算");
			orderStatusMap.put(FIVE, "已关闭");
		}
		return orderStatusMap;
	}
	
	/**
	 * 停车订单支付类型
	 */
	public static Map<Integer, String> getApplyTypeMap()
	{
		if (NULL == applyTypeMap)
		{
			applyTypeMap = new HashMap<Integer, String>();
			applyTypeMap.put(ONE, "缴费");
			applyTypeMap.put(TWO, "续费");
			applyTypeMap.put(THREE, "后付费结算");
			applyTypeMap.put(FOUR, "退款");
		}
		return applyTypeMap;
	}
}
