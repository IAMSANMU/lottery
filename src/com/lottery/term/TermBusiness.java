package com.lottery.term;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lottery.admin.sys.SysErrorService;
import com.lottery.common.model.LotteryTerm;
import com.lottery.common.utils.DateUtil;
import com.lottery.common.utils.HttpUtil;

public class TermBusiness {
	private static Logger log =Logger.getLogger(TermBusiness.class);
	private static final String W500_TERM_URL = "http://trade.500.com/bjdc/";
	private final static String AIBO_TERM_URL="http://client.aibo123.com/lottery/selling.xml";
	private final static String AIBO_LOT_ID="45";
	private static SysErrorService errorService=new SysErrorService();
	

	public static LotteryTerm snatchAIBOTerm() {
		String xml;
		LotteryTerm term=null;
		String url=AIBO_TERM_URL;
		try {
			xml = HttpUtil.getUrlUtf8(url);
		} catch (Exception e1) {
			log.error("[彩期抓取]抓取aibo彩期数据错误"+e1.getMessage());
			e1.printStackTrace();
			return term;
		}
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
			term.setCreateTime(new Date());
		} catch (Exception e) {
			e.printStackTrace();
			errorService.add("解析[aibo彩期]页面错误", url, "解析[aibo彩期]页面错误,请检查dom结构TermBusiness:snatchAIBOTerm");
		}
		return term;
	}

	
	
	public static LotteryTerm snatch500Term() {
		LotteryTerm term=null;
		String url=W500_TERM_URL;
		String html ;
		try{
			html= HttpUtil.getUrl(url);
		}catch(Exception e){
			log.error("[彩期抓取]抓取彩期数据错误"+e.getMessage());
			e.printStackTrace();
			return term;
		}
		try {
			// 解析html
			Document doc = Jsoup.parse(html);
			// 第一期
			Elements eles = doc.select("#expect_select > option");
			Element prevEle=eles.get(1);//上一期
			//上一期的截止时间 的08:00:00当做开始 时间
			String data = prevEle.val();
			String[] arr = data.split("\\|");
			Date startDate=DateUtil.getDateTimeFormat(arr[1]);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.set(Calendar.HOUR_OF_DAY, 8);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			startDate=calendar.getTime();
			
			Element ele=eles.first();
			
			data = ele.val();
			arr = data.split("\\|");
			term = new LotteryTerm();
			term.setTerm(arr[0]);
			term.setEndTime(DateUtil.getDateTimeFormat(arr[1]));
			term.setCreateTime(new Date());
			term.setStartTime(startDate);
		} catch (Exception e) {
			errorService.add("解析[500彩期]页面错误", url, "解析[500彩期]页面错误,请检查dom结构 TermBusiness:snatch500Term");
			log.error("[彩期抓取]抓取彩期数据错误"+e.getMessage());
			e.printStackTrace();
		}
		return term;

	}

	public static void main(String[] args) {
		TermBusiness.snatch500Term();
	}
}
