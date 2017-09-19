package com.lottery.odds.sp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lottery.common.model.DcSpfSp;
import com.lottery.common.utils.HttpUtil;

public class SpBusiness {
	private static final String W500_SP_URL = "http://trade.500.com/bjdc//?expect=";
	private static final String OKOOO_SP_URL = "http://www.okooo.com/Upload/xml/danchang/WDL.xml";
	private static final String OKOOO_DC_URL = "http://www.okooo.com/danchang/";
	private static final String AIBO_SP_URL = "http://trade.aibo123.com/SportsWeb/hpoHandler/GetSp.ashx?lotteryId=44&wareNo=";
	private static final String AIBO_DC_URL = "http://trade.aibo123.com/dc_sfpf/";

	private static Logger log = Logger.getLogger(SpBusiness.class);

	private static String snatchOkCurrentTerm() {
		String term = "";
		try {
			String url = OKOOO_DC_URL;
			String html = HttpUtil.getUrl(url);
			// 解析html
			Document doc = Jsoup.parse(html);
			term = doc.select("#SelectLotteryNo > option[selected]").first().val();
		} catch (Exception e) {
			log.error("[sp抓取]===抓取澳客当前期错误===");
			e.printStackTrace();
		}
		return term;
	}

	public static List<DcSpfSp> snatchOkSp(String dcTerm) throws Exception {
		List<DcSpfSp> list = new ArrayList<DcSpfSp>();
		try {
			String url = OKOOO_SP_URL;
			String currentTerm = snatchOkCurrentTerm();
			if (!currentTerm.equals(dcTerm)) {
				log.error("[sp抓取]===澳客" + currentTerm + "北单当前期与本地" + dcTerm + "不一致===");
			} else {
				String html = HttpUtil.getUrl(url);
				// 解析xml
				Document doc = Jsoup.parse(html);
				Elements rows = doc.select("w");
				for (Element ele : rows) {
					DcSpfSp sp = new DcSpfSp();
					sp.setLineId(ele.attr("n"));
					sp.setTerm(currentTerm);
					sp.setHomeSp(Double.parseDouble(ele.attr("c1")));
					sp.setDrawSp(Double.parseDouble(ele.attr("c3")));
					sp.setGuestSp(Double.parseDouble(ele.attr("c5")));
					sp.setLastUpDate(new Date());
					list.add(sp);
				}
			}
		} catch (Exception e) {
			log.error("[sp抓取]===sp抓取出现错误===");
			e.printStackTrace();
			throw e;
		}
		return list;

	}

	private static String snatchAiboCurrentTerm() {
		String term = "";
		try {
			String url = AIBO_DC_URL;
			String html = HttpUtil.getUrl(url);
			// 解析html
			Document doc = Jsoup.parse(html);
			term = doc.select(".choose .fr .fl").first().text();
			term = 1+term.replaceAll("\\D", "");
		} catch (Exception e) {
			log.error("[sp抓取]===抓取澳客当前期错误===");
			e.printStackTrace();
		}
		return term;
	}

	public static List<DcSpfSp> snatchAiboSP(String dcTerm) throws Exception {
		List<DcSpfSp> list = new ArrayList<DcSpfSp>();
		try {

			String aiboTerm = dcTerm.substring(1);

			String url = AIBO_SP_URL + aiboTerm;
			String currentTerm = snatchAiboCurrentTerm();
			if (!currentTerm.equals(dcTerm)) {
				log.error("[sp抓取]===爱波" + currentTerm + "北单当前期与本地" + dcTerm + "不一致===");
			} else {
				String html = HttpUtil.getUrl(url);
				JSONObject rows = JSONObject.parseObject(html);
				if (rows.getBoolean("Success")) {
					JSONArray jarray = rows.getJSONArray("Data");
					for (Object obj : jarray) {
						JSONObject tmpObj = (JSONObject) obj;
						String lineId = tmpObj.getString("GameNo");
						String[] spArr = tmpObj.getString("Sp").split(",");
						DcSpfSp sp = new DcSpfSp();
						sp.setLineId(lineId);
						sp.setTerm(currentTerm);
						sp.setHomeSp(Double.parseDouble(spArr[0]));
						sp.setDrawSp(Double.parseDouble(spArr[1]));
						sp.setGuestSp(Double.parseDouble(spArr[2]));
						sp.setLastUpDate(new Date());
						list.add(sp);
					}

				} else {
					log.error("[sp抓取]===爱波" + currentTerm + " 数据抓取失败===");
				}
			}
		} catch (Exception e) {
			log.error("[sp抓取]===aiBo sp抓取出现错误===");
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public static void main(String[] args) {
		// String term=snatchOkCurrentTerm();
		// System.out.println("term="+term);
		try {
			List<DcSpfSp> list=snatchAiboSP("170904");
			System.out.println(list.get(0).getSpStr());
		} catch (Exception e) {

		}

	}
}
