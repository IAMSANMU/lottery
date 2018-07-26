package com.lottery.odds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.lottery.common.utils.HttpUtil;

public class OddsBusiness {
	private static Logger log=Logger.getLogger(OddsBusiness.class);
	
	public static final Map<String, String> COMAPNY=new HashMap<String, String>();
	public static final List<String> COMAPNYLIST=new ArrayList<String>();
	public static final Map<String,String> YP_WBW2CLLCOMPANY=new HashMap<String, String>();
	public static final Map<String,String> DXP_WBW2CLLCOMPANY=new HashMap<String, String>();
	static{
//		COMAPNY.put("永利高","yongligao");
		COMAPNY.put("皇冠","huangguan");
//		COMAPNY.put( "伟德","weide");
//		COMAPNY.put( "立博","libo");
//		COMAPNY.put("澳门", "aomen");
//		COMAPNY.put("bet365","ribo");
//		COMAPNY.put("易胜博","yisheng");
//		COMAPNY.put("明陞","mingsheng");
//		COMAPNY.put("利记","liji");
//		COMAPNY.put("nikebet","nikebet");
//		COMAPNY.put("沙巴","shaba");
		DXP_WBW2CLLCOMPANY.put("皇冠", "皇冠");
		//所有博彩公司都当做皇冠, 数据库只记录一种公司.
		//YP_WBW2CLLCOMPANY.put("12BET (壹貳博)", "皇冠");
		
//		YP_WBW2CLLCOMPANY.put("永利高", "永利高");
		YP_WBW2CLLCOMPANY.put("皇冠", "皇冠");
//		YP_WBW2CLLCOMPANY.put("伟德", "伟德");
//		YP_WBW2CLLCOMPANY.put("立博", "立博");
//		YP_WBW2CLLCOMPANY.put("澳门", "澳门");
//		YP_WBW2CLLCOMPANY.put("Bet365", "bet365");
//		YP_WBW2CLLCOMPANY.put("Easybets(易胜博)", "易胜博");
//		YP_WBW2CLLCOMPANY.put("明陞", "明陞");
//		YP_WBW2CLLCOMPANY.put("Sbobet(利记)", "利记");
//		YP_WBW2CLLCOMPANY.put("Nikebet", "nikebet");
//		YP_WBW2CLLCOMPANY.put("沙巴(IBCBET)", "沙巴");
		
//		COMAPNYLIST.add("yongligao");
		COMAPNYLIST.add("huangguan");
//		COMAPNYLIST.add("weide");
//		COMAPNYLIST.add("libo");
//		COMAPNYLIST.add("aomen");
//		COMAPNYLIST.add("ribo");
//		COMAPNYLIST.add("yisheng");
//		COMAPNYLIST.add("mingsheng");
//		COMAPNYLIST.add("liji");
//		COMAPNYLIST.add("nikebet");
//		COMAPNYLIST.add("shaba");
		
	}
	
	
	public static String getRealHtml(String url,Integer homeId ,Integer guestId) throws Exception {
		String html;
		try {
			html = HttpUtil.getUrl(url);
			if (homeId != null && guestId!= null) {
				Document doc = Jsoup.parse(html);
				String str = "_" +homeId+ "_" + guestId;
				String homeIdStr = doc.select(".against_a>a").first().attr("href");
				String guestIdStr = doc.select(".against_b>a").first().attr("href");
				int hId = Integer.parseInt(homeIdStr.replaceAll("500\\.com|\\D", ""));
				int gId = Integer.parseInt(guestIdStr.replaceAll("500\\.com|\\D", ""));
				String equalsStr = "_" + hId + "_" + gId;
				if (!str.equals(equalsStr)) { // 主客队不一致.获取反转html
					html = HttpUtil.getUrl(url + "-r--1");
				}
			}
		} catch (Exception e) {
			log.error("--------获取"+url+"数据页面错误------" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return html;
	}
}
