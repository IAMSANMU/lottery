package com.lottery.odds.yp;

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
import com.lottery.odds.OddsBusiness;

public class YpBusiness {
	public static final String ASIA_ODDS_URL = "http://odds.500.com/fenxi/yazhi-";
	private static Logger log = Logger.getLogger(YpBusiness.class);

	public static String getRealHtml(DcArrange match, String oddId) throws Exception {
		String url = ASIA_ODDS_URL + oddId;
		String html = OddsBusiness.getRealHtml(url, match.getHomeId(), match.getGuestId());
		return html;
	}

	/*
	 * 即时亚盘
	 */
	public static List<DcYp> getNowYp(String html) throws Exception {
		List<DcYp> list = new ArrayList<DcYp>();
		try {
			Document doc = Jsoup.parse(html);
			Elements trEles = doc.select("#datatb tr[id]");
			for (Element trEle : trEles) {
				String company = trEle.child(1).text().trim().replace("  ", "");
				String cllCompany = OddsBusiness.WBW2CLLCOMPANY.get(company);
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
					int index = pk.indexOf(" ");
					if (index > -1) {
						pk = pk.substring(0, index);
					}
					Double numPk = Double.parseDouble(pankouTd.attr("ref").trim());
					yp.setPankou(pk);
					yp.setPankouChange(pkChange);
					yp.setNumPankou(numPk);

					Element guestTd = trEle.child(4);
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

					Date time = DateUtil.formatDate(DateUtil.getNowYear() + "-" + timeStr, "yyyy-MM-dd HH:mm");
					yp.setTime(time);
					yp.setCreateTime(new Date());
					yp.setIsFirst(false);
					list.add(yp);
				}
			}
		} catch (Exception e) {
			log.error("--------[yp抓取]抓取即时亚盘错误--------" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return list;
	}

	/*
	 * 初盘
	 */
	public static List<DcYp> getFirstYp(String html) throws Exception {

		List<DcYp> list = new ArrayList<DcYp>();
		try {
			Document doc = Jsoup.parse(html);
			Elements trEles = doc.select("#datatb tr[id]");
			for (Element trEle : trEles) {
				String company = trEle.child(1).text().trim();
				String cllCompany = OddsBusiness.WBW2CLLCOMPANY.get(company);
				if (StringUtils.isNotEmpty(cllCompany)) {
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

					Element guestTd = trEle.child(8);
					yp.setGuest(Double.parseDouble(guestTd.text()));
					yp.setGuestChange(0);
					String timeStr = trEle.child(9).text().trim();
					Date time = DateUtil.formatDate(DateUtil.getNowYear() + "-" + timeStr, "yyyy-MM-dd HH:mm");
					yp.setTime(time);
					yp.setCreateTime(new Date());
					yp.setIsFirst(true);
					list.add(yp);
				}
			}
		} catch (Exception e) {
			log.error("--------[yp抓取]抓取初盘错误--------" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return list;
	}

	public static void main(String[] args) {
		// String a=null;
		// System.out.println(StringUtils.isNotEmpty(a));
//		DcArrange dc = new DcArrange().setHomeId(867).setGuestId(1775);
		try {
			String url=ASIA_ODDS_URL+662467;
			String html =HttpUtil.getUrl(url); 
			List<DcYp> list = getNowYp(html);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
