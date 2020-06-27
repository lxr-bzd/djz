package com.park.api.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BigInputResult {
	
	List<InputResult> results;
	
	long[] hzBg;
	long[] hbBg;
	long[] xzBg;
	long[] jgbgBg;
	
	Integer[] hzqhBg;
	Integer[] hbqhBg;
	Integer[] xzqhBg;
	
	Long hzJg;
	Long hbJg;
	Long xzJg;

	int  tgTrend;

	long[] xbbg;
	long[] xbbgB;
	long[] xbbgC;


	BigDecimal[] bkhz ;
	Integer[] bkhzHu;

    Integer[] bkbg ;
    BigDecimal[] bkzd;
	Integer newBkbgTrend;

	
	public List<InputResult> getResults() {
		return results;
	}

	public void setResults(List<InputResult> results) {
		this.results = results;
	}

	public long[] getHzBg() {
		return hzBg;
	}

	public void setHzBg(long[] hzBg) {
		this.hzBg = hzBg;
	}

	public long[] getHbBg() {
		return hbBg;
	}

	public void setHbBg(long[] hbBg) {
		this.hbBg = hbBg;
	}

	public long[] getXzBg() {
		return xzBg;
	}

	public void setXzBg(long[] xzBg) {
		this.xzBg = xzBg;
	}

	public Long getHzJg() {
		return hzJg;
	}

	public void setHzJg(Long hzJg) {
		this.hzJg = hzJg;
	}

	public Long getHbJg() {
		return hbJg;
	}

	public void setHbJg(Long hbJg) {
		this.hbJg = hbJg;
	}

	public Long getXzJg() {
		return xzJg;
	}

	public void setXzJg(Long xzJg) {
		this.xzJg = xzJg;
	}

	public Integer[] getHzqhBg() {
		return hzqhBg;
	}

	public void setHzqhBg(Integer[] hzqhBg) {
		this.hzqhBg = hzqhBg;
	}

	public Integer[] getHbqhBg() {
		return hbqhBg;
	}

	public void setHbqhBg(Integer[] hbqhBg) {
		this.hbqhBg = hbqhBg;
	}

	public Integer[] getXzqhBg() {
		return xzqhBg;
	}

	public void setXzqhBg(Integer[] xzqhBg) {
		this.xzqhBg = xzqhBg;
	}

	public long[] getJgbgBg() {
		return jgbgBg;
	}

	public void setJgbgBg(long[] jgbgBg) {
		this.jgbgBg = jgbgBg;
	}


	public Integer[] getBkbg() {
		return bkbg;
	}

	public void setBkbg(Integer[] bkbg) {
		this.bkbg = bkbg;
	}

	public BigDecimal[] getBkhz() {
		return bkhz;
	}

	public void setBkhz(BigDecimal[] bkhz) {
		this.bkhz = bkhz;
	}

	public long[] getXbbg() {
		return xbbg;
	}

	public void setXbbg(long[] xbbg) {
		this.xbbg = xbbg;
	}

	public Integer[] getBkhzHu() {
		return bkhzHu;
	}

	public void setBkhzHu(Integer[] bkhzHu) {
		this.bkhzHu = bkhzHu;
	}

    public BigDecimal[] getBkzd() {
        return bkzd;
    }

    public void setBkzd(BigDecimal[] bkzd) {
        this.bkzd = bkzd;
    }

	public Integer getNewBkbgTrend() {
		return newBkbgTrend;
	}

	public void setNewBkbgTrend(Integer newBkbgTrend) {
		this.newBkbgTrend = newBkbgTrend;
	}
}
