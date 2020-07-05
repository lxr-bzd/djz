package com.park.api.entity;

import com.park.api.service.bean.BigTurnConfig;
import lombok.Data;

@Data
public class BigTurn {
	
	Integer id;
	Integer frow;
	
	String bg;
	String gj;
	
	String zdbg;
	String zdbg_jg;
	Long zdbg_sum;
	Long zdbg_jg_sum;
	String zdbg_lock;

	String zdqh;
	String zdqh_jg;
	Long zdqh_sum;
	Long zdqh_jg_sum;
	String zdqh_lock;


/*	String bga;
	String bga_jg;
	Long bga_sum;
	String bga_last_jg;
	Long bga_jg_sum;

	String bgb;
	String bgb_jg;
	Long bgb_sum;
	String bgb_last_jg;
	Long bgb_jg_sum;*/
	
	Integer state;
	String config_json;


	String tg_trends;

	String jgzd_lock;

	String jgzd;
	String jgzd_jg;
	Long jgzd_sum;
	Long jgzd_jg_sum;

	String inverse_lock;

	String xb_inv_lock;
	String xb_lock;


	String bkbg;
	String bkbg_jg;
	Long bkbg_sum;
	Long bkbg_jg_sum;

	String bkbg_trend;

	String bkbgs;
	String bkbgs_sum;
	String bkbgs_jg_sum;
	String bkbgs_inv_lock;


	String bkzd;
	String bkzd_jg;
	Long bkzd_sum;
	Long bkzd_jg_sum;


	String bkhz;
	String bkhz_jg;
	Long bkhz_sum;
	Long bkhz_jg_sum;
	String bkhz_lock;

	String bkqh;
	String bkqh_jg;
	Long bkqh_sum;
	Long bkqh_jg_sum;

	String bkzd_lock;

	String turn_bkhz_lock;

	String bkbg_inv_lock;

	String bkhz_inv_lock;

	String bkbgs_lock;
	String bkbgs_zd_lock;
	String bkbgs_qh_lock;

	BigTurnConfig bigTurnConfig;


}
