package com.lottery.common.utils;

import java.util.HashMap;
import java.util.Map;

public class YpPKGainScale {

	/**
	 * @param args
	 */
	public static final Map<String, Double> asiaPkMap = new HashMap<String, Double>();
	// 投注比计算上盘系数
	public static final Map<Double, Double> upCoefficientMap = new HashMap<Double, Double>();
	// 投注比计算下盘系数
	public static final Map<Double, Double> dowmCoefficientMap = new HashMap<Double, Double>();
	// 上盘默认系数
	public static final Double UP_DEFAULR = 0.6;
	// 下盘默认系数
	public static final Double DOWM_DEFAULR = 1d;
	static {
		asiaPkMap.put("平手", 0.0);
		asiaPkMap.put("半球", 0.5);
		asiaPkMap.put("一球", 1.0);
		asiaPkMap.put("球半", 1.5);
		asiaPkMap.put("两球", 2.0);
		asiaPkMap.put("两球半", 2.5);
		asiaPkMap.put("三球", 3.0);
		asiaPkMap.put("三球半", 3.5);
		asiaPkMap.put("四球", 4.0);
		asiaPkMap.put("四球半", 4.5);
		asiaPkMap.put("五球", 5.0);
		asiaPkMap.put("五球半", 5.5);
		asiaPkMap.put("六球", 6.0);
		asiaPkMap.put("六球半", 6.5);

		upCoefficientMap.put(0d, 1d);
		upCoefficientMap.put(0.25d, 2d);
		upCoefficientMap.put(0.5d, 1d);
		upCoefficientMap.put(0.75d, 0.9d);
		upCoefficientMap.put(1d, 0.8d);

		dowmCoefficientMap.put(0d, 1d);
		dowmCoefficientMap.put(0.25d, 2d);
		dowmCoefficientMap.put(0.5d, 1d);
		dowmCoefficientMap.put(0.75d, 1d);
		dowmCoefficientMap.put(1d, 1d);
	}

	public static void main(String[] args) {
	}

	/**
	 * 新版亚盘盈利比计算公式
	 * 
	 * @param betScale
	 *            投注比 betScale[0](win); betScale[1](tie); betScale[2](lose)
	 * @param waterLevel
	 *            盘口水位 waterLevel[0](host team); waterLevel[1](away team)
	 * @param concede 让球
	 * @param pankou 盘口值
	 * @return
	 */
	public static Double[] getGainScale(Double[] dcBetScale, Double[] waterLevel, int concede, Double pankou) {
		Double[] result = new Double[2];
		Double[] myBetScale = getNewBetPercent(dcBetScale, concede, pankou);
		if (myBetScale[0] == null || myBetScale[1] == null) {
			result[0] = null;
			result[1] = null;
		} else {
			result[0] = myBetScale[1] - (myBetScale[0] * waterLevel[0]);
			result[1] = myBetScale[0] - (myBetScale[1] * waterLevel[1]);
		}
		return result;
	}

	public static Double[] getNewBetPercent(Double[] betScale, int concede, Double pkInfo) {
		Double matchHomeBetPercent = betScale[0];
		Double matchDrawBetPercent = betScale[1];
		Double matchGuestBetPercent = betScale[2];
		Double numberPkValue = pkInfo;
		double temp = numberPkValue - concede;
		Double upCoefficient = upCoefficientMap.get(Math.abs(temp));
		Double dowmCoefficient = dowmCoefficientMap.get(Math.abs(temp));
		if (upCoefficient == null || dowmCoefficient == null) {
			upCoefficient = UP_DEFAULR;
			dowmCoefficient = DOWM_DEFAULR;
		}
		double homePercent = 0d;
		double guestPercent = 0d;
		if (temp > 0) {
			homePercent += matchHomeBetPercent * dowmCoefficient + matchDrawBetPercent;
			guestPercent += matchGuestBetPercent * upCoefficient;
		} else if (temp < 0) {
			homePercent += matchHomeBetPercent * upCoefficient;
			guestPercent += matchGuestBetPercent * dowmCoefficient + matchDrawBetPercent;
		} else {
			homePercent += matchHomeBetPercent;
			guestPercent += matchGuestBetPercent;
		}
		double sum = homePercent + guestPercent;
		return new Double[] { homePercent / sum, guestPercent / sum };
	}

	public static Double getAsiaPkValue(String pk) {
		if (pk == null || pk.equals("")) {
			return null;
		}
		pk = pk.replaceAll(" ", "");
		Double pkValue = null;
		boolean isPositive = !pk.startsWith("受");
		pk = pk.replaceAll("受让|受", "");
		String[] pkArr = pk.split("\\/");
		pkValue = asiaPkMap.get(pkArr[0]);
		if (pkArr.length > 1) {
			if (pkValue == null) {
				System.out.println(pk);
			} else {
				pkValue += 0.25;
			}
		}
		if (isPositive && pkValue != null) {
			pkValue *= -1;
		}
		return pkValue;
	}

}
