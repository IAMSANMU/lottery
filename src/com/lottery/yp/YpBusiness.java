package com.lottery.yp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lottery.common.model.DcArrange;
import com.lottery.common.model.DcYp;
import com.lottery.common.utils.DateUtil;
import com.lottery.common.utils.HttpUtil;

public class YpBusiness {
	public static final String ASIA_ODDS_URL = "http://odds.500.com/fenxi/yazhi-";
	private static Logger log = Logger.getLogger(YpBusiness.class);

	public static String getRealHtml(DcArrange match, String oddId) throws Exception {
		String html;
		try {
			String url = ASIA_ODDS_URL + oddId;
			html = HttpUtil.getUrl(url);
			if (match.getHomeId() != null && match.getGuestId() != null) {
				Document doc = Jsoup.parse(html);
				String str = "_" + match.getHomeId() + "_" + match.getGuestId();
				String homeIdStr = doc.select(".against_a>a").first().attr("href");
				String guestIdStr = doc.select(".against_b>a").first().attr("href");
				int homeId = Integer.parseInt(homeIdStr.replaceAll("500\\.com|\\D", ""));
				int guestId = Integer.parseInt(guestIdStr.replaceAll("500\\.com|\\D", ""));
				String equalsStr = "_" + homeId + "_" + guestId;
				if (!str.equals(equalsStr)) { // 主客队不一致.获取反转html
					html = HttpUtil.getUrl(url + "-r--1");
				}
			}
		} catch (Exception e) {
			log.error("--------获取亚盘数据页面错误------" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return html;
	}

	/*
	 * 即时亚盘
	 */
	public static List<DcYp> GetNowYp(String html) throws Exception {
		List<DcYp> list = new ArrayList<DcYp>();
		try {
			Document doc = Jsoup.parse(html);
			Elements trEles = doc.select("#datatb tr[id]");
			for (Element trEle : trEles) {
				String company = trEle.child(1).text().trim();
				String cllCompany = DcYp.WBW2CLLCOMPANY.get(company);
				if (StringUtils.isNotEmpty(cllCompany)) {
					DcYp yp = new DcYp();
					yp.setCompany(cllCompany);

					Element homeTd = trEle.child(2);
					String changeStr = homeTd.attr("class").trim();
					int homeChange = 0;
					if (changeStr.equals("red_up")) {
						homeChange = 1;
					} else if (changeStr.equals("green_down")) {
						homeChange = -1;
					}

					yp.setHome(Double.parseDouble(homeTd.text()));
					yp.setHomeChange(homeChange);
					Element pankouTd = trEle.child(3);
					Element changeEle = pankouTd.select("font").first();

					int pkChange = 0;
					if (changeEle != null) {
						pkChange = changeEle.text().equals("升") ? 1 : -1;
					}
					String pk = pankouTd.text();
					Double numPk = Double.parseDouble(pankouTd.attr("ref").trim());
					yp.setPankou(pk);
					yp.setPankouChange(pkChange);
					yp.setNumPankou(numPk);

					Element guestTd = trEle.child(2);
					changeStr = guestTd.attr("class").trim();
					int guestChange = 0;
					if (changeStr.equals("red_up")) {
						guestChange = 1;
					} else if (changeStr.equals("green_down")) {
						guestChange = -1;
					}
					yp.setGuest(Double.parseDouble(guestTd.text()));
					yp.setGuestChange(guestChange);
					String timeStr = trEle.child(5).text().trim();
					
					
					Date time = DateUtil.formatDate(DateUtil.getNowYear()+"-"+timeStr, "yyyy-MM-dd HH:mm");
					yp.setTime(time);
					yp.setCreateTime(new Date());
					yp.setIsFirst(false);
					list.add(yp);
				}
			}
		} catch (Exception e) {
			log.error("--------[yp抓取]抓取即时亚盘错误--------"+e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return list;
	}

	/*
	 * 初盘
	 */
	public static List<DcYp> GetFirstYp(String html) throws Exception {

		List<DcYp> list = new ArrayList<DcYp>();
		try {
			Document doc = Jsoup.parse(html);
			Elements trEles = doc.select("#datatb tr[id]");
			for (Element trEle : trEles) {
				String company = trEle.child(1).text().trim();
				String cllCompany = DcYp.WBW2CLLCOMPANY.get(company);
				if (!StringUtils.isNotEmpty(cllCompany)) {
					DcYp yp = new DcYp();
					yp.setCompany(cllCompany);
					Element homeTd = trEle.child(6);
					yp.setHome(Double.parseDouble(homeTd.text()));
					yp.setHomeChange(0);
					Element pankouTd = trEle.child(7);
					String pk = pankouTd.text();
					Double numPk = Double.parseDouble(pankouTd.attr("ref").trim());
					yp.setPankou(pk);
					yp.setPankouChange(0);
					yp.setNumPankou(numPk);

					Element guestTd = trEle.child(9);
					yp.setGuest(Double.parseDouble(guestTd.text()));
					yp.setGuestChange(0);
					String timeStr = trEle.child(10).text().trim();
					Date time = DateUtil.formatDate(DateUtil.getNowYear()+"-"+timeStr, "yyyy-MM-dd HH:mm");
					yp.setTime(time);
					yp.setCreateTime(new Date());
					yp.setIsFirst(true);
					list.add(yp);
				}
			}
		} catch (Exception e) {
			log.error("--------[yp抓取]抓取初盘错误--------"+e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return list;
	}

	public static void main(String[] args) {
		DcArrange dc = new DcArrange().setHomeId(867).setGuestId(1775);
		try {
			String html = getRealHtml(dc, "698100");
			GetNowYp(html);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
