package com.lottery.pojo;

import java.util.Date;

public class DcBetInfo {
	private Integer id;
	private Integer dcId;
	private String term;
	private String boutIndex;
	private Long winBetCount;
	private Long drawBetCount;
	private Long loseBetCount;
	private Double winBetRate;
	private Double drawBetRate;
	private Double loseBetRate;
	private Double matchBetRate;
	private String oddsId;
	private Date time;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDcId() {
		return dcId;
	}
	public void setDcId(Integer dcId) {
		this.dcId = dcId;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getBoutIndex() {
		return boutIndex;
	}
	public void setBoutIndex(String boutIndex) {
		this.boutIndex = boutIndex;
	}
	public Long getWinBetCount() {
		return winBetCount;
	}
	public void setWinBetCount(Long winBetCount) {
		this.winBetCount = winBetCount;
	}
	public Long getDrawBetCount() {
		return drawBetCount;
	}
	public void setDrawBetCount(Long drawBetCount) {
		this.drawBetCount = drawBetCount;
	}
	public Long getLoseBetCount() {
		return loseBetCount;
	}
	public void setLoseBetCount(Long loseBetCount) {
		this.loseBetCount = loseBetCount;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Double getWinBetRate() {
		return winBetRate;
	}
	public void setWinBetRate(Double winBetRate) {
		this.winBetRate = winBetRate;
	}
	public Double getDrawBetRate() {
		return drawBetRate;
	}
	public void setDrawBetRate(Double drawBetRate) {
		this.drawBetRate = drawBetRate;
	}
	public Double getLoseBetRate() {
		return loseBetRate;
	}
	public void setLoseBetRate(Double loseBetRate) {
		this.loseBetRate = loseBetRate;
	}
	public Double getMatchBetRate() {
		return matchBetRate;
	}
	public void setMatchBetRate(Double matchBetRate) {
		this.matchBetRate = matchBetRate;
	}
	public String getOddsId() {
		return oddsId;
	}
	public void setOddsId(String oddsId) {
		this.oddsId = oddsId;
	}
	public Integer[] getBetCounts(){
		Integer[] betCounts=new Integer[3];
		betCounts[0]=this.getWinBetCount().intValue();
		betCounts[1]=this.getDrawBetCount().intValue();
		betCounts[2]=this.getLoseBetCount().intValue();
		return betCounts;
	}
	public Double[] getBetRates(){
		Double[] betRates=new Double[4];
		betRates[0]=this.getWinBetRate();
		betRates[1]=this.getDrawBetRate();
		betRates[2]=this.getLoseBetRate();
		betRates[3]=this.getMatchBetRate();
		return betRates;
	}
}
