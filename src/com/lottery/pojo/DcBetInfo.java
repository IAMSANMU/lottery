package com.lottery.pojo;


public class DcBetInfo {
	
	private Double winBetRate;
	private Double drawBetRate;
	private Double loseBetRate;
	
	
	public Double[] getBetRates(){
		Double[] betRates=new Double[4];
		betRates[0]=this.getWinBetRate();
		betRates[1]=this.getDrawBetRate();
		betRates[2]=this.getLoseBetRate();
		return betRates;
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
}
