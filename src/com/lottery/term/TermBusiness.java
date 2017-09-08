package com.lottery.term;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.lottery.common.model.LotteryTerm;
import com.lottery.common.utils.DateUtil;
import com.lottery.common.utils.HttpUtil;

public class TermBusiness {
	private static Logger log =Logger.getLogger(TermBusiness.class);
	private static final String W500_TERM_URL = "http://trade.500.com/bjdc/";
	private final static String AIBO_TERM_URL="http://client.aibo123.com/lottery/selling.xml";
	private final static String AIBO_LOT_ID="45";

	public static LotteryTerm snatchAIBOTerm() {
		String xml = HttpUtil.getUrlUtf8(AIBO_TERM_URL);
		LotteryTerm term=null;
		try {
			Document doc =Jsoup.parse(xml);
			Element ele=doc.select("w[li='"+AIBO_LOT_ID+"']").first();
			String beginTime = ele.attr("bt");
			String endTime = ele.attr("et");// 
			String issue = "1" + ele.attr("is");// 彩期要加1;
			Date beginDate = DateUtil.formatDate(beginTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtil.formatDate(endTime, "yyyy-MM-dd HH:mm");
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			endDate = cal.getTime();
			
			term = new LotteryTerm();
			term.setTerm(issue);
			term.setStartTime(beginDate);
			term.setEndTime(endDate);
			term.setCreatTime(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return term;
	}

	
	
	public static LotteryTerm snatch500Term() {
		LotteryTerm term=null;
		try {
			String html = HttpUtil.getUrl(W500_TERM_URL);
			// 解析html
			Document doc = Jsoup.parse(html);
			// 第一期
			Element ele = doc.select("#expect_select > option[selected]").first();
			String data = ele.val();
			String[] arr = data.split("|");
			term = new LotteryTerm();
			term.setTerm(arr[0]);
			term.setEndTime(DateUtil.getDateFormat(arr[1]));
			term.setCreatTime(new Date());
		} catch (Exception e) {
			log.error("[彩期抓取]抓取彩期数据错误"+e.getMessage());
			e.printStackTrace();
		}
		return term;

	}

	public static void main(String[] args) {
		TermBusiness.snatchAIBOTerm();
	}
}
